package com.nm.orm.jdbc.orm.operations;

import javax.persistence.Table;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.util.Assert;

import com.google.common.base.Strings;
import com.nm.orm.jdbc.meta.ColumnFilter;
import com.nm.orm.jdbc.meta.MetaInformation;
import com.nm.orm.jdbc.meta.MetaRepository;
import com.nm.orm.jdbc.orm.listeners.UpdateListener;
import com.nm.orm.jdbc.update.SimpleJdbcUpdate;
import com.nm.orm.utils.JdbcOrmException;

/**
 * 
 * @author MANSOURI Nabil <nabil.mansouri.3@gmail.com>
 *
 */
public class OperationUpdate<T> extends OperationAbstract<T> {
	private boolean ignoreNull;
	private final T entity;
	private final UpdateListener<T> listeners;

	public OperationUpdate(T entity, JdbcTemplate template, UpdateListener<T> listeners) {
		this(entity, template, false, listeners);
	}

	public OperationUpdate(T entity, JdbcTemplate template, boolean ignoreNull, UpdateListener<T> listener) {
		super(template);
		this.entity = entity;
		this.ignoreNull = ignoreNull;
		this.listeners = listener;
	}

	public T operation() throws JdbcOrmException {
		try {
			if (listeners != null) {
				listeners.beforeUpdate(entity);
			}
			Table table = entity.getClass().getAnnotation(Table.class);
			//
			MetaInformation meta = MetaRepository.getOrCreate(entity);
			ColumnFilter filterUpdate = ColumnFilter.onlyNonIds().setOnlyNonNull(ignoreNull).setOnlyUpdatable(true);
			MapSqlParameterSource toUpdate = meta.getMap(entity, filterUpdate);
			String[] updated = meta.getNames(entity, filterUpdate);
			MapSqlParameterSource toRestrict = meta.getMap(entity, ColumnFilter.onlyIds());
			String[] restricted = meta.getNames(entity, ColumnFilter.onlyIds());
			Assert.isTrue(restricted.length > 0, "Restricted must be not empty (update all)");
			// BUILD QUERY
			SimpleJdbcUpdate update = new SimpleJdbcUpdate(getJdbc());
			if (!Strings.isNullOrEmpty(table.name())) {
				update.withTableName(table.name());
			}
			if (!Strings.isNullOrEmpty(table.catalog())) {
				update.withCatalogName(table.catalog());
			}
			if (!Strings.isNullOrEmpty(table.schema())) {
				update.withSchemaName(table.schema());
			}
			update.restrictingColumns(restricted);
			update.updatingColumns(updated);
			update.execute(toUpdate, toRestrict);
			if (listeners != null) {
				listeners.afterUpdate(entity);
			}
			return entity;
		} catch (Exception e) {
			throw new JdbcOrmException(e);
		}
	}
}
