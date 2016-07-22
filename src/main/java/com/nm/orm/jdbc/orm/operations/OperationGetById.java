package com.nm.orm.jdbc.orm.operations;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.JdbcTemplate;
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
public class OperationGetById<T> extends OperationAbstract<T> {
	private final Class<T> clazz;
	private final Object id;

	public OperationGetById(JdbcTemplate template, Class<T> clazz, Object id) {
		super(template);
		this.clazz = clazz;
		this.id = id;
	}

	public T operation() throws JdbcOrmException {
		try {
			final String[] idName = MetaRepository.getOrCreate(clazz).getNames(clazz, ColumnFilter.onlyIds());
			String fullTableName = JdbcOrmUtils.getFullTableName(clazz);
			Assert.isTrue(idName.length > 0, "MUST HAVE IDS authorized");
			// BUILD QUERY
			List<String> ands = Lists.newArrayList();
			for (String s : idName) {
				ands.add(String.format("%s = :%s", s, s));
			}
			String where = StringUtils.join(ands, " AND ");
			String sql = String.format("SELECT * FROM %s WHERE %s", fullTableName, where);
			return getJdbc().queryForObject(sql, new RowMapperObject<T>(clazz), id);
		} catch (Exception e) {
			throw new JdbcOrmException(e);
		}
	}
}
