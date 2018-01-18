package com.wut.backgroundjob.payment;

import java.util.Iterator;

import com.wut.datasources.square.SquareSource;
import com.wut.model.Data;
import com.wut.model.list.ListData;
import com.wut.model.map.MappedData;
import com.wut.model.map.MessageData;
import com.wut.model.scalar.IntegerData;
import com.wut.support.logging.WutLogger;
import com.wut.support.settings.SettingsManager;

public class RenewTokenJob implements Runnable {
	private static WutLogger logger = WutLogger.create(RenewTokenJob.class);

	@Override
	public void run() {
		logger.info("Start RenewTokenJob");
		Data listJobs = BackgroundJobResource.getTodayJob();
		if (MessageData.NO_DATA_FOUND.equals(listJobs))
			return;
		
		ListData jobs = (ListData) listJobs;
		Iterator<? extends Data> it = jobs.iterator();
		logger.info("List jobs: " + jobs);
		while (it.hasNext()) {
			MappedData job = (MappedData) it.next();
			logger.info("Processing: " + job);
			
			String oldToken = job.get("id").toString();
			String customerId = job.get("customerId").toString();

			SquareSource square = new SquareSource();
			MappedData result = square.renewAccessToken(customerId, oldToken);
			logger.info("Result: " + result.toString());
			Integer code = ((IntegerData) result.get("code")).getInteger();
			if (code.equals(MessageData.SUCCESS.getCode())) {
				String newToken = result.get("access_token").toString();
				
				// save new settings
				SettingsManager.setClientSettings(customerId, SquareSource.ACCESS_TOKEN_SETTING, newToken);
				
				// put new job to queue
				BackgroundJobResource.pushRenewTokenJob(customerId, newToken);
				BackgroundJobResource.removeJob(oldToken);
			} else
				logger.error(result.toString());

		}
		logger.info("End RenewTokenJob");
	}
}