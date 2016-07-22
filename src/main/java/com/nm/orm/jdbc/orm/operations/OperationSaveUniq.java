package com.nm.orm.jdbc.orm.operations;

import java.lang.reflect.Field;

import org.springframework.jdbc.core.JdbcTemplate;

import com.nm.orm.jdbc.insert.SimpleJdbcInsertAdapter;
import com.nm.orm.jdbc.meta.ColumnFilter;
import com.nm.orm.jdbc.meta.MetaInformation;
import com.nm.orm.jdbc.meta.MetaRepository;
import com.nm.orm.utils.JdbcOrmException;
import com.nm.orm.utils.ReflectionUtils;

/**
 * 
 * @author MANSOURI Nabil <nabil.mansouri.3@gmail.com>
 *
 */
public class OperationSaveUniq<T> extends OperationAbstract<T> {
	private final T entity;
	private final OperationSaveOrUpdate<T> insert;
	private final OperationUpdate<T> update;
	private final OperationGetUniq<T> uniq;

	public OperationSaveUniq(T entity, SimpleJdbcInsertAdapter adapter, JdbcTemplate template) {
		super(template);
		this.entity = entity;
		insert = new OperationSaveOrUpdate<T>(template, entity, adapter);
		update = new OperationUpdate<T>(entity, template);
		uniq = new OperationGetUniq<T>(template, entity);
	}

	public T operation() throws JdbcOrmException {
		try {
			MetaInformation meta = MetaRepository.getOrCreate(entity);
			Field[] idFields = meta.getFields(ColumnFilter.onlyIds());
			// TRY TO GET THEN UPDATE
			T original = uniq.operation();
			for (Field f : idFields) {
				ReflectionUtils.transfer(f, original, entity);
			}
			return update.operation();
		} catch (Exception e) {
			// IF FAILED => INSERT (OR UPDATE IF ID EXISTS)
			return insert.operation();
		}
	}
}
