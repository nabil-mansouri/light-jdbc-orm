package com.nm.orm.jdbc.orm.listeners;

/**
 * 
 * @author MANSOURI Nabil <nabil.mansouri.3@gmail.com>
 *
 */
public interface InsertListener<T> {
	public void beforeInsert(T entity);

	public void afterInsert(T entity);
}
