package com.kahlua.data.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.kahlua.data.orm.DbColumn;
import com.kahlua.data.orm.DbTable;

/**
 * Build query strings to be used to operate in sql 
 * and cache the queries which are already built.
 * Created by TTCGOKCEL on 8/4/2017.
 * @author TTCGOKCEL
 */

public class KahluaQueryBuilder {

    private static final KahluaQueryBuilder kahluaQueryBuilder = new KahluaQueryBuilder();

    //Store db table metadata and entity relation
    private Map<String, DbTableMetaData> tables = Collections.synchronizedMap(new HashMap<String, DbTableMetaData>());

    //Store sql statements
    private Map<String, String> sqlStatements = Collections.synchronizedMap(new HashMap<String, String>());

    private KahluaQueryBuilder() {
    }

    /**
     * @return Instance of query builder
     */ 
    public static KahluaQueryBuilder getInstance() {
        return kahluaQueryBuilder;
    }

    /**
     * @return Name of the class as the table key
     */ 
    private String getTableKey(final Class clazz) {
        return clazz.getName();
    }
    
    

    /**
     * Get table key and either gets the meta data (column names) from tables 
     * or store it in tables.
     * @param clazz To be used to fetch class name
     * @return Table meta data
     */ 
    
    public DbTableMetaData getTableMetaData(final Class clazz) {
        String key = getTableKey(clazz);
        DbTableMetaData tableMetaData = null;
        if (tables.containsKey(key)) {
            tableMetaData = tables.get(key);
        } else {
            tableMetaData = generateTableMetaData(clazz);
            tables.put(key, tableMetaData);
        }
        return tableMetaData;
    }

    /**
     * Fetch methods and fields of class to generate columns of table. 
     * @param clazz
     * @return Table meta data
     */
    private DbTableMetaData generateTableMetaData(final Class<?> clazz) {
        DbTable dbTable = (DbTable) clazz.getAnnotation(DbTable.class);
        DbTableMetaData table = new DbTableMetaData(clazz.getName(), dbTable.schema(), dbTable.name());
        
        Method[] methods = clazz.getMethods();
        for (Method method : methods) {
            if (method.isAnnotationPresent(DbColumn.class)) {
                DbColumn dbColumn = (DbColumn) method.getAnnotation(DbColumn.class);
                String fieldName = Character.toLowerCase(method.getName().charAt(3)) + method.getName().substring(4);
                DbColumnMetaData column = new DbColumnMetaData(fieldName, dbColumn.name(), dbColumn.idColumn(),
                        dbColumn.sequence());
                table.getColumns().add(column);
            }
        }
        return table;
    }

    /**
     * @param tableMetaData
     * @return Select query
     */
    public String createSelectStatement(DbTableMetaData tableMetaData) {
        StringBuilder statementBuilder = new StringBuilder();
        statementBuilder.append("SELECT ");
        statementBuilder.append(tableMetaData.getAllColumnNameList());
        statementBuilder.append(" FROM ");
        statementBuilder.append(tableMetaData.getFullName());
        return statementBuilder.toString();
    }

    /**
     * @param tableMetaData
     * @return Create query
     */
    public String createInsertStatement(final DbTableMetaData tableMetaData) {
        StringBuilder builder = new StringBuilder();
        builder.append("INSERT INTO ");
        builder.append(tableMetaData.getFullName()).append(" (");
        builder.append(tableMetaData.getAllColumnNameList());
        builder.append(") VALUES (");
        builder.append(tableMetaData.getAllFieldNameList(true));
        builder.append(")");
        return builder.toString();
    }

    /**
     * @param tableMetaData, pkColumn
     * @return Update query
     */
    public String createUpdateStatement(final DbTableMetaData tableMetaData, boolean pkColumn) {

        StringBuilder builder = new StringBuilder();
        builder.append("UPDATE ");
        builder.append(tableMetaData.getFullName());
        builder.append(" SET ");
        appendColumnsMapping(tableMetaData, builder, false, false);
        if (pkColumn) {
            builder.append(" WHERE ");
            appendColumnsMapping(tableMetaData, builder, pkColumn, true);
        }
        return builder.toString();
    }

    /**
     * @param tableMetaData, pkColumn
     * @return Delete query
     */
    public String createDeleteStatement(final DbTableMetaData tableMetaData) {
        StringBuilder builder = new StringBuilder();
        builder.append("DELETE FROM ");
        builder.append(tableMetaData.getFullName());
        builder.append(" WHERE ");
        appendColumnsMapping(tableMetaData, builder, true, true);
        return builder.toString();
    }
    
    public String createDeleteAllStatement(final DbTableMetaData tableMetaData) {
        StringBuilder builder = new StringBuilder();
        builder.append("TRUNCATE TABLE ");
        builder.append(tableMetaData.getFullName());
        return builder.toString();
    }


    /**
     * @param tableMetaData
     * @return Count query
     */
    public String createCountStatement(final DbTableMetaData tableMetaData) {
        StringBuilder builder = new StringBuilder();
        
        String tableName = tableMetaData.getFullName();
        builder.append("SELECT ");
        builder.append("COUNT(*)");
        builder.append(" FROM ");
        builder.append(tableName);
        return builder.toString();
    }

    /**
     * @param tableMetaData
     * @return Find by id query
     */
    public String createFindByIdStatement(final DbTableMetaData tableMetaData) {
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT ");
        builder.append(tableMetaData.getAllColumnNameList());
        builder.append(" FROM ");
        builder.append(tableMetaData.getFullName());
        builder.append(" WHERE ");
        appendColumnsMapping(tableMetaData, builder, true, true);
        return builder.toString();
    }

    /**
     * Generic helper function to build the queries.
     * @param table, builder, onlyPkColumns, isWhereClause
     * @return Select query
     */
    public void appendColumnsMapping(final DbTableMetaData table, StringBuilder builder, final boolean onlyPkColumns,
                                     boolean isWhereClause) {
        boolean check = false;
        List<DbColumnMetaData> columns = table.getColumns();
        for (int i = 0; i < columns.size(); i++) {
            DbColumnMetaData column = columns.get(i);
            if (!(column.isPrimaryKey() ^ onlyPkColumns)) {
                if (check) {
                    if (isWhereClause) {
                        builder.append(" and ");
                    } else {
                        builder.append(',');
                    }
                }
                builder.append(column.getColumnName()).append(" = :").append(column.getFieldName());
                check = true;
            }
        }
    }

}
