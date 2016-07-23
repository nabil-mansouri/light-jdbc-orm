package com.nm.orm.utils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * 
 * @author Nabil MANSOURI
 *
 */
public class BeanAnnotationProperty<T extends Annotation> {
	private AnnotationMode mode;
	private String name;
	private Field field;
	private Method readMethod;
	private Method writeMethod;
	private T annotation;

	public boolean founded() {
		return this.annotation != null;
	}

	public AnnotationMode getMode() {
		return mode;
	}

	public void setMode(AnnotationMode mode) {
		this.mode = mode;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Field getField() {
		return field;
	}

	public void setField(Field field) {
		this.field = field;
	}

	public Method getReadMethod() {
		return readMethod;
	}

	public void setReadMethod(Method readMethod) {
		this.readMethod = readMethod;
	}

	public Method getWriteMethod() {
		return writeMethod;
	}

	public void setWriteMethod(Method writeMethod) {
		this.writeMethod = writeMethod;
	}

	public T getAnnotation() {
		return annotation;
	}

	public void setAnnotation(T annotation) {
		this.annotation = annotation;
	}

	@Override
	public String toString() {
		return "BeanAnnotationProperty [annotation=" + annotation + "mode=" + mode + ", name=" + name + ", field="
				+ field + ", readMethod=" + readMethod + ", writeMethod=" + writeMethod + "]";
	}

}