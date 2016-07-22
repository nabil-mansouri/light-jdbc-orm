package com.nm.orm.jdbc.orm.operations;

import java.util.List;

/**
 * 
 * @author MANSOURI Nabil <nabil.mansouri.3@gmail.com>
 *
 */
public interface AssociationListFilterStrategy {
	public List<Object> filter(List<Object> all) throws Exception;
}
