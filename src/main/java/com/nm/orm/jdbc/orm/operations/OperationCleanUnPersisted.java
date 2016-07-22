package com.nm.orm.jdbc.orm.operations;

import java.util.Collection;

import org.springframework.jdbc.core.JdbcTemplate;

import com.google.common.collect.Lists;
import com.nm.orm.jdbc.meta.MetaInformationAssociation;
import com.nm.orm.jdbc.orm.JdbcOrmAssociationProcessorChecker;
import com.nm.orm.utils.JdbcOrmException;

/**
 * 
 * @author MANSOURI Nabil <nabil.mansouri.3@gmail.com>
 *
 */
public class OperationCleanUnPersisted<T> extends OperationAbstract<T> {
	private final T entity;
	private final AssociationTypeFilterStrategy strategyType;
	private AssociationUnattachedStrategy strategyOperation;
	private Collection<Object> unpersisted = Lists.newArrayList();

	public OperationCleanUnPersisted(T entity, JdbcTemplate template) {
		this(entity, template, null, null);
	}

	public OperationCleanUnPersisted(T entity, JdbcTemplate template, AssociationTypeFilterStrategy strategyType) {
		this(entity, template, strategyType, null);
	}

	public OperationCleanUnPersisted(T entity, JdbcTemplate template, AssociationTypeFilterStrategy strategyType,
			AssociationUnattachedStrategy strategyUn) {
		super(template);
		this.entity = entity;
		this.strategyType = strategyType;
		if (strategyUn == null) {
			// DELETE BY DEFAULT
			this.strategyOperation = new AssociationUnattachedStrategy() {

				public void notAttach(Object o, MetaInformationAssociation context) {
					// DO NOTHING
				}
			};
		} else {
			this.strategyOperation = strategyUn;
		}
	}

	public T operation() throws JdbcOrmException {
		try {
			unpersisted.clear();
			new JdbcOrmAssociationProcessorChecker() {

				@Override
				protected boolean isOk(MetaInformationAssociation a) throws Exception {
					if (strategyType != null) {
						return strategyType.filter(a);
					}
					return true;
				}

				@Override
				protected void notAttach(Object o, MetaInformationAssociation context) {
				}

				@Override
				protected void notPersisted(Object o, MetaInformationAssociation context) throws Exception {
					unpersisted.add(o);
					strategyOperation.notAttach(o, context);
				}
			}.process(entity, getTemplate());
			return entity;
		} catch (Exception e) {
			throw new JdbcOrmException(e);
		}
	}

	public Collection<Object> getUnpersisted() {
		return unpersisted;
	}
}
