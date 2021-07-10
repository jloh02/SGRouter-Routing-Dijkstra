package com.jonathan.sgrouter.routing.models;

import java.util.ArrayList;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class SubRoute implements Comparable<SubRoute> {
  private double time = Double.MAX_VALUE;
  private String service = "", des = "", name;
  private ArrayList<InterPoint> inter = new ArrayList<>();

  public SubRoute() {}

  public SubRoute(double time, String service, String des) {
    this.time = time;
    this.service = service;
    this.des = des;
  }

  public void addTime(double t) {
    this.time += t;
  }

  public void addInter(String id, String name) {
    inter.add(new InterPoint(id, name));
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder("{time=");
    sb.append(time);
    sb.append(", service=");
    sb.append(service);
    sb.append(", des=");
    sb.append(des);

    if (inter.size() != 0) {
      sb.append(", name=");
      sb.append(name);
    }

    if (name != null) {
      sb.append(", name=");
      sb.append(name);
    }
    return sb.toString();
  }

  @Override
  public int compareTo(SubRoute s) {
    return this.time > s.time ? 1 : -1;
  }
}

@Data
@AllArgsConstructor
class InterPoint {
  String id, name;
}
