package com.nm.orm.jdbc.orm.operations;

import java.util.Collection;

import org.springframework.jdbc.core.JdbcTemplate;

import com.google.common.collect.Lists;
import com.nm.orm.utils.JdbcOrmException;

/**
 * 
 * @author MANSOURI Nabil <nabil.mansouri.3@gmail.com>
 *
 */
public class OperationList<T> extends OperationAbstract<T> {
	private Collection<OperationAbstract<T>> list = Lists.newArrayList();

	public OperationList(JdbcTemplate template) {
		super(template);
	}

	public void add(OperationAbstract<T> op) {
		list.add(op);
	}

	public T operation() throws JdbcOrmException {
		try {
			for (OperationAbstract<T> op : list) {
				op.operation();
			}
			return null;
		} catch (Exception e) {
			throw new JdbcOrmException(e);
		}
	}
}
