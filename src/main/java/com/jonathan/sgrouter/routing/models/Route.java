package com.jonathan.sgrouter.routing.models;

import java.util.ArrayList;
import java.util.HashMap;
import lombok.Data;

@Data
public class Route implements Comparable<Route> {
  double time;
  ArrayList<SubRoute> route;

  public Route(
      double time, ArrayList<SubRoute> path, HashMap<String, String> names, double lastWalk) {
    this.time = time + lastWalk;

    this.route = new ArrayList<>();

    SubRoute curr = new SubRoute();
    for (int i = 0; i < path.size() - 1; i++) {
      SubRoute sr = path.get(i), post = path.get(i + 1);

      if (curr.getService().isEmpty()) curr.setService(sr.getService());
      if (curr.getService().contains("Walk")) curr.setService("Walk");

      curr.addTime(sr.getTime());
      if (sr.getService().equals(post.getService())
          || (sr.getService().contains("Walk") && post.getService().contains("Walk")))
        curr.addInter(sr.getDes(), names.get(sr.getDes()));
      else {
        curr.setDes(sr.getDes());
        curr.setName(names.get(sr.getDes()));
        this.route.add(curr);
        curr = new SubRoute();
      }
    }

    SubRoute sr = path.get(path.size() - 1);
    if (curr.getService().isEmpty()) curr.setService(sr.getService());
    if (curr.getService().contains("Walk")) curr.setService("Walk");
    curr.addTime(sr.getTime());

    if (curr.getService().equals("Walk")) {
      curr.addInter(sr.getDes(), sr.getName());

      curr.addTime(lastWalk);
      curr.setService("Walk");
      curr.setDes("");
      curr.setName("Destination");

      this.route.add(curr);
    } else {
      curr.setDes(sr.getDes());
      curr.setName(names.get(sr.getDes()));
      this.route.add(curr);

      curr = new SubRoute();
      curr.addTime(lastWalk);
      curr.setService("Walk");
      curr.setDes("");
      curr.setName("Destination");
      this.route.add(curr);
    }
  }

  @Override
  public int compareTo(Route o) {
    return (this.time < o.getTime()) ? -1 : 1;
  }
  // TODO dp
}
