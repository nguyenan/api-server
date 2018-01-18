
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;

import com.wut.backgroundjob.payment.BackgroundJobResource;
import com.wut.backgroundjob.payment.RenewTokenJob;
import com.wut.datasources.square.SquareSource;
import com.wut.model.Data;
import com.wut.model.map.MappedData;
import com.wut.model.map.MessageData;
import com.wut.support.settings.SettingsManager;

public class SquareTest {
	public static void main(String[] args) {
		String customer = "www.mapiii.com";
		System.out.println(SettingsManager.getClientSettings(customer, SquareSource.ACCESS_TOKEN_SETTING, true));
		//System.out.println(BackgroundJobResource.getTodayJob());
		System.out.println(BackgroundJobResource.getNextJob(6));
	}
 
	public void multiplicationOfZeroIntegersShouldReturnZero() {
		String customer = "www.mapiii.com";
		String oldToken = SettingsManager.getClientSettings(customer, SquareSource.ACCESS_TOKEN_SETTING, true);

		// oldToken = "sq0atp-IihUTm8E-g-kF6-vR1TeiA";
		BackgroundJobResource.pushRenewTokenJob(customer, oldToken);

		MappedData job1 = (MappedData) BackgroundJobResource.getNextJobWithToken(oldToken);
		assertNotSame("New Background job not pushed yet", MessageData.NO_DATA_FOUND, job1);

		RenewTokenJob renewTokenJob = new RenewTokenJob();
		renewTokenJob.run();

		// validate setting
		String newToken = SettingsManager.getClientSettings(customer, SquareSource.ACCESS_TOKEN_SETTING, true);
		assertFalse("token not changed " + oldToken + "-" + newToken, oldToken.equals(newToken));

		// validate old background job
		Data oldJob = BackgroundJobResource.getTodayJobWithToken(oldToken);
		assertEquals("Old Background job not cleaned", MessageData.NO_DATA_FOUND, oldJob);

		// validate new background job
		MappedData job = (MappedData) BackgroundJobResource.getNextJobWithToken(newToken);
		assertNotSame("New Background job not updated", MessageData.NO_DATA_FOUND, job);
		assertEquals("New Background job not updated", job.get("id").toString(), newToken);
		assertEquals("New Background job not updated", job.get("customerId").toString(), customer);

	}
}
