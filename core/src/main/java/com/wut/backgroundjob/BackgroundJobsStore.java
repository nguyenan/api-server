package com.wut.backgroundjob;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.wut.datasources.cassandra.CassandraSource;
import com.wut.model.Data;
import com.wut.model.list.ListData;
import com.wut.model.map.MappedData;
import com.wut.model.map.MessageData;
import com.wut.model.scalar.IdData;
import com.wut.provider.table.DefaultTableProvider;
import com.wut.provider.table.FlatTableProvider;
import com.wut.provider.table.TableProvider;
import com.wut.provider.table.TableResourceProvider;
import com.wut.provider.table.UniqueRowProvider;

public class BackgroundJobsStore {
	private static TableResourceProvider provider = getProvider();
	private static final IdData APPLICATION = new IdData("core");
	private static final IdData CUSTOMER = new IdData("backgroundjob");
	private static final IdData USER = new IdData("public");

	public static TableProvider getTableProvider() {
		CassandraSource dynamoDbSource = new CassandraSource();
		DefaultTableProvider defaultTableProvider = new DefaultTableProvider(dynamoDbSource);
		FlatTableProvider flatTableProvider = new FlatTableProvider(defaultTableProvider, "flat2");
		TableProvider uniqueRowProvider = new UniqueRowProvider(flatTableProvider);
		return uniqueRowProvider;
	}

	public static TableResourceProvider getProvider() {
		TableProvider provider = getTableProvider();
		TableResourceProvider tableResourceProvider = new TableResourceProvider(provider);
		return tableResourceProvider;
	}

	public Data read(String jobTableId, String token) {
		Data data = provider.read(APPLICATION, CUSTOMER, USER, new IdData(jobTableId), new IdData(token), null);
		if (data == null || data.equals(MessageData.NO_DATA_FOUND)) {
			return MessageData.NO_DATA_FOUND;
		} else {
			return data;
		}
	}
	
	public Data list(String jobTableId) {
		Data data = provider.read(APPLICATION, CUSTOMER, USER, new IdData(jobTableId), null, null);
		if (data == null || data.equals(MessageData.NO_DATA_FOUND)) {
			return MessageData.NO_DATA_FOUND;
		} else {
			ListData mappedData = (ListData) data;
			if (mappedData.size() == 0)
				return MessageData.NO_DATA_FOUND;
			return mappedData;
		}
	}

	public Data add(String jobTableId, String rowId, Map<String, String> data) {
		MappedData mappedData = MappedData.convert(data);
		Data d = provider.update(APPLICATION, CUSTOMER, USER, new IdData(jobTableId), new IdData(rowId), mappedData);
		return d;
	}

	public Data delete(String jobId, String rowId) {
		Data d = provider.delete(APPLICATION, CUSTOMER, USER, new IdData(jobId), new IdData(rowId));
		return d;
	}

	public static void main(String[] args) {

		SimpleDateFormat sdfDate = new SimpleDateFormat("yyyyMMdd");
		Date now = new Date();
		String strDate = sdfDate.format(now);
		String jobId = "squaretoken" + strDate;
		BackgroundJobsStore bgJob = new BackgroundJobsStore();
		Map<String, String> mappedData = new HashMap<String, String>();
		mappedData.put("customerId", "www.mapiii.com");
		mappedData.put("date", "today");

		// bgJob.add(jobId, "456", mappedData);
		// bgJob.add(jobId, "789", mappedData);
		// System.out.println(bgJob.delete(jobId, "123"));

		Data listJobs = bgJob.list(jobId);
		if (MessageData.NO_DATA_FOUND.equals(listJobs)) {
			System.out.println(MessageData.NO_DATA_FOUND);
			return;
		}
		ListData jobs = (ListData) listJobs;
		Iterator<? extends Data> it = jobs.iterator();
		while (it.hasNext()) {
			MappedData job = (MappedData) it.next();
			System.out.println(job);
			// System.out.println(job.get("id"));
		}
		System.exit(0);
	}

}
