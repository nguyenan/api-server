package user;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
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

		// customerIds.add("deeprootfarm.com");
		// customerIds.add("www.anisetend.com");
		// customerIds.add("www.fablefoods.com");
		// customerIds.add("www.fourrootfarmbetasetupgmail.com");
		// customerIds.add("www.freshfromstonecirclefarm.com");
		// customerIds.add("www.freshfromstonecirclefarm.com");
		// customerIds.add("www.fourrootfarmbetasetupgmail.com");
		// customerIds.add("www.jccslo.com");
		// customerIds.add("www.oya-organics.com");
		// customerIds.add("casadicampagna.farm");
		//
		// customerIds.add("goldcoast.tendfarm.com");
		// customerIds.add("miesenbergerhof.tendfarm.com");
		// customerIds.add("springhaven.farm");
		// customerIds.add("squashblossom.farm");
		// customerIds.add("www.arfreckledhenfarm.com");
		// customerIds.add("www.cluckandtrowel.com");
		// customerIds.add("www.ecolibriumfarms.com");
		// customerIds.add("www.escargrowfarms.com");
		// customerIds.add("www.everlasting-garden.com");
		// customerIds.add("www.fortunatefarm.com");
		// customerIds.add("www.groundswellfarmsantacruz.com");
		// customerIds.add("www.hawkshaven.farm");
		// customerIds.add("www.jackiesroots.com");
		// customerIds.add("www.lincolnhillsfarm.com");
		// customerIds.add("www.lockewoodacres.com");
		// customerIds.add("www.lolasonoma.com");
		// customerIds.add("www.lovefood.farm");
		// customerIds.add("www.mahoniagardens.com");
		// customerIds.add("www.pvfproduce.com");
		// customerIds.add("www.redclayfarm.com");
		// customerIds.add("www.redearthgardens.com");
		// customerIds.add("www.robinsongfarms.com");
		// customerIds.add("www.roosterridgefarmaptos.com");
		// customerIds.add("www.seedsfarm.org");
		// customerIds.add("www.seedsfarms.com");
		// customerIds.add("www.strong.ag");
		// customerIds.add("www.tend.ag");
		// customerIds.add("www.thepeachjamboree.farm");
		// customerIds.add("www.trilliumfarmwa.com");
		// customerIds.add("vistalarga.tendfarm.com");
		// customerIds.add("farmrockfarm.tendfarm.com");
		// customerIds.add("rigby.tendfarm.com");
		// customerIds.add("milkweedfarm.tendfarm.co");
		//
//		customerIds.add("www.oldhousefarm.net");

//		customerIds.add("l1s1d4f26fa7e56140b2bb1d7e55aff9c201");// sunnybrookfarms.tendfarm.com
//		customerIds.add("l1s1e0ed15578c2941b8a6920d77461d0420");// eatmyfarm.tendfarm.com
//		customerIds.add("l1s1ef3f8487f4fc4b05bfdb1e40f0dceb04");// martinsgarden.tendfarm.com
//		customerIds.add("l1s17d9a1db8c36441f28440b3c1debf915a");// littlepigeonfarm.tendfarm.com
//
		
		// customerIds.add("hakurei1.farmer.guide");
		// customerIds.add("nautic.theme.tend.ag ");
		// customerIds.add("lancelot.theme.tend.ag");
		// customerIds.add("bodark.theme.tend.ag");
		// customerIds.add("hakurei.theme.tend.ag");
		// customerIds.add("kennebec.theme.tend.ag");
		// customerIds.add("at-hakurei.tendfarm.com");
		// customerIds.add("at-lancelot.tendfarm.com");
		// customerIds.add("at-nautic.tendfarm.com");
		// customerIds.add("at-bodark.tendfarm.com");
		// customerIds.add("at-kennebec.tendfarm.com");
		// customerIds.add("test.farmer.guide");

//		customerIds.add("l1s1165bf06f44e64c3b9b28a9ae9ce28556");// goldcoast.tendfarm.com
//		customerIds.add("l1s164f300d1cd1a44449001d682e241a2af");// miesenbergerhof.tendfarm.com
//		customerIds.add("l1s1eea46dbd40eb49d98d5f0c815fa5e261");// vistalarga.tendfarm.com
//		customerIds.add("l1s1f3399d666a844e0ea961a9584c9bf0aa");// farmrockfarm.tendfarm.com
//		customerIds.add("l1s1524228e787f64296a8d027bccce8efcb");// rigby.tendfarm.com
//		customerIds.add("l1s1e2f9fb53d2604a29aac7eae3cdf92168");// milkweedfarm.tendfarm.co
//		customerIds.add("l1s18aa52bc3ae804db3a5850bf6d66c4a5c");// at-hakurei.tendfarm.com
//		customerIds.add("l1s1b1614cdb7aa6474d81b75d16294aec03");// at-lancelot.tendfarm.com
//		customerIds.add("l1s18e2d597936404c44b592e2ff960495dd");// at-nautic.tendfarm.com
//		customerIds.add("l1s1f97581abdbed4c56a91638998cd82b50");// at-bodark.tendfarm.com
//		customerIds.add("l1s1e9c682062922494c8d1385855fe75b1a");// at-kennebec.tendfarm.com
//		customerIds.add("www.oldhousefarm.net");
//		customerIds.add("test.farmer.guide");
//		customerIds.add("l1s14a419da09f404286a6397197aecb6f64");// hakurei1.farmer.guide
		for (String farm : customerIds) {
			ListData listData = getListData(farm, TABLE_PRODUCE_PRODUCT);
			if (!listData.equals(MessageData.NO_DATA_FOUND)){
				List<MappedData> list = listData.toList();
				for (MappedData data : list){
					System.out.println(String.format("%s\t%s",  data.get("id").toString(),data.get("sellable")));
				}
			}
		}

		System.exit(0);
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
