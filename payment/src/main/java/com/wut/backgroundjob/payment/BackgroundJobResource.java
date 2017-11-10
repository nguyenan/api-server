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
	public final static Integer DAYS_OF_EXPIRE = 20;
	private static BackgroundJobsStore bgJob = new BackgroundJobsStore();

	public static void pushRenewTokenJob(String customerId, String accessToken) {
		String jobId = getNextJobId(DAYS_OF_EXPIRE);
		Map<String, String> mappedData = new HashMap<String, String>();
		mappedData.put("customerId", customerId);
		mappedData.put("id", accessToken);
		bgJob.add(jobId, accessToken, mappedData);
	}

	public static void removeJob(String accessToken) {
		String jobId = getTodayJobId();
		bgJob.delete(jobId, accessToken);
	}
	public static void removeJob(String accessToken, int numOfDays) {
		String jobId = getNextJobId(numOfDays);
		bgJob.delete(jobId, accessToken);
	}

	public static Data getTodayJobWithToken(String token) {
		String jobId = getTodayJobId();
		Data job = bgJob.read(jobId, token);
		return job;
	}

	public static Data getTodayJob() {
		String jobId =  getTodayJobId();
		Data listJobs = bgJob.list(jobId);
		return listJobs;
	}

	public static Data getNextJobWithToken(String token) {
		String jobId = getTodayJobId();
		Data job = bgJob.read(jobId, token);
		return job;
	}

	public static Data getNextJob() {
		String jobId = getNextJobId(DAYS_OF_EXPIRE);
		Data listJobs = bgJob.list(jobId);
		return listJobs;
	}

	public static Data getNextJob(int numOfDays) {
		String jobId = getNextJobId(numOfDays);
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

	public static String getNextJobId(int numOfDays) {
		SimpleDateFormat sdfDate = new SimpleDateFormat("yyyyMMdd");

		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, numOfDays);
		Date nextTime = cal.getTime();

		String strDate = sdfDate.format(nextTime);
		String jobId = JOB_ID + strDate;
		return jobId;
	}
}
