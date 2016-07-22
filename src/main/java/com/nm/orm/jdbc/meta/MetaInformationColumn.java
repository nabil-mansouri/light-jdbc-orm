package com.nm.orm.jdbc.meta;

import java.lang.reflect.Field;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import com.nm.orm.utils.ReflectionUtils;

/**
 * 
 * @author MANSOURI Nabil <nabil.mansouri.3@gmail.com>
 *
 */
public class MetaInformationColumn {
	private Column column;
	private Field field;
	private Id id;
	private GeneratedValue generated;
	private boolean inUniqGroup;

	public boolean isInUniqGroup() {
		return inUniqGroup;
	}

	public void setInUniqGroup(boolean inUniqGroup) {
		this.inUniqGroup = inUniqGroup;
	}

	public boolean isGenerated() {
		return this.generated != null;
	}

	public GeneratedValue getGenerated() {
		return generated;
	}

	public void setGenerated(GeneratedValue generated) {
		this.generated = generated;
	}

	public boolean isId() {
		return this.id != null;
	}

	public Column getColumn() {
		return column;
	}

	public void setColumn(Column column) {
		this.column = column;
	}

	public Field getField() {
		return field;
	}

	public void setField(Field field) {
		this.field = field;
	}

	public Id getId() {
		return id;
	}

	public void setId(Id id) {
		this.id = id;
	}

	public Object fieldValueSafe(Object object) {
		try {
			return ReflectionUtils.get(object, this.field);
		} catch (Exception e) {
			throw new IllegalArgumentException(e);
		}
	}
}
