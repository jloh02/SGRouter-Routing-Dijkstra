package com.jonathan.sgrouter.routing.utils;

import java.util.HashMap;
import java.util.Map;

import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.DatastoreOptions;
import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.Key;

public class DatastoreHandler {
	final static Datastore datastore = DatastoreOptions.getDefaultInstance().getService();

	public static double getWalkSpeed(){
		try {
			Key taskKey = datastore.newKeyFactory().setNamespace("sgrouter").setKind("constants").newKey("walkSpeed");
			Entity retrieved = datastore.get(taskKey);
			return retrieved.getDouble("value");
		} catch (Exception e) {
			System.out.println(e);
			System.exit(1);
		}
		return 0.09;
	}
}
