package com.nm.orm.jdbc.orm.operations;

import java.lang.reflect.Field;

import javax.persistence.Table;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.util.Assert;

import com.google.common.base.Strings;
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
public class OperationInsert<T> extends OperationAbstract<T> {
	private final SimpleJdbcInsertAdapter adapter;
	private final T entity;

	public OperationInsert(T entity, JdbcTemplate template) {
		super(template);
		this.entity = entity;
		adapter = new SimpleJdbcInsertAdapter() {

			public SimpleJdbcInsert build(JdbcTemplate template) throws Exception {
				return new SimpleJdbcInsert(template);
			}
		};
	}

	public OperationInsert(T entity, SimpleJdbcInsertAdapter adapter, JdbcTemplate template) {
		super(template);
		this.entity = entity;
		this.adapter = adapter;
	}

	public T operation() throws JdbcOrmException {
		try {
			Table table = entity.getClass().getAnnotation(Table.class);
			// BUILD QUERY
			SimpleJdbcInsert insert = adapter.build(getJdbc());
			if (!Strings.isNullOrEmpty(table.name())) {
				insert.withTableName(table.name());
			}
			if (!Strings.isNullOrEmpty(table.catalog())) {
				insert.withCatalogName(table.catalog());
			}
			if (!Strings.isNullOrEmpty(table.schema())) {
				insert.withSchemaName(table.schema());
			}
			//
			MetaInformation meta = MetaRepository.getOrCreate(entity);
			String[] generated = meta.getNames(entity, ColumnFilter.onlyGenerated());
			if (generated.length > 0) {
				MapSqlParameterSource toUpdate = meta.getMap(entity, ColumnFilter.onlyNonIds().setOnlyNonNull(true));
				String[] updated = meta.getNames(entity, ColumnFilter.onlyNonIds().setOnlyNonNull(true));
				String[] restricted = meta.getNames(entity, ColumnFilter.onlyIds());
				Field[] idField = meta.getFields(ColumnFilter.onlyIds());
				insert.usingColumns(updated);
				insert.usingGeneratedKeyColumns(restricted);
				Assert.isTrue(generated.length == 1, "Only one generated column");
				Assert.isTrue(restricted.length == 1, "Only one id column");
				KeyHolder id = insert.executeAndReturnKeyHolder(toUpdate);
				ReflectionUtils.set(entity, idField[0], id.getKey());
			} else {
				MapSqlParameterSource toUpdate = meta.getMap(entity, ColumnFilter.onlyNonNull());
				String[] updated = meta.getNames(entity, ColumnFilter.onlyNonNull());
				insert.usingColumns(updated);
				Assert.isTrue(updated.length > 0, "Must have columns");
				insert.execute(toUpdate);
			}
			return entity;
		} catch (Exception e) {
			throw new JdbcOrmException(e);
		}
	}
}
