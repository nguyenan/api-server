package com.wut.services;


public class JettyService {
	
//	private Server server;
//	private String jettyHost = "localhost"; //"127.0.0.1"; //System.getProperty("jetty.host");
//	private String jettyHome = "."; //System.getProperty("jetty.home");
//	
//	public JettyService() throws Exception {
//		
//		String host = System.getProperty("jetty.host");
//		String home = System.getProperty("jetty.home");
//		// http://docs.codehaus.org/display/JETTY/Embedding+Jetty
//		
//		//server = new Server();
//		
////		InputStream configStream = JettyService.class.getClassLoader().getResourceAsStream("/com/wut/conf/jetty.xml");
////		
////		StringWriter writer = new StringWriter();
////		IOUtils.copy(configStream, writer);
////		String theString = writer.toString();
//		
////		XmlConfiguration configuration = new XmlConfiguration(new File("conf/jetty.xml").toURL()); //or use new XmlConfiguration(new FileInputStream("myJetty.xml"));
//		
////		configuration.configure(server);
//		
//		
//		// BASIC SERVER CONFIG
//		 server = new Server();
//		 
//		 SelectChannelConnector connector = new SelectChannelConnector();
//		 
//		 connector.setHost(jettyHost);
//		 connector.setPort(8888);
//		 connector.setMaxIdleTime(300000);
//		 connector.setStatsOn(false);
//		 connector.setConfidentialPort(8443);
//		 connector.setLowResourcesConnections(20000);
//		 connector.setLowResourceMaxIdleTime(5000);
//		 
//		 server.addConnector(connector);
//		 
//		 // HANDLERS
//		 HandlerCollection handlers = new HandlerCollection();
//		 
//		 // context handler
//		 ContextHandlerCollection contexts = new ContextHandlerCollection();
//
//		 // default handler
//		 DefaultHandler defaulthandler = new DefaultHandler();
//		 defaulthandler.setServeIcon(false);
//		 
//		 // request log handler
//		 Handler requestLog = new RequestLogHandler();
//
//		 // resource handler
//		 ResourceHandler resorceHandler = new ResourceHandler();
//		 resorceHandler.setWelcomeFiles(new String[] { "index.html" });
//		 resorceHandler.setResourceBase("/nothing"); // TODO why nothing????
//		 
////		 WebAppContext wac = new WebAppContext();
////		 wac.setResourceBase(".");
////		 wac.setDescriptor("WEB-INF/web.xml");
////		 wac.setContextPath("/");
////		 wac.setParentLoaderPriority(true);
//		 
////		 WebAppContext wac = new WebAppContext();
////		 wac.setResourceBase("/applications/russell");
////		 //wac.setDescriptor("/applications/russell/web.xml");
////		 wac.setContextPath("/");
////		 wac.setParentLoaderPriority(true);
//		 
////		 Context russellApp = new Context();
////		 russellApp.setContextPath("russell");
////		 russellApp.setResourceBase("../WUT-JS/test");
////		 russellApp.addServlet(org.eclipse.jetty.jetty.servlet.DefaultServlet.class, "/");
////		 
////		 final URL url = new File("../WUT-JS/test").getAbsoluteFile().toURI().toURL();
////		 final Resource craigResource = new FileResource(url);
////		 final ResourceHandler craigHandler = new ResourceHandler();
////		 craigHandler.setBaseResource(craigResource);
////		 //server.addHandler(handler);
////		 
//		 handlers.setHandlers(new Handler[] { contexts, defaulthandler, requestLog, resorceHandler });
//		 
//		 server.setHandler(handlers);
//		 
//		 org.eclipse.jetty.webapp.WebAppContext webApp1 = new org.eclipse.jetty.webapp.WebAppContext();
//		 webApp1.setContextPath("/russell");
//		 //webApp1.setWar("./applications/russell");
//		 webApp1.setWar("/Users/russell/Documents/workspace/WUT-JS/test");
//		 webApp1.setVirtualHosts(new String[] { "127.0.0.1" });
//		 
////		 <Configure class="org.eclipse.jetty.jetty.webapp.WebAppContext">
////		  <Set name="contextPath">/russell</Set>
////		  <Set name="war"><SystemProperty name="jetty.home" default="."/>/applications/russell</Set>
////		  <Set name="virtualHosts">
////		    <Array type="java.lang.String">
////		      <Item>127.0.0.1</Item>
////		    </Array>
////		  </Set>
////		</Configure>
//		 
//		 server.addHandler(webApp1);
//		 
//		 // DEPLOYERS
//		 
//		 ContextDeployer contextDeployer = new ContextDeployer();
//		 contextDeployer.setContexts(contexts);
//		 contextDeployer.setConfigurationDir(jettyHome + "/conf/contexts");
//		 contextDeployer.setScanInterval(5);
//		 server.addLifeCycle(contextDeployer);
//		 
//		 
////		 WebAppDeployer webAppDeployer = new WebAppDeployer();
////		 webAppDeployer.setContexts(null);
////		 webAppDeployer.setWebAppDir(jettyHome + "/applications");
////		 webAppDeployer.setParentLoaderPriority(false);
////		 webAppDeployer.setExtract(true);
////		 webAppDeployer.setAllowDuplicates(false);
////		 webAppDeployer.setDefaultsDescriptor(jettyHome + "/conf/webdefault.xml");
////		 server.addLifeCycle(webAppDeployer);
//		 
//		 server.start();
//		
////		 Context root = new Context(server, "/", Context.SESSIONS);
////
////		 // hybrid servlet
////		 RestHybridServlet restHybrid = new RestHybridServlet();
////		 ServletHolder holder = new ServletHolder(restHybrid);
////		 root.addServlet(holder, "/api/*");
////		 
////		 DefaultServlet staticServlet = new DefaultServlet();
////		 //staticServlet.getServletContext().setInitParameter("resourceBase", "/Users/");
////		 //staticServlet.getServletContext().setInitParameter("dirAllowed", "true");
////		 ServletHolder staticHolder = new ServletHolder(staticServlet);
////		 //staticHolder.setInitParameter("resourceBase", "/Users/");
////		 staticHolder.setInitParameter("relativeResourceBase", "/");
////		 staticHolder.setInitParameter("dirAllowed", "true");
////		 root.addServlet(staticHolder, "/static/*");
//		 
////		 ServletHandler sh2 = new ServletHandler();
////		 ServletHolder holder2 = new ServletHolder( new DefaultServlet() );
////		 holder.setInitParameter("resourceBase", "/WebContent/");
////		 sh2.addServletWithMapping( holder2, "/static/*" );
////		 staticContext.setContextPath(staticPathSpec);
////		 staticContext.setServletHandler(sh2);
//		 
//		 // static content
//		 //final URL warUrl = this.getClass().getClassLoader().getResource("WebContent");
//		 //final String warUrlString = warUrl.toExternalForm();
//		 //final String warUrlString = "/Users/russell/Documents/workspace/Web Utility Kit War";
////		 final String warUrlString = "/Users/russell/Desktop/apache-tomcat-7.0.25/webapps/wut/";
////		 WebAppContext wac = new WebAppContext(warUrlString, "/static/*");
////		 wac.setDefaultsDescriptor("/my/path/to/webdefault.xml");
////		 server.setHandler(new WebAppContext(warUrlString, "/static/*"));
////
///*
//		server.start();
//*/
//		//Handler handler=new AbstractHandler()
////		{
////		    public void handle(String target, HttpServletRequest request, HttpServletResponse response, int dispatch)
////		        throws IOException, ServletException
////		    {
////		        response.setContentType("text/html");
////		        response.setStatus(HttpServletResponse.SC_OK);
////		        response.getWriter().println("<h1>Hello SSS</h1>");
////		        ((Request)request).setHandled(true);
////		    }
////		};
////		 
////		Server server = new Server(8080);
////		server.setHandler(handler);
////		server.start();
//	}
	
//	// http://docs.codehaus.org/display/JETTY/Virtual+hosts
//	public void addVirtualHost(Context context, String sDomain) throws Exception{
//		   int nOldVHosts = context.getVirtualHosts().length+1;
//		   String[] nNewVHosts = new String[nOldVHosts];
//		   for (int i = 0; i < context.getVirtualHosts().length; i++) {
//		      nNewVHosts[i] = context.getVirtualHosts()[i];
//		   }
//		   nNewVHosts[nOldVHosts-1] = sDomain;
//		  
//		   context.setVirtualHosts(nNewVHosts);
//		  
//		   server.stop();
//		   server.start();
//		}
}
