package com.jonathan.sgrouter.routing.models;

import java.util.HashSet;
import java.util.Set;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class VisitedState {
	Set<String> nodes = new HashSet<>();
	int walks=0;

	public void incrementWalk(){
		walks++;
	}
}
