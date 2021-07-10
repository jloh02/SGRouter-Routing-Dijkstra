package com.jonathan.sgrouter.routing.utils;

import com.jonathan.sgrouter.routing.models.Node;
import com.jonathan.sgrouter.routing.models.Vertex;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SQLiteHandler {
  Connection conn;

  public SQLiteHandler() {
    try {
      this.conn = DataSource.getConnection();
    } catch (SQLException e) {
      log.error(e.getMessage());
    }
  }

  public ArrayList<Node> getNodes() {
    ArrayList<Node> op = new ArrayList<>();
    try (Statement s = conn.createStatement();
        ResultSet res = s.executeQuery("SELECT * FROM nodes")) {
      while (res.next())
        op.add(new Node(res.getString(1), res.getString(2), res.getDouble(3), res.getDouble(4)));
    } catch (Exception e) {
      log.error(e.getMessage());
    }
    return op;
  }

  public HashMap<String, Double> getFreqs() {
    HashMap<String, Double> op = new HashMap<>();
    try (Statement s = conn.createStatement();
        ResultSet res = s.executeQuery("SELECT * FROM freqs")) {
      while (res.next()) op.put(res.getString(1), res.getDouble(2));
    } catch (Exception e) {
      log.error(e.getMessage());
    }
    return op;
  }

  private ArrayList<Vertex> getVerticesAbstract(PreparedStatement ps, ResultSet res)
      throws Exception {
    ArrayList<Vertex> op = new ArrayList<>();
    res = ps.executeQuery();
    while (res.next())
      op.add(new Vertex(res.getString(1), res.getString(2), res.getString(3), res.getDouble(4)));
    return op;
  }

  public ArrayList<Vertex> getVertices(String src) {
    try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM vertex WHERE src=?");
        ResultSet res = null) {
      ps.setString(1, src);

      return getVerticesAbstract(ps, res);
    } catch (Exception e) {
    }
    return new ArrayList<>();
  }

  public ArrayList<Vertex> getVertices(String src, HashSet<String> walkState) {
    StringBuilder sb = new StringBuilder("SELECT * FROM vertex WHERE src=?");
    for (int i = 0; i < walkState.size(); i++) sb.append(" AND service <> ?");
    try (PreparedStatement ps = conn.prepareStatement(sb.toString());
        ResultSet res = null) {
      ps.setString(1, src);
      Iterator<String> walks = walkState.iterator();
      for (int i = 2; i < walkState.size() + 2; i++) ps.setString(i, walks.next());
      return getVerticesAbstract(ps, res);
    } catch (Exception e) {
    }
    return new ArrayList<>();
  }

  public void close() {
    try {
      conn.close();
    } catch (Exception e) {
    }
  }
}
