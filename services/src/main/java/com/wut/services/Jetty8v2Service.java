package com.wut.services;

import java.io.File;
import java.io.IOException;
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

import com.wut.support.settings.SettingsManager;
import com.wut.support.settings.SystemSettings;

public class Jetty8v2Service {
	
	private static String APPLICATIONS_DIR = SystemSettings.getInstance().getSetting("site.dir");
	private static final String LOCALHOST_TEST_DOMAIN = "dev.retailkit.com";
	private static final String TEST_DOMAIN_127_0_0_1 = "www.cleverhen.com";
	private static final String LOCAL_IP = "192.168.1.103"; // TODO remove later on -- this is use for jeremy to access my machine
	
	// GET PORT
	String serverPortString = System.getProperty("http.port", "8888");
	int serverPort = Integer.parseInt(serverPortString);
	
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
		
		Connector[] serverConnectors = getConnectors();
		server.setConnectors(serverConnectors);
		
		// SETUP HANDLERS
		
//		CrossOriginFilter myFiler = new CrossOriginFilter();
//		FilterHolder secondFilter = new FilterHolder(myFiler);
//		api.addFilter(secondFilter, "/*", EnumSet.of(DispatcherType.REQUEST));
		
		final HandlerList handlers = getHandlers();
		
		// ???
		server.setHandler(handlers);
		
		// START SERVER
		server.start();
		server.join();

		return false;
	}
	
	
	private HandlerList getHandlers() {
		final String[] WEB_APPS = new String[] { "storage", "email" };

		// SETUP HANDLERS
		ArrayList<Handler> handlerArrayList = new ArrayList<Handler>();
		handlerArrayList.add(getRewriteHander()); // adds CORS headers
		
		for (String webApp : WEB_APPS) {
//					ServletContextHandler api = new ServletContextHandler(ServletContextHandler.SESSIONS);
//					api.setContextPath("/api");
//					RestHybridServlet restHybrid = new RestHybridServlet();
//					api.addServlet(new ServletHolder(restHybrid), "/");
			
			WebAppContext wac = new WebAppContext();
//			wac.setResourceBase("../" + webApp);
//			wac.setDescriptor("../" + webApp + "/WEB-INF/web.xml");
//			wac.setContextPath("/" + webApp);
//			wac.setParentLoaderPriority(true);

			// works in debug
			wac.setContextPath("/" + webApp);
			wac.setWar("../" + webApp + "/target/" + webApp + ".war");
			//../../jetty-distribution/target/distribution/demo-base/webapps/test.war
			//server.setHandler(wac);
			
			handlerArrayList.add(wac);
		}		
		
//		System.out.println("Using applications directory: " + APPLICATIONS_DIR);
//		File applicationsFolder = new File(APPLICATIONS_DIR);
//		if (!applicationsFolder.exists()) {
//			throw new Exception("Folder applications is missing!!!");
//		}
//		File[] files = applicationsFolder.listFiles();
//		for (File file : files) {
//	        if (file.isDirectory()) {
//	            String name = file.getName();
//	    		createHandler(handlerArrayList, name, name);
//	        } else {
//	            System.out.println("File: " + file.getName() + " will not be available");
//	        }
//	    }
		
		Handler[] handlerArray = (Handler[]) handlerArrayList.toArray(new Handler[handlerArrayList.size()]);
		
		HandlerList handlers = new HandlerList();
		handlers.setHandlers(handlerArray);
		
		return handlers;
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

	public Jetty8v2Service() throws Exception {
		start();
	}
	
	// TODO remove throws Exception()
	private Connector[] getConnectors() throws Exception {
		ArrayList<Connector> connectors = new ArrayList<Connector>();
		
		// SETUP HTTP CONNECTOR
		SelectChannelConnector httpConnector = createHTTPConnector();
		connectors.add(httpConnector);
		
		SelectChannelConnector httpsConnector = createHTTPSConnector("webutilitykit.com");
        connectors.add(httpsConnector);
		
		/*
        // SETUP HTTPS CONNECTORS (ONE FOR EACH CLIENT)
		System.out.println("Using applications directory: " + APPLICATIONS_DIR);
		File applicationsFolder = new File(APPLICATIONS_DIR);
		if (!applicationsFolder.exists()) {
			throw new Exception("Folder applications is missing!!!");
		}
		File[] files = applicationsFolder.listFiles();
		for (File file : files) {
	        if (file.isDirectory()) {
	            String name = file.getName();
	            // TODO making build errors go away
	            SelectChannelConnector httpsConnector = createHTTPSConnector(name);
	            connectors.add(httpsConnector);
	    		//createHandler(handlerArrayList, name, name);
	        } else {
	            System.out.println("File: " + file.getName() + " will not be available");
	        }
	    }
		
		return (Connector[]) connectors.toArray();
		*/
        
		//server.setConnectors(new Connector[] { connector0, connector1, ssl_connector });
        
        return new Connector[] { httpConnector, httpsConnector };
	}
	
	private SelectChannelConnector createHTTPConnector() {
		SelectChannelConnector connector0 = new SelectChannelConnector();
		connector0.setPort(serverPort);
		connector0.setMaxIdleTime(30000);
		connector0.setRequestHeaderSize(8192);
		connector0.setName("generic" + " http");
		return connector0;
	}
	
	private SelectChannelConnector createHTTPSConnector(String customer) {
		// TODO dont think this connector was ever needed
//		SelectChannelConnector connector1 = new SelectChannelConnector();
//		//connector1.setHost("127.0.0.1");
//		connector1.setPort(8889);
//		connector1.setThreadPool(new QueuedThreadPool(20));
//		connector1.setName(customer + " https");

		@SuppressWarnings("deprecation")
		SslSelectChannelConnector ssl_connector = new SslSelectChannelConnector();
		String jetty_home = SettingsManager.getSystemSetting("jetty.home");
		String sslPortString = SettingsManager.getSystemSetting("https.port");
		int sslPort = Integer.parseInt(sslPortString);
		ssl_connector.setPort(sslPort);
		SslContextFactory cf = ssl_connector.getSslContextFactory();
		// TODO use a setting to get the customers ssl certificate name (default to using customer name)
		String keystoreName = SettingsManager.getSystemSetting("keystore.name");
		String customerKeystoreName = customer + ".keystore";
		cf.setKeyStore("../build/" + customerKeystoreName);
		// TODO use a setting to get the customers keystore password (default to using our standard password)
		String keystorePassword = SettingsManager.getSystemSetting("keystore.password");
		cf.setKeyStorePassword(keystorePassword);
		String keyMangerPassword = SettingsManager.getSystemSetting("keymanager.password");
		cf.setKeyManagerPassword(keyMangerPassword);
		
		return ssl_connector;
	}
	
	private void createHandler(ArrayList<Handler> handlers, String directory, String domain) {
		ContextHandler staticHandler = new ContextHandler();
		if (directory.equals("js") || directory.equals("css") || directory.equals("img")) {
			staticHandler.setContextPath("/" + directory);
		} else {
			staticHandler.setContextPath("/");
		}
		staticHandler.setResourceBase(APPLICATIONS_DIR + "/" + directory);
		
		if (domain != null) {
			String[] domains = new String[] { domain };
			
			if (domain.equals(LOCALHOST_TEST_DOMAIN)) {
				domains = new String[] { domain, "localhost", LOCAL_IP };
			} else if (domain.equals(TEST_DOMAIN_127_0_0_1)) {
				domains = new String[] { domain, "127.0.0.1" };
			}
			
			// TODO remove
			if (!directory.equals("js") && !directory.equals("css") && !directory.equals("img")) {
				staticHandler.setVirtualHosts(domains);
				//staticHandler.set
			}
		}
		
		ResourceHandler staticContentHandler = new ResourceHandler();
		staticContentHandler.setDirectoriesListed(true);
		staticContentHandler.setWelcomeFiles(new String[] { "index.html" });
		//staticContentHandler.setResourceBase("../WUT-JS");
		staticContentHandler.setCacheControl("max-age=3600,public");
		
		staticHandler.setHandler(staticContentHandler);
		
		handlers.add(staticHandler);
	}
	
	public static void main(String[] args) throws Exception {
		Jetty8v2Service jetty = new Jetty8v2Service();
		
	}

}
