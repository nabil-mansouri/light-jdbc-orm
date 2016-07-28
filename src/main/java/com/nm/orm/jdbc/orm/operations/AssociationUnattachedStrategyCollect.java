package com.nm.orm.jdbc.orm.operations;

import java.util.Collection;

import com.google.common.collect.Lists;
import com.nm.orm.jdbc.meta.MetaInformationAssociation;

/**
 * 
 * @author mansoun
 *
 */
public class AssociationUnattachedStrategyCollect<T> implements AssociationUnattachedStrategy {
	private Collection<T> founded = Lists.newArrayList();

	@SuppressWarnings("unchecked")
	public void notAttach(Object o, MetaInformationAssociation context) throws Exception {
		founded.add((T) o);
	}

	public Collection<T> getFounded() {
		return founded;
	}
}
