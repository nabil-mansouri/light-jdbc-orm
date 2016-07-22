package com.nm.orm.jdbc.orm.operations;

import java.util.Arrays;
import java.util.List;

import com.google.common.collect.Lists;
import com.nm.orm.jdbc.meta.MetaInformationAssociation;

/**
 * 
 * @author MANSOURI Nabil <nabil.mansouri.3@gmail.com>
 *
 */
public class AssociationTypeFilterStrategyTarget implements AssociationTypeFilterStrategy {
	private List<Class<?>> clazz = Lists.newArrayList();

	public AssociationTypeFilterStrategyTarget(Class<?>... clazz) {
		super();
		this.clazz = Arrays.asList(clazz);
	}

	public boolean filter(MetaInformationAssociation context) {
		return clazz.contains(context.getTarget());
	}

}
