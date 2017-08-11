package com.kahlua.data.repository.interfaces;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by TTCGOKCEL on 8/4/2017.
 * @author TTCGOKCEL
 */
public interface IKahluaRepository <T> {
    void insert(T t) throws SQLException;
    void updateById(T t) throws SQLException;
    void deleteById(long id) throws SQLException;
    //List<T> findAll() throws SQLException;
    T findById(long id) throws SQLException;
    //long count() throws SQLException;
    //void deleteAll() throws SQLException;
    //int updateAll() throws SQLException;
}
