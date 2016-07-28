package com.nm.orm.jdbc.orm.operations;

import java.util.List;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.util.Assert;

import com.google.common.collect.Lists;
import com.nm.orm.jdbc.meta.MetaInformationAssociation;
import com.nm.orm.jdbc.meta.MetaRepository;
import com.nm.orm.jdbc.orm.JdbcOrmUtils;
import com.nm.orm.utils.JdbcOrmException;

/**
 * 
 * @author MANSOURI Nabil <nabil.mansouri.3@gmail.com>
 *
 */
public class OperationDeleteAssociation<T> extends OperationAbstract<T> {
	private final T entity;
	private final AssociationTypeFilterStrategy filter;

	public OperationDeleteAssociation(JdbcTemplate template, T entity) {
		this(template, entity, null);
	}

	public OperationDeleteAssociation(JdbcTemplate template, T entity, AssociationTypeFilterStrategy filter) {
		super(template);
		this.entity = entity;
		this.filter = filter;
	}

	public T operation() throws JdbcOrmException {
		try {

			for (MetaInformationAssociation assoc : MetaRepository.getOrCreate(entity).getAssociationsCollection()) {
				if (filter == null || filter.filter(assoc)) {
					final MapSqlParameterSource all = assoc.getMap(entity);
					String table = JdbcOrmUtils.getFullTableName(assoc.getTarget());
					Assert.isTrue(all.getValues().size() > 0, "Restricted must be not empty (delete all)");
					// QUERY
					List<String> ands = Lists.newArrayList();
					for (Entry<String, Object> entry : all.getValues().entrySet()) {
						Assert.notNull(entry.getValue(), "Delete restriction cannot be null");
						ands.add(String.format("%s = :%s", entry.getKey(), entry.getKey()));
					}
					String where = StringUtils.join(ands, " AND ");
					final String sql = String.format("DELETE FROM %s WHERE %s", table, where);
					//
					getTemplate().update(sql, all);
				}
			}
			return entity;
		} catch (Exception e) {
			throw new JdbcOrmException(e);
		}
	}
}
