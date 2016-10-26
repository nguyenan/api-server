package com.wut.datasources.tablestore;

import java.util.List;
import java.util.Map;

public class SingleTableStore {

    private RelationalStore store;
    private String table;
    
    public SingleTableStore(RelationalStore store, String tableName) {
            this.store = store;
            this.table = tableName;
    }
    
    public String insert(Map<String,String> row) {
            return store.insertRow(table, row);
    }
    public boolean delete(String id) {
            return store.deleteRow(table, id);
    }
    public boolean update(String id, Map<String,String> row) {
            return store.updateRow(table, id, row);
    }
    public Map<String,String> retreive(String id) {
            return store.getRow(table, id);
    }
    public List<Map<String,String>> retreiveAll() {
            return store.getAllRows(table);
    }
    
    /**
     * First try to update. If update not success then insert data. Returns id on success.
     * @param id
     * @param row
     * @return
     */
    public String override(String id, Map<String,String> row) {
            if (store.updateRow(table, id, row)) {
                    return id;
            } else {
                    return store.insertRow(table, row);
            }
    }
}
