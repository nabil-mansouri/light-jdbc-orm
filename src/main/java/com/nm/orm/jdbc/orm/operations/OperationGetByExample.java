package com.nm.orm.jdbc.orm.operations;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

import com.nm.orm.jdbc.meta.ColumnFilter;
import com.nm.orm.jdbc.meta.MetaRepository;
import com.nm.orm.utils.JdbcOrmException;

/**
 * 
 * @author MANSOURI Nabil <nabil.mansouri.3@gmail.com>
 *
 */
public class OperationGetByExample<T> extends OperationAbstract<T> {
	private final T example;

	public OperationGetByExample(JdbcTemplate template, T example) {
		super(template);
		this.example = example;
	}

	@SuppressWarnings("unchecked")
	public T operation() throws JdbcOrmException {
		try {
			final MapSqlParameterSource toRestrict = MetaRepository.getOrCreate(example).getMap(example, ColumnFilter.onlyIds());
			Class<T> clazz = (Class<T>) example.getClass();
			return (T) new OperationGetByMap<T>(jdbc, clazz, toRestrict).operation();
		} catch (Exception e) {
			throw new JdbcOrmException(e);
		}
	}
}
