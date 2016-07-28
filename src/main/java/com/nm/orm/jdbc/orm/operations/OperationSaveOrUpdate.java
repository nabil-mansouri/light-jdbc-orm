package com.nm.orm.jdbc.orm.operations;

import org.springframework.jdbc.core.JdbcTemplate;

import com.nm.orm.jdbc.insert.SimpleJdbcInsertAdapter;
import com.nm.orm.utils.JdbcOrmException;

/**
 * 
 * @author MANSOURI Nabil <nabil.mansouri.3@gmail.com>
 *
 */
public class OperationSaveOrUpdate<T> extends OperationAbstract<T> {
	private final T entity;
	private final SimpleJdbcInsertAdapter adapter;

	public OperationSaveOrUpdate(JdbcTemplate template, T entity, SimpleJdbcInsertAdapter adapter) {
		super(template);
		this.entity = entity;
		this.adapter = adapter;
	}

	public T operation() throws JdbcOrmException {
		T found = null;
		try {
			// TRY TO GET
			found = new OperationGetByExampleId<T>(jdbc, entity).operation();
		} catch (Exception e) {
			// NOTHING
		}
		if (found == null) {
			return new OperationInsert<T>(entity, adapter, jdbc).operation();
		} else {
			return new OperationUpdate<T>(entity, jdbc).operation();
		}
	}
}
