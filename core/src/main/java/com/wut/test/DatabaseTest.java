package com.wut.test;

//import java.io.IOException;
//import java.util.Map;
/*
import org.junit.Test;
//import org.restlet.Response;
//import org.restlet.ext.xml.DomRepresentation;
import org.w3c.dom.Document;

import com.wut.model.Data;
import com.wut.resources.ResourceFactory;
import com.wut.resources.common.WutOperation;
import com.wut.resources.common.WutResource;
*/
public class DatabaseTest {
	/*
        private PerformanceMonitor monitor = PerformanceMonitor.getInstance();
        private TestingHelper helper = new TestingHelper();
        
        @Test
        public void resourceTest() throws IOException {
                ResourceFactory rManager = ResourceFactory.getInstance();
                for (WutResource rDesc : rManager.getResources()) {
                        String group = "not_used_need_to_fix_test"; //rDesc.getGroup();
                        String resource = rDesc.getName();
                        for (WutOperation op : rDesc.getOperations()) {
                                String params = null; //getParameters(op.getParameters()); // TODO fix
                                String monitorName = "external:" + group + ":" + resource;
                                monitor.start(monitorName);
                                // TODO fix null
//                                Response response;// = helper.getResource(null, "xml", group, resource, params);
//                                DomRepresentation dom = null;// = response.getEntityAsDom();
//                                Document doc = dom.getDocument();
//                                assert(doc.getChildNodes().getLength() != 0);
                                monitor.stop(monitorName);
//                                System.out.println("Node Count:" + doc.getChildNodes().getLength());
                        }
                }
                
//              NodeList nodes = doc.getElementsByTagName("title");
//              for (int i=0; i<nodes.getLength(); i++) {
//                      Node n = nodes.item(i);
//                      System.out.println(n.getTextContent());
//              }
        }
        
        private RandomDataGenerator random = new RandomDataGenerator();
        
        private String getParameters(Map<String, Class<? extends Data>> parameters) {
                StringBuilder params = new StringBuilder();
                for (String paramName : parameters.keySet()) {
                        params.append(paramName);
                        params.append("=");
                        Class<? extends Data> paramClass = parameters.get(paramName);
                        String pStr = random.getExampleString(paramClass);
                        params.append(pStr);
                }
                return null;
        }


        public static void main(String[] args) throws IOException {
                DatabaseTest tdb = new DatabaseTest();
                tdb.resourceTest();
        }
        */

}