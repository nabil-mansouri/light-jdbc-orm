package com.nm.orm.jdbc.orm.listeners;

import org.apache.commons.lang3.StringUtils;

import com.nm.orm.jdbc.meta.MetaInformation;
import com.nm.orm.jdbc.meta.MetaInformationColumn;
import com.nm.orm.jdbc.meta.MetaRepository;
import com.nm.orm.utils.ReflectionUtils;

/**
 * 
 * @author MANSOURI Nabil
 *
 */
public class SaveUpdateListenerTruncate<T> implements SaveUpdateListener<T> {

	public void process(T entity) {
		try {
			MetaInformation meta = MetaRepository.getOrCreate(entity);
			for (MetaInformationColumn c : meta.getColumnsCollection()) {
				int length = c.getColumn().length();
				Object value = c.fieldValueSafe(entity);
				if (value != null && value instanceof String && length < StringUtils.length(value.toString())) {
					ReflectionUtils.set(entity, c.getField(), StringUtils.substring(value.toString(), 0, length));
				}
			}
		} catch (Exception e) {
			throw new IllegalArgumentException(e);
		}
	}

	@Override
	public void beforeInsert(T entity) {
		process(entity);
	}

	@Override
	public void afterInsert(T entity) {

	}

	@Override
	public void beforeUpdate(T entity) {
		process(entity);

	}

	@Override
	public void afterUpdate(T entity) {

	}

}
