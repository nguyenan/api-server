package com.wut.backgroundjob.payment;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.wut.backgroundjob.BackgroundJobsStore;
import com.wut.model.Data;

public class BackgroundJobResource {

	private final static String JOB_ID = "squaretoken_";
	private static BackgroundJobsStore bgJob = new BackgroundJobsStore();

	public static void pushRenewTokenJob(String customerId, String accessToken) {
		Map<String, String> mappedData = new HashMap<String, String>();
		mappedData.put("customerId", customerId);
		mappedData.put("id", accessToken);

		pushRenewTokenJob(mappedData);
	}

	public static void pushRenewTokenJob(Map<String, String> mappedData) {
		String jobId = getNextJobId();
		String accessToken = mappedData.get("id");

		bgJob.add(jobId, accessToken, mappedData);
	}

	public static void removeRenewTokenJob(String accessToken) {
		String jobId = getTodayJobId();
		bgJob.delete(jobId, accessToken);
	}

	public static Data getTodayJob() {
		String jobId = getTodayJobId();
		Data listJobs = bgJob.list(jobId);
		return listJobs;
	}
	

	public static Data getTodayJobWithToken(String token) {
		String jobId = getNextJobId();
		Data job = bgJob.read(jobId, token);
		return job;
	}

	public static Data getNextJobWithToken(String token) {
		String jobId = getTodayJobId();
		Data job = bgJob.read(jobId, token);
		return job;
	}

	
	public static Data getNextJob() {
		String jobId = getNextJobId();
		Data listJobs = bgJob.list(jobId);
		return listJobs;
	}

	public static String getTodayJobId() {
		SimpleDateFormat sdfDate = new SimpleDateFormat("yyyyMMdd");
		Date now = new Date();
		String strDate = sdfDate.format(now);
		String jobId = JOB_ID + strDate;
		return jobId;
	}

	public static String getNextJobId() {
		SimpleDateFormat sdfDate = new SimpleDateFormat("yyyyMMdd");

		Calendar cal = Calendar.getInstance();
		//cal.add(Calendar.DATE, 20);
		cal.add(Calendar.DATE, 20);
		Date nextTime = cal.getTime();

		String strDate = sdfDate.format(nextTime);
		String jobId = JOB_ID + strDate;
		return jobId;
	}
}