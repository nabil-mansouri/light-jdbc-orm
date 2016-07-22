package com.nm.orm.jdbc.orm.operations;

import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;

import com.nm.orm.jdbc.meta.MetaInformationAssociation;
import com.nm.orm.jdbc.orm.JdbcOrmAssociationProcessor;
import com.nm.orm.utils.JdbcOrmException;
import com.nm.orm.utils.ReflectionUtils;

/**
 * 
 * @author MANSOURI Nabil <nabil.mansouri.3@gmail.com>
 *
 */
public class OperationRefresh<T> extends OperationAbstract<T> {
	private final T entity;
	private final AssociationListFilterStrategy strategyList;
	private final AssociationTypeFilterStrategy strategyType;

	public OperationRefresh(T entity, JdbcTemplate template) {
		this(entity, template, null, null);
	}

	public OperationRefresh(T entity, JdbcTemplate template, AssociationListFilterStrategy strategyList,
			AssociationTypeFilterStrategy strategyType) {
		super(template);
		this.entity = entity;
		this.strategyList = strategyList;
		this.strategyType = strategyType;
	}

	public T operation() throws JdbcOrmException {
		try {
			new JdbcOrmAssociationProcessor() {

				@Override
				protected boolean isOk(MetaInformationAssociation a) throws Exception {
					if (strategyType != null) {
						return strategyType.filter(a);
					}
					return true;
				}

				@Override
				protected void onFoundedList(Object root, List<Object> founded, MetaInformationAssociation context) throws Exception {
					if (strategyList != null) {
						founded = strategyList.filter(founded);
					}
					switch (context.getType()) {
					case ManyToMany:
					case OneToMany:
						ReflectionUtils.set(entity, context.getField(), founded);
						break;
					case OneToOne:
						if (founded.isEmpty()) {
							ReflectionUtils.set(entity, context.getField(), null);
						} else {
							ReflectionUtils.set(entity, context.getField(), founded.iterator().next());
						}
					default:
						break;

					}
				}

			}.process(entity, getTemplate());
			return entity;
		} catch (Exception e) {
			throw new JdbcOrmException(e);
		}
	}
}
