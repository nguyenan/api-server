package migration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import com.wut.model.list.ListData;
import com.wut.model.map.MappedData;

public class ShareMigration extends MigrationModel {
	private static Logger logger = Logger.getLogger("ProductDevTools");

	public static void main(String[] agrs) throws InterruptedException, SecurityException, IOException {
		setLogFormat();
		buildMapFields();

		List<String> customerIds = new ArrayList<>();

		customerIds.add("l1s1e9c682062922494c8d1385855fe75b1a");// at-kennebec.tendfarm.com

		for (String farm : customerIds) {
			migrateToShare(farm, farm);
		}
		System.exit(0);
	}

	private static void migrateToShare(String fromCustomerId, String toCustomerId) throws IOException {
		logger.info(String.format("[%s] Processing %s ...", TABLE_SHARE, fromCustomerId));
		ListData listProduct = getListProduct(fromCustomerId);
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
			String sellableId = "";
			sellableIds = createGroupSellable(toCustomerId, productId, sellableId, sellableIds, productOption,
					productInfo, TABLE_SHARE);

			// 2. create Share
			createNewProductType(toCustomerId, productId, productInfo, sellableIds, TABLE_SHARE);

			// 3. create Merchandise
			createMerchandise(TABLE_SHARE, toCustomerId, productId, productInfo);

			logger.info("\t" + toCustomerId + "\t Migrated " + productId);
		}
	}

	private static void buildMapFields() {
		productFromProduct.put("notTaxed", "notTaxed");
		productFromProduct.put("updated", "updated");

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
		sellableFromProduct.put("price", "price");

		metadataFromProduct.put("goalValue", "goalValue");

		// sellableFromProductOpts.put("controlInventory", "controlInventory");

		// sellable Inventory
		sellableInvenFromProduct.put("inventory", "quantity");

	}
}