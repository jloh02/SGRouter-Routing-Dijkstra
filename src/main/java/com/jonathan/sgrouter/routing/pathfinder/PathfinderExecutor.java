package com.jonathan.sgrouter.routing.pathfinder;

import com.jonathan.sgrouter.routing.RoutingApplication;
import com.jonathan.sgrouter.routing.models.Node;
import com.jonathan.sgrouter.routing.models.NodeDist;
import com.jonathan.sgrouter.routing.models.NodeDistList;
import com.jonathan.sgrouter.routing.models.RouteList;
import com.jonathan.sgrouter.routing.models.SubRoute;
import com.jonathan.sgrouter.routing.models.Vertex;
import com.jonathan.sgrouter.routing.utils.CloudStorageHandler;
import com.jonathan.sgrouter.routing.utils.DatastoreHandler;
import com.jonathan.sgrouter.routing.utils.SQLiteHandler;
import com.jonathan.sgrouter.routing.utils.Utils;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.geotools.referencing.CRS;
import org.geotools.referencing.GeodeticCalculator;
import org.opengis.referencing.FactoryException;

@Slf4j
public class PathfinderExecutor {
  public static final int kShortest = 3;
  public static HashMap<String, ArrayList<SubRoute>> dp;
  public static SQLiteHandler sqh;
  public static RouteList routes;
  public static HashMap<String, Double> freq;
  public static HashMap<String, ArrayList<Vertex>> adjList;

  public static volatile boolean threadInterrupt;

  public static RouteList route(
      double startLat, double startLon, double endLat, double endLon, ZonedDateTime dt) {
    threadInterrupt = false;

    if (dt.getHour() >= 2 && dt.getHour() <= 4) return new RouteList(3);

    sqh = new SQLiteHandler();
    String dbName =
        String.format("graph_%d_%d.db", dt.getHour(), 5 * (int) (Math.floor(dt.getMinute() / 5.)));
    if (RoutingApplication.appengineDeployment) {
      CloudStorageHandler.downloadDB(dbName);
    } else {
      dbName = "archive/sun_dbs/" + dbName;
      try (InputStream is = new FileInputStream(dbName);
          OutputStream os = new FileOutputStream("graph.db")) {
        byte[] buffer = new byte[5000000];
        int length;
        while ((length = is.read(buffer)) > 0) os.write(buffer, 0, length);
      } catch (Exception e) {
        log.error(e.getMessage());
        System.exit(1);
      }
    }

    routes = new RouteList(kShortest);
    dp = new HashMap<>();

    double walkSpeed = DatastoreHandler.getWalkSpeed();
    if (walkSpeed < 0) return routes;

    freq = sqh.getFreqs();
    adjList = sqh.getVertices();

    ArrayList<Node> nodes = sqh.getNodes();
    NodeDistList starts = new NodeDistList(5), ends = new NodeDistList(5);
    try {
      GeodeticCalculator srcGC = new GeodeticCalculator(CRS.parseWKT(Utils.getLatLonWKT()));
      srcGC.setStartingGeographicPoint(startLon, startLat);
      GeodeticCalculator desGC = new GeodeticCalculator(CRS.parseWKT(Utils.getLatLonWKT()));
      desGC.setStartingGeographicPoint(endLon, endLat);
      for (Node n : nodes) {
        if (!Utils.isBusStop(n.getSrcKey()) && !n.getSrcKey().contains("EXIT")) continue;
        srcGC.setDestinationGeographicPoint(n.getLon(), n.getLat());
        desGC.setDestinationGeographicPoint(n.getLon(), n.getLat());

        starts.add(new NodeDist(n.getSrcKey(), srcGC.getOrthodromicDistance()));
        ends.add(new NodeDist(n.getSrcKey(), desGC.getOrthodromicDistance()));
      }
    } catch (FactoryException e) {
      log.error(e.getMessage());
    }

    log.debug("Start Nodes: {}", starts.toString());
    log.debug("End Nodes: {}", ends.toString());

    ExecutorService executor = Executors.newFixedThreadPool(8);
    for (NodeDist s : starts) {
      for (NodeDist e : ends) {
        executor.execute(
            new Thread(
                new Pathfinder(
                    nodes,
                    s.getSrc(),
                    e.getSrc(),
                    s.getDist() * 0.001 / walkSpeed,
                    e.getDist() * 0.001 / walkSpeed)));
      }
    }

    // Wait for all threads to finish execution or 30s timeout
    executor.shutdown();
    synchronized (executor) {
      try {
        if (!executor.awaitTermination(Long.MAX_VALUE, TimeUnit.MILLISECONDS))
          log.debug("Routing timed out");
      } catch (InterruptedException e) {
        log.error(e.getMessage());
      }
    }
    threadInterrupt = true;
    executor.shutdownNow();

    log.debug("Number of routes found: {}", routes.size());
    log.debug("Routes:\n{}", routes.toString());

    sqh.close();

    return routes;
  }
}
