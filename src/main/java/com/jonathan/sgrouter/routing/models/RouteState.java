package com.jonathan.sgrouter.routing.models;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.Data;

@Data
public class RouteState implements Comparable<RouteState> {
  String src, prevService;
  double time;
  Set<String> traversedNodes;
  Set<String> walked;
  List<SubRoute> path;

  // Used for initialisation of src
  public RouteState(String src, String prevService, double time) {
    this.src = src;
    this.prevService = prevService;
    this.time = time;
    this.walked = new HashSet<>();
    this.path = new ArrayList<>();
    this.traversedNodes = new HashSet<>();
  }

  // Used for appending to pq
  public RouteState(RouteState old, String src, String prevService, double vtxTime) {
    this.src = src;
    this.prevService = prevService;
    this.time = old.getTime();
    this.walked = new HashSet<>(old.getWalked());
    this.path = new ArrayList<>(old.getPath());
    this.traversedNodes = new HashSet<>(old.getTraversedNodes());

    this.time += vtxTime;
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
