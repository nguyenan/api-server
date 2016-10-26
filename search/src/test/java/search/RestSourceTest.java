package search;

import org.junit.Test;

import com.wut.search.datasource.RestDataSource;

public class RestSourceTest {
	
	//@Test
	public void getCall() {
		RestDataSource getCall = new RestDataSource();
		getCall.setUrl("http://ajax.googleapis.com/ajax/services/search/web?v=1.0&q=retail");
		getCall.setBody("HA");
		getCall.addHeaders("Content-Type", "application/xml");
		getCall.addHeaders("Accept", "application/json");
		String output = getCall.sendRequest();
		System.out.println("Output =" + output);
	}
	
	/*
	 * curl 'http://a2.webutilitykit.com/storage/table?token=%27public%27&id=%27customForm%27&customerId=%27dev.retailkit.com%27&userId=%27public%27&operation=%27read%27&visibility=%27owner%27&application=%27wut%27&noCache=%27true%27&format=%27json%27' -H 'Pragma: no-cache' -H 'Origin: http://localhost' -H 'Accept-Encoding: gzip,deflate,sdch' -H 'Accept-Language: en-US,en;q=0.8' -H 'User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_4) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/35.0.1916.153 Safari/537.36' -H 'Content-Type: text/plain' -H 'Accept: /' -H 'Cache-Control: no-cache' -H 'Referer: http://localhost/configuration/form.html?id=l1s1bd214bb3590143d0b6ab938175683fc1' -H 'Connection: keep-alive' --data-binary '
{"id":"customForm","row":"l1s1bd214bb3590143d0b6ab938175683fc1","start":"958115956689","end":"4113789556689"}
'
	 */
	
	//@Test
	public void postCall() {
		RestDataSource getCall = new RestDataSource();
		String url = "http://a2.webutilitykit.com/storage/table?token=%27public%27&id=%27customForm%27&customerId=%27dev.retailkit.com%27&userId=%27public%27&operation=%27read%27&visibility=%27owner%27&application=%27wut%27&noCache=%27true%27&format=%27json%27";
		getCall.setUrl(url);
		getCall.setMethod("POST");
		getCall.setBody("{\"id\":\"customForm\",\"row\":\"l1s1bd214bb3590143d0b6ab938175683fc1\",\"start\":\"958115956689\",\"end\":\"4113789556689\"}");
		getCall.addHeaders("Content-Type", "application/xml");
		getCall.addHeaders("Accept", "application/json");
		String output = getCall.sendRequest();
		System.out.println("Output =" + output);
	}
	
//	@Test
//	public void securePostCall() {
//		RestDataSource getCall = new RestDataSource();
//		String url = "http://secure.a2.webutilitykit.com/storage/table?token=%27public%27&id=%27customForm%27&customerId=%27dev.retailkit.com%27&userId=%27public%27&operation=%27read%27&visibility=%27owner%27&application=%27wut%27&noCache=%27true%27&format=%27json%27";
//		getCall.setUrl(url);
//		getCall.setMethod("POST");
//		getCall.setBody("{\"id\":\"customForm\",\"row\":\"l1s1bd214bb3590143d0b6ab938175683fc1\",\"start\":\"958115956689\",\"end\":\"4113789556689\"}");
//		getCall.addHeaders("Content-Type", "application/xml");
//		getCall.addHeaders("Accept", "application/json");
//		String output = getCall.sendRequest();
//		System.out.println("Output =" + output);
//	}
}
