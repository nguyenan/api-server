package migration;

import java.io.IOException;
import java.util.logging.Logger;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.wut.model.list.ListData;
import com.wut.model.map.MappedData;
import com.wut.model.scalar.StringData;

public class CopyProduct extends MigrationModel {
	private static Logger logger = Logger.getLogger("ProductDevTools");

	public static void main(String[] agrs) throws InterruptedException, SecurityException, IOException {
		setLogFormat();

		String fromCustomerId = "www.cluckandtrowel.com";
		String toCustomerId = "l1s181e18a2c1656476090b058256c409b6c";
		//copyProduct(fromCustomerId, toCustomerId);
		System.exit(0);
	}

	private static void copyProduct(String fromCustomerId, String toCustomerId) throws IOException {
		ListData listProduct = getListProduct(fromCustomerId);

		for (Object obj : listProduct) {
			MappedData productInfo = (MappedData) obj;
			StringData options = (StringData) productInfo.get("options");
			if (options != null) { // copy productOption
				JsonArray productOptionIds = parseProductOptions(options);
				for (JsonElement option : productOptionIds) {
					String productOptionId = option.toString().replaceAll("\\\"", "");
					MappedData productOptionInfo = getProductOption(fromCustomerId, productOptionId);

					createNewProductOptions(toCustomerId, productOptionId, productOptionInfo);
				}
			}

			// copy product
			String rowId = productInfo.get("id").toString();
			String productId = rowId.substring(rowId.lastIndexOf(":") + 1, rowId.length());

			createNewProduct(toCustomerId, productId, productInfo);
			//logger.info(toCustomerId + "\t Copied " + productId);
		}
	}
}
