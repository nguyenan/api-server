package com.wut.backgroundjob.payment;

import java.util.Iterator;

import com.wut.model.Data;
import com.wut.model.list.ListData;
import com.wut.model.map.MappedData;
import com.wut.model.map.MessageData;
import com.wut.support.logging.WutLogger;

public class BackgroundJobStats {
	private static WutLogger logger = WutLogger.create(BackgroundJobStats.class);

	public static void main(String[] args) {
		System.out.println(String.format("\n\n------------------%s-------------------", "TODAY"));
		Data todayJobs = BackgroundJobResource.getTodayJob();
		System.out.println(todayJobs);

//		 for (int i = 0; i > -10; i--) {
//		 System.out.println(i + "\t" + BackgroundJobResource.getNextJob(i));
//		 }

		// for (int i = 0; i < 20; i++) {
		// System.out.println(i + "\t" + BackgroundJobResource.getNextJob(i));
		// }
 
		
		for (int i = 0; i < 21; i++) {

			// System.out.println(i + "\t" +
			// BackgroundJobResource.getNextJob(i));
			Data listJobs = BackgroundJobResource.getNextJob(i);
			if (MessageData.NO_DATA_FOUND.equals(listJobs))
				continue;
			ListData jobs = (ListData) listJobs;
			Iterator<? extends Data> it = jobs.iterator();
			while (it.hasNext()) {
				MappedData job = (MappedData) it.next();
				String oldToken = job.get("id").toString();
				String customerId = job.get("customerId").toString();
				System.out.println(i + "\t" + customerId + "\t" + oldToken);
			}
		}
		System.exit(1);
	}
}
