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
		try {
			// TRY TO GET THEN UPDATE
			new OperationGetByExample<T>(jdbc, entity).operation();
			return new OperationUpdate<T>(entity, jdbc).operation();
		} catch (Exception e) {
			// IF FAILED => INSERT
			return new OperationInsert<T>(entity, adapter, jdbc).operation();
		}
	}
}
