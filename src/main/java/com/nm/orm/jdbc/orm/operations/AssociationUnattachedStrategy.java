package com.nm.orm.jdbc.orm.operations;

import com.nm.orm.jdbc.meta.MetaInformationAssociation;

/**
 * 
 * @author mansoun
 *
 */
public interface AssociationUnattachedStrategy {
	void notAttach(Object o, MetaInformationAssociation context) throws Exception;
}
