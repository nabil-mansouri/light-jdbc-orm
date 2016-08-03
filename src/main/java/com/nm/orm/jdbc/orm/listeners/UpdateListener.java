package com.nm.orm.jdbc.orm.listeners;

/**
 * 
 * @author MANSOURI Nabil <nabil.mansouri.3@gmail.com>
 *
 */
public interface UpdateListener<T> {
	public void beforeUpdate(T entity);

	public void afterUpdate(T entity);
}
