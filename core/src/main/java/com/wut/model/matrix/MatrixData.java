package com.wut.model.matrix;

import java.util.ArrayList;
import java.util.List;

import com.wut.model.AbstractData;
import com.wut.model.Data;
import com.wut.model.Model;

public class MatrixData extends AbstractData {
	@SuppressWarnings("rawtypes")
	private ArrayList<ArrayList> data = new ArrayList<ArrayList>();

	public Object getDatum(int row, int col) {
		if (row < data.size() - 1) {
			@SuppressWarnings("rawtypes")
			ArrayList list = data.get(row);
			if (list != null && col < list.size() - 1) {
				return list.get(col);
			}
		}
		return null;
	}

	public int getNumberRows() {
		return data.size();
	}

	public int getNumberCols() {
		return data.get(0) != null ? data.get(0).size() : 0;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void setDatum(int row, int col, Object datum) {
		while (row > data.size() - 1) {
			data.add(new ArrayList());
		}
		ArrayList list = data.get(row);
		while (col > list.size() - 1) {
			list.add(new ArrayList());
		}
		list.set(col, datum);
	}
	
	@SuppressWarnings("rawtypes")
	public List<? extends List> getData() {
		return data;
	}

	@Override
	public Model<? extends Data> getModel() {
		// TODO Auto-generated method stub
		return null;
	}

}
