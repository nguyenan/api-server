package com.wut.support.logging;

import java.util.Map;

import com.wut.datasources.CrudSource;
import com.wut.datasources.cassandra.CassandraSource;
import com.wut.model.Data;
import com.wut.model.map.MappedData;
import com.wut.model.map.MessageData;
import com.wut.model.scalar.IdData;
import com.wut.provider.table.DefaultTableProvider;
import com.wut.provider.table.FlatTableProvider;
import com.wut.provider.table.TableProvider;
import com.wut.provider.table.TableResourceProvider;
import com.wut.provider.table.UniqueRowProvider;

public class StackTraceStore implements CrudSource {
	private static TableResourceProvider provider = getProvider();
	private static final IdData stackTraceTable = new IdData("stacktrace");
	private static final IdData user = new IdData("public");

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

	@Override
	public IdData create(String customer, String application, Map<String, String> data) {
		MappedData mappedData = MappedData.convert(data);
		IdData create = provider.create(IdData.create(application), IdData.create(customer), user, stackTraceTable,
				mappedData);
		return create;
	}

	public Data read(String customer, String application, String id) {
		Data data = provider.read(IdData.create(application), IdData.create(customer), user, stackTraceTable,
				new IdData(id), null);
		if (data == null || data.equals(MessageData.NO_DATA_FOUND)) {
			return MessageData.NO_DATA_FOUND;
		} else {
			MappedData mappedData = (MappedData) data;
			return mappedData;
		}
	}

	@Override
	public Data update(String customer, String application, String rowId, Map<String, String> data) {
		return MessageData.NOT_IMPLEMENTED;
	}

	@Override
	public Data delete(String customer, String application, String rowId) {
		return MessageData.NOT_IMPLEMENTED;
	}
}
