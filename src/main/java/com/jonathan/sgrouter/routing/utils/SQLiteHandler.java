package com.jonathan.sgrouter.routing.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.io.File;
import java.io.IOException;

import com.jonathan.sgrouter.routing.RoutingApplication;
import com.jonathan.sgrouter.routing.models.Node;
import com.jonathan.sgrouter.routing.models.Vertex;

public class SQLiteHandler {
	Connection conn;

	public SQLiteHandler() {
		String filename = RoutingApplication.config.isAppengineDeployment() ? "/tmp/graph.db" : "graph.db";
		String dbUrl = "jdbc:sqlite:" + filename;

		try {
			File oldDbFile = new File(filename);
			if (oldDbFile.exists() && !oldDbFile.delete())
				throw new IOException("Unable to delete graph.db");

			this.conn = DriverManager.getConnection(dbUrl);
		} catch (SQLException e) {
			System.err.println(e);
		} catch (IOException e) {
			System.err.println(e);
		}
	}

	public List<Node> getNodes() {
		List<Node> op = new ArrayList<>();
		try {
			ResultSet res = conn.createStatement().executeQuery("SELECT * FROM nodes");
			while (res.next())
				op.add(new Node(res.getString("src"), res.getString("name"), res.getDouble("lat"),
						res.getDouble("lon")));

		} catch (SQLException e) {
			System.out.println(e);
		}
		return op;
	}

	private List<Vertex> getVerticesAbstract(PreparedStatement ps) throws SQLException {
		List<Vertex> op = new ArrayList<>();
		ResultSet res = ps.executeQuery();
		while (res.next())
			op.add(new Vertex(res.getString("src"), res.getString("des"), res.getString("service"),
					res.getDouble("time")));
		return op;
	}

	public List<Vertex> getVertices(String src, String prevService) {
		StringBuilder sb = new StringBuilder("SELECT * FROM vertex WHERE src=? AND service<>?");
		if (Utils.isBusService(prevService))
			sb.append(" AND service NOT LIKE ?");
		try {
			PreparedStatement ps = conn.prepareStatement(sb.toString());
			ps.setString(1, src);
			ps.setString(2, prevService);
			if (Utils.isBusService(prevService))
				ps.setString(3, String.format("%%%s%%",prevService.split(" \\(")[0]));

			return getVerticesAbstract(ps);
		} catch (SQLException e) {
			System.out.println(e);
		}
		return new ArrayList<>();
	}

	public List<Vertex> getVertices(String src, Set<String> walkState) {
		StringBuilder sb = new StringBuilder("SELECT * FROM vertex WHERE src=?");
		for (int i = 0; i < walkState.size(); i++)
			sb.append(" AND service NOT LIKE ?");
		try {
			PreparedStatement ps = conn.prepareStatement(sb.toString());
			ps.setString(1, src);
			Iterator<String> walks = walkState.iterator();
			for (int i = 2; i < walkState.size() + 2; i++)
				ps.setString(i, String.format("%%%s%%",walks.next()));
			return getVerticesAbstract(ps);
		} catch (SQLException e) {
			System.out.println(e);
		}
		return new ArrayList<>();
	}

	public void close() {
		try {
			conn.close();
		} catch (SQLException e) {
			System.out.println(e);
		}
	}
}
