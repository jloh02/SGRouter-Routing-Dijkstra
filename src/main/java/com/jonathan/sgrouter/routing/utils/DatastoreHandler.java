package com.jonathan.sgrouter.routing.utils;

import com.google.cloud.Timestamp;
import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.DatastoreOptions;
import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.Key;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
public class DatastoreHandler {
  static final Datastore datastore = DatastoreOptions.getDefaultInstance().getService();
  static long lastCheckedMod = 0;

  public static double getWalkSpeed() {
    try {
      Key taskKey =
          datastore
              .newKeyFactory()
              .setNamespace("sgrouter")
              .setKind("constants")
              .newKey("walkSpeed");
      Entity retrieved = datastore.get(taskKey);
      return retrieved.getDouble("value");
    } catch (Exception e) {
      log.error(e.getMessage());
      throw new ResponseStatusException(
          HttpStatus.INTERNAL_SERVER_ERROR, "Unable to calculate walking speed");
    }
  }

  public static boolean getModRecent() {
    long now = Timestamp.now().getSeconds();

    if (now - lastCheckedMod < 15 * 60) return true;

    Key taskKey =
        datastore
            .newKeyFactory()
            .setNamespace("sgrouter")
            .setKind("constants")
            .newKey("graph-last-modified");
    Entity retrieved = datastore.get(taskKey);

    long lastCheckedMod = retrieved.getTimestamp("value").getSeconds();

    return now - lastCheckedMod < 15 * 60;
  }
}
