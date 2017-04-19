package com.wut.resources.users;

import com.wut.datasources.CrudSource;
import com.wut.model.Data;
import com.wut.model.map.MappedData;
import com.wut.model.map.MessageData;
import com.wut.model.message.ErrorData;
import com.wut.model.scalar.StringData;
import com.wut.pipeline.Authenticator;
import com.wut.pipeline.CustomerStore;
import com.wut.pipeline.WutRequest;

public class AuthenticateUser extends UserOperation {
	private static CustomerStore domainsStore = new CustomerStore();
	public AuthenticateUser(CrudSource source) {
		super(source);
	}
	
	@Override
	public String getName() {
		return "authenticate";
	}

	@Override
	public Data perform(WutRequest ri) throws Exception {
		String customer = ri.getStringParameter("customer");
		String username = ri.getStringParameter("username");
		String requestPassword = ri.getStringParameter("password");
		String application = ri.getApplication();
		
		String id = Authenticator.getUserId(customer, username);
		
		MappedData credentials = (MappedData) source.readSecureInformation(customer, application, id);
		
		if (credentials == null || credentials.get("password") == null) {
			return MessageData.UNKNOWN_USER;
		}

		String actualPassword = credentials.get("password").toString();
		if (requestPassword.equals(actualPassword)) {
			if (!customer.equals("tend")) // Storefront user
				return newToken(customer, username, requestPassword);
			else { // Admin user	
				Data listDomains = domainsStore.read("tend", application, username);
				if (listDomains.equals(MessageData.NO_DATA_FOUND))
					return MessageData.NO_DATA_FOUND;
				String[] listCustomers =  ((StringData) listDomains).toRawString().split(",");
				MappedData domainMap = new MappedData();
				for (String domain : listCustomers){
					domainMap.put(domain,  newToken(domain, username, requestPassword));
				}
				return domainMap;
			}
		} else {
			return ErrorData.INVALID_LOGIN;
		}
		
	}
	
}