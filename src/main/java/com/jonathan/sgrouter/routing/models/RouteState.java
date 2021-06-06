package com.jonathan.sgrouter.routing.models;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import lombok.Data;

@Data
public class RouteState implements Comparable<RouteState> {
	String src, prevService;
	double time;
	Set<String> walked;
	List<SubRoute> path;

	//Used for initialisation of src
	public RouteState(String src, String prevService, double time) {
		this.src = src;
		this.prevService = prevService;
		this.time = time;
		this.walked = new HashSet<>();
		this.path=new ArrayList<>();
	}

	//Used for appending to pq
	public RouteState(String src, String prevService, double time, Set<String> walked, List<SubRoute> path, SubRoute sub) {
		this.src = src;
		this.prevService = prevService;
		this.time = time;
		this.walked = new HashSet<>(walked);
		this.path=new ArrayList<>(path);
		this.path.add(sub);
	}

	@Override
	public int compareTo(RouteState rs) {
		return this.time > rs.time ? 1 : -1;
	}

	public void resetWalk() {
		this.walked = new HashSet<>();
	}
}
