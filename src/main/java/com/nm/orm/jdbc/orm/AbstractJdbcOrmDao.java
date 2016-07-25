package com.nm.orm.jdbc.orm;

import java.util.Collection;

import javax.persistence.Table;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import com.google.common.collect.Sets;
import com.nm.orm.jdbc.insert.SimpleJdbcInsertAdapter;
import com.nm.orm.jdbc.orm.operations.AssociationListFilterStrategy;
import com.nm.orm.jdbc.orm.operations.AssociationTypeFilterStrategy;
import com.nm.orm.jdbc.orm.operations.AssociationTypeFilterStrategyTarget;
import com.nm.orm.jdbc.orm.operations.AssociationUnattachedStrategy;
import com.nm.orm.jdbc.orm.operations.OperationCleanUnAttached;
import com.nm.orm.jdbc.orm.operations.OperationCleanUnPersisted;
import com.nm.orm.jdbc.orm.operations.OperationDelete;
import com.nm.orm.jdbc.orm.operations.OperationGetByExample;
import com.nm.orm.jdbc.orm.operations.OperationGetByExampleId;
import com.nm.orm.jdbc.orm.operations.OperationGetById;
import com.nm.orm.jdbc.orm.operations.OperationGetByIdList;
import com.nm.orm.jdbc.orm.operations.OperationGetByMap;
import com.nm.orm.jdbc.orm.operations.OperationGetUniq;
import com.nm.orm.jdbc.orm.operations.OperationInsert;
import com.nm.orm.jdbc.orm.operations.OperationRefresh;
import com.nm.orm.jdbc.orm.operations.OperationSaveOrUpdate;
import com.nm.orm.jdbc.orm.operations.OperationSaveUniq;
import com.nm.orm.jdbc.orm.operations.OperationUpdate;
import com.nm.orm.jdbc.select.RowMapperObject;
import com.nm.orm.utils.JdbcOrmException;

/**
 * 
 * @author MANSOURI Nabil <nabil.mansouri.3@gmail.com>
 *
 */
public abstract class AbstractJdbcOrmDao {
	private NamedParameterJdbcTemplate namedTemplate;

	public abstract void setJdbcTemplate(JdbcTemplate j);

	public abstract JdbcTemplate getJdbcTemplate();

	public <T> Collection<Object> cleanUnpersisted(final T o) throws JdbcOrmException {
		OperationCleanUnPersisted<T> op = new OperationCleanUnPersisted<T>(o, getJdbcTemplate());
		op.operation();
		return op.getUnpersisted();
	}

	public <T> Collection<Object> cleanUnpersisted(final T o, Class<?>... filters) throws JdbcOrmException {
		OperationCleanUnPersisted<T> op = new OperationCleanUnPersisted<T>(o, getJdbcTemplate(),
				new AssociationTypeFilterStrategyTarget(filters));
		op.operation();
		return op.getUnpersisted();
	}

	public <T> Collection<Object> cleanUnattached(final T o) throws JdbcOrmException {
		OperationCleanUnAttached<T> op = new OperationCleanUnAttached<T>(o, getJdbcTemplate());
		op.operation();
		return op.getUnattached();
	}

	public <T> Collection<Object> cleanUnattached(final T o, AssociationTypeFilterStrategy filterType) throws JdbcOrmException {
		OperationCleanUnAttached<T> op = new OperationCleanUnAttached<T>(o, getJdbcTemplate(), filterType);
		op.operation();
		return op.getUnattached();
	}

	public <T> Collection<Object> cleanUnattached(final T o, AssociationTypeFilterStrategy filterType,
			AssociationUnattachedStrategy strategy) throws JdbcOrmException {
		OperationCleanUnAttached<T> op = new OperationCleanUnAttached<T>(o, getJdbcTemplate(), filterType, strategy);
		op.operation();
		return op.getUnattached();
	}

	public <T> Collection<Object> cleanUnattached(final T o, Class<?>... filters) throws JdbcOrmException {
		OperationCleanUnAttached<T> op = new OperationCleanUnAttached<T>(o, getJdbcTemplate(),
				new AssociationTypeFilterStrategyTarget(filters));
		op.operation();
		return op.getUnattached();
	}

	public <T> void delete(T o) throws JdbcOrmException {
		new OperationDelete<T>(getJdbcTemplate(), o).operation();
	}

	public final <T> Collection<T> findAll(Class<T> clazz) throws JdbcOrmException {
		try {
			Table table = clazz.getAnnotation(Table.class);
			String sql = String.format("SELECT * FROM %s", table.name());
			return getJdbcTemplate().query(sql, new RowMapperObject<T>(clazz));
		} catch (Exception e) {
			throw new JdbcOrmException(e);
		}
	}

	public <T> T get(Class<T> clazz, MapSqlParameterSource id) throws JdbcOrmException {
		return new OperationGetByMap<T>(getJdbcTemplate(), clazz, id).operation();
	}

	public <T> T get(Class<T> clazz, Object id) throws JdbcOrmException {
		return new OperationGetById<T>(getJdbcTemplate(), clazz, id).operation();
	}

	public <T> Collection<T> getByIds(Class<T> clazz, Collection<?> id) throws JdbcOrmException {
		if (id.isEmpty())
			return Sets.newHashSet();
		OperationGetByIdList<T> op = new OperationGetByIdList<T>(getJdbcTemplate(), clazz, id);
		op.operation();
		return op.getResults();
	}

	public <T> T getByExample(T example) throws JdbcOrmException {
		return new OperationGetByExample<T>(getJdbcTemplate(), example).operation();
	}

	public <T> T getByExampleId(T example) throws JdbcOrmException {
		return new OperationGetByExampleId<T>(getJdbcTemplate(), example).operation();
	}

	public NamedParameterJdbcTemplate getNamedTemplate() {
		if (namedTemplate == null) {
			return new NamedParameterJdbcTemplate(getJdbcTemplate());
		}
		return namedTemplate;
	}

	public <T> T getUniq(T o) throws JdbcOrmException {
		return new OperationGetUniq<T>(getJdbcTemplate(), o).operation();
	}

	public <T> void insert(T o) throws JdbcOrmException {
		new OperationInsert<T>(o, insertAdapter(), getJdbcTemplate()).operation();
	}

	protected SimpleJdbcInsertAdapter insertAdapter() {
		return new SimpleJdbcInsertAdapter() {

			public SimpleJdbcInsert build(JdbcTemplate template) throws Exception {
				return new SimpleJdbcInsert(template);
			}
		};
	}

	public <T> void refresh(final T o) throws JdbcOrmException {
		new OperationRefresh<T>(o, getJdbcTemplate()).operation();
	}

	public <T> void refresh(final T o, final AssociationListFilterStrategy s1, AssociationTypeFilterStrategy s2) throws JdbcOrmException {
		new OperationRefresh<T>(o, getJdbcTemplate(), s1, s2).operation();
	}

	public <T> void refresh(final T o, Class<?>... filters) throws JdbcOrmException {
		new OperationRefresh<T>(o, getJdbcTemplate(), null, new AssociationTypeFilterStrategyTarget(filters)).operation();
	}

	public <T> void refresh(final Collection<T> o, Class<?>... filters) throws JdbcOrmException {
		for (T oo : o) {
			refresh(oo, filters);
		}
	}

	public <T> void saveOrUpdate(T o) throws JdbcOrmException {
		new OperationSaveOrUpdate<T>(getJdbcTemplate(), o, insertAdapter()).operation();
	}

	public <T> void saveOrUpdateUniq(T o) throws JdbcOrmException {
		new OperationSaveUniq<T>(o, insertAdapter(), getJdbcTemplate()).operation();
	}

	public <T> void update(Collection<T> o) throws JdbcOrmException {
		for (T oo : o) {
			update(oo);
		}
	}

	public <T> void update(T o) throws JdbcOrmException {
		new OperationUpdate<T>(o, getJdbcTemplate()).operation();
	}

	public <T> void update(T o, boolean ignoreNull) throws JdbcOrmException {
		new OperationUpdate<T>(o, getJdbcTemplate(), ignoreNull).operation();
	}
}
