package com.jonathan.sgrouter.routing.controllers;

import com.google.gson.Gson;
import com.jonathan.sgrouter.routing.models.RouteList;
import com.jonathan.sgrouter.routing.pathfinder.PathfinderExecutor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RoutingController {

  @GetMapping("/ping")
  public String ping() {
    return "pong";
  }

  @GetMapping("/route")
  public String route(
      @RequestParam double startLat,
      @RequestParam double startLon,
      @RequestParam double endLat,
      @RequestParam double endLon,
      @RequestParam(required = false) Long epochTime) { // epochTime in seconds
    RouteList routes = PathfinderExecutor.route(startLat, startLon, endLat, endLon);
    return new Gson().toJson(routes);
  }
}
