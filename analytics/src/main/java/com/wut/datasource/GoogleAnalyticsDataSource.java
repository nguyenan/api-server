/*
 * Copyright (c) 2012 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package com.wut.datasource;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.DataStoreFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.analytics.Analytics;
import com.google.api.services.analytics.Analytics.Data.Ga.Get;
import com.google.api.services.analytics.AnalyticsScopes;
import com.google.api.services.analytics.model.GaData;
import com.google.api.services.analytics.model.GaData.ColumnHeaders;
import com.google.api.services.analytics.model.GaData.ProfileInfo;
import com.google.api.services.analytics.model.GaData.Query;
import com.google.api.services.samples.analytics.cmdline.AnalyticsSample;
import com.wut.support.settings.SettingsManager;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.naming.ldap.SortControl;


public class GoogleAnalyticsDataSource {

  private static final String GOOGLE_SERVICE_ACCOUNT_ID = SettingsManager.getSystemSetting("google.service.account.id");

  private static final int MAX_RESULTS = 100;

/**
   * Be sure to specify the name of your application. If the application name is {@code null} or
   * blank, the application will log a warning. Suggested format is "MyCompany-ProductName/1.0".
   */
  private static final String APPLICATION_NAME = SettingsManager.getSystemSetting("google.service.application.name");

  /**
   * Used to identify from which reporting profile to retrieve data. Format is ga:xxx where xxx is
   * your profile ID.
   */
  private static final String TABLE_ID = "ga:79640172";

	/** Directory to store user credentials. */
//  private static final java.io.File DATA_STORE_DIR =
//      new java.io.File(System.getProperty("user.home"), ".store/analytics_sample");
  private static final java.io.File DATA_STORE_DIR =
		  new java.io.File(SettingsManager.getSystemSetting("analytics.base.directory") + "/credentials");

  /**
   * Global instance of the {@link DataStoreFactory}. The best practice is to make it a single
   * globally shared instance across your application.
   */
  private static FileDataStoreFactory DATA_STORE_FACTORY;

  /** Global instance of the HTTP transport. */
  private static HttpTransport HTTP_TRANSPORT;

  /** Global instance of the JSON factory. */
  private static final JsonFactory JSON_FACTORY = new JacksonFactory();
  
  private Analytics analytics;
  
  static {
	  try {
		HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
		//DATA_STORE_FACTORY = new FileDataStoreFactory(DATA_STORE_DIR);
	} catch (GeneralSecurityException e) {
		e.printStackTrace();
	} catch (IOException e) {
		e.printStackTrace();
	}
  }
  
  public GoogleAnalyticsDataSource() {
	  try {
		analytics = initializeAnalytics();
	} catch (Exception e) {
		e.printStackTrace();
	}
  }
  
	public static String arrayToCSV(String[] array, String itemPrefix) {
		StringBuilder csv = new StringBuilder();
		for (int i = 0; i < array.length; i++) {
			String item = array[i];
			if (itemPrefix != null) {
				// put negatives before prefix
				if (item.charAt(0) == '-') {
					csv.append('-');
					item = item.substring(1);
				}
				csv.append(itemPrefix);
			}
			csv.append(item);
			if (i < array.length - 1) {
				csv.append(",");
			}
		}
		return csv.toString();
	}
  
	public List<Map<String, String>> query(String analyticsViewId,
			String[] dimensions, String[] metrics, String start, String end,
			Map<String, String> filters, String[] sort) {
		List<Map<String, String>> resultList = new ArrayList<Map<String, String>>();

		try {
			// GaData gaData = executeDataQuery(analytics,
			// "ga:"+analyticsViewId);

			String dimensionsCsv = arrayToCSV(dimensions, "ga:");

			String metricsCsv = arrayToCSV(metrics, "ga:");

			Get requestBuilder = analytics.data().ga().get("ga:"+analyticsViewId,
					start,
					end,
					metricsCsv);
			
			requestBuilder.setDimensions(dimensionsCsv);
			
			if (sort != null && sort.length > 0) {
				String sortCsv = arrayToCSV(sort, "ga:");
				requestBuilder.setSort(sortCsv);
			}

			if (filters != null && filters.keySet().size() > 0) {
				String filtersCsv = filterMapToString(filters, "ga:");
				requestBuilder.setFilters(filtersCsv);
			}
			
			GaData results = requestBuilder.setMaxResults(MAX_RESULTS).execute();

			if (results.getTotalResults() > 0) {
				// System.out.println("Data Table:");

				// // Print the column names.
				// List<String> headers = new ArrayList<String>();
				// for (ColumnHeaders header : results.getColumnHeaders()) {
				// System.out.format("%-32s", header.getName());
				// }
				// System.out.println();

				// Print the rows of data.
				for (List<String> rowValues : results.getRows()) {
					int headerIndex = 0;
					Map<String, String> rowMap = new HashMap<String, String>();
					for (String value : rowValues) {
						String key = results.getColumnHeaders()
								.get(headerIndex).getName();
						rowMap.put(key, value);
						headerIndex++;
						// System.out.format("%-32s", value);
					}
					resultList.add(rowMap);
					// System.out.println();
				}
				return resultList;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resultList;
	}

	private static String filterMapToString(Map<String, String> filters, String keyPrefix) {
		StringBuilder filterStr = new StringBuilder();
		for (String filterKey : filters.keySet()) {
			String filterValue = filters.get(filterKey);
			filterStr.append(keyPrefix);
			filterStr.append(filterKey);
			filterStr.append("==");
			filterStr.append(filterValue);
		}
		
		return filterStr.toString();
	}

/**
   * Main demo. This first initializes an Analytics service object. It then queries for the top 25
   * organic search keywords and traffic sources by visits. Finally each important part of the
   * response is printed to the screen.
   *
   * @param args command line args.
   */
  public static void main(String[] args) {
//	  GaData results1 = analytics.data().ga().get(TABLE_ID, // Table Id.
//		        "2012-01-01", // Start date.
//		        "2014-01-14", // End date.
//		        "ga:visits") // Metrics.
//		        .setDimensions("ga:source,ga:keyword")
//		        .setSort("-ga:visits,ga:source")
//		        .setFilters("ga:medium==organic")
//		        .setMaxResults(100)
//		        .execute();
	  
	  String start = "2012-01-01";
	  String end = "2014-01-14";
	  String[] metrics = new String[] { "visits" };
	  String[] dimensions = new String[] { "source", "keyword" };
	  String[] sort = new String[] { "-visits", "source" };
	  Map<String,String> filters = new HashMap<String, String>();
	  filters.put("medium", "organic");
	  
	  GoogleAnalyticsDataSource ga = new GoogleAnalyticsDataSource();
	  List<Map<String, String>> results = ga.query("79640172", dimensions, metrics, start, end, filters, sort);
	  
	  System.out.println("Results:" + results.toString());
	  //try {
    	 

//      printReportInfo(gaData);
//      printProfileInfo(gaData);
//      printQueryInfo(gaData);
//      printPaginationInfo(gaData);
//      printTotalsForAllResults(gaData);
//      printColumnHeaders(gaData);
//      printDataTable(gaData);

//    } catch (GoogleJsonResponseException e) {
//      System.err.println("There was a service error: " + e.getDetails().getCode() + " : "
//          + e.getDetails().getMessage());
//    } catch (Throwable t) {
//      t.printStackTrace();
//    }
  }

	/** Authorizes the installed application to access user's protected data. */
	private static Credential authorize() throws Exception {
		// load client secrets
		GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(
				JSON_FACTORY,
				new InputStreamReader(AnalyticsSample.class
						.getResourceAsStream("/client_secrets.json")));
		if (clientSecrets.getDetails().getClientId().startsWith("Enter")
				|| clientSecrets.getDetails().getClientSecret()
						.startsWith("Enter ")) {
			System.out
					.println("Enter Client ID and Secret from https://code.google.com/apis/console/?api=analytics "
							+ "into analytics-cmdline-sample/src/main/resources/client_secrets.json");
			System.exit(1);
		}
		// set up authorization code flow
		GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
				HTTP_TRANSPORT, JSON_FACTORY, clientSecrets,
				Collections.singleton(AnalyticsScopes.ANALYTICS_READONLY))
				.setDataStoreFactory(DATA_STORE_FACTORY).build();
		// authorize
		return new AuthorizationCodeInstalledApp(flow,
				new LocalServerReceiver()).authorize("user");
	}

  /**
   * Performs all necessary setup steps for running requests against the API.
   *
   * @return an initialized Analytics service object.
   *
   * @throws Exception if an issue occurs with OAuth2Native authorize.
   */
  private static Analytics initializeAnalytics() throws Exception {
    // Authorization.
    //Credential credential = authorize();
	// Build service account credential.
	//final URL p12resource = GoogleAnalyticsDataSource.class.getResource("../../../../../../credentials/retail-kit.p12");
	//final URL p12resource = GoogleAnalyticsDataSource.class.getResource("/../../credentials/retail-kit.p12");
	final String settingsFileURI = SettingsManager.getSystemSetting("analytics.credentials.file");
	final URL p12resource = GoogleAnalyticsDataSource.class.getResource(settingsFileURI);

	final File p12file = new File(p12resource.toURI());
	//FileInputStream inputStream = new FileInputStream(p12file);
	//i_propConfig.load(inputStream);
	//inputStream.close();
	
	//final File retailkitP12 = new File("credentials/retail-kit.p12");
	
	if (!p12file.exists()) {
		throw new RuntimeException("missing analytics auth file:" + p12file.getAbsolutePath());
	}
	  
	GoogleCredential credential = new GoogleCredential.Builder().setTransport(HTTP_TRANSPORT)
	      .setJsonFactory(JSON_FACTORY)
	      .setServiceAccountId(GOOGLE_SERVICE_ACCOUNT_ID)
	      .setServiceAccountScopes(Collections.singleton(AnalyticsScopes.ANALYTICS_READONLY))
	      .setServiceAccountPrivateKeyFromP12File(p12file)
	      .build();
	  
	  // set up global Plus instance
//	  plus = new Plus.Builder(httpTransport, jsonFactory, credential)
//	      .setApplicationName(APPLICATION_NAME).build();
	  
	  
	  
    // Set up and return Google Analytics API client.
    return new Analytics.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential).setApplicationName(
        APPLICATION_NAME).build();
  }

  /**
   * Returns the top 25 organic search keywords and traffic sources by visits. The Core Reporting
   * API is used to retrieve this data.
   *
   * @param analytics the Analytics service object used to access the API.
   * @param tableId the table ID from which to retrieve data.
   * @return the response from the API.
   * @throws IOException if an API error occured.
   */
  private static GaData executeDataQuery(Analytics analytics, String tableId) throws IOException {
    return analytics.data().ga().get(tableId, // Table Id.
        "2012-01-01", // Start date.
        "2014-01-14", // End date.
        "ga:visits") // Metrics.
        .setDimensions("ga:source,ga:keyword")
        .setSort("-ga:visits,ga:source")
        .setFilters("ga:medium==organic")
        .setMaxResults(25)
        .execute();
  }

  /**
   * Prints general information about this report.
   *
   * @param gaData the data returned from the API.
   */
  private static void printReportInfo(GaData gaData) {
    System.out.println();
    System.out.println("Response:");
    System.out.println("ID:" + gaData.getId());
    System.out.println("Self link: " + gaData.getSelfLink());
    System.out.println("Kind: " + gaData.getKind());
    System.out.println("Contains Sampled Data: " + gaData.getContainsSampledData());
  }

  /**
   * Prints general information about the profile from which this report was accessed.
   *
   * @param gaData the data returned from the API.
   */
  private static void printProfileInfo(GaData gaData) {
    ProfileInfo profileInfo = gaData.getProfileInfo();

    System.out.println("Profile Info");
    System.out.println("Account ID: " + profileInfo.getAccountId());
    System.out.println("Web Property ID: " + profileInfo.getWebPropertyId());
    System.out.println("Internal Web Property ID: " + profileInfo.getInternalWebPropertyId());
    System.out.println("Profile ID: " + profileInfo.getProfileId());
    System.out.println("Profile Name: " + profileInfo.getProfileName());
    System.out.println("Table ID: " + profileInfo.getTableId());
  }

  /**
   * Prints the values of all the parameters that were used to query the API.
   *
   * @param gaData the data returned from the API.
   */
  private static void printQueryInfo(GaData gaData) {
    Query query = gaData.getQuery();

    System.out.println("Query Info:");
    System.out.println("Ids: " + query.getIds());
    System.out.println("Start Date: " + query.getStartDate());
    System.out.println("End Date: " + query.getEndDate());
    System.out.println("Metrics: " + query.getMetrics()); // List
    System.out.println("Dimensions: " + query.getDimensions()); // List
    System.out.println("Sort: " + query.getSort()); // List
    System.out.println("Segment: " + query.getSegment());
    System.out.println("Filters: " + query.getFilters());
    System.out.println("Start Index: " + query.getStartIndex());
    System.out.println("Max Results: " + query.getMaxResults());
  }

  /**
   * Prints common pagination information.
   *
   * @param gaData the data returned from the API.
   */
  private static void printPaginationInfo(GaData gaData) {
    System.out.println("Pagination Info:");
    System.out.println("Previous Link: " + gaData.getPreviousLink());
    System.out.println("Next Link: " + gaData.getNextLink());
    System.out.println("Items Per Page: " + gaData.getItemsPerPage());
    System.out.println("Total Results: " + gaData.getTotalResults());
  }

  /**
   * Prints the total metric value for all rows the query matched.
   *
   * @param gaData the data returned from the API.
   */
  private static void printTotalsForAllResults(GaData gaData) {
    System.out.println("Metric totals over all results:");
    Map<String, String> totalsMap = gaData.getTotalsForAllResults();
    for (Map.Entry<String, String> entry : totalsMap.entrySet()) {
      System.out.println(entry.getKey() + " : " + entry.getValue());
    }
  }

  /**
   * Prints the information for each column. The reporting data from the API is returned as rows of
   * data. The column headers describe the names and types of each column in rows.
   *
   * @param gaData the data returned from the API.
   */
  private static void printColumnHeaders(GaData gaData) {
    System.out.println("Column Headers:");

    for (ColumnHeaders header : gaData.getColumnHeaders()) {
      System.out.println("Column Name: " + header.getName());
      System.out.println("Column Type: " + header.getColumnType());
      System.out.println("Column Data Type: " + header.getDataType());
    }
  }

  /**
   * Prints all the rows of data returned by the API.
   *
   * @param gaData the data returned from the API.
   */
  private static void printDataTable(GaData gaData) {
    if (gaData.getTotalResults() > 0) {
      System.out.println("Data Table:");

      // Print the column names.
      for (ColumnHeaders header : gaData.getColumnHeaders()) {
        System.out.format("%-32s", header.getName());
      }
      System.out.println();

      // Print the rows of data.
      for (List<String> rowValues : gaData.getRows()) {
        for (String value : rowValues) {
          System.out.format("%-32s", value);
        }
        System.out.println();
      }
    } else {
      System.out.println("No data");
    }
  }
}