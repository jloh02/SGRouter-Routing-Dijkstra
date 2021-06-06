package com.jonathan.sgrouter.routing.models;

import java.util.HashSet;
import java.util.Set;

import lombok.Data;

@Data
public class RouteState implements Comparable<RouteState> {
	String src, prevService;
	double time;
	Set<String> walked;

	public RouteState(String src, String prevService, double time) {
		this.src = src;
		this.prevService = prevService;
		this.time = time;
		this.walked = new HashSet<>();
	}

	public RouteState(String src, String prevService, double time, HashSet<String> walked) {
		this.src = src;
		this.prevService = prevService;
		this.time = time;
		this.walked = walked;
	}

	@Override
	public int compareTo(RouteState rs) {
		return this.time > rs.time ? 1 : -1;
	}

	public void resetWalk() {
		this.walked = new HashSet<>();
	}
}
