package com.jonathan.sgrouter.routing.utils;

public class Utils {
  public static boolean isBusService(String s) {
    return s.matches("^(\\d{1,3}[ABCEGMNRWXe]?)|(NR\\d)|(CT8)|(CT18)");
  }

  public static boolean isBusStop(String s) {
    return s.matches("^\\d{5}");
  }

  public static String getLatLonWKT() {
    return "GEOGCS[\"WGS 84\",DATUM[\"WGS_1984\",SPHEROID[\"WGS"
               + " 84\",6378137,298.257223563,AUTHORITY[\"EPSG\",\"7030\"]],AUTHORITY[\"EPSG\",\"6326\"]],PRIMEM[\"Greenwich\",0,AUTHORITY[\"EPSG\",\"8901\"]],UNIT[\"degree\",0.01745329251994328,AUTHORITY[\"EPSG\",\"9122\"]],AUTHORITY[\"EPSG\",\"4326\"]]";
  }
}
