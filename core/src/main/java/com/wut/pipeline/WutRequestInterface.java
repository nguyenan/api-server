package com.wut.pipeline;

import com.wut.resources.common.MissingParameterException;

public interface WutRequestInterface {

	// duplicated by authenticationToken()
	//public String getToken() ;
	
	public String getUserId() ;
	
	public String getSetting(String name) ;
	
	public String getId() ;
	
	public String getAuthenticationToken();
	
	public <T> T getParameter(String parameterName) throws MissingParameterException;
}
