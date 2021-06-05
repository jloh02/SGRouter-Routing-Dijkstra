package com.jonathan.sgrouter.routing.utils;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import com.jonathan.sgrouter.routing.RoutingApplication;

import java.nio.file.Paths;

public class CloudStorageHandler {
  final static String dbPath = RoutingApplication.config.isAppengineDeployment() ? "/tmp/graph.db" : "graph.db";
  final static String bucketName = "sg-router.appspot.com";
  final static String objectName = "graph.db";

  public static void downloadDB() {
    Storage store = StorageOptions.getDefaultInstance().getService();
    Blob b = store.get(BlobId.of(bucketName, objectName));
    b.downloadTo(Paths.get(dbPath));
    System.out.println("Downloaded graph.db");
  }
}
