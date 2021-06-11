package com.jonathan.sgrouter.routing.pathfinder;

import com.jonathan.sgrouter.routing.models.Node;
import com.jonathan.sgrouter.routing.models.Route;
import com.jonathan.sgrouter.routing.models.RouteState;
import com.jonathan.sgrouter.routing.models.Vertex;
import com.jonathan.sgrouter.routing.models.VisitedState;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.PriorityQueue;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Pathfinder implements Runnable {
  HashMap<String, VisitedState> vis = new HashMap<>();

  String src, des;
  double firstWalk, lastWalk;
  ArrayList<Node> nodes;

  public Pathfinder(ArrayList<Node> nodes, String src, String des, double firstWalk, double lastWalk) {
    this.src = src;
    this.des = des;
    this.firstWalk = firstWalk;
    this.lastWalk = lastWalk;
    this.nodes = nodes;
  }

  public void run() {
    int routesFound = 0;
    HashMap<String, String> nodeNames = new HashMap<>();
    for (Node n : this.nodes) {
      vis.put(n.getSrcKey(), new VisitedState());
      nodeNames.put(n.getSrcKey(), n.getName());
    }
    PriorityQueue<RouteState> pq = new PriorityQueue<>();
    pq.add(new RouteState(this.src, "Walk", this.firstWalk));
    while (!pq.isEmpty() && routesFound < PathfinderExecutor.kShortest) {

      if (PathfinderExecutor.threadInterrupt) return; // Kill switch

      RouteState curr = pq.poll();

      // log.trace(curr.getPath().toString());
      // log.trace(curr.getWalked().toString());

      if (visited(curr.getSrc(), curr.getPrevService())) continue;
      if (curr.getPrevService().contains("Walk")) vis.get(curr.getSrc()).incrementWalk();
      else vis.get(curr.getSrc()).getServices().add(curr.getPrevService());

      if (curr.getSrc().equals(this.des)) {
        Route r = new Route(curr.getTime(), curr.getPath(), nodeNames, this.lastWalk);

        synchronized (PathfinderExecutor.routes) {
          PathfinderExecutor.routes.add(r);
        }
        routesFound++;
        // synchronized (PathfinderExecutor.dp) {} //TODO dp
        continue;
      }

      ArrayList<Vertex> adjList;
      adjList =
          curr.getPrevService().contains("Walk")
              ? PathfinderExecutor.sqh.getVertices(curr.getSrc(), curr.getWalked())
              : PathfinderExecutor.sqh.getVertices(curr.getSrc(), curr.getPrevService());

      if (!curr.getPrevService().contains("Walk")) curr.resetWalk();
      for (Vertex v : adjList) {
        if (PathfinderExecutor.threadInterrupt) return; // Kill switch

        if (PathfinderExecutor.routes.size() >= PathfinderExecutor.kShortest
            && v.getTime() + curr.getTime()
                > PathfinderExecutor.routes.get(PathfinderExecutor.kShortest - 1).getTime())
          continue;
        if (!visited(v.getDes(), v.getService()) && !curr.getTraversedNodes().contains(v.getDes()))
          pq.add(new RouteState(curr, v.getDes(), v.getService(), v.getTime()));
      }
    }
    log.debug("Thread [{}] completed", Thread.currentThread().getName());
  }

  boolean visited(String src, String prevService) {
    return (vis.get(src).getServices().size() + vis.get(src).getWalks()
            >= PathfinderExecutor.kShortest)
        || (!prevService.contains("Walk") && vis.get(src).getServices().contains(prevService));
  }
}