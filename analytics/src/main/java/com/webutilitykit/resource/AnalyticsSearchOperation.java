package com.webutilitykit.resource;

import com.webutilitykit.provider.AnalyticsProvider;
import com.wut.datasource.GoogleAnalyticsDataSource;
import com.wut.model.Data;
import com.wut.model.list.ListData;
import com.wut.model.map.MappedData;
import com.wut.model.scalar.DateData;
import com.wut.model.scalar.StringData;
import com.wut.pipeline.WutRequest;
import com.wut.resources.common.AbstractOperation;
import com.wut.support.settings.SettingsManager;

public class AnalyticsSearchOperation extends AbstractOperation {
	private GoogleAnalyticsDataSource source;// = new GoogleAnalyticsDataSource();
	private AnalyticsProvider provider;// = new AnalyticsProvider(source);
	
	public AnalyticsSearchOperation() {
		super("search");
		source = new GoogleAnalyticsDataSource();
		provider = new AnalyticsProvider(source);
	}

	@SuppressWarnings("unused")
	private static final long serialVersionUID = 1L;

	@Override
	public Data perform(WutRequest request) throws Exception {
		String customer = request.getCustomer();
		StringData analyticsView = new StringData(SettingsManager.getCustomerSettings(customer, "google-analytics-view"));
		ListData dimensions = request.getParameter("dimensions");
		ListData metrics = request.getParameter("metrics");
		StringData start = request.getParameter("start");
		DateData startDate = new DateData(start.toRawString());
		StringData end = request.getParameter("end");
		DateData endDate = new DateData(end.toRawString());
		MappedData filters = request.getParameter("filters", true);
		ListData sortOrder = request.getParameter("sort", true);
		ListData results = provider.query(analyticsView, dimensions, metrics, startDate, endDate, filters, sortOrder);
		return results;
	}

}
