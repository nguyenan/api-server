package com.wut.datasources.aws;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.AccessControlList;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.BucketWebsiteConfiguration;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.CreateBucketRequest;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.Grant;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.Permission;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.amazonaws.services.s3.model.Region;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.SetBucketWebsiteConfigurationRequest;
import com.wut.datasources.FileSource;
import com.wut.support.StringHelper;
import com.wut.support.binary.Base64;
import com.wut.support.settings.SettingsManager;
import com.wut.support.stream.StreamUtil;
//import net.schmizz.sshj.common.Base64;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.AmazonClientException;

public class SettingsSource implements FileSource {
	private static final String AMAZON_USERNAME = SettingsManager.getSystemSetting("amazon.username");
	private static final String AMAZON_PASSWORD = SettingsManager.getSystemSetting("amazon.password");
	private AmazonS3 s3client;
	
	public SettingsSource() {
			AWSCredentials myCredentials = new BasicAWSCredentials(AMAZON_USERNAME, AMAZON_PASSWORD);
			s3client = new AmazonS3Client(myCredentials);
	}
	
	private String getSettingsFile(String customer) {
		return customer + "/settings.properties";
	}
	
	public InputStream getFile(String bucket, String folder, String filename) {
		String s3key = getObjectKey(folder,filename);
		GetObjectRequest getObjectRequest = new GetObjectRequest(bucket, s3key);
		S3Object object = s3client.getObject(getObjectRequest);
		InputStream objectData = object.getObjectContent();
		// TODO base64 encode this stream
		return objectData; // TODO make sure this gets closed up the stack
	}
	
	// TODO DELETE THIS
	private String getObjectKey(String folder, String filename) {
		return folder + "/" + filename;
	}

	public static void main(String[] args) throws IOException {
		SettingsSource settings = new SettingsSource();
		
		InputStream is = settings.getFile("dev1.tend.ag", "settings", "settings.properties");
		
//		byte[] buffer = new byte[1024];
//		int len;
//		while ((len = is.read(buffer)) != -1) {
//		    System.out.write(buffer, 0, len);
//		}
		
		Properties props = new Properties();
		props.load(is);
		
		System.out.println(props);
		
		String btpk = props.getProperty("braintree-private-key");
		
		System.out.println("BTPK=" + btpk);
		
		System.out.println("the end.");
		
		//settings.get
		
		//s3.updateFile("www.cleverhen.com", "storefront", "test3.test", StringHelper.asInputStream("<hi>love this</hi>"));
	}
	
	
	public boolean updateSettings(String customer, Properties settings) {
		
		
		//settings.store(out, comments);
		
		return false;
		
	}

	
/*
	public boolean updateSettings(String customer, InputStream fileData) {
		System.out.println("Uploading a new object to S3 from a file\n");
		
		ObjectMetadata metadata = new ObjectMetadata();
		metadata.setContentType("text/plain");	
		
		InputStream base64decodedStream;
		
		String streamAsString = StreamUtil.getStringFromInputStream(fileData);
		byte[] base64encodedBytes = Base64.decodeBase64(streamAsString);
		base64decodedStream = new ByteArrayInputStream(base64encodedBytes);
		
		PutObjectRequest putRequest = null; //new PutObjectRequest(bucket, getSettingsFile(customer), base64decodedStream, metadata);
		//putRequest.withCannedAcl(CannedAccessControlList.Private);
		
		try {
			@SuppressWarnings("unused") // TODO check putResult instead of return true
			PutObjectResult putResult = s3client.putObject(putRequest);

			return true;
         } catch (AmazonServiceException ase) {
            System.out.println("Caught an AmazonServiceException, which " +
            		"means your request made it " +
                    "to Amazon S3, but was rejected with an error response" +
                    " for some reason.");
            System.out.println("Error Message:    " + ase.getMessage());
            System.out.println("HTTP Status Code: " + ase.getStatusCode());
            System.out.println("AWS Error Code:   " + ase.getErrorCode());
            System.out.println("Error Type:       " + ase.getErrorType());
            System.out.println("Request ID:       " + ase.getRequestId());
            
            // if bucket doesnt exist, create bucket and try again
            if (ase.getErrorCode().equals("NoSuchBucket")) {
    			try {
    				// wait
    				Thread.sleep(1000);
    				
    				// create bucket/folder
    				boolean wasBucketCreated = true;//  createBucket(bucket);
    				
    				// try request again
    				s3client.putObject(putRequest);
    			} catch (Exception e) {
    				return false;
    			}
            }
            
            return false;
        } catch (AmazonClientException ace) {
            System.out.println("Caught an AmazonClientException, which " +
            		"means the client encountered " +
                    "an internal error while trying to " +
                    "communicate with S3, " +
                    "such as not being able to access the network.");
            System.out.println("Error Message: " + ace.getMessage());
            
            return false;
        }
	}
	*/
	
	// TODO delete this
	private boolean createBucket(String bucket2) {
		// TODO Auto-generated method stub
		return false;
	}

	private synchronized boolean createClientBucket(String client) {
		try {
			// create new bucket
			CreateBucketRequest bucketRequest = new CreateBucketRequest(client, Region.US_Standard);
			Bucket newS3bucket = s3client.createBucket(bucketRequest);

			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	
	// TODO DELETE THESE

	@Override
	public boolean updateFile(String bucket, String folder, String filename,
			InputStream fileData) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean deleteFile(String bucket, String folder, String filename) {
		// TODO Auto-generated method stub
		return false;
	}
}
