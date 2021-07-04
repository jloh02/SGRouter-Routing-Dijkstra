package com.jonathan.sgrouter.routing.utils;

import com.jonathan.sgrouter.routing.models.Node;
import com.jonathan.sgrouter.routing.models.Vertex;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
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
    Statement s = null;
    ResultSet res = null;
    try {
      s = conn.createStatement();
      res = s.executeQuery("SELECT * FROM nodes");
      while (res.next())
        op.add(
            new Node(
                res.getString("src"),
                res.getString("name"),
                res.getDouble("lat"),
                res.getDouble("lon")));
    } catch (Exception e) {
      log.error(e.getMessage());
    } finally {
      cancelTransactions(s, res);
    }
    return op;
  }

  private ArrayList<Vertex> getVerticesAbstract(PreparedStatement ps, ResultSet res)
      throws Exception {
    ArrayList<Vertex> op = new ArrayList<>();
    res = ps.executeQuery();
    while (res.next())
      op.add(
          new Vertex(
              res.getString("src"),
              res.getString("des"),
              res.getString("service"),
              res.getDouble("time")));
    return op;
  }

  public ArrayList<Vertex> getVertices(String src, String prevService) {
    StringBuilder sb = new StringBuilder("SELECT * FROM vertex WHERE src=? AND service<>?");
    if (Utils.isBusService(prevService)) sb.append(" AND service NOT LIKE ?");
    PreparedStatement ps = null;
    ResultSet res = null;
    try {
      ps = conn.prepareStatement(sb.toString());

      ps.setString(1, src);
      ps.setString(2, prevService);
      if (Utils.isBusService(prevService))
        ps.setString(3, String.format("%%%s%%", prevService.split(" \\(")[0]));

      return getVerticesAbstract(ps, res);
    } catch (Exception e) {
    } finally {
      cancelTransactions(ps, res);
    }
    return new ArrayList<>();
  }

  public ArrayList<Vertex> getVertices(String src, HashSet<String> walkState) {
    StringBuilder sb = new StringBuilder("SELECT * FROM vertex WHERE src=?");
    for (int i = 0; i < walkState.size(); i++) sb.append(" AND service NOT LIKE ?");
    PreparedStatement ps = null;
    ResultSet res = null;
    try {
      ps = conn.prepareStatement(sb.toString());
      ps.setString(1, src);
      Iterator<String> walks = walkState.iterator();
      for (int i = 2; i < walkState.size() + 2; i++)
        ps.setString(i, String.format("%%%s%%", walks.next()));
      return getVerticesAbstract(ps, res);
    } catch (Exception e) {
    } finally {
      cancelTransactions(ps, res);
    }
    return new ArrayList<>();
  }

  void cancelTransactions(Statement s, ResultSet res) {
    try {
      res.close();
    } catch (Exception e) {
    }
    try {
      s.close();
    } catch (Exception e) {
    }
  }

  public void close() {
    try {
      conn.close();
    } catch (Exception e) {
    }
  }
}
