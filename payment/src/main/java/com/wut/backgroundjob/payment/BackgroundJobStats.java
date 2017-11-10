package com.wut.backgroundjob.payment;

import com.wut.backgroundjob.BackgroundJobsStore;
import com.wut.model.Data;

public class BackgroundJobStats {

	private static BackgroundJobsStore bgJob = new BackgroundJobsStore();

	public static void main(String[] args) {
		System.out.println(String.format("\n\n------------------%s-------------------", "TODAY"));
		Data todayJobs = BackgroundJobResource.getTodayJob();
		System.out.println(todayJobs);

//		System.out.println(String.format("\n\n------------------%s-------------------", "UPCOMING"));
//		Data tmrJobs1 = BackgroundJobResource.getNextJob(1);
//		System.out.println(tmrJobs1);
//		Data tmrJobs2 = BackgroundJobResource.getNextJob(2);
//		System.out.println(tmrJobs2);
//		Data tmrJobs3 = BackgroundJobResource.getNextJob(3);
//		System.out.println(tmrJobs3);
//
//		System.out.println(String.format("\n\n------------------%s-------------------", "NEXT " + BackgroundJobResource.DAYS_OF_EXPIRE + " DAYS"));
//		Data nextJobs = BackgroundJobResource.getNextJob(BackgroundJobResource.DAYS_OF_EXPIRE);
//
//		System.out.println(nextJobs); 
	}
}
