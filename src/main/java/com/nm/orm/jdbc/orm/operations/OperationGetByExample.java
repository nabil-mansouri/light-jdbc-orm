package com.nm.orm.jdbc.orm.operations;

import java.util.List;

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
	private OperationGetByMapList<T> operation;

	public OperationGetByExample(JdbcTemplate template, T example) {
		super(template);
		this.example = example;
	}

	@SuppressWarnings("unchecked")
	public T operation() throws JdbcOrmException {
		try {
			final MapSqlParameterSource toRestrict = MetaRepository.getOrCreate(example).getMap(example, ColumnFilter.onlyNonNull());
			Class<T> clazz = (Class<T>) example.getClass();
			operation = new OperationGetByMapList<T>(jdbc, clazz, toRestrict);
			return operation.operation();
		} catch (Exception e) {
			throw new JdbcOrmException(e);
		}
	}

	public List<?> getObjects() {
		return operation.getObjects();
	}

}
