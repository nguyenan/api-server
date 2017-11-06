package user;

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

		String customerId = "l1s1a607957b13e64302ade0359264434cd2";// a-migrate.tendfarm.com
//		migrateToShare(customerId);
//		System.out.println(getListMigratedData(customerId, TABLE_EVENT));
//		System.out.println(getListMigratedData(customerId, TABLE_CROP));
//		System.out.println(getListMigratedData(customerId, TABLE_SHARE));
//		System.out.println(getListMigratedData(customerId, TABLE_MERCHANDISE));
//		System.out.println(getListMigratedData(customerId, TABLE_SELLABLE));
//		System.out.println(getListMigratedData(customerId, TABLE_SELLABLE_INVENTORY));
		System.exit(0);
	}

	private static void migrateToShare(String customerId) throws IOException {
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
			String sellableId = "";
			sellableIds = createGroupSellable(customerId, productId, sellableId, sellableIds, productOption,
					productInfo);

			// 2. create Share
			createNewProductType(customerId, productId, productInfo, sellableIds, TABLE_SHARE);

			// 3. create Merchandise
			createMerchandise(TABLE_SHARE, customerId, productId, productInfo);

			logger.info(customerId + "\t Migrated " + productId);
		}
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
		sellableFromProductOpts.put("controlInventory", "controlInventory");

		// sellable Inventory
		sellableInvenFromProduct.put("inventory", "quantity");
	}
}
