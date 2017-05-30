package com.wut.resources.permission;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import com.wut.datasources.CrudSource;
import com.wut.model.Data;
import com.wut.model.message.ErrorData;
import com.wut.pipeline.Authenticator;
import com.wut.pipeline.WutRequest;
import com.wut.support.settings.SystemSettings;
import javax.ws.rs.core.MediaType;
import io.swagger.annotations.*;

@Path("/permission")
@Api(value = "permission", authorizations = { @Authorization(value = "tend_authenticate", scopes = {
		@AuthorizationScope(scope = "read:permission", description = "Read Permission data") }) 
}, tags = "permission")
@Produces({ MediaType.APPLICATION_JSON })

public class ReadPermissionUserOperation extends PermissionOperation {
	private static final String adminCustId = SystemSettings.getInstance().getSetting("admin.customerid");

	public ReadPermissionUserOperation(CrudSource source) {
		super(source);
	}

	@Override
	public String getName() {
		return "read";
	}

	@POST
	@Path("/operation=read")
	  @ApiOperation(value = "Get Permission data, only authenticated users are allowed to access this feature", 
	    notes = "Returns a Mapped Data", 
	    response = Data.class,
	    authorizations = @Authorization(value = "token")
	  )
	  @ApiResponses(value = { @ApiResponse(code = 5012, message = "Invalid Login")})
	@Override
	public Data perform(WutRequest ri) throws Exception {
		String token = ri.getToken();
		String requestingCustomer = ri.getCustomer();
		String requestingUser = ri.getUserId();

		String affectedCustomer = ri.getStringParameter("customer");
		String affectedUser = ri.getStringParameter("username");

		String application = ri.getApplication();
		String affectedUserId = Authenticator.getUserId(adminCustId, affectedUser);

		// only authenticated users are allowed to access this feature
		Authenticator authenticator = new Authenticator();
		boolean isTokenValid = authenticator.validateToken(requestingCustomer, requestingUser, token);
		if (!isTokenValid) {
			return ErrorData.INVALID_TOKEN;
		}
		// read Permission Data
		Data data = source.read(adminCustId, application, affectedUserId, affectedCustomer);
		return data;
	}

}
