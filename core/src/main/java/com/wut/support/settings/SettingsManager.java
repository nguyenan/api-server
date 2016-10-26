package com.wut.support.settings;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.wut.support.logging.WutLogger;

public class SettingsManager {
	//private static ApplicationSettings appSettings = new ApplicationSettings();
	private static SystemSettings sysSettings = new SystemSettings();
	//private static Map<String, Settings> customersSettings = new HashMap<String, Settings>();
	//private static List<String> customers = new ArrayList<String>();
	//private static WutLogger logger = WutLogger.create(SettingsManager.class);
	private static Map<String,ClientSettings> clientSettings = ClientSettings.getDefaults();
	
	/*
	static {
		setAllSettings("beta.secretsaviors.com", "stripe", "", "sk_live_UM2tc23B5s9TSWPeD3XJ55sS", "", "support@secretsaviors.com");
		setAllSettings("secretsaviors.com", "stripe", "", "sk_live_UM2tc23B5s9TSWPeD3XJ55sS", "", "support@secretsaviors.com");

		setAllSettings("cleverhen.com", "braintree", "m4wj4w59pf7mvdyn", "8j6nsmy4czp9fghm", "31756c0c382afa11f449da22205b060d", "support@cleverhen.com", "smtp.sendgrid.net", "587", "cleverhenfoods", "btH3nhous3", "79640172");
		setAllSettings("beta.cleverhen.com", "braintree", "m4wj4w59pf7mvdyn", "8j6nsmy4czp9fghm", "31756c0c382afa11f449da22205b060d", "support@cleverhen.com", "smtp.sendgrid.net", "587", "cleverhenfoods", "btH3nhous3", "79640172");

		setAllSettings("retailkit.com", "stripe", "", "sk_live_TukhpM0QWyzR94SIvus0K8uA", "", "support@retailkit.com");
		setAllSettings("beta.retailkit.com", "stripe", "", "sk_test_q6jpEB3qjQvSYVgns7lOwq2J", "", "support@retailkit.com");
		setAllSettings("dev.retailkit.com", "stripe", "", "sk_test_q6jpEB3qjQvSYVgns7lOwq2J", "", "support@retailkit.com",  "smtp.mailgun.org", "587", "postmaster@rs8342.mailgun.org",  "8k-0cziu55g7", "81365609");
		setAllSettings("demo.retailkit.com", "stripe", "", "sk_test_q6jpEB3qjQvSYVgns7lOwq2J", "", "support@retailkit.com",  "smtp.mandrillapp.com", "587", "rpalmite@gmail.com",  "hh5iVkW_VPi5YapUTy4wqg", "91739917");
		//setAllSettings("demo.retailkit.com", "stripe", "", "sk_test_q6jpEB3qjQvSYVgns7lOwq2J", "", "support@retailkit.com");
		setAllSettings("admin.retailkit.com", "stripe", "", "sk_test_q6jpEB3qjQvSYVgns7lOwq2J", "", "support@retailkit.com", "82220437");
		
		setAllSettings("beta.ranchosonora.com", "braintree", "82rjmn54xpqpz9fh", "vvwff25qxytg5cqt", "14eb8b9d50757e869078098d25703715", "support@ranchosonora.com", "91740817");
		setAllSettings("ranchosonora.com", "braintree", "82rjmn54xpqpz9fh", "vvwff25qxytg5cqt", "14eb8b9d50757e869078098d25703715", "support@ranchosonora.com", "75017007");

		setAllSettings("beta.beautimy.com", "braintree", "no-merchant-id-yet", "", "", "support@beautimy.com");
		setAllSettings("beautimy.com", "braintree", "no-merchant-id-yet", "", "", "support@beautimy.com");

		setAllSettings("beta.pushtherapy.com", "braintree", "no-merchant-id-yet", "", "", "info@pushtherapy.com", "91736737");
		setAllSettings("pushtherapy.com", "braintree", "no-merchant-id-yet", "", "", "info@pushtherapy.com", "84563817");

		setAllSettings("cloudlawyer.co", "stripe", "", "no-live-id-yet", "", "support@cloudlawyer.co");

		setAllSettings("beta.toothbrushsubscriptions.com", "stripe", "", "sk_test_YyaZFmXPHRjcPQ33E90RFQ4x", "", "support@toothbrushsubscriptions.com", "smtp.mailgun.org", "587", "postmaster@toothbrushsubscriptions.com", "7by1eo625nw7", "91734967");
		setAllSettings("toothbrushsubscriptions.com", "stripe", "", "sk_live_dAZbPmgrZps7KNj1YOKam0RT", "", "support@toothbrushsubscriptions.com", "smtp.mailgun.org", "587", "postmaster@toothbrushsubscriptions.com",  "7by1eo625nw7", "76986173");

		setAllSettings("beta.santacruzbarbershop.com", "stripe", "", "sk_live_needed", "", "support@santacruzbarbershop.com", "91739428");
		setAllSettings("santacruzbarbershop.com", "stripe", "", "sk_live_needed", "", "support@santacruzbarbershop.com", "78135553");

		setAllSettings("beta.playaoutlet.com", "stripe", "", "sk_test_DEb5HsrQCUlSESnQbhiwIuKc", "", "support@beta.playoutlet.com");
		setAllSettings("playaoutlet.com", "stripe", "", "sk_live_SlmVPLG7VRa0rgcfR38z02zu", "", "support@playoutlet.com");
        
		//setAllSettings("beta.equipment.presencelearning.com", "stripe", "", "sk_test_4RqkHKmGpFtAAuA9YqpUANWE", "", "support@presencelearning.com");
		//setAllSettings("equipment.presencelearning.com", "stripe", "", "sk_live_4RqkAU1tSS739wvbYFY36fhh", "", "support@presencelearning.com");

		setAllSettings("beta.presencelearningequipment.com", "stripe", "", "sk_live_4RqkAU1tSS739wvbYFY36fhh", "", "support@presencelearningequipment.com", "88913902");
		setAllSettings("presencelearningequipment.com", "stripe", "", "sk_test_4RqkHKmGpFtAAuA9YqpUANWE", "", "support@presencelearningequipment.com", "88916901");

		setAllSettings("beta.lenkeitjewelry.com", "braintree", "missing", "missing", "missing", "support@lenkeitjewelry.com", "91740834");
		setAllSettings("lenkeitjewelry.com", "braintree", "missing", "missing", "missing", "support@lenkeitjewelry.com", "91736951");

		setAllSettings("beta.alpsadultdayservices.org", "braintree", "v5z6c9w6rfqj57k9", "9h8p9c8tgn6wxpcm", "b13cbbb4ecbf305bdaf3f710210fed43", "support@alpsadultdayservices.org", "91743316");
		setAllSettings("www.alpsadultdayservices.org", "braintree", "v5z6c9w6rfqj57k9", "9h8p9c8tgn6wxpcm", "b13cbbb4ecbf305bdaf3f710210fed43", "support@alpsadultdayservices.org", "91734941");

		setAllSettings("beta.myvegas.com", "braintree", "tbd", "tbd", "tbd", "support@myvegas.com", "92743711");
		setAllSettings("www.myvegas.com", "braintree", "tbd", "tbd", "tbd", "support@myvegas.com", "92743711");
		setAllSettings("store.myvegas.com", "braintree", "r6kdr84pq9zydkvp", "y93jggkwbn8v5qm2", "a6a563c18504f79b912f2cc0d428aab1", "store-support@myvegas.com", "92743711");
		setAllSettings("betastore.myvegas.com", "braintree", "ph36xz9fnkg74h94", "wyd73y437s3622r3", "3ba63898f98b2569330022d296e7efbb", "store-support@myvegas.com", "92743711");
		
		setAllSettings("beta.bradstreet.co", "braintree", "mj4zpb5dd4nvsr3h", "bmnh4fkshwrd79rw", "d39d09671b0e4a32673e1537ff17abd3", "support@bradstreet.co", "92967526");
		setAllSettings("www.bradstreet.co", "braintree", "xp8qsvx85cc55779", "cp692mmbt8759thn", "dcc1055cb82616192327077e47e024cd", "support@bradstreet.co", "92967526");
	
		setAllSettings("beta.bradstreet.co", "braintree", "mj4zpb5dd4nvsr3h", "bmnh4fkshwrd79rw", "d39d09671b0e4a32673e1537ff17abd3", "support@bradstreet.co", "92967526");
		setAllSettings("www.bradstreet.co", "braintree", "xp8qsvx85cc55779", "cp692mmbt8759thn", "dcc1055cb82616192327077e47e024cd", "support@bradstreet.co", "92967526");
	
		setAllSettings("beta.bradstreet.co", "braintree", "mj4zpb5dd4nvsr3h", "bmnh4fkshwrd79rw", "d39d09671b0e4a32673e1537ff17abd3", "support@bradstreet.co", "92967526");
		setAllSettings("www.bradstreet.co", "braintree", "xp8qsvx85cc55779", "cp692mmbt8759thn", "dcc1055cb82616192327077e47e024cd", "support@bradstreet.co", "92967526");
	
		setAllSettings("beta.growbetterveggies.com", "braintree", "xp8qsvx85cc55779", "cp692mmbt8759thn", "dcc1055cb82616192327077e47e024cd", "support@growbetterveggies.com", "92967526");
		setAllSettings("growbetterveggies.com", "braintree", "mj4zpb5dd4nvsr3h", "bmnh4fkshwrd79rw", "d39d09671b0e4a32673e1537ff17abd3", "support@growbetterveggies.com", "92967526");
		setAllSettings("www.growbetterveggies.com", "braintree", "mj4zpb5dd4nvsr3h", "bmnh4fkshwrd79rw", "d39d09671b0e4a32673e1537ff17abd3", "support@growbetterveggies.com", "92967526");
		setAllSettings("beta.veggies.retail.rocks", "braintree", "mj4zpb5dd4nvsr3h", "bmnh4fkshwrd79rw", "d39d09671b0e4a32673e1537ff17abd3", "support@veggies.retail.rocks", "92967526");
		setAllSettings("veggies.retail.rocks", "braintree", "mj4zpb5dd4nvsr3h", "bmnh4fkshwrd79rw", "d39d09671b0e4a32673e1537ff17abd3", "support@veggies.retail.rocks", "92967526");

		setAllSettings("www.tend.co", "braintree", "mj4zpb5dd4nvsr3h", "bmnh4fkshwrd79rw", "d39d09671b0e4a32673e1537ff17abd3", "support@tend.co", "101015280");
	}
	
	private static void setAllSettings(String domain, String paymentProcessor, String merchantId, String publicKey, String privateKey, String emailAdderss) {
		setAllSettings(domain, paymentProcessor, merchantId, publicKey, privateKey, emailAdderss, "");
	}
	
	private static void setAllSettings(String domain, String paymentProcessor, String merchantId, String publicKey, String privateKey, String emailAdderss, String analyticsView) {

//				 :authentication => :plain,
//	    	     :address => "smtp.mailgun.org",
//	    	     :port => 587,
//	    	     :domain => "my-mailgun-domain.com",
//	    	     :user_name => "postmaster@my-mailgun-domain.com",
//	    	     :password => "my-password"

		setAllSettings( domain,  paymentProcessor,  merchantId,  publicKey,  privateKey, emailAdderss,  "smtp.sendgrid.net",  "2525", "retailkit",  "r3t41lr0ck5", analyticsView);
	}
	
	 // domain = customerId
	private static void setAllSettings(String domain, String paymentProcessor, String merchantId, String publicKey, String privateKey, 
			String emailAdderss, String emailSmtpServer, String emailSmtpPort, 
			String emailUsername, String emailPassword, String googleAnalyticsView) {
		
		try {
			customers.add(domain);
			
			logger.info("SETTINGS: customer " + domain);

			SettingsMap settings = new SettingsMap();
			
			// payment processor
			settings.putSetting("payment-processor", paymentProcessor);
			if (paymentProcessor.equalsIgnoreCase("stripe")) {
				settings.putSetting("stripe-live-api-key", publicKey); // TODO not used
				settings.putSetting("stripe-api-key", publicKey);
				settings.putSetting("stripe-test-api-key", "<stripe>"); // TODO not used
			} else if (paymentProcessor.equalsIgnoreCase("braintree")) {
				settings.putSetting("braintree-mechant-id", merchantId);
				settings.putSetting("braintree-public-key", publicKey);
				settings.putSetting("braintree-private-key", privateKey);
			}
			
			logger.info("SETTINGS: after payment processing");

	//		settings.putSetting("email-from-address", emailAdderss);
	//		settings.putSetting("email-provider", emailProvider);
	//		settings.putSetting("email-provider-username", emailUsername);
	//		settings.putSetting("email-provider-password", emailPassword);
			
			// email
			settings.putSetting("email-from-address", emailAdderss);
			settings.putSetting("email-domain", domain);
			settings.putSetting("email-smtp-host", emailSmtpServer);
			settings.putSetting("email-smtp-port", emailSmtpPort);
			settings.putSetting("email-provider-password", emailPassword); // old
			settings.putSetting("email-username", emailUsername);
			settings.putSetting("email-password", emailPassword);
			
			String realDomain = getRealDomain(domain);
			logger.info("SETTINGS: real domain " + realDomain);
			settings.putSetting("password-reset-link", "https://" + realDomain + "/account.html?");
			
			settings.putSetting("domain", domain);
	
			String topLevelDomain = getTopLevelDomain(domain);
			settings.putSetting("top-level-domain", topLevelDomain);
	
			//String realDomain = getRealDomain(domain);
			//settings.putSetting("password-reset-link", "https://" + realDomain + "/account.html?");
			
			// git
			logger.info("SETTINGS: before git");
			settings.putSetting("git.repository", "https://rpalmite:REPLACE_ME@bitbucket.org/rpalmite/frontend.git");
			settings.putSetting("git.branch", domain);
			settings.setDirectorySetting("client.code.dir", SystemSettings.getInstance().getSetting("code.dir") + domain);
			logger.info("SETTINGS: after client code dir set");
			settings.setDirectorySetting("client.site.dir", SystemSettings.getInstance().getSetting("site.dir") + domain);
			logger.info("SETTINGS: after git");

			//analytics
			settings.putSetting("google-analytics-view", googleAnalyticsView);
			customersSettings.put(domain, settings);
		} catch (Exception e) {
			logger.info("Error initializing settings for " + domain);
		}
	}
	
//	public static class SettingNotFoundException extends RuntimeException {
//		private static final long serialVersionUID = -9194751332420050905L;
//		public SettingNotFoundException(String string) {
//			super(string);
//		}
//	}
	// getSystemSetting()
	
	*/
	
	public static String getUserSetting(String userId, String settingName) {
		throw new SettingNotFoundException("setting " + settingName + " for user " + userId + " was no found");
	}
	
//	// TODO eliminate application settings for system settings instead
//	public static String getApplicationSetting(String settingName) {
//		String setting = appSettings.getSetting(settingName);
//		return setting;
//	}
	
	public static String getSystemSetting(String settingName) {
		String setting = sysSettings.getSetting(settingName);
		return setting;
	}
	
	// TODO rename getClientSetting()
	public static String getCustomerSettings(String customer, String settingName) {
		ClientSettings customerSettings = clientSettings.get(customer);
		if (customerSettings == null) {
			throw new SettingNotFoundException("no settings for customer " + customer + " were found");
		}
		String settingValue = customerSettings.getSetting(settingName);
		if (settingValue == null) {
			throw new SettingNotFoundException("setting " + settingName + " for customer " + customer + " was no found");
		}
		return settingValue;
	}
	
	//////////////////////////////////////////////////////
	/////////////////////////////////////////////////////
	/////////////// JUNK METHODS ////////////////////////
	/////////////////////////////////////////////////////
	////////////////////////////////////////////////////
	
	private static String os = System.getProperty("os.name").toLowerCase();

	public static String getToolUtility(String basepath, String toolName, String utilityName) {
		StringBuilder toolLocation = new StringBuilder();
		
		toolLocation.append(getToolDirectory(basepath, toolName)); // tool folder
		toolLocation.append("/");
		toolLocation.append(getToolExecutable(utilityName)); // tool executable
		
		return toolLocation.toString();
	}
	
	
	public static String getToolUtility(String toolName, String utilityName) {
		StringBuilder toolLocation = new StringBuilder();
		
		toolLocation.append(getToolDirectory(toolName)); // tool folder
		toolLocation.append("/");
		toolLocation.append(getToolExecutable(utilityName)); // tool executable
		
		return toolLocation.toString();
	}
	
	public static String getToolLocation(String toolName) {
		StringBuilder toolLocation = new StringBuilder();
		
		toolLocation.append(getToolDirectory(toolName)); // tool folder
		toolLocation.append("/");
		toolLocation.append(getToolExecutable(toolName)); // tool executable
		
		return toolLocation.toString();
	}
	
	public static String getToolLocation(String basePath, String toolName) {
		StringBuilder toolLocation = new StringBuilder();
		
		toolLocation.append(getToolDirectory(basePath, toolName)); // tool folder
		toolLocation.append("/");
		toolLocation.append(getToolExecutable(toolName)); // tool executable
		
		return toolLocation.toString();
	}
	
	private static boolean onMacOperatingSystem() {
		return (os.indexOf("mac") >= 0);
	}
	
	private static boolean onUnixOperatingSystem() {
		return (os.indexOf("nix") >= 0 || os.indexOf("nux") >= 0 || os.indexOf("aix") > 0 );
	}
	private static boolean onWindowsOperatingSystem() {
		return (os.indexOf("win") >= 0);
	}

	public static String getToolDirectory(String toolName) {
		String toolsBaseDir = sysSettings.getSetting("tools.base.directory");
		return getToolDirectory(toolsBaseDir, toolName);
	}
	
	public static String getToolDirectory(String basePath, String toolName) {
		StringBuilder toolLocation = new StringBuilder();
		
		toolLocation.append(basePath);
		
		boolean isWindows = onWindowsOperatingSystem();
		boolean isMac = onMacOperatingSystem();
		boolean isUnix = onUnixOperatingSystem();
		boolean isLinux = isUnix;
		boolean isSolaris = (os.indexOf("sunos") >= 0);
		
		boolean jvmIs64bit = System.getProperty("sun.arch.data.model").contains("64");
		boolean osIs64bit = (System.getProperty("os.arch").indexOf("64") != -1);
		boolean is64bit = jvmIs64bit || osIs64bit;

		if (isMac && is64bit) {
			//toolLocation.append("/mac/osx64/");
			toolLocation.append("/");
		} else if (isLinux && is64bit) {
			//toolLocation.append("/linux/ubuntu64/");
			toolLocation.append("/");
		} else {
			//throw new RuntimeException("running unsupported operating system / architecture: " + os + (is64bit ? " 64 bit" : " 32 bit"));
		}
		
		toolLocation.append(toolName); // tool folder
		
		return toolLocation.toString();
	}

	public static String getToolExecutable(String toolName) {
		StringBuilder toolExe = new StringBuilder();
		toolExe.append(toolName); // tool executable
		return toolExe.toString();
	}
	

	
//	/*
//	 * client = customer = domain
//	 */
//	public static boolean isClientLoaded(String client) {
//		return customers.contains(client);
//	}
}
