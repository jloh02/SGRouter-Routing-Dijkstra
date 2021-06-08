package com.jonathan.sgrouter.routing.pathfinder;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

import com.jonathan.sgrouter.routing.models.Node;
import com.jonathan.sgrouter.routing.models.Route;
import com.jonathan.sgrouter.routing.models.RouteState;
import com.jonathan.sgrouter.routing.models.Vertex;
import com.jonathan.sgrouter.routing.models.VisitedState;

public class Pathfinder implements Runnable {
	Map<String, VisitedState> vis = new HashMap<>();

	String src, des;
	double firstWalk, lastWalk;
	List<Node> nodes;

	public Pathfinder(List<Node> nodes, String src, String des, double firstWalk, double lastWalk) {
		this.src = src;
		this.des = des;
		this.firstWalk = firstWalk;
		this.lastWalk = lastWalk;
		this.nodes = nodes;
	}

	public void run() {
		int routesFound = 0;
		Map<String, String> nodeNames = new HashMap<>();
		for (Node n : this.nodes) {
			vis.put(n.getSrcKey(), new VisitedState());
			nodeNames.put(n.getSrcKey(), n.getName());
		}
		PriorityQueue<RouteState> pq = new PriorityQueue<>();
		pq.add(new RouteState(this.src, "Walk", this.firstWalk));
		while (!pq.isEmpty() && routesFound < PathfinderExecutor.kShortest) {

			if(PathfinderExecutor.threadInterrupt) return; //Kill switch

			RouteState curr = pq.poll();

			//System.out.println(curr.getPath());
			//System.out.println(curr.getWalked());

			if (visited(curr.getSrc(), curr.getPrevService()))
				continue;
			if (curr.getPrevService().contains("Walk"))
				vis.get(curr.getSrc()).incrementWalk();
			else
				vis.get(curr.getSrc()).getNodes().add(curr.getPrevService());

			if (curr.getSrc().equals(this.des)) {
				Route r = new Route(curr.getTime(), curr.getPath(), nodeNames, this.lastWalk);

				synchronized (PathfinderExecutor.routes) {
					PathfinderExecutor.routes.add(r);
				}
				routesFound++;
				//synchronized (PathfinderExecutor.dp) {} //TODO dp
				continue;
			}

			List<Vertex> adjList;
			//synchronized (PathfinderExecutor.sqh) {
				adjList = curr.getPrevService().contains("Walk")
						? PathfinderExecutor.sqh.getVertices(curr.getSrc(), curr.getWalked())
						: PathfinderExecutor.sqh.getVertices(curr.getSrc(), curr.getPrevService());
			//}

			if (!curr.getPrevService().contains("Walk"))
				curr.resetWalk();
			for (Vertex v : adjList) {
				if(PathfinderExecutor.threadInterrupt) return; //Kill switch

				if (PathfinderExecutor.routes.size() >= PathfinderExecutor.kShortest && v.getTime()
						+ curr.getTime() > PathfinderExecutor.routes.get(PathfinderExecutor.kShortest - 1).getTime())
					continue;
				if (!visited(v.getDes(), v.getService()) && !curr.getTraversedNodes().contains(v.getDes()))
					pq.add(new RouteState(curr, v.getDes(), v.getService(), v.getTime()));
			}
		}
		// System.out.println(pq.isEmpty());
		// System.out.println(PathfinderExecutor.kShortest);
		// System.out.println(routesFound);
		System.out.println("Done");
	}

	boolean visited(String src, String prevService) {
		//try {
			return (vis.get(src).getNodes().size() + vis.get(src).getWalks() >= PathfinderExecutor.kShortest)
					|| (!prevService.contains("Walk") && vis.get(src).getNodes().contains(prevService));
		/*} catch (NullPointerException e) {
			System.out.println("ERROR");
			System.out.println(vis.get(src));
			System.out.println(PathfinderExecutor.kShortest);

		}
		return true;*/
	}
}
