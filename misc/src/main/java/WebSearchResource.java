


//import org.restlet.data.Reference;

import com.wut.model.Data;
import com.wut.pipeline.WutRequest;
import com.wut.resources.common.MissingParameterException;
import com.wut.resources.common.ObsoleteCrudResource;
//import javax.xml.xpath.XPathFactory;
//import com.wut.resources.common.ResourceGroupAnnotation;
//import com.wut.support.datafetch.HTTPHelper;
//import com.wut.support.xml.DomWrapper;
//import com.wut.model.list.ListData;
//import com.wut.model.map.MappedData;
//import com.wut.model.scalar.StringData;
//import java.net.URLEncoder;
//import java.util.ArrayList;
//import java.util.Iterator;
//import javax.xml.xpath.XPath;
//import javax.xml.xpath.XPathConstants;
//import javax.xml.xpath.XPathExpression;
//import javax.xml.xpath.XPathExpressionException;
//import com.wut.model.scalar.UrlData;

//@ResourceGroupAnnotation(name = "search", group = "web", desc = "web search engine resource")
public class WebSearchResource extends ObsoleteCrudResource {
	private static final long serialVersionUID = -2889749872908274797L;
	//private static final String BASE_URL = "http://search.yahooapis.com/WebSearchService/V1/webSearch?appid=d._ApoDV34Edo_NTxBO_UjNbA.Rc4VyPp.eYoG9.hA88swAQmjnPVCExTQrq72RUdWM2tWGWRls-&query=";

	@Override
	public String getName() {
		return "web";
	}
	
	@Override
	public Data read(WutRequest ri) throws /*XPathExpressionException,*/
			MissingParameterException {
//		String searchTerm = ri.getStringParameter("term");
//		String encodedTerm = URLEncoder.encode(searchTerm);
//		String uri = BASE_URL + encodedTerm;
//		//Response response = new Client(Protocol.HTTP).get(uri);
//		//DomRepresentation dom = response.getEntityAsDom();
//		DomWrapper dom = HTTPHelper.getPageAsDom(uri);
//		
//		String expr = "/ResultSet/Result";
//		DomWrapper nodes = dom.getExpression(expr);
//		Iterator<DomWrapper> itr = nodes.iterator();
//		XPathFactory factory = XPathFactory.newInstance();
//		XPath xpath = factory.newXPath();
//		XPathExpression titleExpr = xpath.compile("Title/text()");
//		XPathExpression summaryExpr = xpath.compile("Summary/text()");
//		XPathExpression urlExpr = xpath.compile("Url/text()");
//
//		ArrayList<MappedData> list = new ArrayList<MappedData>();
//		while (itr.hasNext()) {
//			DomWrapper n = itr.next();
//			String title = String.valueOf(titleExpr.evaluate(n,
//					XPathConstants.STRING));
//			String summary = String.valueOf(summaryExpr.evaluate(n,
//					XPathConstants.STRING));
//			String url = String.valueOf(urlExpr.evaluate(n,
//					XPathConstants.STRING));
//			MappedData result = new MappedData();
//			result.put("title", new StringData(title));
//			result.put("summary", new StringData(summary));
//			result.put("url", new UrlData(url));
//			list.add(result);
//		}
//		return new ListData(list);
		return null;
	}

}
