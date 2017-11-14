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

		List<String> customerIds = new ArrayList<>();

//		customerIds.add("deeprootfarm.com");
//		customerIds.add("www.anisetend.com");
//		customerIds.add("www.fablefoods.com");
//		customerIds.add("www.fourrootfarmbetasetupgmail.com");
//		customerIds.add("www.freshfromstonecirclefarm.com");
//		customerIds.add("www.freshfromstonecirclefarm.com");
//		customerIds.add("www.fourrootfarmbetasetupgmail.com");
//		customerIds.add("www.jccslo.com");
//		customerIds.add("www.oya-organics.com");
//		customerIds.add("casadicampagna.farm");
//
//		customerIds.add("goldcoast.tendfarm.com");
//		customerIds.add("miesenbergerhof.tendfarm.com");
//		customerIds.add("springhaven.farm");
//		customerIds.add("squashblossom.farm");
//		customerIds.add("www.arfreckledhenfarm.com");
//		customerIds.add("www.cluckandtrowel.com");
//		customerIds.add("www.ecolibriumfarms.com");
//		customerIds.add("www.escargrowfarms.com");
//		customerIds.add("www.everlasting-garden.com");
//		customerIds.add("www.fortunatefarm.com");
//		customerIds.add("www.groundswellfarmsantacruz.com");
//		customerIds.add("www.hawkshaven.farm");
//		customerIds.add("www.jackiesroots.com");
//		customerIds.add("www.lincolnhillsfarm.com");
//		customerIds.add("www.lockewoodacres.com");
//		customerIds.add("www.lolasonoma.com");
//		customerIds.add("www.lovefood.farm");
//		customerIds.add("www.mahoniagardens.com");
//		customerIds.add("www.pvfproduce.com");
//		customerIds.add("www.redclayfarm.com");
//		customerIds.add("www.redearthgardens.com");
//		customerIds.add("www.robinsongfarms.com");
//		customerIds.add("www.roosterridgefarmaptos.com");
//		customerIds.add("www.seedsfarm.org");
//		customerIds.add("www.seedsfarms.com");
//		customerIds.add("www.strong.ag");
//		customerIds.add("www.tend.ag");
//		customerIds.add("www.thepeachjamboree.farm");
//		customerIds.add("www.trilliumfarmwa.com");
//		customerIds.add("vistalarga.tendfarm.com");
//		customerIds.add("farmrockfarm.tendfarm.com");
//		customerIds.add("rigby.tendfarm.com");
//		customerIds.add("milkweedfarm.tendfarm.co");
//
//		customerIds.add("hakurei1.farmer.guide");
//		customerIds.add("nautic.theme.tend.ag ");
//		customerIds.add("lancelot.theme.tend.ag");
//		customerIds.add("bodark.theme.tend.ag");
//		customerIds.add("hakurei.theme.tend.ag");
//		customerIds.add("kennebec.theme.tend.ag");
//		customerIds.add("at-hakurei.tendfarm.com");
//		customerIds.add("at-lancelot.tendfarm.com");
//		customerIds.add("at-nautic.tendfarm.com");
//		customerIds.add("at-bodark.tendfarm.com");
//		customerIds.add("at-kennebec.tendfarm.com");
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
		customerIds.add("test.farmer.guide");
//		customerIds.add("l1s14a419da09f404286a6397197aecb6f64");// hakurei1.farmer.guide


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
