package user;

import java.io.IOException;
import java.util.logging.Logger;

import com.wut.model.list.ListData;
import com.wut.model.map.MappedData;

public class CopyData extends MigrationModel {
	private static Logger logger = Logger.getLogger("ProductDevTools");

	public static void main(String[] agrs) throws InterruptedException, SecurityException, IOException {
		setLogFormat();

		String fromCustomerId = "www.cluckandtrowel.com";
		String toCustomerId = "l1s181e18a2c1656476090b058256c409b6c";
		// blog
		// page
		// userImage
		// menu order category badge customer shipMethod contact email tag emailFrom contactList
		copyData(fromCustomerId, toCustomerId, "lead"); 
		System.exit(0);
	}

	private static void copyData(String fromCustomerId, String toCustomerId, String tablename) throws IOException {

		ListData listData = getListData(fromCustomerId, tablename);
		for (Object obj : listData) {
			MappedData mappedData = (MappedData) obj;
			// copy product
			String rowId = mappedData.get("id").toString();
			String realdId = rowId.substring(rowId.lastIndexOf(":") + 1, rowId.length());
			createNewData(toCustomerId, tablename, realdId, mappedData);
		}
	}
}