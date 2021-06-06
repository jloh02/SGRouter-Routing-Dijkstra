package com.jonathan.sgrouter.routing.models;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SubRoute implements Comparable<SubRoute> {
	double time = Double.MAX_VALUE;
	String service = "", prevSrc = "";

	public SubRoute(double time, String service, String prevSrc) {
		this.time = time;
		this.service = service;
		this.prevSrc = prevSrc;
	}

	@Override
	public int compareTo(SubRoute s) {
		return this.time > s.time ? 1 : -1;
	}
}
