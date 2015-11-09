package com.zcp.vote.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeUtils {
	
	public static String getCurrentDate() {
		Date date = new Date();
		SimpleDateFormat matter = new SimpleDateFormat("yyyy-MM-dd");
		return matter.format(date);
	}
	
	public static void main(String[] args) {
		System.out.println(getCurrentDate());
	}
}
