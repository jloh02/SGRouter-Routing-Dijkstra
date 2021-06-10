package com.jonathan.sgrouter.routing.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import lombok.Data;

@Data
public class Route implements Comparable<Route> {
  double time;
  List<SubRoute> route;

  public Route(double time, List<SubRoute> path, Map<String, String> names, double lastWalk) {
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
