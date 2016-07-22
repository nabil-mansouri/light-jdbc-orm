package com.nm.orm.jdbc.meta;

import java.lang.reflect.Field;
import java.util.Map;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

import com.google.common.collect.Maps;
import com.nm.orm.utils.ReflectionUtils;

/**
 * 
 * @author MANSOURI Nabil <nabil.mansouri.3@gmail.com>
 *
 */
public class MetaInformationAssociation {
	public enum TypeOfAssociation {
		OneToMany, ManyToMany, OneToOne
	}

	private Field field;
	private Class<?> target;
	private Map<String, Field> targetFields = Maps.newHashMap();
	private TypeOfAssociation type;

	public MetaInformationAssociation() {
	}

	public Field getField() {
		return field;
	}

	public void setField(Field field) {
		this.field = field;
	}

	public Class<?> getTarget() {
		return target;
	}

	public void setTarget(Class<?> target) {
		this.target = target;
	}

	public Map<String, Field> getTargetFields() {
		return targetFields;
	}

	public void setTargetFields(Map<String, Field> targetFields) {
		this.targetFields = targetFields;
	}

	public TypeOfAssociation getType() {
		return type;
	}

	public void setType(TypeOfAssociation type) {
		this.type = type;
	}

	public MapSqlParameterSource getMap(Object o) throws Exception {
		MapSqlParameterSource map = new MapSqlParameterSource();
		for (Map.Entry<String, Field> a : this.targetFields.entrySet()) {
			map.addValue(a.getKey(), ReflectionUtils.get(o, a.getValue()));
		}
		return map;
	}
}
