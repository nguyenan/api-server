package com.wut.backgroundjob.payment;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import com.wut.support.logging.WutLogger;

@WebListener
public class BackgroundJobManager implements ServletContextListener {

	private ScheduledExecutorService scheduler;
	private static WutLogger logger = WutLogger.create(BackgroundJobManager.class);

	@Override
	public void contextInitialized(ServletContextEvent event) {
		scheduler = Executors.newSingleThreadScheduledExecutor();
		scheduler.scheduleAtFixedRate(new RenewTokenJob(), 0, 1, TimeUnit.DAYS);
	}

	@Override
	public void contextDestroyed(ServletContextEvent event) {
		scheduler.shutdown();
		try {
			scheduler.awaitTermination(1, TimeUnit.DAYS);
		} catch (InterruptedException ex) {
			logger.error(ex.getMessage());
		}
	}

}
