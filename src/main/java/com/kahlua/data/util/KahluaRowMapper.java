/*

package com.kahlua.data.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.ColumnMapRowMapper;

public class KahluaRowMapper<T> {

	public List mapRows(Class clazz) {

	    List<T> list = new ArrayList<T>();

	    ColumnMapRowMapper mapper = new ColumnMapRowMapper();

	    try {
	                    List<Map<String, Object>> result = (List<Map<String, Object>>) getSimpleJdbcTemplate().query(sql,
	                                                    mapper);

	                    for (Map<String, Object> map : result) {
	                                    List<Field> fields = new ArrayList<Field>();
	                                    for (Field field : clazz.getDeclaredFields()) {
	                                                    fields.add(field);
	                                    }
	                                    if (clazz.getSuperclass() != null) {
	                                                    for (Field field : clazz.getSuperclass().getDeclaredFields()) {
	                                                                    fields.add(field);
	                                                    }
	                                    }
	                                    for (Field field : fields) {
	                                                    String fieldName = field.getName();

	                                                    if (fieldName.startsWith("serialVersion"))
	                                                                    continue;

	                                                    String columnName = QueryFactory.getInstance().getColumnName(clazz, fieldName);
	                                                    Object value = map.get(columnName);
	                                                    map.remove(columnName);
	                                                    if (value != null)
	                                                                    map.put(fieldName, value);
	                                    }

	                                    Object entity = Class.forName(clazz.getCanonicalName()).newInstance();
	                                    org.springframework.beans.BeanUtils.copyProperties(entity, map);
	                                    BeanUtils.copyProperties(entity, map);
	                                    list.add((T) entity);
	                    }
	    } catch (Exception e) {
	                    logger.error(e.getMessage());
	    }
	    return list;
	}
}

*/


