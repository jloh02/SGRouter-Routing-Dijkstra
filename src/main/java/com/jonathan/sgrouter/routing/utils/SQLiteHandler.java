package com.jonathan.sgrouter.routing.utils;

import com.jonathan.sgrouter.routing.models.Node;
import com.jonathan.sgrouter.routing.models.Vertex;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
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

  public HashMap<String, ArrayList<Vertex>> getVertices() {
    HashMap<String, ArrayList<Vertex>> op = new HashMap<>();
    try (Statement s = conn.createStatement();
        ResultSet res = s.executeQuery("SELECT * FROM vertex")) {
      while (res.next()) {
        String src = res.getString(1);
        if (!op.containsKey(src)) op.put(src, new ArrayList<>());
        op.get(src).add(new Vertex(src, res.getString(2), res.getString(3), res.getDouble(4)));
      }
    } catch (Exception e) {
    }
    return op;
  }

  public void close() {
    try {
      conn.close();
    } catch (Exception e) {
    }
  }
}
