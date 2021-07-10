package com.jonathan.sgrouter.routing.models;

import com.jonathan.sgrouter.routing.pathfinder.PathfinderExecutor;
import java.util.ArrayList;
import java.util.HashSet;
import lombok.Data;

@Data
public class RouteState implements Comparable<RouteState> {
  String src, prevSrc, prevService;
  double time;
  HashSet<String> traversedNodes;
  HashSet<String> walked;
  ArrayList<SubRoute> path;

  // Used for initialisation of src
  public RouteState(String src, String prevService, double time) {
    this.src = src;
    this.prevSrc = src;
    this.prevService = prevService;
    this.time = time;
    this.walked = new HashSet<>();
    this.path = new ArrayList<>();
    this.traversedNodes = new HashSet<>();
  }

  // Used for appending to pq
  public RouteState(RouteState old, String src, String service, double vtxTime) {
    this.src = src;
    this.prevSrc = old.src;
    this.prevService = service;
    this.time = old.getTime();
    this.walked = new HashSet<>(old.getWalked());
    this.path = new ArrayList<>(old.getPath());
    this.traversedNodes = new HashSet<>(old.getTraversedNodes());

    this.time += vtxTime;
    if (!old.getPrevService().equals(prevService)) {
      this.time +=
          service.contains("Walk")
              ? 0
              : PathfinderExecutor.freq.getOrDefault(service, PathfinderExecutor.freq.get("train"));
    }
    this.path.add(new SubRoute(vtxTime, prevService, src));
    this.traversedNodes.add(src);
    if (this.prevService.contains("Walk")) this.walked.add(prevService);
  }

  @Override
  public int compareTo(RouteState rs) {
    return this.time > rs.time ? 1 : -1;
  }

  public void resetWalk() {
    this.walked = new HashSet<>();
  }
}
