package com.nm.orm.jdbc.orm.operations;

import com.nm.orm.jdbc.meta.MetaInformationAssociation;

/**
 * 
 * @author MANSOURI Nabil <nabil.mansouri.3@gmail.com>
 *
 */
public interface AssociationTypeFilterStrategy {
	public boolean filter(MetaInformationAssociation a) throws Exception;
}
