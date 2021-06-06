package com.jonathan.sgrouter.routing.pathfinder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

import com.jonathan.sgrouter.routing.models.Node;
import com.jonathan.sgrouter.routing.models.RouteState;
import com.jonathan.sgrouter.routing.models.SubRoute;
import com.jonathan.sgrouter.routing.models.Vertex;
import com.jonathan.sgrouter.routing.utils.SQLiteHandler;

public class Dijkstra {
	final static int kShortest = 3; //TODO change
	Map<String, SubRoute> dis = new HashMap<>();
	Map<String, HashSet<String>> vis = new HashMap<>();

	public void route(SQLiteHandler sqh, String src, String des) { //TODO return something
		int routesFound = 0;
		for (Node n : sqh.getNodes()) {
			dis.put(n.getSrcKey(), new SubRoute());
			vis.put(n.getSrcKey(), new HashSet<>());
		}
		PriorityQueue<RouteState> pq = new PriorityQueue<>();
		pq.add(new RouteState(src, "null", 0));
		while (!pq.isEmpty() && routesFound<kShortest) {
			RouteState curr = pq.poll();	

			//System.out.println((vis.get(curr.getSrc()).size() >= kShortest) || (vis.get(curr.getSrc()).contains(curr.getPrevService())));
			if (visited(curr.getSrc(), curr.getPrevService()))
				continue;
			vis.get(curr.getSrc()).add(curr.getPrevService());

			if(curr.getSrc().equals(des)){
				System.out.println(vis.get(curr.getSrc()));
				SubRoute csr = dis.get(des);
				List<SubRoute> route = new ArrayList<>();
				do{
					route.add(csr);
					csr = dis.get(csr.getPrevSrc());
				} while(!csr.getPrevSrc().equals(src));
				routesFound++;
				System.out.println(route);
			}

			List<Vertex> adjList;
			synchronized (sqh) {
				adjList = curr.getPrevService().contains("Walk") ? sqh.getVertices(curr.getSrc(), curr.getWalked())
						: sqh.getVertices(curr.getSrc(), curr.getPrevService());
			}
			if(!curr.getPrevService().contains("Walk")) curr.resetWalk();
			// System.out.println(adjList);
			for (Vertex v : adjList) {
				if (!visited(v.getDes(), v.getService()) && v.getTime() + curr.getTime() < dis.get(v.getDes()).getTime()) { //
					// System.out.println(v);
					// System.out.println(pq.size());
					dis.put(v.getDes(),new SubRoute(v.getTime() + curr.getTime(), v.getService(), v.getSrc()));
					pq.add(new RouteState(v.getDes(), v.getService(), v.getTime() + curr.getTime()));
				}
			}
		}
	}

	boolean visited(String src, String prevService) {
		return (vis.get(src).size() >= kShortest) || (vis.get(src).contains(prevService));
	}
}
