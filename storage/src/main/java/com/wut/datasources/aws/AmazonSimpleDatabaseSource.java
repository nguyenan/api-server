package com.wut.datasources.aws;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.simpledb.AmazonSimpleDB;
import com.amazonaws.services.simpledb.AmazonSimpleDBClient;
import com.amazonaws.services.simpledb.model.Attribute;
import com.amazonaws.services.simpledb.model.DeleteAttributesRequest;
import com.amazonaws.services.simpledb.model.Item;
import com.amazonaws.services.simpledb.model.PutAttributesRequest;
import com.amazonaws.services.simpledb.model.ReplaceableAttribute;
import com.amazonaws.services.simpledb.model.SelectRequest;
import com.wut.datasources.MultiSource;
import com.wut.model.Data;
import com.wut.model.list.ListData;
import com.wut.model.map.MappedData;
import com.wut.model.scalar.BooleanData;
import com.wut.model.scalar.IdData;
import com.wut.model.scalar.ScalarData;
import com.wut.model.scalar.StringData;
import com.wut.support.ErrorHandler;
import com.wut.support.UniqueIdGenerator;
import com.wut.support.settings.SettingsManager;

public class AmazonSimpleDatabaseSource extends MultiSource {
	
	static {
		// CHECKING CERTIFICATES OCCASIONALLY CAUSES PROBLEMS WITH SIMPLE DB
		System.setProperty("com.amazonaws.sdk.disableCertChecking", "true");
	}
	
	// private AmazonSimpleDB service = new AmazonSimpleDBClient(accessKeyId,
	// secretAccessKey);
	private AmazonSimpleDB service; // = new AmazonSimpleDBClient(new
									// PropertiesCredentials(AmazonSimpleDB.class.getResourceAsStream("AwsCredentials.properties")));
	private static final String FLAT_DOMAIN_ID = "flat";
	private static final String FLAT_TABLE_ID = "tableid";
	private static final String FLAT_ROW_ID = "rowid";
	
	private static final String AMAZON_USERNAME = SettingsManager.getSystemSetting("amazon.simpledb.accesskey");
	private static final String AMAZON_PASSWORD = SettingsManager.getSystemSetting("amazon.simpledb.secretkey");

	
	public AmazonSimpleDatabaseSource() {
			AWSCredentials myCredentials = new BasicAWSCredentials(AMAZON_USERNAME, AMAZON_PASSWORD);
			service = new AmazonSimpleDBClient(myCredentials);
	}

	public static IdData customer = new IdData("www.test.com");
	public static IdData application = new IdData("test");
	
	public static void main(String[] args) {
		final IdData tableId = new IdData("test-table-2");

		AmazonSimpleDatabaseSource source = new AmazonSimpleDatabaseSource();

		Map<String, String> attributes = new TreeMap<String, String>();
		attributes.put("color", "red");
		attributes.put("mood", "happy");
		attributes.put("major", "csc");
		attributes.put("smoker", "no");
		MappedData newRow = new MappedData(attributes);
		final IdData rowId = new IdData("test-row");

		source.updateRow(customer, application, tableId, rowId, newRow);

		ListData allRows = source.getAllRows(customer, application, tableId);
		System.out.println("ALL ROWS=" + allRows);

		MappedData retreivedRow = source.getRow(customer, application, tableId, rowId);

		System.out.println("RETRIEVED ROW=" + retreivedRow);
	}

	@Override
	public ListData getAllRows(IdData customer, IdData application, IdData tableId) {
		return getRowsWithFilter(customer, application, tableId, null);
	}

	private List<Item> getItems(String query) {
		SelectRequest selectRequest = new SelectRequest(query);
		return service.select(selectRequest).getItems();
	}

	// TODO use QueryBuilder

	@Override
	public ListData getRowsWithFilter(IdData customer, IdData application, IdData tableId, MappedData filter) {
		String selectQuery = "select * " + "from " + FLAT_DOMAIN_ID + " "
				+ "where " + FLAT_TABLE_ID + " = '" + tableId.toRawString()
				+ "'";

		if (filter != null && filter.getMap().size() >= 0) {
			for (ScalarData key : filter.getMap().keySet()) {
				ScalarData value = (ScalarData) filter.get(key);
				selectQuery += " and " + key + " = '" + value.toRawString()
						+ "'";
			}
		}

		ListData results = new ListData();
		System.out.println("QUERY:" + selectQuery);
		// response = service.select(request);
		// SelectResult result = response.getSelectResult();
		for (Item item : getItems(selectQuery)) {
			// System.out.println("item" + item.getAttribute().toString());
			MappedData row = new MappedData();
			for (Attribute attr : item.getAttributes()) {
				row.put(attr.getName(), attr.getValue());
			}
			row.remove(new StringData("tableid"));
			// Data rowId = row.get(new IdData("rowid"));
			try {
				Data rowId = row.get(new StringData("rowid"));
				String rowIdStr = ((StringData) rowId).toRawString();
				rowIdStr = rowIdStr.substring(rowIdStr.lastIndexOf(':') + 1,
						rowIdStr.length());
				row.put(new IdData("id"), new StringData(rowIdStr));
			} catch (Exception e) {
				ErrorHandler.systemError("bad parsing of rowid", e);
			}
			row.remove(new StringData("rowid"));
			results.add(row);
		}

		return results;
	}

	@Override
	public MappedData getRow(IdData customer, IdData application, IdData tableId, IdData rowId) {

		String selectQuery = "select * " + "from " + FLAT_DOMAIN_ID + " "
				+ "where " + "itemName()" + " = '"
				+ getRowScope(customer, application, tableId, rowId) + "'";

		System.out.println("QUERY:" + selectQuery);
		SelectRequest request = new SelectRequest();
		request.setSelectExpression(selectQuery);
		List<Item> items = getItems(selectQuery);
//		for (Item item : items) {
//			// System.out.println("item" + item.getAttribute().toString());
//		}
		if (items.size() < 1) {
			return null;
		}
		MappedData row = new MappedData();
		Item item = items.get(0);
		for (Attribute attr : item.getAttributes()) {
			row.put(attr.getName(), attr.getValue());
		}
		row.remove(new StringData("tableid"));
		row.remove(new StringData("rowid"));
		return row;
	}

	@Override
	public BooleanData deleteRow(IdData customer, IdData application, IdData tableId, IdData rowId) {
		String itemName = getRowScope(customer, application, tableId, rowId);

		DeleteAttributesRequest request = new DeleteAttributesRequest();
		request.withItemName(itemName);
		request.withDomainName(FLAT_DOMAIN_ID);
		// request.setDomainName(FLAT_DOMAIN_ID);
		// request.setItemName(itemName);

		service.deleteAttributes(request);

		return BooleanData.TRUE;
	}

	@Override
	public BooleanData crupdateRow(IdData customer, IdData application, IdData tableId, IdData rowId, MappedData data) {
//		PutAttributesRequest request = new PutAttributesRequest();
//		request.setDomainName(FLAT_DOMAIN_ID);
//		request.setItemName(getRowScope(tableId, rowId));

		data.put(FLAT_TABLE_ID, tableId);
		data.put(FLAT_ROW_ID, getRowScope(customer, application, tableId, rowId));

		List<ReplaceableAttribute> sdbAttrs = new ArrayList<ReplaceableAttribute>();
		for (ScalarData key : data.getMap().keySet()) {
			String value = data.get(key).toString();
			ReplaceableAttribute replaceableAttribute = new ReplaceableAttribute(
					key.toRawString(), value, true);
			sdbAttrs.add(replaceableAttribute);
		}
		PutAttributesRequest putRequest = new PutAttributesRequest(FLAT_DOMAIN_ID, getRowScope(customer, application, tableId, rowId), sdbAttrs);
		//request.setAttribute(sdbAttrs);

		service.putAttributes(putRequest);
		
		return BooleanData.TRUE;
	}

	@Override
	public IdData insertRow(IdData customer, IdData application, IdData tableId, MappedData data) {
		IdData newId = new IdData(UniqueIdGenerator.getId());
		IdData rowId = new IdData(getRowScope(customer, application, tableId, newId));
		updateRow(customer, application, tableId, rowId, data);
		return rowId;
	}

	private String getRowScope(IdData customer, IdData application, IdData tableId, IdData rowId) {
		return tableId.toRawString() + ":" + rowId.toRawString();
	}

	@Override
	public BooleanData deleteRows(IdData customer, IdData application, IdData tableId, MappedData filter) {
		// TODO Auto-generated method stub
		return null;
	}

}
