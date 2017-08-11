package com.kahlua.data.util;

/**
 * Class holding metadata of columns including id, field name, column name, sequence and primary key. 
 * Created by TTCGOKCEL on 8/4/2017.
 * @author TTCGOKCEL
 */
public class DbColumnMetaData {

    private int id; 
    private String fieldName;
    private String columnName;
    private String sequence;
    private boolean primaryKey = false;

    public DbColumnMetaData(String fieldName, String columnName) {
        this.fieldName = fieldName;
        this.columnName = columnName;        
    }

    public DbColumnMetaData(String fieldName, String columnName, boolean primaryKey) {
        this(fieldName, columnName);
        this.primaryKey = primaryKey;
    }

    public DbColumnMetaData(String fieldName, String columnName, boolean primaryKey, String sequence) {
        this(fieldName, columnName, primaryKey);
        this.sequence = sequence;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public boolean isPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(boolean primaryKey) {
        this.primaryKey = primaryKey;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSequence() {
        return sequence;
    }

    public void setSequence(String sequence) {
        this.sequence = sequence;
    }

}

