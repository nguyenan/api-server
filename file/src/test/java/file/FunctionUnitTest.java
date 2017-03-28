package file;
import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import org.junit.Test;

import com.wut.datasources.aws.S3FileSource;
import com.wut.support.binary.Base64;
import com.wut.support.stream.StreamUtil;

public class FunctionUnitTest {

	@Test
	public void testGetBucketNotExist() throws UnsupportedEncodingException {
		String bucket = "dev1";
		String folder = "init.txt";
		String filename = "init.txt";
		S3FileSource source = new S3FileSource();
		InputStream file = source.getFile(bucket, folder, filename);
		assertNull(file);
	}

	@Test
	public void testGetFileNotExist() throws UnsupportedEncodingException {
		String bucket = "dev1.tend.ag";
		String folder = "css";
		String filename = "dev1";
		S3FileSource source = new S3FileSource();
		InputStream file = source.getFile(bucket, folder, filename);
		assertNull(file);
	}

	@Test
	public void testGetFileNoFolder() throws IOException {
		String bucket = "dev1.tend.ag";
		String folder = null;
		String filename = "init.txt";
		S3FileSource source = new S3FileSource();
		InputStream inputStream = source.getFile(bucket, folder, filename);
		assertNotNull(inputStream);
		assertEquals("Test content", streamToString(inputStream));
	}

	@Test
	public void testGetFile() throws UnsupportedEncodingException {
		String bucket = "dev1.tend.ag";
		String folder = "css";
		String filename = "theme1of2.css";
		S3FileSource source = new S3FileSource();
		InputStream file = source.getFile(bucket, folder, filename);
		assertNotNull(file);
	}

	@Test
	public void testUpdateFile() throws IOException {
		String bucket = "dev1.tend.ag";
		String folder = "test";
		String filename = "testUpdate_" + System.currentTimeMillis();
		String data = "test Update on " + bucket;
		
		S3FileSource source = new S3FileSource();
		byte[] encodeBase64 = Base64.encodeBase64(data.getBytes("UTF-8"));
		InputStream fileData = new ByteArrayInputStream(encodeBase64);
		boolean updateFile = source.updateFile(bucket, folder, filename, fileData);
		assertTrue(updateFile);
		
		InputStream file = source.getFile(bucket, folder, filename);
		String streamToString = StreamUtil.getStringFromInputStream(file);
		assertEquals(streamToString, data);
	}
	
//	@Test - pending util DeleteBucket function available
//	public void testUpdateFileNewBucket() throws IOException {
//		String bucket = "test" + System.currentTimeMillis();
//		String folder = "test";
//		String filename = "testUpdate_" + System.currentTimeMillis();
//		String data = "test Update on " + bucket;
//		
//		S3FileSource source = new S3FileSource();
//		byte[] encodeBase64 = Base64.encodeBase64(data.getBytes("UTF-8"));
//		InputStream fileData = new ByteArrayInputStream(encodeBase64);
//		boolean updateFile = source.updateFile(bucket, folder, filename, fileData);
//		assertTrue(updateFile);
//		
//		InputStream file = source.getFile(bucket, folder, filename);
//		String streamToString = StreamUtil.getStringFromInputStream(file);
//		assertEquals(streamToString, data);
//	}
	
	@Test
	public void testDeleteFileBucketNotExist() throws IOException {
		String bucket = "test";
		String folder = "test";
		String filename = "testDelete_" + System.currentTimeMillis();		
		
		S3FileSource source = new S3FileSource();
		boolean deleteFile = source.deleteFile(bucket, folder, filename);
		assertFalse(deleteFile);
	}
	
	@Test
	public void testDeleteFileNotExist() throws IOException {
		String bucket = "dev1.tend.ag";
		String folder = "test";
		String filename = "testDelete_" + System.currentTimeMillis();		
		
		S3FileSource source = new S3FileSource();
		boolean deleteFile = source.deleteFile(bucket, folder, filename);
		assertTrue(deleteFile);
	}
	
	@Test
	public void testDeleteFile() throws IOException {
		String bucket = "dev1.tend.ag";
		String folder = "test";
		String filename = "testDelete_" + System.currentTimeMillis();
		String data = "test delete on " + bucket;
		byte[] encodeBase64 = Base64.encodeBase64(data.getBytes("UTF-8"));
		InputStream fileData = new ByteArrayInputStream(encodeBase64);
		S3FileSource source = new S3FileSource();
		
		// create file first
		boolean updateFile = source.updateFile(bucket, folder, filename, fileData);
		assertTrue(updateFile);
		
		// make sure file existed		
		InputStream file = source.getFile(bucket, folder, filename);
		String streamToString = StreamUtil.getStringFromInputStream(file);
		assertEquals(streamToString, data);
		
		boolean deleteFile = source.deleteFile(bucket, folder, filename);
		assertTrue(deleteFile);
		
		// verify file was deleted		
		file = source.getFile(bucket, folder, filename);
		assertNull(file);				
	}

	private String streamToString(InputStream inputStream) throws IOException {
		ByteArrayOutputStream result = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int length;
		while ((length = inputStream.read(buffer)) != -1) {
			result.write(buffer, 0, length);
		}
		return result.toString("UTF-8");
	}
}
