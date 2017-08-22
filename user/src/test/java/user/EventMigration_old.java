package user;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
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

public class EventMigration_old {
	private static CassandraSource cassSource = new CassandraSource();
	private static final String applicationStr = "core";
	private static final IdData application = new IdData(applicationStr);
	private static final IdData tableId = IdData.create("flat2");
	private static Logger logger = Logger.getLogger("ProductDevTools");
	private static FileHandler fh;
	private static Map<String, String> eventMap = new HashMap<String, String>();
	private static Map<String, String> sellableMapFromProduct = new HashMap<String, String>();

	private static Map<String, String> sellableMap = new HashMap<String, String>();
	private static Map<String, String> sellableInventoryMap = new HashMap<String, String>();
	private static Map<String, String> merchandiseMap = new HashMap<String, String>();

	public static void main(String[] agrs) throws InterruptedException, SecurityException, IOException {
		setLogFormat();
		buildMapFields();

		// migrateToEvent("beta.tend.ag");
		// migrateToEvent("test.farmer.guide");
		// migrateToEvent("dev1.tend.ag");

		System.exit(0);
	}

	public static void migrateToEvent(String customerId) throws IOException {
		ListData listProduct = getListProduct(customerId);
		String productId = "";
		String productOptionId = "";
		StringData options = new StringData("");

		loopProduct: for (Object obj : listProduct) {
			MappedData productInfo = (MappedData) obj;
			options = (StringData) productInfo.get("options");

			if (options == null)
				continue;

			JsonArray productOptionIds = parseProductOptions(options);
			List<MappedData> productOptions = new ArrayList<MappedData>();
			for (JsonElement option : productOptionIds) {
				productOptionId = option.toString().replaceAll("\\\"", "");
				MappedData productOption = getProductOption(customerId, productOptionId);

				if (!isEvent(customerId, productOption))
					// if product isn't event => continue
					continue loopProduct;
				productOptions.add(productOption);
			}

			// if product is event => migrate
			String rowId = productInfo.get("id").toString();
			productId = rowId.substring(rowId.lastIndexOf(":") + 1, rowId.length());

			// 1. Add Sellable
			List<Long> startEndTime = new ArrayList<Long>();
			List<String> sellableIds = new ArrayList<String>();
			for (MappedData productOption : productOptions) {
				startEndTime.add(dateToLong(productOption.get("start").toString()));
				startEndTime.add(dateToLong(productOption.get("end").toString()));
				String sellableId = addSellable(customerId, productOption, productInfo);
				sellableIds.add(sellableId);
			}
			Gson gson = new Gson();
			String sellables = gson.toJson(sellableIds).toString().replace("\"", "\\\"");

			// 2. add Merchandise
			String merchandiseId = addMerchandise(customerId, sellables, productInfo);

			List<String> merchandiseIds = new ArrayList<String>();
			merchandiseIds.add(merchandiseId);
			Gson gson2 = new Gson();
			String merchandises = gson2.toJson(merchandiseIds).toString().replace("\"", "\\\"");

			// 3. add Event

			addEvent(customerId, productId, productInfo, startEndTime, merchandises);

			// Backup and Delete Product Data
			// backupAndDeleteProduct(customerId, productInfo);
		}
	}

	public static BooleanData addEvent(String customerId, String productId, MappedData productInfo,
			List<Long> startEndTime, String merchandiseId) {
		// Obtain tableId and new RowId
		String table = getTableName(customerId, "event");
		IdData rowId = getRowIdData(table, productId);

		// Migrate data
		MappedData data = new MappedData();
		data.put("id", rowId.toString());
		data.put("table", table);
		data.put("merchandise", merchandiseId);
		data.put("start", getStart(startEndTime));
		data.put("end", getEnd(startEndTime));
		for (Map.Entry<String, String> entry : eventMap.entrySet()) {
			if (productInfo.get(entry.getKey()) != null)
				data.put(entry.getValue(), productInfo.get(entry.getKey()));
		}

		logger.info(customerId + "\t Migrated " + data.get("id"));
		return cassSource.updateRow(new IdData(customerId), application, tableId, rowId, data);
	}

	public static String addMerchandise(String customerId, String sellableIds, MappedData productInfo) {
		// Obtain tableId
		String table = getTableName(customerId, "merchandise");

		// Migrate data
		MappedData data = new MappedData();
		data.put("table", table);
		data.put("sellable", sellableIds);
		for (Map.Entry<String, String> entry : merchandiseMap.entrySet()) {
			if (productInfo.get(entry.getKey()) != null)
				data.put(entry.getValue(), productInfo.get(entry.getKey()));
		}

		IdData merchandiseId = cassSource.insertRow(new IdData(customerId), application, tableId, data);
		IdData newId = getRowIdData(table, merchandiseId.toString());
		data.put("id", newId.toString());
		cassSource.updateRow(new IdData(customerId), application, tableId, newId, data);
		return merchandiseId.toString();

	}

	public static String addSellable(String customerId, MappedData productOptionInfo, MappedData productInfo) {
		// Obtain tableId and new RowId
		String table = getTableName(customerId, "sellable");
		String oldId = productOptionInfo.get("id").toString();
		String sellableId = oldId.substring(oldId.lastIndexOf(":") + 1, oldId.length());
		String newId = getRowId(table, sellableId);

		// Add sellableInventoryId
		String sellableInventoryId = addSellableInventory(customerId, productOptionInfo);
		List<String> sellableInventoryIds = new ArrayList<String>();
		sellableInventoryIds.add(sellableInventoryId);
		Gson gson = new Gson();
		String sellableInventories = gson.toJson(sellableInventoryIds).toString().replace("\"", "\\\"");

		// Migrate data
		MappedData data = new MappedData();
		data.put("id", newId);
		data.put("table", table);
		data.put("sellableInventory", sellableInventories);
		data.put("url", String.format("/event/%s.html", sellableId));

		for (Map.Entry<String, String> entry : sellableMap.entrySet()) {
			if (productOptionInfo.get(entry.getKey()) != null)
				data.put(entry.getValue(), productOptionInfo.get(entry.getKey()));
		}

		for (Map.Entry<String, String> entry : sellableMapFromProduct.entrySet()) {
			if (productInfo.get(entry.getKey()) != null)
				data.put(entry.getValue(), productInfo.get(entry.getKey()));
		}

		if (cassSource.updateRow(new IdData(customerId), application, tableId, new IdData(newId), data)
				.equals(BooleanData.TRUE))
			return sellableId;
		else
			return null;

	}

	public static String addSellableInventory(String customerId, MappedData productOptionInfo) {
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
		IdData sellableInventoryId = cassSource.insertRow(new IdData(customerId), application, tableId, data);
		IdData newId = getRowIdData(table, sellableInventoryId.toString());
		data.put("id", newId.toString());
		cassSource.updateRow(new IdData(customerId), application, tableId, newId, data);
		return sellableInventoryId.toString();
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
		eventMap.put("inventory", "maxSubscribers");
		eventMap.put("reservationInterval", "reservationInterval");
		eventMap.put("availability", "availability");
		// eventMap.put("url", "url");

		sellableMapFromProduct.put("updated", "updated");
		sellableMapFromProduct.put("notTaxed", "notTaxed");
		sellableMapFromProduct.put("customizations", "customizations");
		sellableMapFromProduct.put("enabled", "enabled");

		sellableMap.put("default", "default");
		sellableMap.put("price", "price");
		sellableMap.put("unit", "unit");
		sellableMap.put("weight", "weight");
		sellableMap.put("choices", "choices");
		sellableMap.put("discounts", "discounts");
		sellableMap.put("start", "start");
		sellableMap.put("end", "end");

		sellableInventoryMap.put("inventory", "quantity");

		merchandiseMap.put("name", "name");
		merchandiseMap.put("tags", "tags");
		merchandiseMap.put("metaDescription", "metaDescription");
		merchandiseMap.put("description", "description");
		merchandiseMap.put("shortDescription", "shortDescription");
		merchandiseMap.put("allImages", "allImages");
	}

	private static JsonArray parseProductOptions(StringData options) {
		String rawString = StringEscapeUtils.unescapeJava(options.toRawString().trim());
		JsonParser parser = new JsonParser();
		return (JsonArray) parser.parse(rawString);
	}

	private static String getTableName(String customerId, String resource) {
		return String.format("core:%s:public:%s", customerId, resource);
	}

	private static String getRowId(String tableName, String row) {
		return String.format("%s:%s", tableName, row);
	}

	private static IdData getRowIdData(String tableName, String row) {
		return new IdData(String.format("%s:%s", tableName, row));
	}

	private static String getBackupFile(String customerId) {
		return String.format("/data/backup/product_%s", customerId);
	}

	private static String getStart(List<Long> startEndTime) {
		Collections.sort(startEndTime);
		return longToDate(startEndTime.get(0));
	}

	private static String getEnd(List<Long> startEndTime) {
		Collections.sort(startEndTime);
		return longToDate(startEndTime.get(startEndTime.size() - 1));
	}

	private static long dateToLong(String date) {
		SimpleDateFormat f = new SimpleDateFormat("MM/dd/yyyy, hh:mm:ss aa");
		try {
			Date d = f.parse(date);
			long milliseconds = d.getTime();
			return milliseconds;
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return -1L;
	}

	private static String longToDate(long val) {
		Date date = new Date(val);
		SimpleDateFormat df2 = new SimpleDateFormat("MM/dd/yyyy, hh:mm:ss aa");
		String dateText = df2.format(date);
		return dateText;
	}

	private static void setLogFormat() throws SecurityException, IOException {
		fh = new FileHandler("/data/devlog/product_" + System.currentTimeMillis());
		logger.addHandler(fh);
		fh.setFormatter(new LogFormatter());
	}
}
