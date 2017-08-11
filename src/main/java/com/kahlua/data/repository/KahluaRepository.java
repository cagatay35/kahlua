package com.kahlua.data.repository;

import java.lang.reflect.ParameterizedType;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.asm.Type;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import com.kahlua.data.KahluaDataSource;
import com.kahlua.data.query.DbOperationType;
import com.kahlua.data.repository.interfaces.IKahluaRepository;
import com.kahlua.data.util.DbTableMetaData;
import com.kahlua.data.util.KahluaQueryBuilder;

/**
 * Abstract class for CRUD operations.
 * Created by TTCGOKCEL on 8/4/2017.
 * @author TTCGOKCEL
 */

public abstract class KahluaRepository<T> extends JdbcDaoSupport implements IKahluaRepository<T> {
	
	
    private KahluaDataSource kahluaDataSource;
    
    //TODO: BUNLARI TEST CLASSINDA HALLET.
	private NamedParameterJdbcTemplate jdbcTemplate;

    public KahluaRepository(DataSource ds) {
        kahluaDataSource = new KahluaDataSource(ds);
        jdbcTemplate = new NamedParameterJdbcTemplate(kahluaDataSource.getDataSource());
    }
    
    

    
    public KahluaRepository() {
    }
    
    public Class<T> getGenericType() {
        ParameterizedType parameterizedType = (ParameterizedType) getClass().getGenericSuperclass();

        return (Class<T>) parameterizedType.getActualTypeArguments()[0];
    }



    /**
     * @params sequenceName
     * @return Next sequence value 
     */
    public long getSequenceNextValue(String sequenceName) {
        String sql = "SELECT " + sequenceName + ".NEXTVAL FROM DUAL";
        Long id = jdbcTemplate.queryForObject(sql, new MapSqlParameterSource(), Long.class);
        return id;
    }
    
    
    public void resetSequenceValue(String sequenceName){
    	String sql = "DROP SEQUENCE " + sequenceName;
		jdbcTemplate.update(sql, new MapSqlParameterSource());
        sql = "CREATE SEQUENCE " + sequenceName + " START WITH 1 INCREMENT BY 1";
        jdbcTemplate.update(sql, new MapSqlParameterSource());

    	
    }

	public int updateAll() throws SQLException{
		return jdbcTemplate.update(generateSQL(DbOperationType.UPDATE_ALL), new MapSqlParameterSource());

	}

	public void insert(T t) throws SQLException {
		jdbcTemplate.update(generateSQL(DbOperationType.INSERT), getParameterSource(t));
	}

	public void deleteById(long id) throws SQLException {
		jdbcTemplate.update(generateSQL(DbOperationType.DELETE_BY_ID), new MapSqlParameterSource("id", id));
	}

	public void updateById(T t) throws SQLException {
		jdbcTemplate.update(generateSQL(DbOperationType.UPDATE_BY_ID), getParameterSource(t));


	}

	public List<T> findAll() throws SQLException {
		return jdbcTemplate.query(generateSQL(DbOperationType.SELECT), new BeanPropertyRowMapper<T>(getGenericType()));
	}

	public T findById(long id) throws SQLException {
		return jdbcTemplate.queryForObject(generateSQL(DbOperationType.FIND_BY_ID), new MapSqlParameterSource("id", id), new BeanPropertyRowMapper<T>(getGenericType()));
	}

	
	public long count() throws SQLException {		
		return jdbcTemplate.queryForObject(generateSQL(DbOperationType.COUNT), new MapSqlParameterSource(), Long.class);

	}
	
	public void deleteAll() throws SQLException {
		jdbcTemplate.update(generateSQL(DbOperationType.DELETE_ALL), new MapSqlParameterSource());

	}

	public boolean exists(long id) throws SQLException {
		return findById(id)!=null;
	}
	


    /**
     * Encapsulate query builder functions.
     * @params class, DbOperationType
     * @return Query string to be sent to the db 
     */
	
	private String generateSQL(final DbOperationType DbOperationType) {
		return generateSQL(getGenericType(), DbOperationType);
    }
	
    private String generateSQL(final Class clazz, final DbOperationType DbOperationType) {
        String retval = null;
        final DbTableMetaData tableMetaData = KahluaQueryBuilder.getInstance().getTableMetaData(clazz);
        if (DbOperationType.SELECT.equals(DbOperationType)) {
            retval = KahluaQueryBuilder.getInstance().createSelectStatement(tableMetaData);
        } else if (DbOperationType.COUNT.equals(DbOperationType)) {
            retval = KahluaQueryBuilder.getInstance().createCountStatement(tableMetaData);
        } else if (DbOperationType.FIND_BY_ID.equals(DbOperationType) || DbOperationType.EXISTS.equals(DbOperationType)) {
            retval = KahluaQueryBuilder.getInstance().createFindByIdStatement(tableMetaData);
        } else if (DbOperationType.INSERT.equals(DbOperationType)) {
            retval = KahluaQueryBuilder.getInstance().createInsertStatement(tableMetaData);
        } else if (DbOperationType.UPDATE_BY_ID.equals(DbOperationType)) {
            retval = KahluaQueryBuilder.getInstance().createUpdateStatement(tableMetaData, true);
        } else if (DbOperationType.UPDATE_ALL.equals(DbOperationType)) {
            retval = KahluaQueryBuilder.getInstance().createUpdateStatement(tableMetaData, false);
        } else if (DbOperationType.DELETE_BY_ID.equals(DbOperationType)) {
            retval = KahluaQueryBuilder.getInstance().createDeleteStatement(tableMetaData);
        } else if (DbOperationType.DELETE_ALL.equals(DbOperationType)) {
            retval = KahluaQueryBuilder.getInstance().createDeleteAllStatement(tableMetaData);
        } 
        return retval;
    }
    
    private SqlParameterSource getParameterSource(T entity) {
    	return new BeanPropertySqlParameterSource(entity);
    }
}
