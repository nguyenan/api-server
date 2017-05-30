package com.wut.resources.permission;

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
import com.wut.model.scalar.BooleanData;
import com.wut.model.scalar.StringData;
import com.wut.pipeline.Authenticator;
import com.wut.pipeline.WutRequest;
import com.wut.support.settings.SystemSettings;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;
import io.swagger.annotations.AuthorizationScope;


@Path("/permission")
@Api(value = "permission", authorizations = { @Authorization(value = "tend_authenticate", scopes = {
		@AuthorizationScope(scope = "update:permission", description = "Update Permission data") }) 
}, tags = "permission")
@Produces({ MediaType.APPLICATION_JSON })
public class UpdatePermissionOperation extends PermissionOperation {
	private static final String adminCustId = SystemSettings.getInstance().getSetting("admin.customerid");

	public UpdatePermissionOperation(CrudSource source) {
		super(source);
	}

	@Override
	public String getName() {
		return "update";
	}

	@POST
	@Path("/operation=update")
	  @ApiOperation(value = "Update Permission data, only authenticated users which have permission on affectedCustomer are allowed to update", 
	    notes = "Returns a Mapped Data", 
	    response = Data.class,
	    authorizations = @Authorization(value = "token")
	  )
	  @ApiResponses(value = { @ApiResponse(code = 5013, message = "Invalid Token"),
			  @ApiResponse(code = 180, message = "invalid permissions"),
			  @ApiResponse(code = 7010, message = "invalid role")})
	@Override
	public Data perform(WutRequest ri) throws Exception {
		String token = ri.getToken();
		String requestingCustomer = ri.getCustomer();
		String requestingUser = ri.getUserId();
		String requestingUserId = Authenticator.getUserId(adminCustId, requestingUser);

		String affectedCustomer = ri.getStringParameter("customer");
		String affectedUser = ri.getStringParameter("username");
		String affectedUserId = Authenticator.getUserId(adminCustId, affectedUser);
		String role = ri.getStringParameter("role");
		String application = ri.getApplication();
		
		// only authenticated users are allowed to access this feature
		Authenticator authenticator = new Authenticator();
		boolean isTokenValid = authenticator.validateToken(requestingCustomer, requestingUser, token);
		if (!isTokenValid) {
			return ErrorData.INVALID_TOKEN;
		}

		// check if requestingUser has permission on affectedCustomer
		Data d = source.read(adminCustId, application, requestingUserId, affectedCustomer);
		if (MessageData.NO_DATA_FOUND.equals(d)) {
			return ErrorData.INVALID_PERMISSIONS;
		}
		
		if (!PermissionRole.contains(role))
			return ErrorData.ROLE_INVALID;

		// do update
		MappedData permissionData = (MappedData) source.read(adminCustId, application, affectedUserId);
		if (permissionData.equals(MessageData.NO_DATA_FOUND))
			permissionData = new MappedData();
		permissionData.put(affectedCustomer, new StringData(role));

		BooleanData update = (BooleanData) source.update(adminCustId, application, affectedUserId,
				permissionData.getMapAsPojo());
		
		return MessageData.successOrFailure(update);
	}
}
