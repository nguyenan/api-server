package com.wut.resources.common;

import java.util.List;

import com.wut.model.Data;
import com.wut.pipeline.WutRequest;
import com.wut.resources.OperationParameter;

// TODO turn this into an interface!!!
public abstract class WutOperation {
	// TODO eliminate these .. use the enum below
	public static String CREATE = "create";
	public static String READ = "read";
	public static String UPDATE = "update";
	public static String DELETE = "delete";
	public static String SEARCH = "search";
	public static String GET_SETTING = "get-setting";
	public static String SET_SETTING = "set-setting";
	
	public enum TYPE { CREATE, READ, UPDATE, DELETE, AUTHENTICATE, GET_SETTING, SET_SETTING }; // TODO rename ACTION
	
	// TODO settings !!!!!!!! it depend on aka uses
	
	// TODO RENAME THIS GetAction()
	// turn this into get TYPE or get action and limit the types!!!! limited operations is better!!! or is it????
	public abstract String getName();
	
	public abstract Data perform(WutRequest request) throws Exception;
	
	public abstract boolean checkPermission(WutRequest request);
	
	public abstract List<OperationParameter> getParameters();
	
//	public List<Parameter> getParameters() {
//		Class<?> claz = this.getClass();
//		List<Parameter> list = new ArrayList<Parameter>();
//		if (claz.isAnnotationPresent(ParamsAnnotation.class)) {
//			ParamsAnnotation params = claz.getAnnotation(ParamsAnnotation.class);
//			for (ParamAnnotation p : params.value()) {
//				Parameter param = new Parameter();
//				param.setName(p.name());
//				param.setOptional(false);
//				param.setType(p.clazz());
//				list.add(param);
//			}
//		}
//		return list;
//	}
	
	//public Collection<String> getSupportedFormats();
	@Override
	public String toString() {
		return getName();
	}
	// public int cost()
	
	// TODO performCache?
	public abstract Data cacheGet(WutRequest request) throws Exception;
	
	// can be executed multiple times without changing the effect (state of the system)
	public abstract boolean isIdempotent();
	
	// returns true if this operation does not change state and therefore has no permanent effect
	public abstract boolean isSafe();
	
	public abstract WutOperation getIdempotentCompanion();

	public boolean cachePut(WutRequest request, Data data) throws MissingParameterException {
		return false;
	}
}
