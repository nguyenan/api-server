package user;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import com.wut.datasources.cassandra.CassandraSource;
import com.wut.model.list.ListData;
import com.wut.model.map.MappedData;
import com.wut.model.map.MessageData;
import com.wut.model.scalar.BooleanData;
import com.wut.model.scalar.IdData;
import com.wut.model.scalar.StringData;
import com.wut.pipeline.Authenticator;
import com.wut.pipeline.UserStore;
import com.wut.provider.table.TableProvider;
import com.wut.resources.storage.TableResource;

public class CassandraImportStorefrontUser {
	private static CassandraSource cassSource = new CassandraSource();
	private static final String applicationStr = "core";
	private static final IdData application = new IdData(applicationStr);
	private static final IdData tableId = IdData.create("flat2");

	private static Logger logger = Logger.getLogger("MyLog");
	private static FileHandler fh;
	private static UserStore userStore = new UserStore();

	public static void main(String[] agrs) throws InterruptedException, SecurityException, IOException {
		fh = new FileHandler("/data/importStorefrontUser");
		logger.addHandler(fh);
		fh.setFormatter(new LogFormatter());

		List<String> imported = new ArrayList<String>();
		imported.add("an.test16.farmer.guide");
		imported.add("beta.tend.ag");
		imported.add("birddogfarm.com");
		imported.add("blankslatefarm.com");
		imported.add("bodark.theme.tend.ag");
		imported.add("anisetend.com");
		imported.add("baybranchfarm.com");
		imported.add("beta.retailkit.com");
		imported.add("betaadmin.tend.ag");
		imported.add("betatendtest.com");
		imported.add("casadicampagna.farm");
		imported.add("ecofarmdemotend.ag");
		imported.add("fablefoods.com");
		imported.add("fourrootfarmbetasetupgmail.com");
		imported.add("freshfromstonecirclefarm.com");
		imported.add("fvgsefeas#dwad.com");
		imported.add("greenheartsfamilyfarm.com");
		imported.add("johnanthony3gmail.com");
		imported.add("l1s1ef7709f1773248398ae7a29ad292effc");
		imported.add("nautic.theme.tend.ag");
		imported.add("paradisevalleyproduce.farm");
		imported.add("peacefulbelly.com");
		imported.add("pieranchbetasetupgmail.com");
		imported.add("pos.dev.tend.ag");
		imported.add("ranchosoquel.tend.ag");
		imported.add("redhawkfarmnc.com");
		imported.add("sanjosefarm.tend");
		imported.add("squashblossom.farm");
		imported.add("stephaniehclubswim.com");
		imported.add("stephaniehspiraledge.com");
		imported.add("stephaniehtend.ag");
		imported.add("stephaniehtend.co");
		imported.add("stephantend.ag");
		imported.add("topleaffarms.com");
		imported.add("whitegatebetasetupgmail.com");
		imported.add("www.benaroyafamilyfarm.com");
		imported.add("www.bloomsburyfarms.com");
		imported.add("www.cluckandtrowel.com");
		imported.add("www.danieleyogaoutlet.com");
		imported.add("www.danieltest.com");
		imported.add("www.danieltest2.com");
		imported.add("www.dirtygirlproduce.com");
		imported.add("www.dttestfarm.com");
		imported.add("www.escargrowfarms.com");
		imported.add("www.everlasting-garden.com");
		imported.add("www.farmer.land");
		imported.add("www.ficklecreekfarm.com");
		imported.add("www.fourrootfarm.com");
		imported.add("www.fpfarm.com");
		imported.add("www.groundswellfarmsantacruz.com");
		imported.add("www.hawkshaven.com");
		imported.add("www.hawkshaven.farm");
		imported.add("www.homesteadproduce.com");
		imported.add("www.jackiesroots.com");
		imported.add("www.johnnysfarm.com");
		imported.add("www.lincolnhillsfarm.com");
		imported.add("www.lockewoodacres.com");
		imported.add("www.lolasonoma.com");
		imported.add("www.lolasonoma.com/");
		imported.add("www.lovefood.farm");
		imported.add("www.mahoniagardens.com");
		imported.add("www.massarofarm.org");
		imported.add("www.msunorthfarm.org");
		imported.add("www.oldhousefarm.net");
		imported.add("www.pieranch.org");
		imported.add("www.redearthgardens.com");
		imported.add("www.robinsongfarms.com");
		imported.add("www.roosterridgefarmaptos.com");
		imported.add("www.singlethreadfarms.com");
		imported.add("www.steadfast-farm.com");
		imported.add("www.stonebarnscenter.org");
		imported.add("www.strong.ag");
		imported.add("www.tend.ag");
		imported.add("www.thepeachjamboree.farm");
		imported.add("www.trilliumfarmwa.com");
		imported.add("danielisport.com");
		imported.add("danielspiraledge.com");
		imported.add("danieltend.ag");
		imported.add("danielyoga24.com");
		imported.add("daybreakflowerfarm.com");
		imported.add("demo.tend.ag");

		imported.add("demo.tend.co");

		List<String> allCustomers = getAllCustomers();
		// List<String> allCustomers = new ArrayList<String>();
		// allCustomers.add("demo.tend.co");
		// allCustomers.add("dev.retailkit.com");
		// allCustomers.add("test.farmer.guide");

		for (String customer : allCustomers) {
			/*
			 * if (imported.contains(customer)) { logger.info("###" + customer +
			 * "\tskipped"); continue; }
			 */
			logger.info(customer);
			/*
			 * IdData customerId = new IdData(customer); logger.info("###" +
			 * customerId);
			 */
			// List<String> storefrontUsers = getStorefrontUsers(customerId,
			// logger);
			/*
			 * if (storefrontUsers.size() > 200) { logger.info("###" + customer
			 * + "\tskipped\t" + storefrontUsers.size()); continue; }
			 */
			/*
			 * Thread.sleep(2000); cloneUserContactInfo2(customerId, logger);
			 */

		}

		System.exit(0);

	}

	public static List<String> getStorefrontUsers(IdData customerId, Logger logger) {
		List<String> storefrontUsers = new ArrayList<String>();
		TableProvider table = TableResource.getTableProvider();
		IdData usersTable = new IdData("users");
		ListData listUsers = table.getRows(customerId, IdData.create("core"), usersTable);

		String email = "";
		for (Object item : listUsers) {
			MappedData user = (MappedData) item;
			email = user.get("id").toString().replaceAll(customerId + ":", "");
			storefrontUsers.add(email);
		}
		logger.info("Total storefront users\t" + storefrontUsers.size());
		return storefrontUsers;
	}

	public static void cloneUserContactInfo2(IdData customerId, Logger logger) {
		MappedData filter = new MappedData();
		String userTable = String.format("core:%s:public:%s", customerId, "user");
		String userStorefrontTable = String.format("core:%s:public:%s", customerId.toRawString(), "userStorefront");

		filter.put("table", new IdData(userTable));
		ListData rowsWithFilter = cassSource.getRowsWithFilter(customerId, application, tableId, filter);
		logger.info("User table\t" + rowsWithFilter.size());

		String email = "";
		int skipped = 1;
		int imported = 1;
		for (Object obj : rowsWithFilter) {
			try {
				MappedData userData = (MappedData) obj;
				email = userData.get("id").toString().replaceAll(userTable + ":", "");

				MappedData storefrontUserInfo = getStorefrontUserInfo(customerId.toRawString(), email);
				if (!storefrontUserInfo.equals(MessageData.NO_DATA_FOUND)) {
					userData.put("table", new IdData(userStorefrontTable));
					userData.remove(new StringData("permission"));
					BooleanData update = cassSource.updateRow(customerId, application, tableId,
							new IdData(userStorefrontTable + ":" + email), userData);
					logger.info("Imported\t#" + imported++ + "\t" + email + "\t" + update);
				} else {
					logger.info("Skipped\t#" + skipped++ + "\t" + email);
				}
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
	}

	public static MappedData getStorefrontUserInfo(String customerId, String email) {
		MappedData userData = (MappedData) (userStore.readSecureInformation(customerId, applicationStr,
				Authenticator.getUserId(customerId, email)));
		return userData;
	}

	public static void cloneUserContactInfo(IdData customerId, List<String> storefrontUsers, Logger logger) {
		MappedData filter = new MappedData();
		String userTable = String.format("core:%s:public:%s", customerId, "user");
		String userStorefrontTable = String.format("core:%s:public:%s", customerId.toRawString(), "userStorefront");

		filter.put("table", new IdData(userTable));
		ListData rowsWithFilter = cassSource.getRowsWithFilter(customerId, application, tableId, filter);
		logger.info("User table\t" + rowsWithFilter.size());

		String email = "";
		int skipped = 1;
		int imported = 1;
		for (Object obj : rowsWithFilter) {
			try {
				MappedData userData = (MappedData) obj;
				email = userData.get("id").toString().replaceAll(userTable + ":", "");
				if (storefrontUsers.contains(email)) {
					userData.put("table", new IdData(userStorefrontTable));
					userData.remove(new StringData("permission"));
					BooleanData update = cassSource.updateRow(customerId, application, tableId,
							new IdData(userStorefrontTable + ":" + email), userData);
					logger.info("Imported\t#" + imported++ + "\t" + email + "\t" + update);
				} else {
					logger.info("Skipped\t#" + skipped++ + "\t" + email);
				}
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
	}

	public static List<String> getAllCustomers() {
		List<String> customers = new ArrayList<String>();
		MappedData filter = new MappedData();
		String table = String.format("core:%s:public:%s", "www.tend.ag", "site");
		filter.put("table", new IdData(table));

		ListData rowsWithFilter = cassSource.getRowsWithFilter(new IdData("www.tend.ag"), application, tableId, filter);
		System.out.println(rowsWithFilter.size() + " customers");
		String customerId = "";
		for (Object obj : rowsWithFilter) {
			MappedData row = (MappedData) obj;
			customerId = row.get("id").toString().replaceAll(table + ":", "");
			customers.add(customerId);
		}
		return customers;
	}
}
