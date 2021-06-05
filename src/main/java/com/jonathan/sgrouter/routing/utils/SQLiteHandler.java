package com.jonathan.sgrouter.routing.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
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

	public List<Vertex> getVertices(String src, String prevService) {
		List<Vertex> op = new ArrayList<>();
		try {
			PreparedStatement ps = conn.prepareStatement("SELECT * FROM vertex WHERE src=? AND service<>?");
			ps.setString(1, src);
			ps.setString(2, prevService);
			ResultSet res = ps.executeQuery();
			while (res.next())
				op.add(new Vertex(res.getString("src"), res.getString("des"), res.getString("service"),
						res.getDouble("time")));
		} catch (SQLException e) {
			System.out.println(e);
		}
		return op;
	}

	public void close(){
		try {
			conn.close();
		} catch (SQLException e) {
			System.out.println(e);
		}
	}
}
