package com.wut.rackspace;
/*
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import net.schmizz.sshj.common.IOUtils;

//import org.jclouds.ContextBuilder;
//import org.jclouds.blobstore.BlobStore;
//import org.jclouds.blobstore.BlobStoreContext;
//import org.jclouds.blobstore.KeyNotFoundException;
//import org.jclouds.blobstore.domain.Blob;
import org.jclouds.openstack.swift.CommonSwiftAsyncClient;
import org.jclouds.openstack.swift.CommonSwiftClient;
import org.jclouds.openstack.swift.domain.ObjectInfo;
import org.jclouds.openstack.swift.domain.SwiftObject;
import org.jclouds.openstack.swift.options.CreateContainerOptions;
//import org.jclouds.rest.RestContext;

import com.google.common.collect.ImmutableMap;
import com.wut.datasources.FileSource;
import com.wut.model.scalar.ScalarData;
*/
//public class RackSpaceFiles implements FileSource {
//	
//	   private BlobStore storage;
//	   
//	   @SuppressWarnings("deprecation")
//	   private RestContext<CommonSwiftClient, CommonSwiftAsyncClient> swift;
//	   
//	   public RackSpaceFiles() {
//		   // TODO enable this if you want this to work
//		   //this.init();
//	   }
//
//	   /**
//	    * To get a username and API key see http://www.jclouds.org/documentation/quickstart/rackspace/
//	    * 
//	    * The first argument (args[0]) must be your username
//	    * The second argument (args[1]) must be your API key
//	    */
//	   public static void main(String[] args) {
//		   RackSpaceFiles uploadContainer = new RackSpaceFiles();
//
//	      try {
//	         uploadContainer.init();
//	         uploadContainer.uploadObjectFromFile();
//	         uploadContainer.uploadObjectFromString(Constants.CONTAINER, "filename.html", "say hi");
//	         uploadContainer.uploadObjectFromStringWithMetadata();
//	      }
//	      catch (IOException e) {
//	         e.printStackTrace();
//	      }
//	      finally {
//	         uploadContainer.close();
//	      }
//	   }
//
//	   private void init() {
//	      // The provider configures jclouds To use the Rackspace Cloud (US)
//	      // To use the Rackspace Cloud (UK) set the provider to "cloudfiles-uk"
//	      String provider = "cloudfiles-us";
//
//	      String username = "russellpalmiter";
//	      String apiKey = "REPLACE_ME";
//
//	      BlobStoreContext context = ContextBuilder.newBuilder(provider)
//	            .credentials(username, apiKey)
//	            .buildView(BlobStoreContext.class);
//	      storage = context.getBlobStore();
//	      swift = context.unwrap();
//	   }
//
//	   /**
//	    * Upload an object from a File using the Swift API. 
//	    */
//	   private void uploadObjectFromFile() throws IOException {
//	      System.out.println("Upload Object From File");
//
//	      String filename = "uploadObjectFromFile";
//	      String suffix = ".txt";
//
//	      File tempFile = File.createTempFile(filename, suffix);
//	      tempFile.deleteOnExit();
//
//	      BufferedWriter out = new BufferedWriter(new FileWriter(tempFile));
//	      out.write("uploadObjectFromFile");
//	      out.close();
//
//	      SwiftObject object = swift.getApi().newSwiftObject();
//	      object.getInfo().setName(filename + suffix);
//	      object.setPayload(tempFile);
//
//	      try {
//	    	  swift.getApi().putObject(Constants.CONTAINER, object);	    	  
//	      } catch (KeyNotFoundException knfe) {
//	    	  // if invalid container error, create the container and try again
//	    	  createContainer(Constants.CONTAINER);
//	    	  swift.getApi().putObject(Constants.CONTAINER, object);
//	      }
//
//	      System.out.println("  " + filename + suffix);
//	   }
//	   
//	   private void createContainer(String name) {
//		   System.out.println("Create Container");
//
//		      CreateContainerOptions options = CreateContainerOptions.Builder
//		            .withMetadata(ImmutableMap.of("customer", name, "key2", "value2"));
//
//		      swift.getApi().createContainer(Constants.CONTAINER, options);
//
//		      System.out.println("  " + Constants.CONTAINER);
//	   }
//
//	   /**
//	    * Upload an object from a String using the Swift API. 
//	    */
//	   private void uploadObjectFromString(String bucket, String filename, String contents) {
//	      System.out.println("Upload Object From String");
//
//	      //String filename = "uploadObjectFromString.txt";
//
//	      SwiftObject object = swift.getApi().newSwiftObject();
//	      object.getInfo().setName(filename);
//	      object.setPayload(contents);
//
//	      swift.getApi().putObject(bucket, object);
//
//	      System.out.println("  " + filename);
//	   }
//
//	   /**
//	    * Upload an object from a String with metadata using the BlobStore API. 
//	    */
//	   private void uploadObjectFromStringWithMetadata() {
//	      System.out.println("Upload Object From String With Metadata");
//
//	      String filename = "uploadObjectFromStringWithMetadata.txt";
//
//	      Map<String, String> userMetadata = new HashMap<String, String>();
//	      userMetadata.put("key1", "value1");
//
//	      Blob blob = storage.blobBuilder(filename)
//	            .payload("uploadObjectFromStringWithMetadata")
//	            .userMetadata(userMetadata)
//	            .build();
//
//	      storage.putBlob(Constants.CONTAINER, blob);
//
//	      System.out.println("  " + filename);
//	   }
//
//	   /**
//	    * Always close your service when you're done with it.
//	    */
//	   public void close() {
//	      if (storage != null) {
//	         storage.getContext().close();
//	      }
//	   }
//	   
//
//	@Override
//	public ScalarData getFile(String bucket, String fileName) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public boolean updateFile(String bucket, String filename, String fileData) {
//		//InputStream inputStream = new ByteArrayInputStream(str.getBytes()); 
//		
////		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
////		BufferedOutputStream bufferedStream = new BufferedOutputStream(outputStream);
////		bufferedStream.write();
////		
////		StringWriter writer = new StringWriter();
////		IOUtils.copy(fileData, writer, "utf8");
////		String theString = writer.toString();
//		
//		uploadObjectFromString(bucket, filename, fileData);
//		
//		return true;
//	}
//
//	
//	public Set readBucket(String bucket) {
//		Set<ObjectInfo> objects = swift.getApi().listObjects(Constants.CONTAINER);
//
//	      for (ObjectInfo objectInfo: objects) {
//	         System.out.println("  " + objectInfo);
//	      }
//	      
//		return objects;
//
//	}
//}
