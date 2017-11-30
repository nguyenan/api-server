package user;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

import com.wut.datasources.cassandra.CassandraSource;
import com.wut.model.Data;
import com.wut.model.PermissionRole;
import com.wut.model.list.ListData;
import com.wut.model.map.MappedData;
import com.wut.model.map.MessageData;
import com.wut.model.scalar.IdData;
import com.wut.model.scalar.StringData;
import com.wut.pipeline.Authenticator;
import com.wut.pipeline.PermissionStore;
import com.wut.pipeline.UserStore;

public class UserDevTools {
	private static UserStore userStore = new UserStore();
	private static PermissionStore permissionStore = new PermissionStore();
	private static CassandraSource cassSource = new CassandraSource();
	private static final String applicationStr = "core";
	private static final IdData application = new IdData(applicationStr);
	//
	private static final String adminCustId = "admin.tend.ag";
	private static final IdData tableId = IdData.create("flat2");

	private static Logger logger = Logger.getLogger("UserDevTools");
	private static FileHandler fh;

	public static void main(String[] agrs) throws InterruptedException, SecurityException, IOException {
		fh = new FileHandler("/data/scripts/devlog/user_" + System.currentTimeMillis());
		logger.addHandler(fh);
		fh.setFormatter(new LogFormatter());
		String parentCustomerId = "www.tend.ag";
		List<String> customerIds = CustomerDevTools.getAllCustomers(parentCustomerId);
		String scannedStr = "anisetend.com,baybranchfarm.com,beta.retailkit.com,beta.tend.ag,betatendtest.com,birddogfarm.com,blankslatefarm.com,bodark.theme.tend.ag,casadicampagna.farm,danielisport.com,danielspiraledge.com,danieltend.ag,danielyoga24.com,daybreakflowerfarm.com,demo.tend.ag,design.tend.ag,dev.tend.co,dev1.tend.ag,dev2.tend.ag,dev3.tend.ag,dirtygirlbetasetupgmail.com,ecofarmdemotend.ag,fablefoods.com,farmer.events,farmer.express,fourrootfarmbetasetupgmail.com,freshfromstonecirclefarm.com,greenheartsfamilyfarm.com,grubcsafarm.com,hakurei.theme.tend.ag,jccslo.com,johnanthony3gmail.com,kennebec.theme.tend.ag,l1s101f0254e7f084ce19c1b9c3fa907f5d6,l1s1033673476495493dab163b1ef936e257,l1s1034b0e6a33ed4b8a9a3762ecd4bfe5bd,l1s103f2ee7bc58344a099d4736d18ce26a6,l1s10491e87ed794441fadee753c126c414a,l1s107b69fbcb9c142ff9ec0f57c57b5ea93,l1s10876d52f97ee4f1082ef85d3282627c5,l1s109be4f8e089b438cb92efae6709c283c,l1s10a6b773d01154f389277fec38f42b948,l1s10b90880d920b40d09f7c7b8b55a91064,l1s10b9cd9693bfd45b5a74e32aef67800a8,l1s10dab1591de0c47f9ae55a8b322b0dc18,l1s10ef5770f037946998ef4c72dcd12b295,l1s10f21d958e56945d298d92d9b859d434d,l1s10f4fd652c7b3426a8295b6a012141a67,l1s10f6d95054e4e44bba061ef44444c3160,l1s1104d05bdc47c46548697d63dda699793,l1s11223b73e13c342599805bf81f876fcab,l1s11242bb197b3345608edb064581ca38ce,l1s114827d6f09c642b998016d806f7f6ce2,l1s114c74d3c3edd4c37abffe2a3ad42a2b5,l1s11643725779c54afda9bb3554082825f1,l1s1165bf06f44e64c3b9b28a9ae9ce28556,l1s116bcf3f019094471a9a52db344100c84,l1s1178566286a3440089bbe6a0ee566abb4,l1s117d378d39f83421ba63f8043f1340c4a,l1s11836608c06a5419abab4bc677a350aca,l1s11b682706cb17411ebde7b5957d09d2d5,l1s11b8d8416b1df490c8ffb3919d14d1047,l1s12365ac1385ba4c14bd51ca8adbb4a213,l1s123be0b6a5d7c483c8f4612d2e3dae61b,l1s1251dac85f50c4c67bfa1d507d7787944,l1s125e9886c668e4979b5ffcf479c4551ef,l1s126f236d9aaff42d0af3e9c448ce72d21,l1s1282a5d482d9b4081af1749a2df74d678,l1s12d4b52013fee4430aaa221d34a069759,l1s12d87327cf3ed42cbbfee9f8424b9fe08,l1s12f4ef8aa5d5b4d838882db460bf2ab2d,l1s12f7ec3e8a4a6479caa74d24415f0240c,l1s12f9a0788a88e471187192629a13af28b,l1s13129a24537c5422ebcfab765644fff18,l1s132bb7f9c82204c92a9b70d1e465df295,l1s133f98c302c5c47918d5650087677fc5c,l1s135aa5ccbf094438d9a901640499c772b,l1s13895596cf2544919a8067d807b288e96,l1s138b0ffdaaf3841f59ef541a2f6f7256b,l1s13a750a2d5f2441158788e05447c45841,l1s13b7d3a13b1e9475ebfc4c05458b97f82,l1s13ce350a02dc74033b1c2c1ccc89709f5,l1s13d52568358ce4429b83e80c77679ec38,l1s13dd7d913f5bb4a1eab8e3afc1d4c6057,l1s13f348b3695854fdc8a30ee0fc6c038dc,l1s13fb03cbda270449287f5d07eb2716592,l1s13fe2a7ab601544f2a13d953afbb5a42b,l1s142179133272a49379613b3a08fcc04ee,l1s14267b60f8b2b410a8fbf61128f7d68d7,l1s14378352140f344c8a43f8ed2d9a4f30f,l1s143a1114a4c5b4377b591da731c593112,l1s144ae1dd62f38413383a0768f9d6602ee,l1s144f3e14b053547f0bcf0b43f8d280681,l1s146f22ffbb6ad43b886b099b5953196fe,l1s1476dd930a9274fc79b90c2a03dada43f,l1s1485016044ac246d9b39b219579fcd508,l1s1485ed7b74eb94883b63bb1fff3a35880,l1s1499f43995f6b4ef0ab52c7f300158452,l1s14a419da09f404286a6397197aecb6f64,l1s14b0099449aca4548a4391489aebd7edb,l1s14b7c0df5da7b4732a1a55061f31fc988,l1s14c1c8f5dc7d0426985db888158c91145,l1s14d998e09e33d4566bb7ce9b227ecf4e9,l1s14e02bb69df5b4bc895ece00c7dcb9371,l1s14fa6fabaa2c44cd18ddcdd8f41e304f3,l1s1515d719bda604a43b72b3c5e605a42df,l1s15239c63766c24bfca151c0f673749f7e,l1s1524228e787f64296a8d027bccce8efcb,l1s1531afeb807874cb3a13514ec98e69427,l1s1536b533079b24d2fa0e5d671934f79a2,l1s153d0a7bc6fe14615a09cb87c9ad720d8,l1s1542bb27394a34314937ddc7cf5827eb6,l1s15486c2ce280a4234bfd627a131c5c0c6,l1s155673a8d0b724b1a8d2bea933245650c,l1s1558d56d66da242a6af46b45672501b56,l1s158fa8f0fd6684cc1a124e2f978dae0bb,l1s15a35cb041b3d48ac9e17d053e2856ce8,l1s15ae0414f0a6e4dae8762a6feeb5b7a4a,l1s15ae1ebec25fc4e7e9ca22942335bc1c5,l1s15db946f658a54df8a4226c2d9ca7e603,l1s15e7f27b05c454cfdbdfdb28ffb6e4606,l1s15edab33977234e04ba2d5267fc303d67,l1s15f8751483bf24814b4a7adc4f7257d5d,l1s15fb337f6e8fd4d90bb696c5a89927ac8,l1s15fd84eeed45b4a139cd0482d973532b7,l1s15ffada0dbb2d434a96331f859e8b94af,l1s1603c2b4bda9f4feebd4f780d8780cc78,l1s162f135e32fe54c0ab64f444837995e82,l1s163d7d62512fd4084815002a666680940,l1s163de48d59ebb440488adeff24c63b8e7,l1s164482d418d494b9f92a0e789be905dd6,l1s164f300d1cd1a44449001d682e241a2af,l1s164fc670066b3449fb3913dff4116daf9,l1s1659d5f2c52304c3eb9a529748ecd21ac,l1s1664a687f68ce4f149e607230cbae5c9e,l1s166c179f1fde8410c87d01b0bfa812e82,l1s167ad2cdb0dfe4e8db160c77707c59345,l1s168289b5ce8774107b5bcc7a47007ec0d,l1s168c2fd18f11d4916a775a055a025a739,l1s168ebc2463a8b4da9b4af81590a6755dc,l1s1697171faea314553bbb6575b6059d534,l1s16a0641e3def84b02a61b2f3c0bd4bfca,l1s16ac555fc1e644e7f811cb174298f2dd5,l1s16ba96746aa0c4aa4a55d97c268da91b6,l1s16c8e1071e00c430bb6e238be45cb9c64,l1s16e6ed9c31b0b4762b00f20975b875078,l1s16ef307b731cc4834af19dd6b1df45cdf,l1s16f3b0bb572ec4510b14b5216de092434,l1s16f5f4bd459fe4ad48cbfa27d5f109c63,l1s1715b77dbfb4a486bac755d2e5cef806d,l1s171fcde7ddb0946fdbef9d9ac05501213,l1s172f117f5fd834f498b8a85da6233f736,l1s173ed504cd25449edabfc61091ebe864f,l1s1747fb04db3cd4c4aa17fea0647150831,l1s1777529089b3c4c458e0b5ccebb765af8,l1s17855ab1803cb447fa3593778934c1349,l1s17d89c60a177c4af9a8ab9c798ee3bdde,l1s17d9a1db8c36441f28440b3c1debf915a,l1s1803080ba61f54a239f4c92659c4d913b,l1s181df3fcda61f4512b54061b5e3fe63ef,l1s181e18a2c1656476090b058256c409b6c,l1s185c9229ff0fa4cdca0522f2bc3839b5a,l1s187ad5de8d6bd4d11af96b1a83ec0c6d8,l1s18a5cd1bb708649d98bda73e6cb54dd40,l1s18aa52bc3ae804db3a5850bf6d66c4a5c,l1s18b31e2380d5440e195752cf6544a4220,l1s18cb5f60298ce4c9b881cf5390bf23d96,l1s18e2d597936404c44b592e2ff960495dd,l1s19640de0cf8624e88997419b2620002ac,l1s196bd536f86e040eab43594ed8c86aae5,l1s1974c4199849848868e059f0b54c50c66,l1s1985ee1441b7b448aae8fdd8f889a323a,l1s1988ca315b1e84483997463ad11b0a87e,l1s19a1ef763da544aa7bae77f07302f9072,l1s19a8a4a2fdf33450f973e87c827b87ece,l1s19cd9c90c649844aea4ba9b168a4a37ab,l1s19eed5c0060bf46c6ad248c3148308bb8,l1s1a00c5ae726de46f3a33cda242080abde,l1s1a0f9fd718bff474db995c4e1dbee03eb,l1s1a3b5f5d9f3084aa4b88b357a63e2573a,l1s1a4153dc9020d432f9176b014b7f252f5,l1s1a591a933df63405c9f43b3f6b25e7ff1,l1s1a607957b13e64302ade0359264434cd2,l1s1a80daec7d17e46bcaccc94d2bb5b9c9d,l1s1aa04418a9ee34864a95d89fff989b46d,l1s1acbb2366045a44c382ac561c2fca8e07,l1s1ade99d8f37bc4151b5e79cd97afb3074,l1s1b1614cdb7aa6474d81b75d16294aec03,l1s1b1891a92271944f7aa17c20df7423eb5,l1s1b35ea5cac4684f2f8bdd9cb035cc1045,l1s1b53f5f6bb6e9465491028c821311fe9b,l1s1b654dd9713634a39b8357d351c9bb7d5,l1s1b686c40e1d8d4e0a9da9befe7fcbc335,l1s1b7fa50adadce47dfb32a1e54ab6df54c,l1s1b7fa605191304571ac15294fd819a5df,l1s1b91743b501e6465faefb2fc10b13a1d6,l1s1b9c3698e120d48bda9952f0b64614018,l1s1ba416d0b3d5c49bdadc4fb419bf9bafa,l1s1bae49b584f6347f5852be1193d7206a0,l1s1bc2f33e2e2d34598b3aa4b554e864029,l1s1bc9ee06a561d44db9c59d6c7b70318b9,l1s1bd0fc6bfe06b45f89531594b5029f664,l1s1bd62720fe6b34d29b381513882da4a9e,l1s1bf12b64120df483f8b1c9d8ebcd7263f,l1s1c1ae7694ed0b484e86aa93d5b64defc0,l1s1c2b417edd3ba4bb3816a643c10d2a518,l1s1c4680126104c4e489c222d710d027a81,l1s1c59c399e26e04b6798015f22dd6079db,l1s1c62b1243389c49c698a100745091824a,l1s1c66e9912956f4337a23e0ba6ac9f39e9,l1s1c694e1c435a54b22b5c0c8040f181931,l1s1c8a1ca84809f40659dbf22ea29190aba,l1s1c909e8f42fae480a9a1d0b45cef841e1,l1s1ca69efbdf8af43a9a4880d2a9e65e895,l1s1cb6f54f8a3ef4014b0ad3ac425f6e058,l1s1cf616bc4fc1b4e08b23be7daff2fbf3e,l1s1d015cafe645a454782f73480203e51d3,l1s1d068e7403064447c927126571d4b5117,l1s1d1c7cba15ad04eab9f65bb96bf1b804b,l1s1d3816a19a28d4a06bc0cfd81da30fda3,l1s1d390f93522144c52ae2d4431c2cbc782,l1s1d4f26fa7e56140b2bb1d7e55aff9c201,l1s1d5a0c9e344f34391ab824cbc64a284d9,l1s1d825f5271cf04c98bedd949b5aa880a3,l1s1dbeaa29b336347dbafd2ff0c5311ebdf,l1s1dd91634991d24f1e905e7cff33ab22b7,l1s1ddd07984426b494286ff0db688dfd375,l1s1dfe9dfa5ce0844da9eab30ac692a2cf2,l1s1e0ed15578c2941b8a6920d77461d0420,l1s1e162fca938d84aed94fc915e7bda49d8,l1s1e26154d996d84962ab759bec3f0088b2,l1s1e2ca18ee38e64b4e974a710ed8f5c0bb,l1s1e2f9fb53d2604a29aac7eae3cdf92168,l1s1e3e8abf36c5148199f0b31a8011bdb70,l1s1e47ddc2c3080454880dec4f20251abdc,l1s1e68fbe8d36a94c22a1e69ef25d9ba015,l1s1e6a60b8694f04a99ae4ebf633fc3bcda,l1s1e6d67d04f68949d3a2348312a7f5080b,l1s1e8ac93b3ce294db99c85308af50d7acd,l1s1e8f44eceab9a46828875e76232a0770d,l1s1e972e3d7601c4e7b87e39b46be9c1c92,l1s1e9b044a2cadb4314bc92bd7f7a538714,l1s1e9c682062922494c8d1385855fe75b1a,l1s1eaa68028886b41709908f9191c013b20,l1s1eb5819fd2ae248109c94b788b298804f,l1s1eb8a49aef0084e9092d23bdf29f571d9,l1s1ecbc3a5d82564c079984e963fd809683,l1s1edf6f6fd6d054edd9240b01c351caa7d,l1s1eea46dbd40eb49d98d5f0c815fa5e261,l1s1eeab8318ddf54e0daf4d6ec04c884f77,l1s1ef3f8487f4fc4b05bfdb1e40f0dceb04,l1s1ef4d30571ede41439158d3d0b43b3c04";
		List<String> scanned = new ArrayList<String>(Arrays.asList(scannedStr.split(",")));
		// List<String> customerIds = new ArrayList<>();
		// customerIds.add("www.mapiii.com");
		for (String customer : customerIds) {
			if (customer.equals("dev.retailkit.com") || customer.equals("test.farmer.guide"))
				continue;
			if (scanned.contains(customer))
				continue;
			MappedData rowFrontendMapResourceTable = CustomerDevTools.getRowFrontendMapResourceTable(customer, logger);
			if (rowFrontendMapResourceTable == null) continue;

				System.out.println(customer + "\t" + rowFrontendMapResourceTable.get("companyShortName") + "\t"
						+ getListOwnerUsers(customer));
		}
		System.exit(0);
	}

	public static void getAllUserAuthen(String customerId) {
		Data userData = (userStore.readRows(customerId, applicationStr));
		logger.info(userData.toString());
	}

	public static void migrateTendUsers(String customerId, int count) {
		Map<String, String> listUsers = getListAdminUsers(customerId);
		logger.info("#" + count + " " + customerId + "\t size: " + listUsers.size());

		String username = "";
		String role = "";
		String adminUserId = "";
		String oldUserId = "";
		for (Map.Entry<String, String> entry : listUsers.entrySet()) {

			role = entry.getValue();
			username = entry.getKey();
			adminUserId = Authenticator.getUserId(adminCustId, username);

			if (username.startsWith("l1s1")) {
				logger.info("skipped 2 " + username);
				continue;
			}
			// check if account was imported
			MappedData userData = (MappedData) (userStore.readSecureInformation(adminCustId, applicationStr,
					adminUserId));
			if (userData.equals(MessageData.NO_DATA_FOUND)) {
				oldUserId = Authenticator.getUserId(customerId, username);
				userData = (MappedData) userStore.readSecureInformation(customerId, applicationStr, oldUserId);
				if (userData.equals(MessageData.NO_DATA_FOUND)) {
					logger.info("skipped " + username);
					continue;
				}
				cloneUserWithNewCustomer(userData, adminCustId);
			}
			updateTendPermission(customerId, adminUserId, role);
		}
	}

	public static void cloneUserWithNewCustomer(MappedData userData, String toCustomerId) {
		StringData username = (StringData) userData.get("username");
		String newUserId = Authenticator.getUserId(toCustomerId, username.toRawString());

		// update corresponde customer
		logger.info("added " + userData);
		userData.put("id", new IdData(newUserId));
		userData.put("customer", new IdData(toCustomerId));
		userStore.update(toCustomerId, application.toRawString(), newUserId, userData.getMapAsPojo());
	}

	public static void updateTendPermission(String affectedCustomerId, String affectedUserId, String role) {
		MappedData permissionData = (MappedData) permissionStore.read(adminCustId, applicationStr, affectedUserId);
		if (permissionData.equals(MessageData.NO_DATA_FOUND))
			permissionData = new MappedData();
		permissionData.put(affectedCustomerId, new StringData(role));

		permissionStore.update(adminCustId, applicationStr, affectedUserId, permissionData.getMapAsPojo());
		logger.info("permission " + affectedUserId);
	}

	public static Map<String, String> getListAdminUsersWithContact(String customerId) {
		IdData customer = new IdData(customerId);
		Map<String, String> admins = new HashMap<String, String>();
		MappedData filter = new MappedData();
		String table = String.format("core:%s:public:%s", customerId, "user");
		filter.put("table", new IdData(table));
		ListData rowsWithFilter = cassSource.getRowsWithFilter(customer, application, tableId, filter);
		// logger.info("\n" + customerId + "\t" + rowsWithFilter.size());
		String email = "";
		for (Object obj : rowsWithFilter) {
			MappedData row = (MappedData) obj;
			email = "";
			StringData contact = (StringData) row.get("contact");
			email = row.get("id").toString().replaceAll(table + ":", "");

			admins.put(email, contact == null ? "null"
					: contact.toString().replaceAll("\\[\\\\\"", "").replaceAll("\\\\\"\\]", ""));

			// System.out.println(String.format("%s\t%s", email, contact == null
			// ? "null"
			// : contact.toString().replaceAll("\\[\\\\\"",
			// "").replaceAll("\\\\\"\\]", "")));
		}
		return admins;
	}

	public static String getContact(String customerId, String contactId) {
		IdData customer = new IdData(customerId);
		String table = String.format("core:%s:public:%s", customerId, "contact");
		MappedData row = cassSource.getRow(customer, application, tableId, new IdData(table + ":" + contactId));
		String email = "";
		StringData firstName = new StringData("");
		StringData lastName = new StringData("");
		if (row == null)
			return "null";
		email = row.get("id").toString().replaceAll(table + ":", "");
		firstName = (StringData) row.get("firstName");
		lastName = (StringData) row.get("lastName");
		/*
		 * admins.put(email, (firstName == null ? "null" : firstName.toString())
		 * + "-" + (lastName == null ? "null" : lastName.toString()));
		 */
		return (String.format("%s\t%s\t%s", email, firstName == null ? "null" : firstName.toString(),
				lastName == null ? "null" : lastName.toString()));
	}

	public static Map<String, String> getContact(String customerId) {
		IdData customer = new IdData(customerId);
		Map<String, String> admins = new HashMap<String, String>();
		MappedData filter = new MappedData();
		String table = String.format("core:%s:public:%s", customerId, "contact");
		filter.put("table", new IdData(table));
		ListData rowsWithFilter = cassSource.getRowsWithFilter(customer, application, tableId, filter);
		logger.info("\n" + customerId + "\t" + rowsWithFilter.size());
		String email = "";
		StringData firstName = new StringData("");
		StringData lastName = new StringData("");
		for (Object obj : rowsWithFilter) {
			MappedData row = (MappedData) obj;
			email = row.get("id").toString().replaceAll(table + ":", "");
			firstName = (StringData) row.get("firstName");
			lastName = (StringData) row.get("lastName");
			/*
			 * admins.put(email, (firstName == null ? "null" :
			 * firstName.toString()) + "-" + (lastName == null ? "null" :
			 * lastName.toString()));
			 */
			System.out.println(String.format("%s\t%s\t%s", email, firstName == null ? "null" : firstName.toString(),
					lastName == null ? "null" : lastName.toString()));
		}
		return admins;
	}

	public static Map<String, String> getListAdminUsers(String customerId) {
		IdData customer = new IdData(customerId);
		Map<String, String> admins = new HashMap<String, String>();
		MappedData filter = new MappedData();
		String table = String.format("core:%s:public:%s", customerId, "permission");
		filter.put("table", new IdData(table));
		ListData rowsWithFilter = cassSource.getRowsWithFilter(customer, application, tableId, filter);
		// logger.info("\n" + customerId + "\t" + rowsWithFilter.size() + "
		// users have permission");
		String email = "";
		for (Object obj : rowsWithFilter) {
			MappedData row = (MappedData) obj;
			email = "";
			Data admin = row.get("admin");
			Data role = row.get("role");
			email = row.get("id").toString().replaceAll(table + ":", "");
			if ((new StringData("true")).equals(admin)) {
				admins.put(email, "admin");
			}

			if (role != null && PermissionRole.contains(role.toString())) {
				admins.put(email, role.toString());
			}

		}
		return admins;
	}

	public static Map<String, String> getListOwnerUsers(String customerId) {
		IdData customer = new IdData(customerId);
		Map<String, String> admins = new HashMap<String, String>();
		MappedData filter = new MappedData();
		String table = String.format("core:%s:public:%s", customerId, "permission");
		filter.put("table", new IdData(table));
		ListData rowsWithFilter = cassSource.getRowsWithFilter(customer, application, tableId, filter);
		String email = "";
		for (Object obj : rowsWithFilter) {
			MappedData row = (MappedData) obj;
			email = "";
			Data role = row.get("role");
			email = row.get("id").toString().replaceAll(table + ":", "");

			if (role != null && role.toString().equals(PermissionRole.OWNER.getValue())) {
				admins.put(email, role.toString());
			}

		}
		return admins;
	}

	public static void deleteUser(String customerId, String username) {
		String userId = Authenticator.getUserId(customerId, username);

		Data delete = userStore.delete(customerId, applicationStr, userId);
		logger.info(username + ": " + delete);
	}

	public static MappedData getUserInfo(String customerId, String username) {
		String userId = Authenticator.getUserId(customerId, username);

		return (MappedData) (userStore.readSecureInformation(customerId, applicationStr, userId));
	}

	public static void getUserPermission(String customerId, String username) {
		System.out.println(
				permissionStore.read(adminCustId, applicationStr, Authenticator.getUserId(adminCustId, username)));
	}

	public static void resetPassword(String customerId, String username, String encryptedpassword) {
		String userId = Authenticator.getUserId(customerId, username);

		MappedData userData = (MappedData) (userStore.readSecureInformation(customerId, applicationStr, userId));
		// update password
		userData.put("password", new IdData(encryptedpassword));
		userStore.update(customerId, application.toRawString(), userId, userData.getMapAsPojo());
		getUserInfo(customerId, username);
	}
}
