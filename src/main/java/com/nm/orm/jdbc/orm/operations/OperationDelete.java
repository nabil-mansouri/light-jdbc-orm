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
import com.nm.orm.utils.JdbcOrmException;

/**
 * 
 * @author MANSOURI Nabil <nabil.mansouri.3@gmail.com>
 *
 */
public class OperationDelete<T> extends OperationAbstract<T> {
	private final T entity;

	public OperationDelete(JdbcTemplate template, T entity) {
		super(template);
		this.entity = entity;
	}

	public T operation() throws JdbcOrmException {
		try {
			final MapSqlParameterSource all = MetaRepository.getOrCreate(entity).getMap(entity, ColumnFilter.onlyIds());
			String tableName = JdbcOrmUtils.getFullTableName(entity);
			Assert.isTrue(all.getValues().size() > 0, "Restricted must be not empty (delete all)");
			// QUERY
			List<String> ands = Lists.newArrayList();
			for (String s : all.getValues().keySet()) {
				Assert.notNull(all.getValues().get(s), "Update restriction cannot be null");
				ands.add(String.format("%s = :%s", s, s));
			}
			String where = StringUtils.join(ands, " AND ");
			final String sql = String.format("DELETE FROM %s WHERE %s", tableName, where);
			//
			getTemplate().update(sql, all);
			return entity;
		} catch (Exception e) {
			throw new JdbcOrmException(e);
		}
	}
}
