package com.jonathan.sgrouter.routing.models;

import lombok.Data;

@Data
public class SubRoute implements Comparable<SubRoute> {
	double time = Double.MAX_VALUE;
	String service = "", des = "", name;

	public SubRoute() {
	}

	public SubRoute(double time, String service, String des) {
		this.time = time;
		this.service = service;
		this.des = des;
	}

	@Override
	public int compareTo(SubRoute s) {
		return this.time > s.time ? 1 : -1;
	}
}
