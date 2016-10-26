package com.wut.datasources.google;
/*
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;

import com.google.gdata.client.spreadsheet.SpreadsheetService;
import com.google.gdata.data.Link;
import com.google.gdata.data.PlainTextConstruct;
import com.google.gdata.data.batch.BatchOperationType;
import com.google.gdata.data.batch.BatchUtils;
import com.google.gdata.data.spreadsheet.CellEntry;
import com.google.gdata.data.spreadsheet.CellFeed;
import com.google.gdata.data.spreadsheet.ListEntry;
import com.google.gdata.data.spreadsheet.ListFeed;
import com.google.gdata.data.spreadsheet.SpreadsheetEntry;
import com.google.gdata.data.spreadsheet.SpreadsheetFeed;
import com.google.gdata.data.spreadsheet.WorksheetEntry;
import com.google.gdata.util.AuthenticationException;
import com.google.gdata.util.ServiceException;
import com.wut.datasources.MultiSource;
import com.wut.model.AbstractData;
import com.wut.model.Data;
import com.wut.model.list.ListData;
import com.wut.model.map.MappedData;
import com.wut.model.scalar.BooleanData;
import com.wut.model.scalar.IdData;
import com.wut.model.scalar.ScalarData;
import com.wut.support.ErrorHandler;
import com.wut.support.UniqueIdGenerator;

public class GoogleSpreadsheetSource extends MultiSource {
	private SpreadsheetService service = new SpreadsheetService("WebUtilityKit-v1");

	// Define the URL to request. This should never change.
	private URL SPREADSHEET_FEED_URL;

//	
//	 * A basic struct to store cell row/column information and the associated
//	 * RnCn identifier.
//	
//	private static class CellAddress {
//		public final int row;
//		public final int col;
//		public final String idString;
//
//	
//		 * Constructs a CellAddress representing the specified {@code row} and
//		 * {@code col}. The idString will be set in 'RnCn' notation.
//		
//		public CellAddress(int row, int col) {
//			this.row = row;
//			this.col = col;
//			this.idString = String.format("R%sC%s", row, col);
//		}
//	}

	public GoogleSpreadsheetSource(String username, String password) {
		try {
			// TODO don't create this on every request -- make URL static
			SPREADSHEET_FEED_URL = new URL(
					"https://spreadsheets.google.com/feeds/spreadsheets/private/full");
			authenticate(username, password);
		} catch (MalformedURLException e) {
			ErrorHandler.systemError(e, "unable to start goolge spreadsheet source");
		}
	}

	private void authenticate(String username, String password) {
		try {
			service.setUserCredentials(username, password);
		} catch (AuthenticationException e) {
			ErrorHandler.systemError(e,
					"unable to authenticate goolge spreadsheet source");
		}
	}

	private void printAllSpreadSheets() throws IOException, ServiceException {
		// Make a request to the API and get all spreadsheets.
		SpreadsheetFeed feed = service.getFeed(SPREADSHEET_FEED_URL,
				SpreadsheetFeed.class);
		List<SpreadsheetEntry> spreadsheets = feed.getEntries();

		// Iterate through all of the spreadsheets returned
		for (SpreadsheetEntry spreadsheet : spreadsheets) {
			// Print the title of this spreadsheet to the screen
			System.out.println(spreadsheet.getTitle().getPlainText());
		}
	}

	private SpreadsheetEntry getSpreadSheet(String name) throws IOException,
			ServiceException {
		// Make a request to the API and get all spreadsheets.
		SpreadsheetFeed feed = service.getFeed(SPREADSHEET_FEED_URL,
				SpreadsheetFeed.class);
		List<SpreadsheetEntry> spreadsheets = feed.getEntries();

		if (spreadsheets.size() == 0) {
			return null;
		}

		SpreadsheetEntry requestedSpreadsheet = null;

		// Iterate through all of the spreadsheets returned
		for (SpreadsheetEntry spreadsheet : spreadsheets) {
			// Print the title of this spreadsheet to the screen
			// System.out.println(spreadsheet.getTitle().getPlainText());
			if (spreadsheet.getTitle().getPlainText().equalsIgnoreCase(name)) {
				requestedSpreadsheet = spreadsheet;
			}
		}

		return requestedSpreadsheet;
	}

	// TODO not useful
	private List<WorksheetEntry> getWorksheets(SpreadsheetEntry spreadsheet)
			throws IOException, ServiceException {
		// Make a request to the API to fetch information about all
		// worksheets in the spreadsheet.
		List<WorksheetEntry> worksheets = spreadsheet.getWorksheets();

		// Iterate through each worksheet in the spreadsheet.
		for (WorksheetEntry worksheet : worksheets) {
			// Get the worksheet's title, row count, and column count.
			String title = worksheet.getTitle().getPlainText();
			int rowCount = worksheet.getRowCount();
			int colCount = worksheet.getColCount();

			// Print the fetched information to the screen for this worksheet.
			System.out.println("\t" + title + "- rows:" + rowCount + " cols: "
					+ colCount);
		}

		return worksheets;
	}

	private WorksheetEntry getWorksheet(String spreadsheetName,
			String worksheetName) {
		SpreadsheetEntry spreadsheet = null;
		WorksheetEntry worksheet = null;
		try {
			spreadsheet = getSpreadSheet(spreadsheetName);
			worksheet = getWorksheet(spreadsheet, worksheetName);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return worksheet;
	}

	private WorksheetEntry getWorksheet(SpreadsheetEntry spreadsheet,
			String worksheetName) throws IOException, ServiceException {
		// Make a request to the API to fetch information about all
		// worksheets in the spreadsheet.
		List<WorksheetEntry> worksheets = spreadsheet.getWorksheets();

		// Iterate through each worksheet in the spreadsheet.
		for (WorksheetEntry worksheet : worksheets) {
			// Get the worksheet's title, row count, and column count.
			String title = worksheet.getTitle().getPlainText();
			int rowCount = worksheet.getRowCount();
			int colCount = worksheet.getColCount();

			// Print the fetched information to the screen for this worksheet.
			System.out.println("\t" + title + " - rows:" + rowCount + " cols: "
					+ colCount);

			if (title.equalsIgnoreCase(worksheetName)) {
				return worksheet;
			}
		}

		return null;
	}

	private WorksheetEntry createWorksheet(SpreadsheetEntry spreadsheet,
			String name, int cols, int rows) throws IOException,
			ServiceException {
		// Create a local representation of the new worksheet
		WorksheetEntry worksheet = new WorksheetEntry();
		worksheet.setTitle(new PlainTextConstruct(name));
		worksheet.setColCount(cols);
		worksheet.setRowCount(rows);

		// Send the local representation of the worksheet to the API for
		// creation. The URL to use here is the worksheet feed URL of our
		// spreadsheet.
		URL worksheetFeedUrl = spreadsheet.getWorksheetFeedUrl();
		service.insert(worksheetFeedUrl, worksheet);
		return worksheet;
	}

	private void updateWorksheet(WorksheetEntry worksheet, String name,
			int cols, int rows) throws IOException, ServiceException {
		// Update the local representation of the worksheet.
		worksheet.setTitle(new PlainTextConstruct("Updated Worksheet"));
		worksheet.setColCount(5);
		worksheet.setRowCount(15);

		// Send the local representation of the worksheet to the API for
		// modification.
		worksheet.update();
	}

	private ListData getWorksheetData(WorksheetEntry worksheet) {
		return getWorksheetData(worksheet, null);
	}

	private ListData getWorksheetData(WorksheetEntry worksheet,
			MappedData filter) {
		ListData allRowData = new ListData();

		// Fetch the list feed of the worksheet
		URL listFeedUrl = worksheet.getListFeedUrl();
		ListFeed listFeed = null;
		try {
			listFeed = service.getFeed(listFeedUrl, ListFeed.class);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

		// List<String> columns = new ArrayList<String>(); //new String[] {
		// "id", "name", "description", "price" };

		// ListEntry firstRow = listFeed.getEntries().get(0);
		// if (firstRow != null) {
		// //System.out.print("KEY:" + row.getTitle().getPlainText() + "\t");
		// Set<String> tagSet = firstRow.getCustomElements().getTags();
		// for (int i=0; i<tagSet.size(); i++) {
		// String tag = String.valueOf(tagSet.toArray()[i]);
		// columns.add(tag);
		// }
		// }

		// first row
		Set<String> columns = new TreeSet<String>();
		ListEntry firstRow = listFeed.getEntries().get(0);
		if (firstRow != null) {
			columns = firstRow.getCustomElements().getTags();
		}

		// Iterate through each row, printing its cell values
		for (ListEntry row : listFeed.getEntries()) {
			MappedData rowData = new MappedData();

			// Iterate over the remaining columns, and print each cell value
			Set<String> tagSet = row.getCustomElements().getTags();

			// System.out.println(" -- TAGS:" + tagSet);

			for (int i = 0; i < columns.size(); i++) {
				String columnName = String.valueOf(columns.toArray()[i]);
				String cellValue = row.getCustomElements().getValue(columnName);
				rowData.put(columnName, cellValue);
			}

			// System.out.println(rowData + " TAGS:" + tagSet);

			if (filter != null) {
				boolean isValidRow = true;
				for (ScalarData key : filter.keys()) {
					Data filterValue = filter.get(key);
					Data rowValue = rowData.get(key);
					if (!filterValue.equals(rowValue)) {
						isValidRow = false;
					}
				}
				if (isValidRow) {
					allRowData.add(rowData);
				}
			} else {
				allRowData.add(rowData);
			}
		}

		return allRowData;
	}

	// filters
	// // Based on the sample above for getting rows in a worksheet
	// URL listFeedUrl = new URI(worksheet.getListFeedUrl().toString()
	// + "?sq=age>25%20and%20height<175").toURL();

	private boolean insert(WorksheetEntry worksheet, MappedData data) {
		// Fetch the list feed of the worksheet.
		URL listFeedUrl = worksheet.getListFeedUrl();
		// ListFeed listFeed = service.getFeed(listFeedUrl, ListFeed.class);

		// Create a local representation of the new row.
		ListEntry row = new ListEntry();
		for (ScalarData key : data.getMap().keySet()) {
			Data d = data.get(key);
			row.getCustomElements().setValueLocal(key.toRawString(),
					d.toString());
		}
		// row.getCustomElements().setValueLocal("num", "Something" + new
		// Random().nextInt(300));
		row.getCustomElements().setValueLocal("updated",
				new Date().toGMTString());
		// row.getCustomElements().setValueLocal("age", "26");
		// row.getCustomElements().setValueLocal("height", "176");
		// Send the new row to the API for insertion.
		try {
			row = service.insert(listFeedUrl, row);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return true;
	}

	
	private boolean update(WorksheetEntry worksheet, IdData rowId, MappedData data) {

		// Fetch the list feed of the worksheet
		URL listFeedUrl = worksheet.getListFeedUrl();
		ListFeed listFeed = null;
		try {
			listFeed = service.getFeed(listFeedUrl, ListFeed.class);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		boolean foundRow = false;
		
		// Iterate through each row, printing its cell values
		for (ListEntry row : listFeed.getEntries()) {
			String idValue = row.getCustomElements().getValue("id");
			if (idValue.equals(rowId.toRawString())) {
				foundRow = true;
				for (ScalarData key : data.keys()) {
					Data value = data.get(key);
					row.getCustomElements().setValueLocal(key.toRawString(), AbstractData.getValue(value));
				}
			}
		}
		
		// TURN UPDATE INTO A CRUPDATE
		if (!foundRow) {
			data.put("id", rowId);
			boolean insertSuccess = insert(worksheet, data);
			return insertSuccess;
		}
		
		return false;
	}

	private void printCells(WorksheetEntry worksheet) throws IOException,
			ServiceException {
		// Fetch the cell feed of the worksheet
		URL cellFeedUrl = worksheet.getCellFeedUrl();
		CellFeed cellFeed = service.getFeed(cellFeedUrl, CellFeed.class);

		// Iterate through each cell, printing its value.
		for (CellEntry cell : cellFeed.getEntries()) {
			// Print the cell's address in A1 notation
			System.out.print(cell.getTitle().getPlainText() + "\t");
			// Print the cell's address in R1C1 notation
			System.out.print(cell.getId().substring(
					cell.getId().lastIndexOf('/') + 1)
					+ "\t");
			// Print the cell's formula or text value
			System.out.print(cell.getCell().getInputValue() + "\t");
			// Print the cell's calculated value if the cell's value is numeric
			// Prints empty string if cell's value is not numeric
			System.out.print(cell.getCell().getNumericValue() + "\t");
			// Print the cell's displayed value (useful if the cell has a
			// formula)
			System.out.println(cell.getCell().getValue() + "\t");
		}
	}

	private void play() throws IOException, ServiceException {
		printAllSpreadSheets();
		// SpreadsheetEntry basic = getSpreadSheet("basic");
		// List<WorksheetEntry> worksheets = getWorksheets(basic);
		// WorksheetEntry newWorksheet = createWorksheet(basic, "New Shhhheet",
		// 5,
		// 5);
		WorksheetEntry worksheet = getWorksheet("basic", "Something");
		MappedData filter = new MappedData();
		filter.put("num", "four");
		ListData d = getWorksheetData(worksheet, filter);
		System.out.println(d);
		// printCells(worksheet);

		MappedData newData = new MappedData();
		newData.put("name", "rawr");
		newData.put("val", String.valueOf(new Random().nextInt(300)));
		newData.put("ugg", "meh");
		insert(worksheet, newData);
		// updateWorksheet(worksheets.get(0), "NewName", 6, 6);
	}

//	
//	 * Connects to the specified {@link SpreadsheetService} and uses a batch
//	 * request to retrieve a {@link CellEntry} for each cell enumerated in
//	 * {@code cellAddrs}. Each cell entry is placed into a map keyed by its RnCn
//	 * identifier.
//	 * 
//	 * @param ssSvc
//	 *            the spreadsheet service to use.
//	 * @param cellFeedUrl
//	 *            url of the cell feed.
//	 * @param cellAddrs
//	 *            list of cell addresses to be retrieved.
//	 * @return a map consisting of one {@link CellEntry} for each address in
//	 *         {@code cellAddrs}
//	 
	public static Map<String, CellEntry> getCellEntryMap(
			SpreadsheetService ssSvc, URL cellFeedUrl,
			List<CellAddress> cellAddrs) throws IOException, ServiceException {
		CellFeed batchRequest = new CellFeed();
		for (CellAddress cellId : cellAddrs) {
			CellEntry batchEntry = new CellEntry(cellId.row, cellId.col,
					cellId.idString);
			batchEntry.setId(String.format("%s/%s", cellFeedUrl.toString(),
					cellId.idString));
			BatchUtils.setBatchId(batchEntry, cellId.idString);
			BatchUtils.setBatchOperationType(batchEntry,
					BatchOperationType.QUERY);
			batchRequest.getEntries().add(batchEntry);
		}

		CellFeed cellFeed = ssSvc.getFeed(cellFeedUrl, CellFeed.class);
		CellFeed queryBatchResponse = ssSvc.batch(
				new URL(cellFeed.getLink(Link.Rel.FEED_BATCH, Link.Type.ATOM)
						.getHref()), batchRequest);

		Map<String, CellEntry> cellEntryMap = new HashMap<String, CellEntry>(
				cellAddrs.size());
		for (CellEntry entry : queryBatchResponse.getEntries()) {
			cellEntryMap.put(BatchUtils.getBatchId(entry), entry);
			System.out.printf(
					"batch %s {CellEntry: id=%s editLink=%s inputValue=%s\n",
					BatchUtils.getBatchId(entry), entry.getId(), entry
							.getEditLink().getHref(), entry.getCell()
							.getInputValue());
		}

		return cellEntryMap;
	}

	public static void main(String[] args) throws AuthenticationException,
			MalformedURLException, IOException, ServiceException {
		GoogleSpreadsheetSource gSpread = new GoogleSpreadsheetSource("fake.palmiter@gmail.com", "REPLACE_ME");
		gSpread.play();
		System.out.println("Done.");
	}

	@Override
	public ListData getAllRows(IdData tableId) {
		WorksheetEntry worksheet = getWorksheet("basic", tableId.toRawString());
		ListData allRows = getWorksheetData(worksheet);
		return allRows;
	}

	@Override
	public ListData getRowsWithFilter(IdData tableId, MappedData filter) {
		WorksheetEntry worksheet = getWorksheet("basic", tableId.toRawString());
		ListData filteredRows = getWorksheetData(worksheet, filter);
		return filteredRows;
	}

	@Override
	public MappedData getRow(IdData tableId, IdData rowId) {
		WorksheetEntry worksheet = getWorksheet("basic", tableId.toRawString());
		MappedData filter = new MappedData();
		filter.put("id", rowId);
		ListData allRows = getWorksheetData(worksheet, filter);
		MappedData row = null;
		if (allRows.size() > 0) {
			row = (MappedData) allRows.get(0);
		}
		return row;
	}

	@Override
	public BooleanData deleteRow(IdData tableId, IdData rowId) {
		System.out.println("SPREADSHEET DELETE " + tableId + " " + rowId);

		// TODO Auto-generated method stub
		//throw new RuntimeException("delete not supported");
		
		return BooleanData.FALSE;
	}

	@Override
	public BooleanData crupdateRow(IdData tableId, IdData rowId, MappedData data) {
		System.out.println("SPREADSHEET UPDATE " + tableId + " " + rowId);
		
		WorksheetEntry worksheet = getWorksheet("basic", tableId.toRawString());
		boolean updateSuccess = update(worksheet, rowId, data);
		
		return updateSuccess ? BooleanData.TRUE : BooleanData.FALSE;
	}

	@Override
	public IdData insertRow(IdData tableId, MappedData data) {
		System.out.println("SPREADSHEET INSERT " + tableId);

		IdData newId = new IdData(UniqueIdGenerator.getId());
		IdData rowId = new IdData(getRowScope(tableId, newId));

		data.put("id", rowId);
		
		WorksheetEntry worksheet = getWorksheet("basic", tableId.toRawString());
		boolean insertSuccess = insert(worksheet, data);

		return insertSuccess ? rowId : null;
	}

	private String getRowScope(IdData tableId, IdData rowId) {
		return tableId.toRawString() + ":" + rowId.toRawString();
	}
}
*/