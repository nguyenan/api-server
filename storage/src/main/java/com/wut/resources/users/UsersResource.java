package com.wut.resources.users;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.wut.pipeline.Authenticator;
import com.wut.pipeline.UserStore;
import com.wut.resources.common.CrudResource;
import com.wut.resources.common.WutOperation;
import com.wut.support.ErrorHandler;
import com.wut.support.settings.SystemSettings;

public class UsersResource extends CrudResource {
	private static final long serialVersionUID = -2367984055220639780L;
	private static UserStore userStore = new UserStore();

	public UsersResource() {
		super("user", userStore);
	}

	@Override
	public boolean initialize() {
		boolean parentInitializationSucceeded = super.initialize();

		// TODO create users as part of some other initiation process
		// format of user info: customer, user, password, token
		String[][] defaultUsers = new String[][] { new String[] { 
				SystemSettings.getInstance().getSetting("defaultUserCustomer"), 
				SystemSettings.getInstance().getSetting("defaultUserUsername"), 
				SystemSettings.getInstance().getSetting("defaultUserPassword"), 
				SystemSettings.getInstance().getSetting("defaultUserToken")
			}
		};
		
		for (String[] userInfo : defaultUsers) {
			String customer = userInfo[0];
			String userId = userInfo[1];
			String password = userInfo[2];
			String token = userInfo[3];
			
			try {
				Map<String, String> newUser = new HashMap<String, String>();
				newUser.put("customer", customer); // TODO rename client (clientId)
				newUser.put("username", userId);
				newUser.put("password", password);
				newUser.put("token", token);
				String key = Authenticator.getUserId(customer, userId);
				
				//AuthenticationHelper helper;
				//Authenticator.update(user);
				//userStore.update(key, newUser);
			} catch (Exception e) {
				ErrorHandler.systemError(e, "failed to initialize default user " + userId);
			}
		}

		return parentInitializationSucceeded;
	}

	public AuthenticateUserWithoutDomain getAuthenticateOperation() {
		return new AuthenticateUserWithoutDomain(getSource());
	}
	
	public ValidateToken getValidateTokenOperation() {
		return new ValidateToken(getSource());
	}
	
	public PingOperation getPingOperation() {
		return new PingOperation(getSource());
	}
	
	@Override
	public UpdateUserOperation getUpdateOperation() {
		return new UpdateUserOperation(getSource());
	}
	
	@Override
	public ReadUserOperation getReadOperation() {
		return new ReadUserOperation(getSource());
	}

	@Override
	public Collection<WutOperation> getOperations() {
		ArrayList<WutOperation> operationList = new ArrayList<WutOperation>();
		operationList.add(getReadOperation());
		operationList.add(getUpdateOperation());
		operationList.add(new ResetUserOperation(getSource()));
		operationList.add(getAuthenticateOperation());
		operationList.add(getValidateTokenOperation());
		operationList.add(getPingOperation());
		return operationList;
	}

}
