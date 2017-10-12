package user;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.wut.datasources.cassandra.CassandraSource;
import com.wut.model.list.ListData;
import com.wut.model.map.MappedData;
import com.wut.model.scalar.BooleanData;
import com.wut.model.scalar.IdData;
import com.wut.model.scalar.StringData;

public class EventMigration extends MigrationModel {
	private static CassandraSource cassSource = new CassandraSource();
	private static final String applicationStr = "core";
	private static final IdData application = new IdData(applicationStr);
	private static final IdData tableId = IdData.create("flat2");
	private static Logger logger = Logger.getLogger("ProductDevTools");

	public static void main(String[] agrs) throws InterruptedException, SecurityException, IOException {
		setLogFormat();
		buildMapFields();

		migrateToEvent("l1s15ae1ebec25fc4e7e9ca22942335bc1c5");//nautic.farmer.events
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

			List<String> sellableIds = new ArrayList<String>();
			for (MappedData productOption : productOptions) {
				String oldId = productOption.get("id").toString();
				String productOptionId = oldId.substring(oldId.lastIndexOf(":") + 1, oldId.length());
				// Create sellableInventoryId
				String sellableInventories = createSellableInventory(customerId, productInfo, productOption);

				// Create Sellable
				createSellable(customerId, productId, productOptionId, productOption, productInfo, sellableInventories);

				// Create sellableIds
				sellableIds.add(productOptionId);
			}

			// 2. create Event
			createEvent(customerId, productId, productInfo, sellableIds);

			// 3. create Merchandise
			createMerchandise("event", customerId, productId, productInfo);

			logger.info(customerId + "\t Migrated " + productId);
			// Backup and Delete Product Data
			// backupAndDeleteProduct(customerId, productInfo);
		}
	}

	public static BooleanData createEvent(String customerId, String productId, MappedData productInfo,
			List<String> sellableIds) {
		// Obtain tableId and new RowId
		String table = getTableName(customerId, "event");
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

	private static void buildMapFields() {
		// event
		metadataFromProductOpts.put("start", "start");
		metadataFromProductOpts.put("end", "end");

		// merchandise
		merchandiseFromProduct.put("update", "update");
		merchandiseFromProduct.put("description", "description");
		merchandiseFromProduct.put("metaDescription", "metaDescription");
		merchandiseFromProduct.put("shortDescription", "shortDescription");
		merchandiseFromProduct.put("url", "url");
		merchandiseFromProduct.put("tags", "tags");
		merchandiseFromProduct.put("name", "name");
		merchandiseFromProduct.put("type", "type");
		merchandiseFromProduct.put("images", "images");
		merchandiseFromProduct.put("enabled", "enabled");

		// sellable
		sellableFromProduct.put("notTaxed", "taxable");

		sellableFromProductOpts.put("update", "update");
		sellableFromProductOpts.put("price", "price");
		sellableFromProductOpts.put("unit", "unit");
		sellableFromProductOpts.put("weight", "weight");
		sellableFromProductOpts.put("choices", "choices");

		// sellable Inventory
		sellableIFromProductOpts.put("inventory", "quantity");
	}
}
