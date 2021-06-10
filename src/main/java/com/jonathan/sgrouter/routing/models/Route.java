package com.jonathan.sgrouter.routing.models;

import java.util.ArrayList;
import java.util.HashMap;
import lombok.Data;

@Data
public class Route implements Comparable<Route> {
  double time;
  ArrayList<SubRoute> route;

  public Route(double time, ArrayList<SubRoute> path, HashMap<String, String> names, double lastWalk) {
    this.time = time + lastWalk;
    this.route = new ArrayList<>(path);
    for (SubRoute sr : this.route) sr.setName(names.get(sr.getDes()));
    this.route.add(new SubRoute(lastWalk, "Walk", "Destination"));
  }

  @Override
  public int compareTo(Route o) {
    return (this.time < o.getTime()) ? -1 : 1;
  }
  // TODO dp
}
