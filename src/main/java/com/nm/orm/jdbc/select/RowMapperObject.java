package com.nm.orm.jdbc.select;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.persistence.Column;

import org.springframework.jdbc.core.RowMapper;

import com.google.common.collect.Maps;
import com.nm.orm.utils.BeanAnnotationProperty;
import com.nm.orm.utils.ReflectionUtils;

/**
 * 
 * @author MANSOURI Nabil <nabil.mansouri.3@gmail.com>
 *
 * @param <T>
 */
public class RowMapperObject<T> implements RowMapper<T> {
	private final Class<T> clazz;
	private Map<String, BeanAnnotationProperty<Column>> fieldBySql = Maps.newHashMap();

	public RowMapperObject(Class<T> clazz) throws Exception {
		this.clazz = clazz;
		build();
	}

	private void build() throws Exception {
		List<BeanAnnotationProperty<Column>> result = ReflectionUtils.findAnnotationProperty(clazz, Column.class);
		fieldBySql = result.stream().collect(Collectors.toMap(u -> u.getAnnotation().name(), u -> u));
	}

	public T mapRow(ResultSet rs, int rowNum) throws SQLException {
		try {
			T obj = clazz.newInstance();
			ResultSetMetaData rsmd = rs.getMetaData();
			int columnCount = rsmd.getColumnCount();
			for (int i = 1; i <= columnCount; i++) {
				String name = rsmd.getColumnName(i);
				ReflectionUtils.setValue(fieldBySql.get(name), obj, rs.getObject(name));
			}
			return obj;
		} catch (Exception e) {
			throw new SQLException(e);
		}
	}

}
