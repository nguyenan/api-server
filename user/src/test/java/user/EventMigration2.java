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
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.wut.datasources.cassandra.CassandraSource;
import com.wut.model.list.ListData;
import com.wut.model.map.MappedData;
import com.wut.model.scalar.BooleanData;
import com.wut.model.scalar.IdData;
import com.wut.model.scalar.StringData;

public class EventMigration2 {
	private static CassandraSource cassSource = new CassandraSource();
	private static final String applicationStr = "core";
	private static final IdData application = new IdData(applicationStr);
	private static final IdData tableId = IdData.create("flat2");
	private static Logger logger = Logger.getLogger("ProductDevTools");
	private static FileHandler fh;

	private static Map<String, String> merchandiseFromProduct = new HashMap<String, String>();

	private static Map<String, String> eventFromProduct = new HashMap<String, String>();
	private static Map<String, String> metadataFromProductOpts = new HashMap<String, String>();

	private static Map<String, String> sellableFromProduct = new HashMap<String, String>();
	private static Map<String, String> sellableFromProductOpts = new HashMap<String, String>();

	private static Map<String, String> sellableInventoryMap = new HashMap<String, String>();

	public static void main(String[] agrs) throws InterruptedException, SecurityException, IOException {
		setLogFormat();
		buildMapFields();

		migrateToEvent("beta.tend.ag");
		// migrateToEvent("test.farmer.guide");
		// migrateToEvent("dev1.tend.ag");

		System.exit(0);
	}

	public static void migrateToEvent(String customerId) throws IOException {
		ListData listProduct = getListProduct(customerId);
		String productId = "";
		StringData options = new StringData("");

		loopProduct: for (Object obj : listProduct) {
			MappedData productInfo = (MappedData) obj;
			options = (StringData) productInfo.get("options");
			if (options == null)
				continue;

			JsonArray productOptionIds = parseProductOptions(options);
			List<MappedData> productOptions = new ArrayList<MappedData>();
			for (JsonElement option : productOptionIds) {
				String productOptionId = option.toString().replaceAll("\\\"", "");
				MappedData productOption = getProductOption(customerId, productOptionId);

				if (!isEvent(customerId, productOption))
					// if product isn't event => continue
					continue loopProduct;
				productOptions.add(productOption);
			}

			// if product is event => migrate
			String rowId = productInfo.get("id").toString();
			productId = rowId.substring(rowId.lastIndexOf(":") + 1, rowId.length());

			// 1. Create SellableInventory, Sellable, metadata
			List<MappedData> metadatas = new ArrayList<MappedData>();
			for (MappedData productOption : productOptions) {
				String oldId = productOption.get("id").toString();
				String productOptionId = oldId.substring(oldId.lastIndexOf(":") + 1, oldId.length());
				// Create sellableInventoryId
				String sellableInventories = createSellableInventory(customerId, productOption);

				// Create Sellable
				createSellable(customerId, productId, productOptionId, productOption, productInfo, sellableInventories);

				// Create metadata
				MappedData metadata = createMetadata(productOption, productOptionId);
				metadatas.add(metadata);
			}

			// 2. create Event
			createEvent(customerId, productId, productInfo, metadatas);

			// 3. create Merchandise
			createMerchandise(customerId, productId, productInfo);

			logger.info(customerId + "\t Migrated " + productId);
			// Backup and Delete Product Data
			// backupAndDeleteProduct(customerId, productInfo);
		}
	}

	public static BooleanData createEvent(String customerId, String productId, MappedData productInfo,
			List<MappedData> metadatas) {
		// Obtain tableId and new RowId
		String table = getTableName(customerId, "event");
		IdData rowId = getRowIdData(table, productId);

		// Migrate data
		MappedData data = new MappedData();
		data.put("table", table);
		data.put("id", rowId.toString());
		Gson gson = new Gson();
		data.put("metadata", metadatas.toString().replace("\"", "\\\""));
		System.out.println(data.toString());
		return cassSource.updateRow(new IdData(customerId), application, tableId, rowId, data);
	}

	public static BooleanData createMerchandise(String customerId, String productId, MappedData productInfo) {
		// Obtain tableId
		String table = getTableName(customerId, "merchandise");
		IdData rowId = getRowIdData(table, productId);

		// Migrate data
		MappedData data = new MappedData();
		data.put("table", table);
		data.put("id", rowId.toString());
		for (Map.Entry<String, String> entry : merchandiseFromProduct.entrySet()) {
			if (productInfo.get(entry.getKey()) != null)
				data.put(entry.getValue(), productInfo.get(entry.getKey()));
		}
		System.out.println(data.toString());
		return cassSource.updateRow(new IdData(customerId), application, tableId, rowId, data);

	}

	public static MappedData createMetadata(MappedData productOption, String productOptionId) {
		MappedData metadata = new MappedData();
		
		List<String> sellableIds = new ArrayList<String>();
		sellableIds.add(productOptionId);
		Gson gson2 = new Gson();
		String sellables = gson2.toJson(sellableIds).toString().replace("\"", "\\\"");
		
		metadata.put("sellable", sellables);
		for (Map.Entry<String, String> entry : metadataFromProductOpts.entrySet()) {
			if (productOption.get(entry.getKey()) != null)
				metadata.put(entry.getValue(), productOption.get(entry.getKey()));
		}
		System.out.println(metadata);
		return metadata;
	}

	public static BooleanData createSellable(String customerId, String productId, String productOptionId,
			MappedData productOptionInfo, MappedData productInfo, String sellableInventories) {
		// Obtain tableId and new RowId
		String table = getTableName(customerId, "sellable");
		IdData rowId = getRowIdData(table, productOptionId);

		// Migrate data
		MappedData data = new MappedData();
		data.put("table", table);
		data.put("id", rowId.toString());
		data.put("merchandise", productId);
		data.put("sellableInventory", sellableInventories);

		for (Map.Entry<String, String> entry : sellableFromProductOpts.entrySet()) {
			if (productOptionInfo.get(entry.getKey()) != null)
				data.put(entry.getValue(), productOptionInfo.get(entry.getKey()));
		}

		for (Map.Entry<String, String> entry : sellableFromProduct.entrySet()) {
			if (productInfo.get(entry.getKey()) != null)
				data.put(entry.getValue(), productInfo.get(entry.getKey()));
		}

		System.out.println(data.toString());
		return cassSource.updateRow(new IdData(customerId), application, tableId, rowId, data);
	}

	public static String createSellableInventory(String customerId, MappedData productOptionInfo) {
		// Obtain tableId
		String table = getTableName(customerId, "sellableInventory");

		// Migrate data
		MappedData data = new MappedData();
		data.put("table", table);
		data.put("location", "On Farm");
		for (Map.Entry<String, String> entry : sellableInventoryMap.entrySet()) {
			if (productOptionInfo.get(entry.getKey()) != null)
				data.put(entry.getValue(), productOptionInfo.get(entry.getKey()));
		}
		System.out.println(data.toString());
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

	public static boolean isEvent(String customerId, MappedData productOption) {
		if (productOption == null)
			return false;
		StringData start = (StringData) productOption.get("start");
		StringData end = (StringData) productOption.get("end");
		return (start != null && end != null);
	}

	public static ListData getListProduct(String customerId) {
		IdData customer = new IdData(customerId);
		MappedData filter = new MappedData();
		String table = getTableName(customerId, "product");
		filter.put("table", new IdData(table));
		return cassSource.getRowsWithFilter(customer, application, tableId, filter);
	}

	public static MappedData getProductOption(String customerId, String productOptionId) {
		IdData customer = new IdData(customerId);
		String table = getTableName(customerId, "productOption");
		IdData rowId = getRowIdData(table, productOptionId);
		return cassSource.getRow(customer, application, tableId, rowId);
	}

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

	private static void buildMapFields() {
		// merchandise
		merchandiseFromProduct.put("update", "update");
		merchandiseFromProduct.put("metaDescription", "metaDescription");
		merchandiseFromProduct.put("images", "images");
		merchandiseFromProduct.put("description", "description");
		merchandiseFromProduct.put("tags", "tags");
		merchandiseFromProduct.put("shortDescription", "shortDescription");
		merchandiseFromProduct.put("name", "name");
		merchandiseFromProduct.put("type", "type");
		merchandiseFromProduct.put("url", "url");
		merchandiseFromProduct.put("enabled", "enabled");
		// merchandiseFromProduct.put("id", "id");

		// event
		// eventFromProduct.put("id", "id");

		metadataFromProductOpts.put("start", "start");
		metadataFromProductOpts.put("end", "end");
		metadataFromProductOpts.put("choices", "choices");
		// metadataFromProductOpts.put("id", "sellable");

		// sellable
		// sellableFromProduct.put("id", "merchandise");
		sellableFromProduct.put("notTaxed", "taxable");
		sellableFromProduct.put("customizations", "customizations");

		// sellableFromProductOpts.put("id", "id");
		sellableFromProductOpts.put("update", "update");
		sellableFromProductOpts.put("price", "price");
		sellableFromProductOpts.put("unit", "unit");
		sellableFromProductOpts.put("weight", "weight");

		// sellable Inventory
		sellableInventoryMap.put("inventory", "quantity");
	}

	private static JsonArray parseProductOptions(StringData options) {
		String rawString = StringEscapeUtils.unescapeJava(options.toRawString().trim());
		JsonParser parser = new JsonParser();
		return (JsonArray) parser.parse(rawString);
	}

	private static String getTableName(String customerId, String resource) {
		return String.format("core:%s:public:%s", customerId, resource);
	}

	private static IdData getRowIdData(String tableName, String row) {
		return new IdData(String.format("%s:%s", tableName, row));
	}

	private static String getBackupFile(String customerId) {
		return String.format("/data/backup/product_%s", customerId);
	}

	private static void setLogFormat() throws SecurityException, IOException {
		fh = new FileHandler("/data/scripts/devlog/product_" + System.currentTimeMillis());
		logger.addHandler(fh);
		fh.setFormatter(new LogFormatter());
	}
}
