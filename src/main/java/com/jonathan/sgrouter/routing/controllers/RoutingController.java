package com.jonathan.sgrouter.routing.controllers;

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
	public String route(@RequestParam("startLat") double startLat, @RequestParam("startLon") double startLon,
			@RequestParam("endLat") double endLat, @RequestParam("endLon") double endLon) {
		return PathfinderExecutor.route(startLat, startLon, endLat, endLon);
	}

	
}
