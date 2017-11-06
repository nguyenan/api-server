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

public class CropMigration extends MigrationModel {
	private static Logger logger = Logger.getLogger("ProductDevTools");

	public static void main(String[] agrs) throws InterruptedException, SecurityException, IOException {
		setLogFormat();
		buildMapFields();

		// migrateToCrop("l1s15ae1ebec25fc4e7e9ca22942335bc1c5");
		// migrateToEvent("test.farmer.guide");
		// migrateToEvent("dev1.tend.ag");
		String customerId = "l1s1a607957b13e64302ade0359264434cd2";
		//migrateToCrop(customerId);
		System.exit(0);
	}

	public static void migrateToCrop(String customerId) throws IOException {
		ListData listProduct = getListProduct(customerId);
		String productId = "";
		StringData options = new StringData("");

		loopProduct: for (Object obj : listProduct) {
			MappedData productInfo = (MappedData) obj;
			if (isShare(productInfo))
				// if product isn't share => continue
				continue loopProduct;

			options = (StringData) productInfo.get("options");
			if (options == null)
				continue;

			JsonArray productOptionIds = parseProductOptions(options);
			List<MappedData> productOptions = new ArrayList<MappedData>();
			boolean isEvent = true;
			for (JsonElement option : productOptionIds) {
				String productOptionId = option.toString().replaceAll("\\\"", "");
				MappedData productOption = getProductOption(customerId, productOptionId);

				productOptions.add(productOption);
				if (!isEvent(customerId, productOption)) {
					isEvent = false;
				}
			}
			if (isEvent)
				continue loopProduct;

			// if product is crop => migrate
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

			// 2. create Crop
			createNewProductType(customerId, productId, productInfo, sellableIds, TABLE_CROP);

			// 3. create Merchandise
			createMerchandise(TABLE_CROP, customerId, productId, productInfo);

			logger.info(customerId + "\t Migrated " + productId);
			// Backup and Delete Product Data
			// backupAndDeleteProduct(customerId, productInfo);
		}
	}

	private static void buildMapFields() {
		// event
		metadataFromProductOpts.put("start", "start");
		metadataFromProductOpts.put("end", "end");

		metadataFromProductOpts.put("images", "images");
		metadataFromProductOpts.put("manufacturerCode", "manufacturerCode");
		metadataFromProductOpts.put("upc", "upc");
		metadataFromProductOpts.put("cost", "cost");
		metadataFromProductOpts.put("weight", "weight");
		metadataFromProductOpts.put("default", "default");
		metadataFromProductOpts.put("priceUnit", "unit");

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
		sellableFromProductOpts.put("choices", "choices");
		sellableFromProductOpts.put("controlInventory", "controlInventory");
		
		// sellable Inventory
		sellableInvenFromProductOpts.put("inventory", "quantity");
	}
}
