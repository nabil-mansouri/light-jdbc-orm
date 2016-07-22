package com.nm.orm.utils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;

/**
 * 
 * @author MANSOURI Nabil <nabil.mansouri.3@gmail.com>
 *
 */
public class ReflectionUtils {
	public static boolean isCollection(Field field) throws Exception {
		return Collection.class.isAssignableFrom(field.getType());
	}

	public static Class<?> getGenericParameter(Field field) throws Exception {
		ParameterizedType paramType = (ParameterizedType) field.getGenericType();
		Class<?> param = (Class<?>) paramType.getActualTypeArguments()[0];
		return param;
	}

	public static List<Field> getFieldHavingAnnotation(Class<?> source, Class<? extends Annotation> anno) {
		List<Field> fields = Lists.newArrayList();
		for (Field f : ReflectionUtils.getAllFieldsRecursive(source)) {
			Object column = f.getAnnotation(anno);
			if (column != null) {
				fields.add(f);
			}
		}
		return fields;
	}

	public static void transfer(Field f, Object source, Object dest) throws Exception {
		set(dest, f.getName(), get(source, f.getName()));
	}

	public static Map<String, Field> getAllFields(Class<?> type) {
		return getAllFields(new HashMap<String, Field>(), type);
	}

	public static Map<String, Field> getAllFields(Map<String, Field> fields, Class<?> type) {
		for (Field field : type.getDeclaredFields()) {
			fields.put(field.getName(), field);
		}

		if (type.getSuperclass() != null) {
			fields = getAllFields(fields, type.getSuperclass());
		}

		return fields;
	}

	public static Collection<Field> getAllFieldsRecursive(Class<?> type) {
		return getAllFields(new HashMap<String, Field>(), type).values();
	}

	public static Object getRecursively(Object o, String fieldName) throws Exception {
		Field staticField = getAllFields(o.getClass()).get(fieldName);
		makeModifiable(staticField);
		return staticField.get(o);
	}

	public static Object get(Object o, Field staticField) throws Exception {
		makeModifiable(staticField);
		return staticField.get(o);
	}

	public static Object get(Object o, String fieldName) throws Exception {
		Field staticField = o.getClass().getDeclaredField(fieldName);
		makeModifiable(staticField);
		return staticField.get(o);
	}

	public static void set(Object o, Field fieldName, Object newValue) throws Exception {
		set(o, fieldName.getName(), newValue);
	}

	public static void set(Object o, String fieldName, Object newValue) throws Exception {
		Field staticField = o.getClass().getDeclaredField(fieldName);
		makeModifiable(staticField);
		staticField.set(o, newValue);
	}

	public static void setRecursively(Object o, String fieldName, Object newValue) throws Exception {
		Field staticField = getAllFields(o.getClass()).get(fieldName);
		makeModifiable(staticField);
		staticField.set(o, newValue);
	}

	public static void makeModifiable(Field nameField) throws Exception {
		nameField.setAccessible(true);
		int modifiers = nameField.getModifiers();
		Field modifierField = nameField.getClass().getDeclaredField("modifiers");
		modifiers = modifiers & ~Modifier.FINAL;
		modifierField.setAccessible(true);
		modifierField.setInt(nameField, modifiers);
	}
}
