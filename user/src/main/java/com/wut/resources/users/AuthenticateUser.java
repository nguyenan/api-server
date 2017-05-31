package com.wut.resources.users;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.wut.datasources.CrudSource;
import com.wut.model.Data;
import com.wut.model.PermissionRole;
import com.wut.model.map.MappedData;
import com.wut.model.map.MessageData;
import com.wut.model.message.ErrorData;
import com.wut.model.scalar.ScalarData;
import com.wut.pipeline.Authenticator;
import com.wut.pipeline.PermissionStore;
import com.wut.pipeline.WutRequest;
import com.wut.support.settings.SystemSettings;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;
import io.swagger.annotations.AuthorizationScope;

@Path("/user")
@Api(value = "user", authorizations = { @Authorization(value = "tend_authenticate", scopes = {
		@AuthorizationScope(scope = "authenticate:user", description = "") }) 
}, tags = "user")
@Produces({ MediaType.APPLICATION_JSON })
public class AuthenticateUser extends UserOperation {
	private static PermissionStore permissionStore = new PermissionStore();
	private static final String adminCustId = SystemSettings.getInstance().getSetting("admin.customerid");

	public AuthenticateUser(CrudSource source) {
		super(source);
	}

	@Override
	public String getName() {
		return "authenticate";
	}

	@POST
	@Path("/operation=authenticate")
	  @ApiOperation(value = "After an Admin user login successful", 
	    notes = "Generate tokens for ALL farms this user has admin permission. Returns a Mapped Data", 
	    response = Data.class,
	    authorizations = @Authorization(value = "token")
	  )
	  @ApiResponses(value = { 
			  @ApiResponse(code = 5012, message = "Invalid Login"),
			  @ApiResponse(code = 7001, message = "account doesn't have permission to access any customer")})
	@Override
	public Data perform(WutRequest ri) throws Exception {
		String customer = ri.getStringParameter("customer");
		String username = ri.getStringParameter("username");
		String userId = Authenticator.getUserId(adminCustId, username);
		String requestPassword = ri.getStringParameter("password");
		String application = ri.getApplication();

		String id = Authenticator.getUserId(customer, username);

		MappedData credentials = (MappedData) source.readSecureInformation(customer, application, id);

		if (credentials == null || credentials.get("password") == null) {
			return MessageData.UNKNOWN_USER;
		}

		String actualPassword = credentials.get("password").toString();
		if (requestPassword.equals(actualPassword)) {
			if (!customer.equals(adminCustId)) // Storefront user
				return newToken(customer, username, requestPassword);
			else { // Admin user

				Data permissions = permissionStore.read(adminCustId, application, userId);
				if (permissions.equals(MessageData.NO_DATA_FOUND))
					return ErrorData.INVALID_ACCESS;
				MappedData mappedData = (MappedData) permissions;
				MappedData customerIdMap = new MappedData();
				for (ScalarData key : mappedData.keys()) {
					Data role = mappedData.get(key);
					if (PermissionRole.contains(role.toString()))
						customerIdMap.put(key, newToken(key.toRawString(), username, requestPassword));
				}
				return customerIdMap;
			}
		} else {
			return ErrorData.INVALID_LOGIN;
		}
	}
}