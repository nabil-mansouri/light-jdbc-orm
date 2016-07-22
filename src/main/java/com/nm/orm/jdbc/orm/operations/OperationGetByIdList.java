package com.nm.orm.jdbc.orm.operations;

import java.util.Collection;
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
public class OperationGetByIdList<T> extends OperationAbstract<T> {
	private final Class<T> clazz;
	private final Collection<?> ids;
	private Collection<T> results = Lists.newArrayList();

	public OperationGetByIdList(JdbcTemplate template, Class<T> clazz, Collection<?> ids) {
		super(template);
		this.clazz = clazz;
		this.ids = ids;
	}

	public T operation() throws JdbcOrmException {
		try {
			results.clear();
			String fullTableName = JdbcOrmUtils.getFullTableName(clazz);
			final String[] idName = MetaRepository.getOrCreate(clazz).getNames(clazz, ColumnFilter.onlyIds());
			Assert.isTrue(idName.length > 0, "MUST HAVE IDS authorized");
			Assert.isTrue(ids.size() > 0, "MUST HAVE IDS values");
			// BUILD QUERY
			List<String> ands = Lists.newArrayList();
			MapSqlParameterSource source = new MapSqlParameterSource();
			for (String s : idName) {
				ands.add(String.format("%s IN (%s)", s, StringUtils.join(ids, ",")));
			}
			String where = StringUtils.join(ands, " AND ");
			String sql = String.format("SELECT * FROM %s WHERE %s", fullTableName, where);
			results.addAll(getJdbc().query(sql, new RowMapperObject<T>(clazz), source));
			return null;
		} catch (Exception e) {
			throw new JdbcOrmException(e);
		}
	}

	public Collection<T> getResults() {
		return results;
	}
}
