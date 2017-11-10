package com.wut.backgroundjob.payment;

import com.wut.model.Data;
import com.wut.support.logging.WutLogger;

public class BackgroundJobStats {
	private static WutLogger logger = WutLogger.create(BackgroundJobStats.class);
	public static void main(String[] args) {
		System.out.println(String.format("\n\n------------------%s-------------------", "TODAY"));
		Data todayJobs = BackgroundJobResource.getTodayJob();
		System.out.println(todayJobs);
		 
		System.exit(1);
	}
}
