/*package user;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import com.wut.datasources.cassandra.CassandraSource;
import com.wut.model.Data;
import com.wut.model.list.ListData;
import com.wut.model.map.MappedData;
import com.wut.model.scalar.BooleanData;
import com.wut.model.scalar.IdData;
import com.wut.support.settings.SettingsManager;

import java.util.logging.FileHandler;
import java.util.logging.Logger;

public class DataDevTools {
	private static CassandraSource cassSource = new CassandraSource();
	private static final String applicationStr = "core";
	private static final IdData application = new IdData(applicationStr);
	//
	private static final IdData tableId = IdData.create("flat2");

	private static Logger logger = Logger.getLogger("CustomerDevTools");
	private static FileHandler fh;

	public static void main(String[] agrs) throws InterruptedException, SecurityException, IOException {
		fh = new FileHandler("/data/scripts/devlog/customer_" + System.currentTimeMillis());
		logger.addHandler(fh);
		fh.setFormatter(new LogFormatter());
		String customerId = "www.mapiii.com";
		String table = String.format("core:%s:public:%s", customerId, "blog");
//		ListData allRows = cassSource.getAllRows(new IdData(customerId), application, tableId);
//		List<MappedData> list = allRows.toList();
//		for (MappedData item : list) {
//			IdData rowId = new IdData(item.get("id").toString());
//			System.out.println(rowId + "\t"+ item);
//		}
		MappedData filter = new MappedData();
		filter.put("table", table);
		ListData rowsWithFilter = cassSource.getRowsWithFilter(new IdData(customerId), application, tableId, filter);
		List<MappedData> list = rowsWithFilter.toList();
		for (MappedData item : list) {
			IdData rowId = new IdData(item.get("id").toString());
			System.out.println(rowId + "\t"+ item);
		}
		System.exit(0);
	}

}
*/