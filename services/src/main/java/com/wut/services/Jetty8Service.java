package com.wut.services;

import java.io.File;
import java.util.ArrayList;
import java.util.EnumSet;

import javax.servlet.DispatcherType;

import org.eclipse.jetty.rewrite.handler.HeaderPatternRule;
import org.eclipse.jetty.rewrite.handler.RewriteHandler;
import org.eclipse.jetty.rewrite.handler.Rule;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.NCSARequestLog;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.handler.DefaultHandler;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.RequestLogHandler;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.server.nio.SelectChannelConnector;
import org.eclipse.jetty.server.ssl.SslSelectChannelConnector;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.FilterMapping;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.servlets.CrossOriginFilter;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.eclipse.jetty.util.thread.QueuedThreadPool;
import org.eclipse.jetty.webapp.WebAppContext;

//import com.wut.protocols.http.resthybrid.RestHybridServlet;
import com.wut.support.logging.WutLogger;
import com.wut.support.settings.SettingsManager;
import com.wut.support.settings.SystemSettings;
/*
public class Jetty8Service {
	
	private static String APPLICATIONS_DIR = SystemSettings.getInstance().getSetting("site.dir");
	private static final String LOCALHOST_TEST_DOMAIN = "dev.retailkit.com";
	private static final String TEST_DOMAIN_127_0_0_1 = "www.cleverhen.com";
	private static final String LOCAL_IP = "192.168.1.103"; // TODO remove later on -- this is use for jeremy to access my machine
	private static final String[] WEB_APPS = new String[] { "storage", "email" };
	
	private static void requestLogging() {
		// from http://stackoverflow.com/questions/14390577/how-to-add-servlet-filter-with-embedded-jetty
		// Bonus ... request logs.
		RequestLogHandler logHandler = new RequestLogHandler();
		NCSARequestLog requestLog = new NCSARequestLog("/tmp/jetty-yyyy_mm_dd.request.log");
		requestLog.setRetainDays(90);
		requestLog.setAppend(true);
		requestLog.setExtended(false);
		requestLog.setLogTimeZone("GMT");
		logHandler.setRequestLog(requestLog);
	}

	private boolean start() throws Exception {
		// CREATE SERVER
		Server server = new Server();
		server.setSendServerVersion(false);

		// SETUP CONNECTORS
		String serverPortString = System.getProperty("http.port", "8080");
		int serverPort = Integer.parseInt(serverPortString);
		//SocketAttachingConnector sockConnect = new SocketAttachingConnector();
		SelectChannelConnector connector0 = new SelectChannelConnector();
		connector0.setPort(serverPort);
		connector0.setMaxIdleTime(30000);
		connector0.setRequestHeaderSize(8192);
		connector0.setName("zero");

		SelectChannelConnector connector1 = new SelectChannelConnector();
		//connector1.setHost("127.0.0.1");
		connector1.setPort(8889);
		connector1.setThreadPool(new QueuedThreadPool(20));
		connector1.setName("one");

		String sslSupport = SettingsManager.getSystemSetting("ssl.support");
		if (sslSupport.equals("true")) {
			
			@SuppressWarnings("deprecation")
			SslSelectChannelConnector ssl_connector = new SslSelectChannelConnector();
			String jetty_home = SettingsManager.getSystemSetting("jetty.home");
			String sslPortString = SettingsManager.getSystemSetting("https.port");
			int sslPort = Integer.parseInt(sslPortString);
			ssl_connector.setPort(sslPort);
			SslContextFactory cf = ssl_connector.getSslContextFactory();
			String keystoreName = SettingsManager.getSystemSetting("keystore.name");
			cf.setKeyStore(keystoreName);
			String keystorePassword = SettingsManager.getSystemSetting("keystore.password");
			cf.setKeyStorePassword(keystorePassword);
			String keyMangerPassword = SettingsManager.getSystemSetting("keymanager.password");
			cf.setKeyManagerPassword(keyMangerPassword);
			
			server.setConnectors(new Connector[] { connector0, connector1, ssl_connector });
		} else { 
			server.setConnectors(new Connector[] { connector0, connector1 });
		}

		
		// SETUP HANDLERS
		ArrayList<Handler> handlerArrayList = new ArrayList<Handler>();
		handlerArrayList.add(getRewriteHander()); // adds CORS headers
		
		for (String webApp : WEB_APPS) {
//			ServletContextHandler api = new ServletContextHandler(ServletContextHandler.SESSIONS);
//			api.setContextPath("/api");
//			RestHybridServlet restHybrid = new RestHybridServlet();
//			api.addServlet(new ServletHolder(restHybrid), "/");
			
			WebAppContext wac = new WebAppContext();
//			wac.setResourceBase(".");
//			wac.setDescriptor("WEB-INF/web.xml");
//			wac.setContextPath("/" + webApp + "/");
//			wac.setParentLoaderPriority(true);
			// works in debug
			wac.setWar("../" + webApp + "/target/" + webApp + ".war");
			//../../jetty-distribution/target/distribution/demo-base/webapps/test.war
			//server.setHandler(wac);
			
			handlerArrayList.add(wac);
		}
		
		//handlerArrayList.add(api); // adds api calls
		
//		System.out.println("Using applications directory: " + APPLICATIONS_DIR);
//		File applicationsFolder = new File(APPLICATIONS_DIR);
//		if (!applicationsFolder.exists()) {
//			throw new Exception("Folder applications is missing!!!");
//		}
//		File[] files = applicationsFolder.listFiles();
//		for (File file : files) {
//	        if (file.isDirectory()) {
//	            String name = file.getName();
//	            String domain = SettingsManager.getRealDomain(name);
//	            // TODO remove this hack when customer ids change
////	            if (name.indexOf('.') == name.lastIndexOf('.')) {
////	            	createHandler(handlerArrayList, name, "www." + name);
////	            } else {
////	            	createHandler(handlerArrayList, name, name);
////	            }
//	            createHandler(handlerArrayList, name, domain);
//	        } else {
//	            System.out.println("File: " + file.getName() + " will not be available");
//	        }
//	    }
		
		handlerArrayList.add(new DefaultHandler()); // TODO remove default handler
		
		Handler[] handlerArray = (Handler[]) handlerArrayList.toArray(new Handler[handlerArrayList.size()]);
		
		HandlerList handlers = new HandlerList();
		handlers.setHandlers(handlerArray);
		
		// ???
		server.setHandler(handlers);
		
		// START SERVER
		server.start();
		server.join();

		return false;
	}
	
	private Handler getRewriteHander() {
		RewriteHandler customerHeaderHandler =  new RewriteHandler();
		
		HeaderPatternRule headerRule = new HeaderPatternRule();
		
		headerRule.setPattern("/*");
		headerRule.setName("Access-Control-Allow-Origin");
		headerRule.setValue("*");
		Rule[] rules = new Rule[] { headerRule };
		
		customerHeaderHandler.setRules(rules);
		
		return customerHeaderHandler;
	}

	public Jetty8Service() throws Exception {
		start();
	}
	
	private void createHandler(ArrayList<Handler> handlers, String directory, String domain) {
		ContextHandler staticHandler = new ContextHandler();
		//if (directory.equals("js") || directory.equals("css") || directory.equals("img")) {
		//	staticHandler.setContextPath("/" + directory);
		//} else {
		staticHandler.setContextPath("/");
		//}
		final String resourceBase = APPLICATIONS_DIR + "/" + directory;
		staticHandler.setResourceBase(resourceBase);
		WutLogger.create(getClass()).info("Using directory " + resourceBase);
		
		if (domain != null) {
			String[] domains;
			
			if (domain.equals(LOCALHOST_TEST_DOMAIN)) {
				domains = new String[] { domain, "localhost", LOCAL_IP };
			} else if (domain.equals(TEST_DOMAIN_127_0_0_1)) {
				domains = new String[] { domain, "127.0.0.1" };
			} else { // the normal case
				domains = new String[] { domain };
			}
			
			staticHandler.setVirtualHosts(domains);
//			// TODO remove
//			if (!directory.equals("js") && !directory.equals("css") && !directory.equals("img")) {
//			}
		}
		
		ResourceHandler staticContentHandler = new ResourceHandler();
		staticContentHandler.setDirectoriesListed(false);
		staticContentHandler.setWelcomeFiles(new String[] { "index.html" });
		//staticContentHandler.setResourceBase("../WUT-JS");
		staticContentHandler.setCacheControl("max-age=3600,public");
		
		staticHandler.setHandler(staticContentHandler);
		
		handlers.add(staticHandler);
	}

}
*/