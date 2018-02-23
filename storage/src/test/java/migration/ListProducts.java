package migration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.wut.datasources.cassandra.CassandraSource;
import com.wut.model.list.ListData;
import com.wut.model.map.MappedData;
import com.wut.model.scalar.IdData;
import com.wut.model.scalar.StringData;
import com.wut.support.settings.SettingsManager;

public class ListProducts extends MigrationModel {
	private static CassandraSource cassSource = new CassandraSource();
	private static final String applicationStr = "core";
	private static final IdData application = new IdData(applicationStr);
	private static final IdData tableId = IdData.create("flat2");
	private static Logger logger = Logger.getLogger("ProductDevTools");

	public static void main(String[] agrs) throws InterruptedException, SecurityException, IOException {
		setLogFormat();

		List<String> listCustomer = new ArrayList<>();
		listCustomer.add("casadicampagna.farm"); 
		for (String farm : listCustomer) {
			listProducts(farm);
		} 
		System.exit(0);
	}

	public static void listProducts(String customerId) throws IOException {
		String payment = "";
		int numOfProducts = 0;
		int numOfShares = 0;
		int numOfEvents = 0;
		String listProductName = "";
		String listShareName = "";
		String listEventName = "";
		ListData listProduct = getListProduct(customerId);
		String productId = "";
		StringData options = new StringData("");

		loopProduct: for (Object obj : listProduct) {
			MappedData productInfo = (MappedData) obj;
			if (isShare(productInfo)) {
				numOfShares++;
				listShareName += productInfo.get("name").toString() + ", ";
				continue;
			}

			options = (StringData) productInfo.get("options");
			if (options == null) {
				continue;
			}

			JsonArray productOptionIds = parseProductOptions(options);
			boolean isEvent = true;
			for (JsonElement option : productOptionIds) {
				String productOptionId = option.toString().replaceAll("\\\"", "");
				MappedData productOption = getProductOption(customerId, productOptionId);
				if (!isEvent(customerId, productOption)) {
					isEvent = false;
				}
			}
			if (isEvent) {
				numOfEvents++;
				listEventName += productInfo.get("name").toString() + ", ";
			} else {
				numOfProducts++;
				listProductName += productInfo.get("name").toString() + ", ";
			}
			payment = SettingsManager.getClientSettings(customerId, "payment.braintree-mechant-id");

		}
		System.out.println(String.format("%s\t%d\t%s\t%d\t%s\t%s\t%d\t%s", customerId, numOfProducts, listProductName,
				numOfShares, listShareName, payment, numOfEvents, listEventName));
	}

	public static boolean isEvent(String customerId, MappedData productOption) {
		if (productOption == null)
			return false;
		StringData start = (StringData) productOption.get("start");
		StringData end = (StringData) productOption.get("end");
		return (start != null && end != null);
	}
}
