package com.wut.resources.users;

//import java.util.Map;
//
//import com.wut.datasources.CrudSource;
//import com.wut.model.Data;
//import com.wut.model.map.MessageData;
//import com.wut.pipeline.WutRequest;
//import com.wut.resources.common.WutOperation;

// TODO this is not used anywhere any more.... remove this class
public class CreateUser /*extends WutOperation*/ {
//	private static final String[] fieldNames = new String[] { "id", "name", "password" };
//	public FieldSet fields = new FieldSet(fieldNames);
//	private UserStore source;
//
//	public CreateUser(CrudSource source) {
//		this.source = (UserStore) source;
//	}
//
//	@Override
//	public String getName() {
//		return "create";
//	}
//
//	@Override
//	public Data perform(WutRequest request) throws Exception {
//		// TODO ensure that current user has the ability to create users
//		if (!request.getUser().getUsername().equals("admin")) {
//			return MessageData.INVALID_PERMISSIONS;
//		}
//		
//		Map<String, String> data = fields.getMap(request);
//		
//		String customer = request.getCustomer();
//		data.put("customer", customer);
//
//		Data id = source.create(data);
//		MessageData successMsg = new MessageData(MessageData.success());
//		successMsg.setData(id); // TODO how does this work
//		// TODO dont we need to call newToken()
//
//		return successMsg;
//	}
//	
	
}