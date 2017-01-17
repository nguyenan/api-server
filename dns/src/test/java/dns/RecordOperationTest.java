package dns;

import static org.junit.Assert.*;

import org.junit.Test;

import com.wut.datasources.cloudflare.CFSource;
import com.wut.model.Data;
import com.wut.model.map.MappedData;
import com.wut.model.map.MessageData;

public class RecordOperationTest {
	static String domainName = "mapiii.com";

	@Test
	public void createRecordTesting() {
		long currentTimeMillis = System.currentTimeMillis();
		String recordName = "admin" + currentTimeMillis;
		CFSource cfSource = new CFSource();
		
		// Create new record
		MessageData createRecord = (MessageData) cfSource.createRecord(domainName, recordName,
				String.format("www.%s.%s.s3-us-west-2.amazonaws.com", domainName, recordName));
		assertEquals(createRecord.get("message").toString(), createRecord, MessageData.SUCCESS);

		// Verify Record details
		Data recordDetails = cfSource.getRecordDetails(domainName, recordName);
		assertEquals(recordDetails.toString(), recordDetails.getClass().getSimpleName(), "MappedData");

		MappedData data = (MappedData) recordDetails;
		assertEquals("Wrong recordName", data.get("name").toString(), recordName + "." + domainName);
	}
	
	// TODO *update* and *read* Record unit test

}
