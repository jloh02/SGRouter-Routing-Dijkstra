package com.jonathan.sgrouter.routing.models;

import java.util.ArrayList;

public class NodeDistList extends ArrayList<NodeDist> {
  private int limit;

  public NodeDistList(int limit) {
    this.limit = limit;
  }

  @Override
  public boolean add(NodeDist e) {
    int idx = binSearch(e.getDist());
    if (idx < 0) idx = ~idx;
    super.add(idx, e);
    if (super.size() > limit) super.remove(limit);
    return true;
  }

  int binSearch(double target) {
    int lower = 0, upper = super.size() - 1;
    while (upper >= lower) {
      int mid = (upper + lower) >>> 1;
      if (super.get(mid).getDist() == target) return mid;
      else if (super.get(mid).getDist() > target) upper = mid - 1;
      else lower = mid + 1;
    }
    return -(lower + 1);
  }
}
