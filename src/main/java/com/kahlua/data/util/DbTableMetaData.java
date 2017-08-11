package com.kahlua.data.util;

import java.util.ArrayList;
import java.util.List;

/**
 * Hold schema name, class name and table name as table meta data for a table.
 * Created by TTCGOKCEL on 8/4/2017.
 * @author TTCGOKCEL
 */
public class DbTableMetaData {

    private String className;
    private String tableName;
    private String schemaName;
    private List<DbColumnMetaData> columns;

    public DbTableMetaData(String className, String schemaName, String tableName) {
        this.className = className;
        this.schemaName = schemaName;
        this.tableName = tableName;
        
    }
    public DbTableMetaData(String className, String schemaName, String tableName, List<DbColumnMetaData> columns) {
        this(className, schemaName, tableName);
        this.columns = columns;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getSchemaName() {
        return schemaName;
    }

    public void setSchemaName(String schemaName) {
        this.schemaName = schemaName;
    }

    public List<DbColumnMetaData> getColumns() {
        if (columns == null) {
            columns = new ArrayList<DbColumnMetaData>();
        }
        return columns;
    }

    /**
     * Get schema_name.table_name for a table to use in queries.
     * @return Full table name
     */
    public String getFullName() {
        if (schemaName == null || schemaName.trim().equals("")) {
            return tableName;
        } else {
            return schemaName + "." + tableName;
        }

    }

    /**
     * Get string of column names seperated by commas for a table to use in queries.
     * @params ignorePkColumns Boolean value to ignore or not to ignore 
     * PK column names. 
     * @return Columns as string 
     */
    public String getColumnNameList(boolean ignorePkColumns) {
        StringBuilder sb = new StringBuilder();
        boolean check = false;
        for (int i = 0; i < columns.size(); i++) {
            DbColumnMetaData column = columns.get(i);
            if (!ignorePkColumns || !column.isPrimaryKey()) {
                if (check)
                    sb.append(',');
                sb.append(column.getColumnName());
                check = true;
            }
        }
        return sb.toString();
    }

    /**
     * Get the column name list without ignoring PK columns.
     * @return All the columns
     */
    public String getAllColumnNameList() {
        return getColumnNameList(false);
    }

    /**
     * Get the field name list.
     * @params ignorePkColumns Determine whether to ignore or not to ignore 
     * PK column names. 
     * 		   useSequenceField Determine whether to use or not to use the sequence field. 
     * @return Field name list as string
     */
    public String getFieldNameList(boolean ignorePkColumns, boolean useSequenceField) {
        StringBuilder sb = new StringBuilder();
        boolean check = false;
        for (int i = 0; i < columns.size(); i++) {
            DbColumnMetaData column = columns.get(i);
            if (!ignorePkColumns || !column.isPrimaryKey()) {
                if (check)
                    sb.append(',');
                if (!column.getSequence().equals("") && useSequenceField) {
                    sb.append(column.getSequence()).append(".NEXTVAL");
                } else {
                    sb.append(':').append(column.getFieldName());
                }
                check = true;

            }
        }
       
        return sb.toString();
    }

    /**
     * Get the field name list without ignoring PK columns.
     * @params useSequenceField
     * @return All the field names
     */
    public String getAllFieldNameList(boolean useSequenceField) {
        return getFieldNameList(false, useSequenceField);
    }

    /**
     * Get column name corresponding to the field name of the object.
     * @params fieldName
     * @return Column name or null
     */
    public String getColumnName(String fieldName) {
        for (DbColumnMetaData column : columns) {
            if (column.getFieldName().equals(fieldName))
                return column.getColumnName();
        }
        return null;
    }

    /**
     * Get the PK column's name. 
     * @return Column name or null
     */
    public String getPrimaryKey() {
        for (DbColumnMetaData column : columns) {
            if (column.isPrimaryKey()) {
                return column.getColumnName();
            }
        }
        return null;
    }

}