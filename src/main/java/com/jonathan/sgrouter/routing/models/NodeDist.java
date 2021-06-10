package com.jonathan.sgrouter.routing.models;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class NodeDist implements Comparable<NodeDist> {
  String src;
  double dist;

  @Override
  public int compareTo(NodeDist o) {
    return this.dist < o.dist ? -1 : 1;
  }
}
