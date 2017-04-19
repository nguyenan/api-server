package com.wut.resources.users;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.wut.datasources.CrudSource;
import com.wut.model.Data;
import com.wut.model.map.MappedData;
import com.wut.model.map.MessageData;
import com.wut.model.message.ErrorData;
import com.wut.model.scalar.StringData;
import com.wut.pipeline.CustomerStore;
import com.wut.pipeline.WutRequest;
import com.wut.support.settings.SystemSettings;

// TODO I dont think this operation is used currently
public class UpdateListCustomerOperation extends UserOperation {
	private static CustomerStore customerStore = new CustomerStore();
	private static String adminCustomerId = SystemSettings.getInstance().getSetting("admin.customerid");
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
		Data result = customerStore.read(adminCustomerId, application, username);
		if (result.equals(MessageData.NO_DATA_FOUND))
			domains = new StringData(customer);
		else {
			List<String> listCustomers = Arrays.asList(((StringData) result).toRawString().split(","));
			listCustomers = new ArrayList<String>(listCustomers);
			if (listCustomers.contains(customer))
				return ErrorData.CUSTOMER_EXISTED;
			listCustomers.add(customer);
			domains = new StringData(StringUtils.join(listCustomers, ","));
		}
		MappedData customerValue = new MappedData();
		customerValue.put("value", domains);
		customerValue.put("id", username);
		Data update = customerStore.update(adminCustomerId, application, username, customerValue.getMapAsPojo());

		return update;
	}

}
