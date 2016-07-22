package com.nm.orm.jdbc.orm.operations;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.util.Assert;

import com.google.common.collect.Lists;
import com.nm.orm.jdbc.meta.ColumnFilter;
import com.nm.orm.jdbc.meta.MetaRepository;
import com.nm.orm.jdbc.orm.JdbcOrmUtils;
import com.nm.orm.jdbc.select.RowMapperObject;
import com.nm.orm.utils.JdbcOrmException;

/**
 * 
 * @author MANSOURI Nabil <nabil.mansouri.3@gmail.com>
 *
 */
public class OperationGetUniq<T> extends OperationAbstract<T> {
	private final T entity;

	public OperationGetUniq(JdbcTemplate template, T entity) {
		super(template);
		this.entity = entity;
	}

	public T operation() throws JdbcOrmException {
		try {
			@SuppressWarnings("unchecked")
			Class<T> clazz = (Class<T>) entity.getClass();
			String fullTableName = JdbcOrmUtils.getFullTableName(clazz);
			final String[] idName = MetaRepository.getOrCreate(clazz).getNames(entity, ColumnFilter.onlyInUniqGroup());
			MapSqlParameterSource id = MetaRepository.getOrCreate(clazz).getMap(entity, ColumnFilter.onlyInUniqGroup());
			Assert.isTrue(idName.length > 0, "MUST HAVE IDS authorized");
			// BUILD QUERY
			List<String> ands = Lists.newArrayList();
			for (String s : idName) {
				ands.add(String.format("%s = :%s", s, s));
			}
			String where = StringUtils.join(ands, " AND ");
			String sql = String.format("SELECT * FROM %s WHERE %s", fullTableName, where);
			return getTemplate().queryForObject(sql, id, new RowMapperObject<T>(clazz));
		} catch (Exception e) {
			throw new JdbcOrmException(e);
		}
	}
}
