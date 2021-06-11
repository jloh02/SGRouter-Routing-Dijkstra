package com.jonathan.sgrouter.routing.models;

import java.util.HashSet;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class VisitedState {
  HashSet<String> services = new HashSet<>();
  int walks = 0;

  public void incrementWalk() {
    walks++;
  }
}
