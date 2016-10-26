package com.wut.datasources.aws;

//import java.util.Collections;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.TreeMap;

//import com.amazonaws.sdb.AmazonSimpleDB;
//import com.amazonaws.sdb.AmazonSimpleDBClient;
//import com.amazonaws.sdb.AmazonSimpleDBException;
//import com.amazonaws.sdb.model.Attribute;
//import com.amazonaws.sdb.model.CreateDomainRequest;
//import com.amazonaws.sdb.model.CreateDomainResponse;
//import com.amazonaws.sdb.model.DeleteAttributesRequest;
//import com.amazonaws.sdb.model.DeleteAttributesResponse;
//import com.amazonaws.sdb.model.GetAttributesRequest;
//import com.amazonaws.sdb.model.GetAttributesResponse;
//import com.amazonaws.sdb.model.GetAttributesResult;
//import com.amazonaws.sdb.model.ListDomainsRequest;
//import com.amazonaws.sdb.model.ListDomainsResponse;
//import com.amazonaws.sdb.model.ListDomainsResult;
//import com.amazonaws.sdb.model.PutAttributesRequest;
//import com.amazonaws.sdb.model.PutAttributesResponse;
//import com.amazonaws.sdb.model.ReplaceableAttribute;
//import com.amazonaws.sdb.model.ResponseMetadata;
//import com.wut.support.UniqueIdGenerator;

// TODO: Rename AmazonSimpleDB
public class AmazonSimple {
//	private static final String accessKeyId = ""; // TODO removed key for security
//	private static final String secretAccessKey = ""; // TODO removed key for security
//	private AmazonSimpleDB service = new AmazonSimpleDBClient(accessKeyId,secretAccessKey);
//	//private AmazonSimpleDB service = new AmazonSimpleDBMock();
//
//	public static void main(String[] args) throws AmazonSimpleDBException {
//		final String domain = "flat";
//		
//		AmazonSimple as = new AmazonSimple();
//		as.listDomains();
//		as.createDomain(domain);
//		as.listDomains();
//		
//		Map<String, String> attributes = new TreeMap<String, String>();
//		attributes.put("color", "red");
//		attributes.put("mood", "happy");
//		attributes.put("major", "csc");
//		attributes.put("smoker", "no");
//		as.putAttributes("funky", "chicken", attributes);
//		
//		as.getItem(domain, "chicken");
//
//	}
//
//	public Map<String, String> getItem(String domain, String objectName) throws AmazonSimpleDBException {
//		GetAttributesRequest request = new GetAttributesRequest();
//		
//		request.setDomainName(domain);
//		request.setItemName(objectName);
//		
//		GetAttributesResponse response = service.getAttributes(request);
//		if (response.isSetGetAttributesResult()) {
//			GetAttributesResult  getAttributesResult = response.getGetAttributesResult();
//			List<Attribute> attributeList = getAttributesResult.getAttribute();
//			Map<String, String> properList = new HashMap<String,String>();
//			for (Attribute attribute : attributeList) {
//				if (attribute.isSetName()) {
//					final String name = attribute.getName();
//					final String value = attribute.getValue();
//					properList.put(String.valueOf(name), String.valueOf(value));
//					System.out.print("Name:" + name + " ");
//					System.out.println("Value:" + value);
//				}
//			}
//			
//			if (response.isSetResponseMetadata()) {
//				ResponseMetadata  responseMetadata = response.getResponseMetadata();
//				System.out.println("RequestId:" + responseMetadata.getRequestId());
//				System.out.println("BoxUsage:" + responseMetadata.getBoxUsage());
//			}
//			
//			return properList;
//		}
//		return null;
//	}
//
//	public boolean putAttributes(String domain, String objectName,
//			Map<String, String> attributes) throws AmazonSimpleDBException {
//		
//        PutAttributesRequest request = new PutAttributesRequest();
//        
//        request.setDomainName(domain);
//        
//        request.setItemName(objectName);
//        
//        for (String key : attributes.keySet()) {
//        	String value = attributes.get(key);
//        	ReplaceableAttribute replaceableAttribute = new ReplaceableAttribute(key, value, true);
//			request.setAttribute(Collections.singletonList(replaceableAttribute));
//        }
//        
//		PutAttributesResponse response = service.putAttributes(request);
//		
//		if (response.isSetResponseMetadata()) {
//			ResponseMetadata  responseMetadata = response.getResponseMetadata();
//			System.out.println("RequestId:" + responseMetadata.getRequestId());
//			System.out.println("BoxUsage:" + responseMetadata.getBoxUsage());
//		}
//		
//		return true;
//	}
//
//	public boolean createDomain(String name) throws AmazonSimpleDBException {
//		CreateDomainRequest request = new CreateDomainRequest();
//		request.withDomainName(name);
//
//		CreateDomainResponse response = service.createDomain(request);
//		ResponseMetadata responseMetadata = response.getResponseMetadata();
//		if (responseMetadata.isSetRequestId()) {
//			System.out.println("RequestId:" + responseMetadata.getRequestId());
//		}
//		if (responseMetadata.isSetBoxUsage()) {
//			System.out.println("BoxUsage:" + responseMetadata.getBoxUsage());
//		}
//		return response.isSetResponseMetadata();
//	}
//
//	public List<String> listDomains() throws AmazonSimpleDBException {
//		ListDomainsRequest request1 = new ListDomainsRequest();
//
//		ListDomainsResponse response1 = service.listDomains(request1);
//
//		ListDomainsResult listDomainsResult = response1.getListDomainsResult();
//
//		List<String> domainNameList = listDomainsResult.getDomainName();
//		System.out.println("DOMAINS:");
//		for (String domainName : domainNameList) {
//			System.out.println("NAME:" + domainName);
//		}
//		return domainNameList;
//	}
//
//	public boolean deleteDomain(String table) {
//		// TODO Auto-generated method stub
//		return false;
//	}
//
//	public String createRecord(String table, Map<String, String> attrs) throws AmazonSimpleDBException {
//		String id = UniqueIdGenerator.getId();
//		boolean success = putAttributes(table, id, attrs);
//		return success ? id : null;
//	}
//	
//	public boolean deleteRecord(String table, String id) throws AmazonSimpleDBException {
//		DeleteAttributesRequest request = new DeleteAttributesRequest();
//        
//        request.setDomainName(table);
//        
//        request.setItemName(id);
//        
//		DeleteAttributesResponse response = service.deleteAttributes(request);
//		
//		if (response.isSetResponseMetadata()) {
//			ResponseMetadata  responseMetadata = response.getResponseMetadata();
//			System.out.println("RequestId:" + responseMetadata.getRequestId());
//			System.out.println("BoxUsage:" + responseMetadata.getBoxUsage());
//			return true;
//		}
//		
//		return false;
//	}
	
}
