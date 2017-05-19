package storage;

import static org.junit.Assert.*;

import org.junit.Test;

import com.wut.datasources.cassandra.CassandraSource;
import com.wut.model.Data;
import com.wut.model.map.MappedData;
import com.wut.model.map.MessageData;
import com.wut.model.scalar.IdData;
import com.wut.provider.table.DefaultTableProvider;
import com.wut.provider.table.FlatTableProvider;
import com.wut.provider.table.TableProvider;
import com.wut.provider.table.TableResourceProvider;
import com.wut.provider.table.UniqueRowProvider;

public class TestCassandra {

	private static final IdData customer = new IdData("www.mapiii.com");
	private static final IdData application = new IdData("core");
	private static final IdData user = new IdData("public");
	// new IdData(

	private static TableResourceProvider provider = getProvider();

	public static TableProvider getTableProvider() {
		CassandraSource CassandraDbSource = new CassandraSource();
		DefaultTableProvider defaultTableProvider = new DefaultTableProvider(CassandraDbSource);
		FlatTableProvider flatTableProvider = new FlatTableProvider(defaultTableProvider, "flat2");
		TableProvider uniqueRowProvider = new UniqueRowProvider(flatTableProvider);
		return uniqueRowProvider;
	}

	public static TableResourceProvider getProvider() {
		TableProvider provider = getTableProvider();
		TableResourceProvider tableResourceProvider = new TableResourceProvider(provider);
		return tableResourceProvider;
	}

	@Test
	public void testUpdate() {
		IdData productId = new IdData("tomatoes");
		String oldName = "Tomatoes" + System.currentTimeMillis();
		String newName = "Potatoes" + System.currentTimeMillis();
		MappedData product = new MappedData();
		IdData tableName = new IdData("product");
		product.put("notTaxed", "false");
		product.put("_deleted_", "false");
		product.put("subscription", "false");
		product.put("description", "This is description");
		product.put("tags", "[\\\"vegetable\\\"]");
		product.put("enabled", "true");
		product.put("name", oldName);
		product.put("updated", "1490858583056");

		Data update = provider.crupdate(application, customer, user, tableName, productId, product);
		product.put("name", newName);
		update = provider.update(application, customer, user, tableName, productId, product);
		assertEquals(MessageData.SUCCESS, update);

		MappedData read = (MappedData) provider.read(application, customer, user, tableName, productId, null);
		assertEquals(newName, read.get("name").toString());
		provider.delete(application, customer, user, tableName, productId);
	}

	@Test
	public void testCRUpdate() {
		IdData productId = new IdData("tomatoess");
		String name = "Tomatoes" + System.currentTimeMillis();
		MappedData product = new MappedData();
		IdData tableName = new IdData("product");
		product.put("notTaxed", "false");
		product.put("_deleted_", "false");
		product.put("subscription", "false");
		product.put("description", "This is description");
		product.put("tags", "[\\\"vegetable\\\"]");
		product.put("enabled", "true");
		product.put("name", name);
		product.put("updated", "1490858583056");

		Data update = provider.crupdate(application, customer, user, tableName, productId, product);
		assertEquals(MessageData.SUCCESS, update);
		MappedData read = (MappedData) provider.read(application, customer, user, tableName, productId, null);
		assertEquals(name, read.get("name").toString());
		provider.delete(application, customer, user, tableName, productId);
	}

	@Test
	public void testDelete() {
		IdData productId = new IdData("tomatoess");
		String name = "Tomatoes" + System.currentTimeMillis();
		MappedData product = new MappedData();
		IdData tableName = new IdData("product");
		product.put("notTaxed", "false");
		product.put("_deleted_", "false");
		product.put("subscription", "false");
		product.put("description", "This is  description");
		product.put("tags", "[\\\"vegetable\\\"]");
		product.put("enabled", "true");
		product.put("name", name);
		product.put("updated", "1490858583056");

		Data update = provider.crupdate(application, customer, user, tableName, productId, product);
		assertEquals(MessageData.SUCCESS, update);
		MappedData read = (MappedData) provider.read(application, customer, user, tableName, productId, null);
		assertNotEquals(MessageData.NO_DATA_FOUND, read);
		provider.delete(application, customer, user, tableName, productId);
		MappedData readAgain = (MappedData) provider.read(application, customer, user, tableName, productId, null);
		assertEquals(MessageData.NO_DATA_FOUND, readAgain);
	}

}
