package com.wut.support.xml;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathException;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.wut.support.TextHelper;

// GAE XML EXAMPLE HERE:
// http://gaejexperiments.wordpress.com/

// TODO rename WutXmlDom
public class DomWrapper implements Iterable<DomWrapper> {
	//private NodeList nodes;
	private Node node;
	//private boolean isSigleNode;

	public DomWrapper(NodeList nodes) {
		Document doc = this.node.getOwnerDocument();
		Node newNode = doc.createElement("nodes");
		for (int i = 0; i < nodes.getLength(); i++) {
			newNode.appendChild(nodes.item(i));
		}
		this.node = newNode;
	}

	public DomWrapper(Node node) {
		this.node = node;
	}
	
	public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException {
		String xml = "<?xml version=\"1.0\" encoding=\"utf-8\"?>"+
			    "   <s:Envelope xmlns:s=\"http://schemas.xmlsoap.org/soap/envelope/\">"+
			    "       <s:Header>"+
			    "           <ActivityId CorrelationId=\"15424263-3c01-4709-bec3-740d1ab15a38\" xmlns=\"http://schemas.microsoft.com/2004/09/ServiceModel/Diagnostics\">50d69ff9-8cf3-4c20-afe5-63a9047348ad</ActivityId>"+
			    "           <clalLog_CorrelationId xmlns=\"http://clalbit.co.il/clallog\">eb791540-ad6d-48a3-914d-d74f57d88179</clalLog_CorrelationId>"+
			    "       </s:Header>"+
			    "       <s:Body>"+
			    "           <ValidatePwdAndIPResponse xmlns=\"http://tempuri.org/\">"+
			    "           <ValidatePwdAndIPResult xmlns:a=\"http://schemas.datacontract.org/2004/07/ClalBit.ClalnetMediator.Contracts\" xmlns:i=\"http://www.w3.org/2001/XMLSchema-instance\">"+
			    "           <a:ErrorMessage>Valid User</a:ErrorMessage>"+
			    "           <a:FullErrorMessage i:nil=\"true\" />"+
			    "           <a:IsSuccess>true</a:IsSuccess>"+
			    "           <a:SecurityToken>999993_310661843</a:SecurityToken>"+
			    "           </ValidatePwdAndIPResult>"+
			    "           </ValidatePwdAndIPResponse>"+
			    "       </s:Body>\n"+
			    "   </s:Envelope>\n";
		
		//DomWrapper dom = DomWrapper.create("<?xml version=\"1.0\" encoding=\"UTF-8\"?><credentials xmlns=\"http://docs.rackspacecloud.com/auth/api/v1.1\" username=\"russellpalmiter\" key=\"REPLACE_ME\" />");
		DomWrapper dom = DomWrapper.create(xml);
		dom.printValues();
		
		String xml2 = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><credentials xmlns=\"http://docs.rackspacecloud.com/auth/api/v1.1\" username=\"russellpalmiter\" key=\"REPLACE_ME\" />";
		DomWrapper dom2 = DomWrapper.create(xml2);
		dom2.print();
		
		System.out.println("username="+dom2.attribute("username"));
		System.out.println("key="+dom2.attribute("key"));
	}
	
	public static DomWrapper create(String xml) throws ParserConfigurationException, SAXException, IOException {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		final byte[] xmlBytes = xml.getBytes("utf-8");
		final ByteArrayInputStream xmlInputStream = new ByteArrayInputStream(xmlBytes);
		final InputSource xmlInputSource = new InputSource(xmlInputStream);
		Document doc = builder.parse(xmlInputSource);
		//Node n = doc.getParentNode();
		Node node = doc.getParentNode();
		return new DomWrapper(node);
	}

	public DomWrapper getExpression(String expr) {
		try {
			 XPathFactory factory = XPathFactory.newInstance();
			 XPath xpath = factory.newXPath();
			 XPathExpression xpathExpr = xpath.compile(expr);
			 
			 Document doc = this.node.getOwnerDocument();
			 Object result = xpathExpr.evaluate(doc, XPathConstants.NODESET);
			 NodeList nodes = (NodeList) result;
//			 Node xpathNode = doc.createElement("xpath");
//			 for (int i = 0; i < nodes.getLength(); i++) {
//				 xpathNode.appendChild(nodes.item(i));
//			 }
//			 return new DomWrapper(xpathNode);
			 return new DomWrapper(nodes);
		} catch (XPathException e) {
			return null; // ???
		}
	}
	
	// TODO not useful -- wraps it with a new parent
	public DomWrapper children() {
		return new DomWrapper(node.getChildNodes());
	}
	
	// TODO firstChild()
	public DomWrapper child() {
		return new DomWrapper(node.getChildNodes().item(0));
	}
	
	public DomWrapper child(String id) {
		for (DomWrapper child : this) {
			if (id.equals(child.id())) {
				return child;
			}
		}
		return null;
	}
	
	public DomWrapper filter(String string) {
		return filter(new String[] { string });
	}
	
	// TODO usefull???
	public List<String> names() {
		ArrayList<String> names = new ArrayList<String>();
		NodeList nodes = node.getChildNodes();
		for (int i=0; i<nodes.getLength(); i++) {
			Node node = nodes.item(i);
			String name = node.getNodeName();
			names.add(name);
		}
		return names;
	}
	
	public String name() {
		String name = node.getNodeName();
		return name;
	}
	
	public Map<String, String> attributes() {
		Map<String, String> attributes = new HashMap<String, String>();
		if (node.hasAttributes()) {
	        NamedNodeMap attrs = node.getAttributes();
	        for (int i=0; i < attrs.getLength(); i++) {
	        	Node child = attrs.item(i);
	        	String name = child.getNodeName();
	        	String content = child.getTextContent();
	        	attributes.put(name, content);
	        }
	    }
		return attributes;
	}
	
	public String attribute(String name) {
		return attributes().get(name);
	}
	
	public String id() {
		return attributes().get("id");
	}
	
	public DomWrapper filter(String[] exprs) {
		/*
			List<String> list = Arrays.asList(exprs);
			NodeSet returnSet = new NodeSet();
			for (int i=0; i<nodes.getLength(); i++) {
				Node node = nodes.item(i);
				String name = node.getNodeName();
				if (list.contains(name)) {
					returnSet.addNode(node);
				}
			}
			return new DomWrapper(returnSet);
			*/
			
			return null;
			
			// better way below
			
//			try {
//				//XPathFactory factory = XPathFactory.newInstance();
//				//XPath xpath = factory.newXPath();
//				
//				// compile sub expressions (from sub tag names)
//				Map<String, XPathExpression> xpathExprs = new HashMap<String, XPathExpression>();
//				for (String expr : exprs) {
//					//XPathExpression compiledExpr = xpath.compile(expr);
//					//NodeSet newNodes = (NodeSet) compiledExpr.evaluate(nodes, XPathConstants.NODESET);
//					
//					NodeList nodelist = XPathAPI.selectNodeList(nodes, expr);
//					returnSet.addNodes(nodelist);
//				}
//			} catch (XPathExpressionException e) {
//				e.printStackTrace();
//			}

			
	}

	public List<String> contents() {
		List<String> result = new ArrayList<String>();
		// TODO convert to using an iterator
		NodeList nodes = node.getChildNodes();
		for (int i=0; i<nodes.getLength(); i++) {
			Node node = nodes.item(i);
			//String name = node.getNodeName();
			String value = node.getTextContent();
			result.add(value);
		}
		return result;
	}
	
	public String content() {
		return node.getTextContent();
		//return contents().size() != 0 ? contents().get(0) : "[empty]";
	}
	
//	// TODO needed????
//	public NodeList getNodeList() {
//		return nodes;
//	}

	// TODO only elements below this element in the dom tree
	public DomWrapper getElements(String elementName) {
		return getExpression("//" + elementName);
	}

//	public DomWrapper getSubnodes(String expr) {
//		return getSubnodes(new String[] {expr});
//	}
	
	public void printValues() {
		List<String> values = contents();
		for (String value : values) {
			System.out.println(value);
		}
	}
	
	@SuppressWarnings("unused")
	public void print() {
		for (DomWrapper child : this) {
			String name = child.name();
			Map<String,String> attributes = child.attributes();
			String context = child.content();
			System.out.println("Name:"+name);
			System.out.println("Content:"+name);
		}
	}

//	public boolean isEmpty() {
//		return nodes.getLength() == 0;
//	}

	// Handles single nodes DomWrappers by pretending its a set of 1
	public Iterator<DomWrapper> iterator() {
		return new Iterator<DomWrapper>() {
			private NodeList nodes = node.getChildNodes();
			private int index = 0;
			
			public boolean hasNext() {
				return index < nodes.getLength();
			}

			public DomWrapper next() {
				return new DomWrapper(nodes.item(index++));
			}

			public void remove() {
				throw new RuntimeException("not implemented");
			}
		};
	}

	@Override
	public String toString() {
		return TextHelper.collectionToString(contents());
	}

	public String getAttribute(String name) {
		throw new RuntimeException("get attribute not implemented");
	}
	
}
