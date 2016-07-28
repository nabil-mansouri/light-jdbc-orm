package com.nm.orm.jdbc.orm.operations;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

import com.google.common.collect.Lists;
import com.nm.orm.jdbc.orm.JdbcOrmUtils;
import com.nm.orm.jdbc.select.RowMapperObject;
import com.nm.orm.utils.JdbcOrmException;

/**
 * 
 * @author MANSOURI Nabil <nabil.mansouri.3@gmail.com>
 *
 */
public class OperationGetByMapList<T> extends OperationAbstract<T> {
	private final Class<T> clazz;
	private final MapSqlParameterSource map;
	private List<?> objects;

	public OperationGetByMapList(JdbcTemplate template, Class<T> clazz, MapSqlParameterSource map) {
		super(template);
		this.clazz = clazz;
		this.map = map;
	}

	public T operation() throws JdbcOrmException {
		try {
			String fullTableName = JdbcOrmUtils.getFullTableName(clazz);
			// BUILD QUERY
			List<String> ands = Lists.newArrayList();
			for (String s : map.getValues().keySet()) {
				ands.add(String.format("%s = :%s", s, s));
			}
			String where = StringUtils.join(ands, " AND ");
			String sql = String.format("SELECT * FROM %s WHERE %s", fullTableName, where);
			objects = getTemplate().query(sql, map, new RowMapperObject<T>(clazz));
			return null;
		} catch (Exception e) {
			throw new JdbcOrmException(e);
		}
	}

	public List<?> getObjects() {
		return objects;
	}
}
