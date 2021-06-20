package com.jonathan.sgrouter.routing.utils;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import com.jonathan.sgrouter.routing.RoutingApplication;
import java.nio.file.Paths;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CloudStorageHandler {
  static final String dbPath =
      RoutingApplication.appengineDeployment ? "/tmp/graph.db" : "graph.db";
  static final String bucketName = "sg-router.appspot.com";
  static final String objectName = "graph.db";

  public static void downloadDB() {
    Storage store = StorageOptions.getDefaultInstance().getService();
    Blob b = store.get(BlobId.of(bucketName, objectName));
    b.downloadTo(Paths.get(dbPath));
    log.debug("Downloaded graph.db");
  }
}
