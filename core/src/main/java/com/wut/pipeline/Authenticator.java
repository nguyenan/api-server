package com.wut.pipeline;

import java.util.HashMap;
import java.util.Map;

import com.wut.datasources.TableRowSource;
import com.wut.datasources.jdbc.derby.Derby;
import com.wut.model.Data;
import com.wut.model.map.MappedData;
import com.wut.model.map.MessageData;
import com.wut.model.message.ErrorData;
import com.wut.model.scalar.IdData;
import com.wut.support.ErrorHandler;

// TODO rename AuthenticationProcessor
// TODO rename Token Something -- This only checks, creates, updates TOKENS!!!
public class Authenticator extends AbstractProcessor {
	// Token -> User
	private static Map<String, Map<String,String>> userTokens = new HashMap<String, Map<String,String>>();
	//private static TableRowSource tokens = new Derby();
	
	public Authenticator() {
		//UserStore userStore = new UserStore();
		//authHelper = new AuthenticationHelper(userStore);
		//ResourceFactory resourceFactory = ResourceFactory.getInstance();
		//UsersResource user = (UsersResource) resourceFactory.getResource("user");
		//authOp = user.getAuthenticateOperation();
	}
	
	// THIS CHECKS A TOKEN
	@Override
	public boolean process(WutRequest request, WutResponse response) {
		// if authentication request, let it go through
		if (request.isAuthenticationRequest() || request.isValidateTokenRequest()) {
			return true; // continue
		}
		
		// TODO can this be moved into the User resource as a "verify" action
		
		// else verify authentication token
		String token = request.getAuthenticationToken();
		String customer = null;
		String username = null;
		try {
			customer = request.getCustomer();
			username = request.getUserId();
		} catch (Exception e) {
			ErrorHandler.userError("missing user identification");
		}
		if (customer == null || username == null || token == null) {
			response.setError(ErrorData.INVALID_LOGIN);
			return false; // stop
		} else if (username.equals("public") && token.equals("public")) { // TODO this is odd & new
			User publicUser = User.getPublicUser(customer);
			request.setUser(publicUser);
			return true; // continue
		} else if (checkToken(customer, username, token)) {
			//User user = authHelper.getUserObj(customer, username); // TODO rename method
			
			//Map<String,String> userData = tokens.get("tokens", token);
			User user = getUser(token);
			
			//User user = userTokens.get(token);
			
			// TODO fix this so request can be immutable again
			// TODO load user instead
			request.setUser(user);
			// authentication success
			return true; // continue
		} else if (request.getAction() == null) { // why is this needed?
			response.setError(ErrorData.MISSING_OPERATION);
			return false; // stop
		} else {
			response.setError(ErrorData.INVALID_TOKEN);
			return false;
		}
	}
	
	// return true if valid token
	// used by authenticator in processing pipeline
	private boolean checkToken(String customer, String username, String token) { // TODO what about customer!!!!
		if (customer == null || username == null || token == null) {
			return false;
		} else {
			// TODO why can't this just come first again????? this seems fine to me
			if ("public".equals(username) && customer != null) {
				return "public".equals(token);
			}
			
//			//String id = Authenticator.getUserId(customer, username);
//			//MappedData user = (MappedData) source.readSecureInformation(id); // TODO this needs to read user id!!!
//			if ("public".equals(String.valueOf(user.get("username")))) { // TODO need to check that client/customer exists
//				return "public".equals(token);
//			} else {
//				Data serverToken = user.get("token");
//				if (serverToken != null) {
//					String serverTokenStr = serverToken.toString();
//					return token.equals(serverTokenStr);
//				}
//			}
			
			System.out.println("*** requesting token " + token);
			User user = getUser(token);
			System.out.println("*** got " + String.valueOf(user));
			if (user != null) {
				String serverTokenStr = user.getToken();
				return token.equals(serverTokenStr);
			}
		}
		return false;
	}
	
	public User validateToken( String token) { // TODO what about customer!!!!			
		System.out.println("*** validate token " + token);
		User user = getUser(token);
		System.out.println("*** got " + String.valueOf(user));
		if (user == null) 
			return null;
		
		if (!token.equals(user.getToken())) 
			return null;	
		return user;
	}
	
	// TODO name setToken
	public static void updateUser(User user) {
		Map<String,String> userMap = new HashMap<String, String>();
		userMap.put("firstName", user.getFirstName());
		userMap.put("lastName", user.getLastName());
		userMap.put("username", user.getUsername());
		userMap.put("id", user.getId());
		userMap.put("password", user.getPassword());
		userMap.put("token", user.getToken());
		userMap.put("customer", user.getCustomer());
		
		String token = user.getToken();
		//boolean updated = tokens.crupdate("tokens", token, userMap);
		userTokens.put(token, userMap);
//		if (!updated) {
//			ErrorHandler.fatalError("unable to save new token");
//		}
	}

	public static User getUser(String token) {
		//Map<String,String> userData = tokens.get("tokens", token);
		Map<String,String> userData = userTokens.get(token);
		if (userData != null) {
			User user = new User();
			user.setFirstName(userData.get("firstName"));
			user.setLastName(userData.get("lastName"));
			user.setUsername(userData.get("username"));
			user.setId(userData.get("id"));
			user.setPassword(userData.get("password"));
			user.setToken(userData.get("token"));
			user.setCustomer(userData.get("customer"));
			return user;
		}
		return null;
	}
	
	/*
	public Data authenticate(String customer, String username, String requestPassword) {
		String id = Authenticator.getUserId(customer, username);
		
		MappedData credentials = (MappedData) source.readSecureInformation(id);
		
		if (credentials == null || credentials.get("password") == null) {
			return MessageData.UNKNOWN_USER;
		}

		String actualPassword = credentials.get("password").toString();
		if (requestPassword.equals(actualPassword)) {
			return newToken(customer, username, requestPassword);
		} else {
			return ErrorData.INVALID_LOGIN;
		}
	}
	*/
	
	public static String getUserId(String customer, String username) {
		return customer + ":" + username;
	}
}
