package com.wut.datasources.aws;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.BucketCrossOriginConfiguration;
import com.amazonaws.services.s3.model.BucketWebsiteConfiguration;
import com.amazonaws.services.s3.model.CORSRule;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.CopyObjectRequest;
import com.amazonaws.services.s3.model.CreateBucketRequest;
//import net.schmizz.sshj.common.Base64;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ListObjectsRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.amazonaws.services.s3.model.Region;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.amazonaws.services.s3.model.SetBucketWebsiteConfigurationRequest;
import com.wut.datasources.FileSource;
import com.wut.model.Data;
import com.wut.model.list.ListData;
import com.wut.model.map.MessageData;
import com.wut.support.ErrorHandler;
import com.wut.support.binary.Base64;
import com.wut.support.settings.SettingsManager;
import com.wut.support.stream.StreamUtil;

public class S3FileSource implements FileSource {
	private static final String AMAZON_USERNAME = SettingsManager.getSystemSetting("amazon.username");
	private static final String AMAZON_PASSWORD = SettingsManager.getSystemSetting("amazon.password");
	private AmazonS3 s3client;

	public S3FileSource() {
		AWSCredentials myCredentials = new BasicAWSCredentials(AMAZON_USERNAME, AMAZON_PASSWORD);
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
		GetObjectRequest getObjectRequest = new GetObjectRequest(bucket, getObjectKey(folder, filename));
		try {
			S3Object object = s3client.getObject(getObjectRequest);
			InputStream objectData = object.getObjectContent();

			// TODO base64 encode this stream

			return objectData; // TODO make sure this gets closed up the stack

		} catch (AmazonS3Exception e) {
			ErrorHandler.systemError(e.getErrorCode() + " - Exception geting file - " + getObjectKey(folder, filename),
					e);
			return null;
		}
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

		// InputStream base64decodedStream;

		String streamAsString = StreamUtil.getStringFromInputStream(fileData);
		byte[] base64encodedBytes = Base64.decodeBase64(streamAsString);

		metadata.setContentLength(base64encodedBytes.length);
		ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(base64encodedBytes);

		// base64decodedStream = new ByteArrayInputStream(base64encodedBytes);

		PutObjectRequest putRequest = new PutObjectRequest(bucket, getObjectKey(folder, filename), byteArrayInputStream,
				metadata);
		putRequest.withCannedAcl(CannedAccessControlList.PublicRead);

		try {
			@SuppressWarnings("unused")
			PutObjectResult putResult = s3client.putObject(putRequest);

			return true;
		} catch (AmazonServiceException ase) {
			System.out.println("Caught an AmazonServiceException, which " + "means your request made it "
					+ "to Amazon S3, but was rejected with an error response" + " for some reason.");
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
					return true;
				} catch (Exception e) {
					return false;
				}
			}
			return false;
		} catch (AmazonClientException ace) {
			System.out.println("Caught an AmazonClientException, which " + "means the client encountered "
					+ "an internal error while trying to " + "communicate with S3, "
					+ "such as not being able to access the network.");
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
			SetBucketWebsiteConfigurationRequest websiteRequest = new SetBucketWebsiteConfigurationRequest(bucket,
					websiteConfig);
			s3client.setBucketWebsiteConfiguration(websiteRequest);

			// s3client.setBucketAcl(bucket,
			// CannedAccessControlList.PublicRead);

			// add CORS to bucket
			CORSRule rule1 = new CORSRule().withId("admin CORS")
					.withAllowedMethods(Arrays.asList(new CORSRule.AllowedMethods[] { CORSRule.AllowedMethods.GET }))
					.withAllowedOrigins(
							Arrays.asList(new String[] { "www.tend.ag", "betaadmin.tend.ag" }));

			BucketCrossOriginConfiguration corsConfig = new BucketCrossOriginConfiguration();
			corsConfig.setRules(Arrays.asList(new CORSRule[] { rule1 }));
			s3client.setBucketCrossOriginConfiguration(bucket, corsConfig);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public synchronized boolean deleteBucket(String bucketName) {
		try {
			ObjectListing filesInBucket = s3client.listObjects(bucketName);
			boolean isNextBatch = false;
			do {
				if (isNextBatch)
					filesInBucket = s3client.listNextBatchOfObjects(filesInBucket);
				for (Iterator<?> iterator = filesInBucket.getObjectSummaries().iterator(); iterator.hasNext();) {
					S3ObjectSummary summary = (S3ObjectSummary) iterator.next();
					s3client.deleteObject(bucketName, summary.getKey());
				}
				isNextBatch = true;
			} while (filesInBucket.isTruncated());

			// bucket ready to delete
			s3client.deleteBucket(bucketName);
			return true;
		} catch (AmazonServiceException e) {
			ErrorHandler.userError("Error when deleting S3Bucket '" + bucketName + "' : " + e.getErrorCode());
			return false;
		}
	}

	public synchronized boolean copyBucket(String source, String destination) {
		try {
			String sourceBucketName = getBucketName(source);
			String sourceFolder = getFolderPath(source);
			String destinationBucketName = getBucketName(destination);

			ObjectListing filesInBucket = s3client.listObjects(sourceBucketName, sourceFolder);
			boolean isNextBatch = false;
			String fullPath = "";
			String newKey = "";

			// if destinationBucketName doesnt exist, create bucket
			if (!s3client.doesBucketExist(destinationBucketName)) {
				try {
					createBucket(destinationBucketName);
				} catch (AmazonServiceException e) {
					ErrorHandler.userError("Error when createBucket " + source + ": " + e.getErrorCode());
					return false;
				}
			}

			do {
				if (isNextBatch)
					filesInBucket = s3client.listNextBatchOfObjects(filesInBucket);
				for (Iterator<?> iterator = filesInBucket.getObjectSummaries().iterator(); iterator.hasNext();) {
					S3ObjectSummary summary = (S3ObjectSummary) iterator.next();
					fullPath = sourceBucketName + "/" + summary.getKey();
					newKey = fullPath.replaceFirst(source, destination).replaceFirst(destinationBucketName + "/", "");

					CopyObjectRequest copyRequest = new CopyObjectRequest(sourceBucketName, summary.getKey(),
							destinationBucketName, newKey);
					copyRequest.withCannedAccessControlList(CannedAccessControlList.PublicRead);

					s3client.copyObject(copyRequest);
				}
				isNextBatch = true;
			} while (filesInBucket.isTruncated());
			return true;
		} catch (AmazonServiceException e) {
			ErrorHandler.userError("Error when clonning " + source + " to " + destination + ": " + e.getErrorCode());
			return false;
		}
	}

	public Data listDirectory(String bucketName, String prefix) {
		String delimiter = "/";
		if (!prefix.endsWith(delimiter)) {
			prefix += delimiter;
		}

		ListObjectsRequest listObjectsRequest = new ListObjectsRequest().withBucketName(bucketName).withPrefix(prefix)
				.withDelimiter(delimiter);
		ObjectListing objects = s3client.listObjects(listObjectsRequest);

		List<String> directories = objects.getCommonPrefixes();
		if (directories.isEmpty())
			return MessageData.NO_DATA_FOUND;
		return new ListData(directories);
	}

	public Data listFile(String bucketName, String prefix) {
		String delimiter = "/";
		if (!prefix.endsWith(delimiter)) {
			prefix += delimiter;
		}

		ListObjectsRequest listObjectsRequest = new ListObjectsRequest().withBucketName(bucketName).withPrefix(prefix)
				.withDelimiter(delimiter);
		ObjectListing objects = s3client.listObjects(listObjectsRequest);
		List<String> files = new ArrayList<String>();
		for (S3ObjectSummary objectSummary : objects.getObjectSummaries()) {
			files.add(objectSummary.getKey());
		}
		if (files.isEmpty())
			return MessageData.NO_DATA_FOUND;
		return new ListData(files);
	}

	private static String getBucketName(String path) {
		int firstSlash = path.indexOf("/");
		if (firstSlash < 0)
			return path;
		return path.substring(0, firstSlash);
	}

	private static String getFolderPath(String path) {
		int firstSlash = path.indexOf("/");
		if (firstSlash < 0)
			return "";
		String folder = path.substring(firstSlash + 1);
		if (!folder.endsWith("/"))
			folder = folder + "/";
		return folder;
	}
}
