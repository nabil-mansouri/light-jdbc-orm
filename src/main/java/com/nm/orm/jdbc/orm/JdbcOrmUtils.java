package com.nm.orm.jdbc.orm;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.persistence.Column;
import javax.persistence.Table;

import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.nm.orm.utils.ReflectionUtils;

/**
 * 
 * @author MANSOURI Nabil <nabil.mansouri.3@gmail.com>
 *
 */
public class JdbcOrmUtils {

	public static boolean compareIds(MapSqlParameterSource map1, MapSqlParameterSource map2) throws Exception {
		// FIX COMPARE LONG AND INTEGER
		Map<String, Object> map1T = fixCompare(map1);
		Map<String, Object> map2T = fixCompare(map2);
		return map1T.equals(map2T);
	}

	private static Map<String, Object> fixCompare(MapSqlParameterSource map) {
		Map<String, Object> map1T = Maps.newHashMap();
		// FIX COMPARE INT WITH LONG (=> NORMALIZE)
		for (Entry<String, Object> entry : map.getValues().entrySet()) {
			if (entry.getValue() instanceof Integer) {
				Integer i = (Integer) entry.getValue();
				map1T.put(entry.getKey(), new Long(i));
			} else {
				map1T.put(entry.getKey(), entry.getValue());
			}
		}
		return map1T;
	}

	public static Field getFieldForColumn(Class<?> clazz, String c) throws Exception {
		for (Field f : ReflectionUtils.getAllFieldsRecursive(clazz)) {
			Column column = ReflectionUtils.getAnnotation(f, Column.class);
			if (column != null && StringUtils.equalsIgnoreCase(column.name(), c)) {
				return f;
			}
		}
		return null;
	}

	public static <T> String getFullTableName(T o) throws Exception {
		return getFullTableName(o.getClass());
	}

	public static <T> String getFullTableName(Class<T> o) throws Exception {
		Table table = o.getAnnotation(Table.class);
		List<String> names = Lists.newArrayList();
		if (!Strings.isNullOrEmpty(table.catalog())) {
			names.add(table.catalog());
		}
		if (!Strings.isNullOrEmpty(table.schema())) {
			names.add(table.schema());
		}
		if (!Strings.isNullOrEmpty(table.name())) {
			names.add(table.name());
		}
		return StringUtils.join(names, ".");
	}

	public static <T> String getTableName(T o) throws Exception {
		Table table = o.getClass().getAnnotation(Table.class);
		return table.name();
	}

}
