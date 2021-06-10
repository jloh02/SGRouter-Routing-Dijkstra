package com.jonathan.sgrouter.routing.models;

import java.util.ArrayList;

public class RouteList extends ArrayList<Route> {
  private int limit;

  public RouteList(int limit) {
    this.limit = limit;
  }

  @Override
  public boolean add(Route e) {
    boolean added = false;
    for (int i = 0; i < super.size(); i++) {
      if (super.get(i).getTime() > e.getTime()) {
        super.add(i, e);
        added = true;
        break;
      }
    }
    if (!added) super.add(e);
    if (super.size() > limit) super.remove(limit);
    return true;
  }
}
