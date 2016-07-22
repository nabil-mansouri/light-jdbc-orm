package com.nm.orm.jdbc.select;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Map;

import javax.persistence.Column;

import org.springframework.jdbc.core.RowMapper;

import com.google.common.collect.Maps;
import com.nm.orm.utils.ReflectionUtils;

/**
 * 
 * @author MANSOURI Nabil <nabil.mansouri.3@gmail.com>
 *
 * @param <T>
 */
public class RowMapperObject<T> implements RowMapper<T> {
	private final Class<T> clazz;
	private Map<String, String> fieldBySql = Maps.newHashMap();
	private Map<String, String> sqlByFields = Maps.newHashMap();

	public RowMapperObject(Class<T> clazz) throws Exception {
		this.clazz = clazz;
		build();
	}

	private void build() throws Exception {
		for (Field f : ReflectionUtils.getAllFieldsRecursive(clazz)) {
			// COLUMN
			Column column = ReflectionUtils.getAnnotation(f, Column.class);
			if (column != null) {
				sqlByFields.put(f.getName(), column.name());
				fieldBySql.put(column.name(), f.getName());
			}
		}
	}

	public T mapRow(ResultSet rs, int rowNum) throws SQLException {
		try {
			T obj = clazz.newInstance();
			ResultSetMetaData rsmd = rs.getMetaData();
			int columnCount = rsmd.getColumnCount();
			for (int i = 1; i <= columnCount; i++) {
				String name = rsmd.getColumnName(i);
				ReflectionUtils.setRecursively(obj, fieldBySql.get(name), rs.getObject(name));
			}
			return obj;
		} catch (Exception e) {
			throw new SQLException(e);
		}
	}

}
