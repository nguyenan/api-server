package com.webutilitykit.provider;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.wut.datasource.GoogleAnalyticsDataSource;
import com.wut.model.list.ListData;
import com.wut.model.map.MappedData;
import com.wut.model.scalar.DateData;
import com.wut.model.scalar.StringData;
import com.wut.provider.Provider;

public class AnalyticsProvider implements Provider {
	private GoogleAnalyticsDataSource source;

	public AnalyticsProvider(GoogleAnalyticsDataSource source) {
		this.source = source;
	}
	
	public ListData query(StringData analyticsView, ListData dimensions, ListData metrics, DateData start, DateData end, MappedData filters, ListData sortOrder) {
		String[] dimensionsArray = new String[dimensions.size()];
		List<StringData> dimensionList = dimensions.toList();
		for (int i=0;i<dimensionList.size();i++) {
			String str = dimensionList.get(i).toRawString();
			dimensionsArray[i] = str;
		}
		//dimensionList.toArray(dimensionsArray);
		String[] metricsArray = new String[metrics.size()];
		List<StringData> metricsList = metrics.toList();
		for (int i=0;i<metricsList.size();i++) {
			String str = metricsList.get(i).toRawString();
			metricsArray[i] = str;
		}
		//String[] metricsList = metrics.toList().toArray(new String[] { });
		
		String[] sortList = new String[] { };
		if (sortOrder != null) {
			sortList = new String[sortOrder.size()];
			final List<Object> sortAsAlist = sortOrder.toList();
			for (int i=0;i<sortAsAlist.size();i++) {
				String itemString = String.valueOf(sortAsAlist.get(i));
				sortList[i] = itemString;
			}
			//sortList = sortOrder.toList().toArray(new String[] { });
		}
		Map<String,String> filtersMap = null;
		if (filters != null) {
			filtersMap = new HashMap<String, String>();
			final Map<Object, Object> filtmap = filters.toMap();
			for (Object filter : filtmap.keySet()) {
				String key = String.valueOf(filter);
				String value = String.valueOf(filtmap.get(filter));
				filtersMap.put(key, value);
			}
		}
		List<Map<String, String>> queryResults = source.query(analyticsView.toRawString(), dimensionsArray, metricsArray, start.toYyyyMmDd(), end.toYyyyMmDd(), filtersMap, sortList);
		ListData results = ListData.convert(queryResults);
		return results;
	}
}
