package com.wut.search.datasource;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.wut.datasources.SearchSource;
import com.wut.search.datasource.GoogleResults.Result;

public class GoogleSearchDataSource implements SearchSource {

	public static void main(String[] args) throws IOException {
		GoogleSearchDataSource gSource = new GoogleSearchDataSource();
		List<Map<String,String>> results = gSource.search("rubber site:amazon.com");
		System.out.println(results.toString());
	}

	@Override
	public List<Map<String,String>> search(String searchTerm) {
		String google = "http://ajax.googleapis.com/ajax/services/search/web?v=1.0&q=";
	    String search = searchTerm;
	    String charset = "UTF-8";
	    
	    List<Map<String,String>> results = new ArrayList<Map<String,String>>();

	    try {
			URL url = new URL(google + URLEncoder.encode(search, charset));
			Reader reader = new InputStreamReader(url.openStream(), charset);
			GoogleResults googleResults = new Gson().fromJson(reader, GoogleResults.class);
			List<Result> googleResultList = googleResults.getResponseData().getResults();
			int position = 1;
			for (Result result : googleResultList) {
				Map<String,String> resultMap = new HashMap<String, String>();
				resultMap.put("position", new Integer(position).toString());
				resultMap.put("title", result.getTitle());
				resultMap.put("url", result.getUrl());
				results.add(resultMap);
				position++;
			}
		} catch (JsonSyntaxException e) {
			e.printStackTrace();
		} catch (JsonIOException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	    
	    return results;
	}

}
