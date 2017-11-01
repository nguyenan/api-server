package user;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.wut.model.list.ListData;
import com.wut.model.map.MappedData;
import com.wut.model.scalar.StringData;

public class EventMigration extends MigrationModel {
	private static Logger logger = Logger.getLogger("ProductDevTools");

	public static void main(String[] agrs) throws InterruptedException, SecurityException, IOException {
		setLogFormat();
		buildMapFields();
		String customerId = "l1s14378352140f344c8a43f8ed2d9a4f30f";// a-migrate.tendfarm.com
		migrateToEvent(customerId);

		System.out.println(getListMigratedData(customerId, TABLE_EVENT));
		System.out.println(getListMigratedData(customerId, TABLE_CROP));
		System.out.println(getListMigratedData(customerId, TABLE_SHARE));
		System.out.println(getListMigratedData(customerId, TABLE_MERCHANDISE));
		System.out.println(getListMigratedData(customerId, TABLE_SELLABLE));
		System.out.println(getListMigratedData(customerId, TABLE_SELLABLE_INVENTORY));

		// ListData listProduct = getListProduct(customerId);
		// for (Object obj : listProduct) {
		// MappedData productInfo = (MappedData) obj;
		// System.out.println(productInfo);
		// }

		System.exit(0);
	}

	private static void migrateToEvent(String customerId) throws IOException {
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
				String sellableId = oldId.substring(oldId.lastIndexOf(":") + 1, oldId.length());
				sellableIds = createGroupSellable(customerId, productId, sellableId, sellableIds, productOption,
						productInfo);
			}

			// 2. create Event
			createNewProductType(customerId, productId, productInfo, sellableIds, TABLE_EVENT);

			// 3. create Merchandise
			createMerchandise(TABLE_EVENT, customerId, productId, productInfo);

			logger.info(customerId + "\t Migrated " + productId);
			// Backup and Delete Product Data
			// backupAndDeleteProduct(customerId, productInfo);
		}
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
		sellableInvenFromProductOpts.put("controlInventory", "controlInventory");
		sellableInvenFromProductOpts.put("inventory", "quantity");
	}
}
