package com.zcp.vote.utils;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ThreadPoolUtils {
	
	private static Executor pool = Executors.newFixedThreadPool(50);

	public static Executor getPool() {
		return pool;
	}
}
