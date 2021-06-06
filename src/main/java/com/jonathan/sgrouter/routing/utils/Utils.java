package com.jonathan.sgrouter.routing.utils;

public class Utils {
	public static boolean isBusService(String s) {
		return s.matches("^(\\d{1,3}[ABCEGMNRWXe]?)|(NR\\d)|(CT8)|(CT18)");
	}

	public static boolean isBusStop(String s) {
		return s.matches("^\\d{5}");
	}

	public static boolean isLRT(String s) {
		return s.matches("(^BP\\d{1,2}$)|(^STC$)|(^PTC$)|(^SE\\d{1,2}$)|(^SW\\d{1,2}$)|(^PE\\d{1,2}$)|(^PW\\d{1,2}$)");
	}
}
