package user;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.wut.model.Data;
import com.wut.model.list.ListData;
import com.wut.model.map.MappedData;
import com.wut.model.map.MessageData;
import com.wut.model.scalar.StringData;

public class ProduceProductMigration extends MigrationModel {
	private static Logger logger = Logger.getLogger("ProductDevTools");

	public static void main(String[] agrs) throws InterruptedException, SecurityException, IOException {
		setLogFormat();
		buildMapFields();
		List<String> customerIds = new ArrayList<>();
		// System.out.println(CustomerDevTools.getRowFrontendMapResourceTable(
		// "l1s1a607957b13e64302ade0359264434cd2", logger));

		// List<String> allCustomers =
		// CustomerDevTools.getAllCustomers("www.tend.ag");
		// System.out.println(CustomerDevTools.getRowFrontendMapResourceTable("dev1.tend.ag",logger).get("companyShortName"));
		List<String> allCustomers = new ArrayList<>();

		allCustomers.add("demo.tend.ag");
		allCustomers.add("dev1.tend.ag");
		allCustomers.add("l1s114c74d3c3edd4c37abffe2a3ad42a2b5");
		allCustomers.add("l1s14378352140f344c8a43f8ed2d9a4f30f");
		allCustomers.add("l1s168ebc2463a8b4da9b4af81590a6755dc");
		allCustomers.add("l1s18aa52bc3ae804db3a5850bf6d66c4a5c");
		allCustomers.add("l1s19a1ef763da544aa7bae77f07302f9072");
		allCustomers.add("l1s1a607957b13e64302ade0359264434cd2");// anhbodarkfarm.tendfarm.com
		allCustomers.add("l1s1b1614cdb7aa6474d81b75d16294aec03");
		allCustomers.add("l1s1dd9d4d38539849b5bcfb9fe72a6958e3");
		allCustomers.add("l1s1f97581abdbed4c56a91638998cd82b50");
		allCustomers.add("www.lockewoodacres.com");
		allCustomers.add("www.strong.ag");
		allCustomers.add("www.trilliumfarmwa.com");
		for (String farm : allCustomers) {
			if (farm.equals("www.tend.ag") || farm.equals("dev.retailkit.com") || farm.equals("test.farmer.guide"))
				continue;
			logger.info(farm);
			ListData listData = getListData(farm, TABLE_PRODUCE_PRODUCT);
			if (listData.equals(MessageData.NO_DATA_FOUND) || listData.size() <= 0) {
				continue;
			}
			List<MappedData> list = listData.toList();
			for (MappedData data : list) {
				Data sellable = data.get("sellable");
				if (sellable == null)
					continue;
				String sellableStr = sellable.toString();
				if (!sellableStr.contains(","))
					continue;
				sellableStr = sellableStr.replace("\\\"", "").replace("[", "").replace("]", "");
				String[] sellableIds = sellableStr.split(",");
				String productName = data.get("id").toString().replace(data.get("table").toString() + ":", "");
				logger.info(printSellableDetails(farm, productName, sellableIds));
			}
		}

		System.exit(0);
	}

	public static String printSellableDetails(String customerId, String productName, String[] sellableIds) {
		Data farmName = CustomerDevTools.getRowFrontendMapResourceTable(customerId, logger).get("companyShortName");
		Data customerDomain = CustomerDevTools.getRowFrontendMapResourceTable(customerId, logger).get("customerDomain");

		String result = String.format("%s\t%s\t%s\t%s", customerId, customerDomain, farmName.toString(), productName);
		List<String> units = new ArrayList<>();
		boolean needReview = false;
		for (String sellableId : sellableIds) {
			MappedData sellable = getSellable(customerId, sellableId);
			String unit = sellable.get("priceUnit").toString();
			if (units.contains(unit))
				needReview = true;
			units.add(unit);
			result += "\t" + (String.format("%s:%s", unit,
					sellable.get("id").toString().replace(sellable.get("table").toString() + ":", ""))

			);
		}
		result = needReview + "\t" + result;
		return result;
	}

	public static void migrateToProduceProduct(String fromCustomerId, String toCustomerId) throws IOException {
		logger.info(String.format("[%s] Processing %s ...", TABLE_PRODUCE_PRODUCT, fromCustomerId));
		ListData listProduct = getListProduct(fromCustomerId);
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
				MappedData productOption = getProductOption(fromCustomerId, productOptionId);

				productOptions.add(productOption);
				if (!isEvent(productOption)) {
					isEvent = false;
				}
			}
			if (isEvent)
				continue loopProduct;

			// if product is crop => migrate
			String rowId = productInfo.get("id").toString();
			productId = rowId.substring(rowId.lastIndexOf(":") + 1, rowId.length());

			System.out.println(rowId);

			// 1. Create SellableInventory, Sellable, metadata

			List<String> sellableIds = new ArrayList<String>();
			for (MappedData productOption : productOptions) {
				String oldId = productOption.get("id").toString();
				String sellableId = oldId.substring(oldId.lastIndexOf(":") + 1, oldId.length());
				sellableIds = createGroupSellable(toCustomerId, productId, sellableId, sellableIds, productOption,
						productInfo, TABLE_PRODUCE_PRODUCT);
			}
			// 2. create Produce
			createNewProductType(toCustomerId, productId, productInfo, sellableIds, TABLE_PRODUCE_PRODUCT);

			// 3. create Merchandise
			createMerchandise(TABLE_PRODUCE_PRODUCT, toCustomerId, productId, productInfo);

			logger.info("\t" + toCustomerId + "\t Migrated " + productId);
			// Backup and Delete Product Data
			// backupAndDeleteProduct(customerId, productInfo);
		}
	}

	private static void buildMapFields() {
		productFromProduct.put("notTaxed", "notTaxed");
		productFromProduct.put("name", "name");
		productFromProduct.put("updated", "updated");

		// merchandise
		merchandiseFromProduct.put("updated", "updated");
		merchandiseFromProduct.put("description", "description");
		merchandiseFromProduct.put("metaDescription", "metaDescription");
		merchandiseFromProduct.put("shortDescription", "shortDescription");
		merchandiseFromProduct.put("url", "url");
		merchandiseFromProduct.put("tags", "tags");
		merchandiseFromProduct.put("name", "name");
		merchandiseFromProduct.put("type", "type");
		merchandiseFromProduct.put("images", "images");
		merchandiseFromProduct.put("enabled", "enabled");

		// sellableFromProduct.put("images", "images");
		sellableFromProduct.put("manufacturerCode", "manufacturerCode");
		sellableFromProduct.put("upc", "upc");
		sellableFromProduct.put("cost", "cost");
		sellableFromProduct.put("weight", "weight");
		sellableFromProduct.put("default", "default");
		// sellableFromProduct.put("priceUnit", "unit");

		sellableFromProductOpts.put("update", "update");
		sellableFromProductOpts.put("price", "price");
		sellableFromProductOpts.put("priceUnit", "priceUnit");
		sellableFromProductOpts.put("images", "images");
		sellableFromProductOpts.put("choices", "choices");
		sellableFromProductOpts.put("default", "default");
		sellableFromProductOpts.put("controlInventory", "controlInventory");

		sellableInvenFromProductOpts.put("inventory", "quantity");

		metadataFromProductOpts.put("start", "start");
		metadataFromProductOpts.put("end", "end");
		// sellable Inventory
	}
}
