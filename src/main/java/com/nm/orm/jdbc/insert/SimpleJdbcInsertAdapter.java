package com.nm.orm.jdbc.insert;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

/**
 * 
 * @author MANSOURI Nabil <nabil.mansouri.3@gmail.com>
 *
 */
public interface SimpleJdbcInsertAdapter {
	public SimpleJdbcInsert build(JdbcTemplate template) throws Exception;
}
