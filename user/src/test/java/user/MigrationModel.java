package user;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

import org.apache.commons.lang3.StringEscapeUtils;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.wut.datasources.cassandra.CassandraSource;
import com.wut.model.list.ListData;
import com.wut.model.map.MappedData;
import com.wut.model.scalar.BooleanData;
import com.wut.model.scalar.IdData;
import com.wut.model.scalar.StringData;

public class MigrationModel {
	private static CassandraSource cassSource = new CassandraSource();
	private static final String applicationStr = "core";
	private static final IdData application = new IdData(applicationStr);
	private static final IdData tableId = IdData.create("flat2");	

	protected static final String TABLE_CROP = "crop";
	protected static final String TABLE_SHARE = "share";
	protected static final String TABLE_EVENT = "event";

	protected static final String TABLE_SELLABLE = "sellable";
	protected static final String TABLE_SELLABLE_INVENTORY = "sellableInventory";
	protected static final String TABLE_MERCHANDISE = "merchandise";

	protected static final String TABLE_PRODUCT = "product";
	protected static final String TABLE_PRODUCT_OPTIONS = "productOption";


	private static Logger logger = Logger.getLogger("ProductDevTools");
	private static FileHandler fh;

	// MAPPING FIELDS: old key => new key
	public static Map<String, String> merchandiseFromProduct = new HashMap<String, String>();

	public static Map<String, String> metadataFromProductOpts = new HashMap<String, String>();
	public static Map<String, String> metadataFromProduct = new HashMap<String, String>();

	public static Map<String, String> sellableFromProduct = new HashMap<String, String>();
	public static Map<String, String> sellableFromProductOpts = new HashMap<String, String>();

	public static Map<String, String> sellableInvenFromProductOpts = new HashMap<String, String>();
	public static Map<String, String> sellableInvenFromProduct = new HashMap<String, String>();

	// CREATE NEW DATA
	public static BooleanData createNewProductType(String customerId, String productId, MappedData productInfo,
			List<String> sellableIds, String tableName) {
		// Obtain tableId and new RowId
		String table = getTableName(customerId, tableName);
		IdData rowId = getRowIdData(table, productId);

		// Migrate data
		MappedData data = new MappedData();
		data.put("table", table);
		data.put("id", rowId.toString());

		Gson gson = new Gson();
		String sellables = gson.toJson(sellableIds).toString().replace("\"", "\\\"");
		data.put("sellable", sellables);

		List<String> merchandiseIds = new ArrayList<String>();
		merchandiseIds.add(productId);
		Gson gson2 = new Gson();
		String merchandises = gson2.toJson(merchandiseIds).toString().replace("\"", "\\\"");
		data.put("merchandise", merchandises);

		return cassSource.updateRow(new IdData(customerId), application, tableId, rowId, data);
	}

	public static BooleanData createMerchandise(String type, String customerId, String productId,
			MappedData productInfo) {
		// Obtain tableId and new RowId
		String table = getTableName(customerId, TABLE_MERCHANDISE);
		IdData rowId = getRowIdData(table, productId);

		// Migrate data
		MappedData data = new MappedData();
		data.put("table", table);
		data.put("id", rowId.toString());
		data.put("type", type);
		for (Map.Entry<String, String> entry : merchandiseFromProduct.entrySet()) {
			if (productInfo.get(entry.getKey()) != null)
				data.put(entry.getValue(), productInfo.get(entry.getKey()));
		}
		return cassSource.updateRow(new IdData(customerId), application, tableId, rowId, data);

	}

	public static MappedData createMetadata(MappedData product, MappedData productOption) {
		MappedData metadata = new MappedData();

		for (Map.Entry<String, String> entry : metadataFromProductOpts.entrySet()) {
			if (productOption.get(entry.getKey()) != null)
				metadata.put(entry.getValue(), productOption.get(entry.getKey()).toString().replace("\"", "\\\""));
		}
		for (Map.Entry<String, String> entry : metadataFromProduct.entrySet()) {
			if (product.get(entry.getKey()) != null)
				metadata.put(entry.getValue(), product.get(entry.getKey()).toString().replace("\"", "\\\""));
		}
		return metadata;
	}

	public static List<String> createGroupSellable(String customerId, String productId, String sellableId,
			List<String> sellableIds, MappedData productOption, MappedData productInfo) {

		// Create sellableInventoryId
		String sellableInventories = createSellableInventory(customerId, productInfo, productOption);

		// Create Sellable
		sellableId = createSellable(customerId, productId, sellableId, productOption, productInfo, sellableInventories);

		// Update list sellableIds
		sellableIds.add(sellableId);

		return sellableIds;
	}

	public static String createSellable(String customerId, String productId, String sellableId,
			MappedData productOptionInfo, MappedData productInfo, String sellableInventories) {
		// Obtain tableId and new RowId
		String table = getTableName(customerId, TABLE_SELLABLE);
		if (sellableId == null || sellableId.isEmpty()) {
			MappedData data = new MappedData();
			data.put("table", table);
			sellableId = cassSource.insertRow(new IdData(customerId), application, tableId, data).toRawString();
		}

		IdData rowId = getRowIdData(table, sellableId);

		// Migrate data
		MappedData data = new MappedData();
		data.put("table", table);
		data.put("id", rowId.toString());
		data.put("sellableInventory", sellableInventories);

		for (Map.Entry<String, String> entry : sellableFromProductOpts.entrySet()) {
			if (productOptionInfo.get(entry.getKey()) != null)
				data.put(entry.getValue(), productOptionInfo.get(entry.getKey()));
		}

		for (Map.Entry<String, String> entry : sellableFromProduct.entrySet()) {
			if (productInfo.get(entry.getKey()) != null)
				data.put(entry.getValue(), productInfo.get(entry.getKey()));
		}
		
		// Create metadata
		MappedData metadata = createMetadata(productInfo, productOptionInfo);
		List<MappedData> metadatas = new ArrayList<MappedData>();
		metadatas.add(metadata);
		data.put("metadata", metadatas.toString().replace("\"", "\\\""));
		cassSource.updateRow(new IdData(customerId), application, tableId, rowId, data);

		return sellableId;
	}

	public static String createSellableInventory(String customerId, MappedData productInfo,
			MappedData productOptionInfo) {
		// Obtain tableId
		String table = getTableName(customerId, TABLE_SELLABLE_INVENTORY);

		// Migrate data
		MappedData data = new MappedData();
		data.put("table", table);
		data.put("location", "On Farm");
		for (Map.Entry<String, String> entry : sellableInvenFromProductOpts.entrySet()) {
			if (productOptionInfo.get(entry.getKey()) != null)
				data.put(entry.getValue(), productOptionInfo.get(entry.getKey()));
		}
		for (Map.Entry<String, String> entry : sellableInvenFromProduct.entrySet()) {
			if (productInfo.get(entry.getKey()) != null)
				data.put(entry.getValue(), productInfo.get(entry.getKey()));
		}
		IdData sellableInventoryId = cassSource.insertRow(new IdData(customerId), application, tableId, data);
		IdData newId = getRowIdData(table, sellableInventoryId.toString());
		data.put("id", newId.toString());
		cassSource.updateRow(new IdData(customerId), application, tableId, newId, data);

		List<String> sellableInventoryIds = new ArrayList<String>();
		sellableInventoryIds.add(sellableInventoryId.toString());
		Gson gson = new Gson();
		String sellableInventories = gson.toJson(sellableInventoryIds).toString().replace("\"", "\\\"");
		return sellableInventories;
	}

	// CHECK PRODUCT_TYPE
	public static boolean isEvent(String customerId, MappedData productOption) {
		if (productOption == null)
			return false;
		StringData start = (StringData) productOption.get("start");
		StringData end = (StringData) productOption.get("end");
		return (start != null && end != null);
	}

	public static boolean isShare(MappedData product) {
		if (product == null)
			return false;
		StringData goalValue = (StringData) product.get("goalValue");
		return (goalValue != null);
	}

	// LIST DATA
	public static ListData getListMigratedData(String customerId, String tableName) {
		IdData customer = new IdData(customerId);
		MappedData filter = new MappedData();
		String table = getTableName(customerId, tableName);
		filter.put("table", new IdData(table));
		return cassSource.getRowsWithFilter(customer, application, tableId, filter);
	}

	public static ListData getListProduct(String customerId) {
		IdData customer = new IdData(customerId);
		MappedData filter = new MappedData();
		String table = getTableName(customerId, TABLE_PRODUCT);
		filter.put("table", new IdData(table));
		return cassSource.getRowsWithFilter(customer, application, tableId, filter);
	}

	public static MappedData getProductOption(String customerId, String productOptionId) {
		IdData customer = new IdData(customerId);
		String table = getTableName(customerId, TABLE_PRODUCT_OPTIONS);
		IdData rowId = getRowIdData(table, productOptionId);
		return cassSource.getRow(customer, application, tableId, rowId);
	}

	// BACKUP AND DELETE
	public static BooleanData backupAndDeleteProduct(String customerId, MappedData productInfo) throws IOException {
		IdData rowId = new IdData(productInfo.get("id").toString());
		try {
			String filename = getBackupFile(customerId);
			FileWriter fw = new FileWriter(filename, true);
			fw.write(productInfo.toString() + "\n");
			fw.close();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
		return cassSource.deleteRow(new IdData(customerId), application, tableId, rowId);
	}

	public static BooleanData restoreProduct(String customerId) throws FileNotFoundException, IOException {
		String fileName = getBackupFile(customerId);
		try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
			String line;
			while ((line = br.readLine()) != null) {
				if (line.isEmpty())
					continue;
				line = line.replace("\\\"", "\\\\\\\"");
				Map<String, String> retMap = new Gson().fromJson(line, new TypeToken<HashMap<String, String>>() {
				}.getType());
				MappedData productInfo = MappedData.convert(retMap);
				IdData rowId = new IdData(productInfo.get("id").toString());
				cassSource.updateRow(new IdData(customerId), application, tableId, rowId, productInfo);
			}
		}
		return BooleanData.TRUE;
	}

	public static JsonArray parseProductOptions(StringData options) {
		String rawString = StringEscapeUtils.unescapeJava(options.toRawString().trim());
		JsonParser parser = new JsonParser();
		return (JsonArray) parser.parse(rawString);
	}

	// UTIL FUNCTION
	public static String getTableName(String customerId, String resource) {
		return String.format("core:%s:public:%s", customerId, resource);
	}

	public static IdData getRowIdData(String tableName, String row) {
		return new IdData(String.format("%s:%s", tableName, row));
	}

	private static String getBackupFile(String customerId) {
		return String.format("/data/backup/product_%s", customerId);
	}

	public static void setLogFormat() throws SecurityException, IOException {
		fh = new FileHandler("/data/scripts/devlog/product_" + System.currentTimeMillis());
		logger.addHandler(fh);
		fh.setFormatter(new LogFormatter());
	}
}
