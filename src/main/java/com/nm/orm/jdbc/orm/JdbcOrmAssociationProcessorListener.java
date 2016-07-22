package com.nm.orm.jdbc.orm;

import java.util.List;

import com.nm.orm.jdbc.meta.MetaInformationAssociation;

/**
 * 
 * @author MANSOURI Nabil <nabil.mansouri.3@gmail.com>
 *
 */
public interface JdbcOrmAssociationProcessorListener {
	public boolean isOk(MetaInformationAssociation context);

	public List<Object> filter(List<Object> original);

	public void notAttached(Object o, MetaInformationAssociation context);

	public void notPersisted(Object o, MetaInformationAssociation context);
}
