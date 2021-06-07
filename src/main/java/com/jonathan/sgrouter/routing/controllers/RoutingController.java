package com.jonathan.sgrouter.routing.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import com.jonathan.sgrouter.routing.models.Node;
import com.jonathan.sgrouter.routing.models.NodeDist;
import com.jonathan.sgrouter.routing.models.NodeDistList;
import com.jonathan.sgrouter.routing.models.SubRoute;
import com.jonathan.sgrouter.routing.pathfinder.Dijkstra;
import com.jonathan.sgrouter.routing.utils.CloudStorageHandler;
import com.jonathan.sgrouter.routing.utils.DatastoreHandler;
import com.jonathan.sgrouter.routing.utils.SQLiteHandler;
import com.jonathan.sgrouter.routing.utils.Utils;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.geotools.referencing.CRS;
import org.geotools.referencing.GeodeticCalculator;
import org.opengis.referencing.FactoryException;

@RestController
public class RoutingController {
	public static Map<String, List<SubRoute>> dp;
	public static List<List<SubRoute>> routes;

	@GetMapping("/ping")
	public String ping() {
		return "pong";
	}

	@GetMapping("/route")
	public String route(@RequestParam("startLat") double startLat, @RequestParam("startLon") double startLon,
			@RequestParam("endLat") double endLat, @RequestParam("endLon") double endLon) {
		return routeAbs(startLat, startLon, endLat, endLon);
	}

	public static String routeAbs(double startLat, double startLon,double endLat, double endLon){
		SQLiteHandler sqh = new SQLiteHandler(); //!IMPORANT ensure this line is before download
		CloudStorageHandler.downloadDB();

		routes = new ArrayList<>();
		dp = new HashMap<>();

		double walkSpeed = DatastoreHandler.getWalkSpeed();

		List<Node> nodes = sqh.getNodes();
		NodeDistList starts = new NodeDistList(5), ends = new NodeDistList(5);

		try {
			GeodeticCalculator srcGC = new GeodeticCalculator(CRS.parseWKT(Utils.getLatLonWKT()));
			srcGC.setStartingGeographicPoint(startLon, startLat);
			GeodeticCalculator desGC = new GeodeticCalculator(CRS.parseWKT(Utils.getLatLonWKT()));
			desGC.setStartingGeographicPoint(endLon, endLat);
			for (Node n : nodes) {
				srcGC.setDestinationGeographicPoint(n.getLon(), n.getLat());
				starts.add(new NodeDist(n.getSrcKey(), srcGC.getOrthodromicDistance()));
				desGC.setDestinationGeographicPoint(n.getLon(), n.getLat());
				ends.add(new NodeDist(n.getSrcKey(), desGC.getOrthodromicDistance()));
			}
		} catch (FactoryException e) {
			System.err.println(e);
		}

		System.out.println(starts);
		System.out.println(ends);

		ExecutorService executor = Executors.newFixedThreadPool(4);
		for (NodeDist s : starts)
			for (NodeDist e : ends)
				executor.execute(
						new Thread(new Dijkstra(nodes, sqh, s.getSrc(), e.getSrc(), s.getDist()*0.001/walkSpeed, e.getDist()*0.001/walkSpeed)));

		//Wait for all threads to finish execution or 30s timeout
		executor.shutdown();
		synchronized (executor) {
			try {
				executor.awaitTermination(30000, TimeUnit.MILLISECONDS);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		executor.shutdownNow();

		sqh.close();
		return "";
	}
}
