package user;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import com.google.gson.Gson;
import com.wut.datasources.cassandra.CassandraSource;
import com.wut.model.list.ListData;
import com.wut.model.map.MappedData;
import com.wut.model.scalar.BooleanData;
import com.wut.model.scalar.IdData;
import com.wut.model.scalar.StringData;

public class ShareMigration extends MigrationModel {
	private static CassandraSource cassSource = new CassandraSource();
	private static final String applicationStr = "core";
	private static final IdData application = new IdData(applicationStr);
	private static final IdData tableId = IdData.create("flat2");
	private static Logger logger = Logger.getLogger("ProductDevTools");

	public static void main(String[] agrs) throws InterruptedException, SecurityException, IOException {
		setLogFormat();
		buildMapFields();

		migrateToShare("l1s15ae1ebec25fc4e7e9ca22942335bc1c5");

		System.exit(0);
	}

	public static void migrateToShare(String customerId) throws IOException {
		ListData listProduct = getListProduct(customerId);
		String productId = "";

		loopProduct: for (Object obj : listProduct) {
			MappedData productInfo = (MappedData) obj;
			if (!isShare(productInfo))
				// if product isn't share => continue
				continue loopProduct;

			// if product is share => migrate
			String rowId = productInfo.get("id").toString();
			productId = rowId.substring(rowId.lastIndexOf(":") + 1, rowId.length());

			// 1. Create SellableInventory, Sellable, metadata

			MappedData productOption = new MappedData();
			List<String> sellableIds = new ArrayList<String>();
			// Create sellableInventoryId
			String sellableInventories = createSellableInventory(customerId, productInfo, productOption);

			// Create Sellable
			String sellableId = createSellable(customerId, productId, productOption, productInfo, sellableInventories);

			// Create metadata
			sellableIds.add(sellableId);

			// 2. create Share
			createShare(customerId, productId, productInfo, sellableIds);

			// 3. create Merchandise
			createMerchandise("share", customerId, productId, productInfo);

			logger.info(customerId + "\t Migrated " + productId);
		}
	}

	public static BooleanData createShare(String customerId, String productId, MappedData productInfo,
			List<String> sellableIds) {
		// Obtain tableId and new RowId
		String table = getTableName(customerId, "share");
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
		// share
		metadataFromProduct.put("goalValue", "goalValue");

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
		sellableFromProduct.put("price", "price");

		sellableFromProductOpts.put("update", "update");
		sellableFromProductOpts.put("price", "price");
		sellableFromProductOpts.put("unit", "unit");
		sellableFromProductOpts.put("weight", "weight");
		sellableFromProductOpts.put("choices", "choices");

		// sellable Inventory
		sellableIFromProduct.put("inventory", "quantity");
	}
}
