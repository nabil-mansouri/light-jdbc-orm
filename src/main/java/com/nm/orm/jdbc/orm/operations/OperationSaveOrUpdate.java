package com.nm.orm.jdbc.orm.operations;

import org.springframework.jdbc.core.JdbcTemplate;

import com.nm.orm.jdbc.insert.SimpleJdbcInsertAdapter;
import com.nm.orm.jdbc.orm.listeners.SaveUpdateListener;
import com.nm.orm.utils.JdbcOrmException;

/**
 * 
 * @author MANSOURI Nabil <nabil.mansouri.3@gmail.com>
 *
 */
public class OperationSaveOrUpdate<T> extends OperationAbstract<T> {
	private final T entity;
	private final SimpleJdbcInsertAdapter adapter;
	private final SaveUpdateListener<T> listeners;

	public OperationSaveOrUpdate(JdbcTemplate template, T entity, SimpleJdbcInsertAdapter adapter, SaveUpdateListener<T> l) {
		super(template);
		this.entity = entity;
		this.adapter = adapter;
		this.listeners = l;
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
			return new OperationInsert<T>(entity, adapter, jdbc, listeners).operation();
		} else {
			return new OperationUpdate<T>(entity, jdbc, listeners).operation();
		}
	}
}
