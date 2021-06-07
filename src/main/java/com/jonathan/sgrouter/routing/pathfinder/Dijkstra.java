package com.jonathan.sgrouter.routing.pathfinder;

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

public class Dijkstra implements Runnable {
	final static int kShortest = 3;
	Map<String, HashSet<String>> vis = new HashMap<>();
	SQLiteHandler sqh;
	String src, des;
	double firstWalk, lastWalk;
	List<Node> nodes;

	public Dijkstra(List<Node> nodes, SQLiteHandler sqh, String src, String des, double firstWalk, double lastWalk) {
		this.src = src;
		this.des = des;
		this.firstWalk = firstWalk;
		this.lastWalk = lastWalk;
		this.sqh = sqh;
		this.nodes=nodes;
	}

	public void run() { //TODO return routes
		int routesFound = 0;
		for (Node n : this.nodes) {
			vis.put(n.getSrcKey(), new HashSet<>());
		}
		PriorityQueue<RouteState> pq = new PriorityQueue<>();
		pq.add(new RouteState(this.src, "null", this.firstWalk));
		while (!pq.isEmpty() && routesFound < kShortest) {
			RouteState curr = pq.poll();

			//System.out.println((vis.get(curr.getSrc()).size() >= kShortest) || (vis.get(curr.getSrc()).contains(curr.getPrevService())));
			if (visited(curr.getSrc(), curr.getPrevService()))
				continue;
			vis.get(curr.getSrc()).add(curr.getPrevService());

			if (curr.getSrc().equals(this.des)) {
				System.out.println(curr.getPath());
				continue;
			}

			List<Vertex> adjList;
			synchronized (this.sqh) {
				adjList = curr.getPrevService().contains("Walk") ? this.sqh.getVertices(curr.getSrc(), curr.getWalked())
						: this.sqh.getVertices(curr.getSrc(), curr.getPrevService());
			}
			if (!curr.getPrevService().contains("Walk"))
				curr.resetWalk();
			for (Vertex v : adjList) {
				if (!visited(v.getDes(), v.getService())) {
					pq.add(new RouteState(v.getDes(), v.getService(), v.getTime() + curr.getTime(), curr.getWalked(),
							curr.getPath(), new SubRoute(v.getTime() + curr.getTime(), v.getService(), v.getSrc())));
				}
			}
		}
	}

	boolean visited(String src, String prevService) {
		return (vis.get(src).size() >= kShortest) || (vis.get(src).contains(prevService));
	}
}
