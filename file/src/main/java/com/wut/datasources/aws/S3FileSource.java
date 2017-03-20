package com.wut.datasources.aws;


import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Iterator;

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
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.Permission;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.amazonaws.services.s3.model.Region;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.amazonaws.services.s3.model.SetBucketWebsiteConfigurationRequest;
import com.wut.datasources.FileSource;
import com.wut.support.ErrorHandler;
import com.wut.support.StringHelper;
import com.wut.support.binary.Base64;
import com.wut.support.settings.SettingsManager;
import com.wut.support.stream.StreamUtil;
//import net.schmizz.sshj.common.Base64;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.AmazonClientException;

public class S3FileSource implements FileSource {
	private static final String AMAZON_USERNAME = SettingsManager.getSystemSetting("amazon.username");
	private static final String AMAZON_PASSWORD = SettingsManager.getSystemSetting("amazon.password");
	private AmazonS3 s3client;
	
	public S3FileSource() {
			AWSCredentials myCredentials = new BasicAWSCredentials(
					AMAZON_USERNAME, AMAZON_PASSWORD);
			s3client = new AmazonS3Client(myCredentials);
	}
	
	private String getObjectKey(String folder, String filename) {
		if (folder != null) {
			return folder + "/" + filename;
		} else {
			return filename;
		}
	}
	
	public InputStream getFile(String bucket, String folder, String filename) {
		GetObjectRequest getObjectRequest = new GetObjectRequest(bucket, getObjectKey(folder,filename));
		S3Object object = s3client.getObject(getObjectRequest);
		InputStream objectData = object.getObjectContent();
		
		// TODO base64 encode this stream
		
		return objectData; // TODO make sure this gets closed up the stack
	}
	
	public static void main(String[] args) {
		S3FileSource s3 = new S3FileSource();
		s3.updateFile("www.cleverhen.com", "storefront", "test3.test", StringHelper.asInputStream("<hi>love this</hi>"));
	}

	public boolean updateFile(String bucket, String folder, String filename, InputStream fileData) {
		System.out.println("Uploading a new object to S3 from a file\n");
		
		ObjectMetadata metadata = new ObjectMetadata();
		if (filename.endsWith(".html")) {
			metadata.setContentType("text/html");			
		} else if (filename.endsWith(".js")) {
			metadata.setContentType("application/javascript");			
		} else if (filename.endsWith(".svg")) {
			metadata.setContentType("image/svg+xml");			
		} else if (filename.endsWith(".png")) {
			metadata.setContentType("image/png");			
		} else if (filename.endsWith(".jpg")) {
			metadata.setContentType("image/jpeg");			
		} else if (filename.endsWith(".jpeg")) {
			metadata.setContentType("image/jpeg");			
		} else if (filename.endsWith(".gif")) {
			metadata.setContentType("image/gif");	
		} else if (filename.endsWith(".css")) {
			metadata.setContentType("text/css");			
		} else if (filename.endsWith(".json")) {
			metadata.setContentType("application/json");	
		} else if (filename.endsWith(".txt")) {
			metadata.setContentType("text/plain");	
		} else if (filename.endsWith(".xml")) {
			metadata.setContentType("text/xml");	
		} else if (filename.endsWith(".template")) {
			metadata.setContentType("text/plain");	
		} else if (filename.endsWith(".csv")) {
			metadata.setContentType("text/csv");	
		} else if (filename.endsWith(".appcache")) {
			metadata.setContentType("text/cache-manifest");	
		}
		
		InputStream base64decodedStream;
		
		String streamAsString = StreamUtil.getStringFromInputStream(fileData);
		byte[] base64encodedBytes = Base64.decodeBase64(streamAsString);
		base64decodedStream = new ByteArrayInputStream(base64encodedBytes);
		
		PutObjectRequest putRequest = new PutObjectRequest(bucket, getObjectKey(folder, filename), base64decodedStream, metadata);
		putRequest.withCannedAcl(CannedAccessControlList.PublicRead);
		
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
    				boolean wasBucketCreated = createBucket(bucket);
    				
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

	public boolean deleteFile(String bucket, String folder, String filename) {
		try {
			s3client.deleteObject(new DeleteObjectRequest(bucket, getObjectKey(folder, filename)));	
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	private synchronized boolean createBucket(String bucket) {
		try {
			// create new bucket
			CreateBucketRequest bucketRequest = new CreateBucketRequest(bucket, Region.US_Standard);
			Bucket newS3bucket = s3client.createBucket(bucket);
			
			// set bucket up for website hosting
			BucketWebsiteConfiguration websiteConfig = new BucketWebsiteConfiguration("index.html", "404.html");
			SetBucketWebsiteConfigurationRequest websiteRequest = new SetBucketWebsiteConfigurationRequest(bucket, websiteConfig);
			s3client.setBucketWebsiteConfiguration(websiteRequest);

			//s3client.setBucketAcl(bucket, CannedAccessControlList.PublicRead);
			
			// add CORS to bucket
			
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
    public synchronized boolean deleteBucket(String bucketName) {
        try {
                ObjectListing object_listing = s3client.listObjects(bucketName);
                while (true) {
                        for (Iterator<?> iterator = object_listing.getObjectSummaries().iterator(); iterator.hasNext();) {
                                S3ObjectSummary summary = (S3ObjectSummary) iterator.next();
                                s3client.deleteObject(bucketName, summary.getKey());
                        }

                        // more object_listing to retrieve?
                        if (object_listing.isTruncated()) {
                                object_listing = s3client.listNextBatchOfObjects(object_listing);
                        } else {
                                break;
                        }
                }
                ;

                // bucket ready to delete
                s3client.deleteBucket(bucketName);
                return true;
        } catch (AmazonServiceException e) {
                ErrorHandler.userError("Error when deleting S3Bucket '" + bucketName + "' : " + e.getErrorCode());
                return false;
        }
}

}
