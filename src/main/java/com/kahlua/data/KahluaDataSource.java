package com.kahlua.data;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

/**
 * Set the data source as the data source for this library.
 * Created by TTCGOKCEL on 8/4/2017.
 * @author TTCGOKCEL
 */
public class KahluaDataSource {

    private DataSource dataSource;
    private static Connection connection;

    public KahluaDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
        try {
			connection = dataSource.getConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		}
    }

    /** 
     * @return Data source
     */ 
    public DataSource getDataSource() {
        return dataSource;
    }
    
    public Connection getConnection(){
    	return connection;
    }
    
}
