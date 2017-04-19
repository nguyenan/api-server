package com.wut.resources.users;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.wut.datasources.CrudSource;
import com.wut.model.Data;
import com.wut.model.map.MappedData;
import com.wut.model.map.MessageData;
import com.wut.model.scalar.StringData;
import com.wut.pipeline.CustomerStore;
import com.wut.pipeline.WutRequest;

// TODO I dont think this operation is used currently
public class UpdateListCustomerOperation extends UserOperation {
	private static CustomerStore customerStore = new CustomerStore();

	public UpdateListCustomerOperation(CrudSource source) {
		super(source);
	}

	@Override
	public String getName() {
		return "updatelist";
	}

	@Override
	public Data perform(WutRequest ri) throws Exception {
		String customer = ri.getStringParameter("customer");
		String username = ri.getStringParameter("username");
		String application = ri.getApplication();

		StringData domains = new StringData("");
		Data result = customerStore.read("tend", application, username);
		if (result.equals(MessageData.NO_DATA_FOUND))
			domains = new StringData(customer);
		else {
			List<String> listCustomers = Arrays.asList(((StringData) result).toRawString().split(","));
			listCustomers = new ArrayList<String>(listCustomers);
			if (listCustomers.contains(customer))
				return MessageData.SUCCESS;
			listCustomers.add(customer);
			domains = new StringData(StringUtils.join(listCustomers, ","));
		}
		MappedData customerValue = new MappedData();
		customerValue.put("value", domains);
		customerValue.put("id", username);
		Data update = customerStore.update("tend", application, username, customerValue.getMapAsPojo());

		return update;
	}

}
