package com.nm.orm.utils;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import com.google.common.collect.Lists;

/**
 * 
 * @author MANSOURI Nabil <nabil.mansouri.3@gmail.com>
 *
 */
public class ReflectionUtils {
	@SuppressWarnings("unchecked")
	public static <T extends Annotation> BeanAnnotationProperty<T> findAnnotationProperty(Class<?> type, Field field,
			Class<T> toTest) {
		BeanAnnotationProperty<T> b = new BeanAnnotationProperty<>();
		b.setField(field);
		b.setName(field.getName());
		try {
			PropertyDescriptor property = new PropertyDescriptor(field.getName(), type);
			try {
				// FIND BY FIELD
				for (Annotation annotation : b.getField().getAnnotations()) {
					if (annotation.annotationType().equals(toTest)) {
						b.setAnnotation((T) annotation);
						b.setMode(AnnotationMode.Field);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				b.setReadMethod(property.getReadMethod());
				// FIND BY GETTER
				for (Annotation annotation : b.getReadMethod().getAnnotations()) {
					if (annotation.annotationType().equals(toTest)) {
						b.setAnnotation((T) annotation);
						b.setMode(AnnotationMode.Getter);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				b.setWriteMethod(property.getWriteMethod());
				// FIND BY SETTER
				for (Annotation annotation : b.getWriteMethod().getAnnotations()) {
					if (annotation.annotationType().equals(toTest)) {
						b.setAnnotation((T) annotation);
						b.setMode(AnnotationMode.Setter);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (IntrospectionException e) {
			// FIND BY FIELD
			for (Annotation annotation : b.getField().getAnnotations()) {
				if (annotation.annotationType().equals(toTest)) {
					b.setAnnotation((T) annotation);
					b.setMode(AnnotationMode.Field);
				}
			}
		}
		return b;
	}

	public static <T extends Annotation> List<BeanAnnotationProperty<T>> findAnnotationProperty(Class<?> type,
			Class<T> toTest) throws Exception {
		List<BeanAnnotationProperty<T>> result = Lists.newArrayList();
		//
		List<Field> fields = getAllFields(type);
		for (Field field : fields) {
			BeanAnnotationProperty<T> b = findAnnotationProperty(type, field, toTest);
			if (b.founded()) {
				result.add(b);
			}
		}
		return result;
	}

	public static Object get(Object o, Field staticField) throws Exception {
		makeModifiable(staticField);
		return staticField.get(o);
	}

	public static Object get(Object o, Method staticField) throws Exception {
		makeModifiable(staticField);
		return staticField.invoke(o);
	}

	public static List<Field> getAllFields(Class<?> type) {
		List<Field> fields = Lists.newArrayList();
		fields.addAll(Arrays.asList(type.getDeclaredFields()));
		if (type.getSuperclass() != null) {
			fields.addAll(getAllFields(type.getSuperclass()));
		}
		return fields;
	}

	public static Class<?> getGenericParameter(Field field) throws Exception {
		ParameterizedType paramType = (ParameterizedType) field.getGenericType();
		Class<?> param = (Class<?>) paramType.getActualTypeArguments()[0];
		return param;
	}

	public static Object getRecursively(Object o, String fieldName) throws Exception {
		Field staticField = getAllFields(o.getClass()).stream().filter(u -> u.getName().equalsIgnoreCase(fieldName))
				.findFirst().get();
		makeModifiable(staticField);
		return staticField.get(o);
	}

	public static Object getValue(BeanAnnotationProperty<?> p, Object o) throws Exception {
		if (p.getReadMethod() != null) {
			return get(o, p.getReadMethod());
		} else {
			return get(o, p.getField());
		}
	}

	public static boolean isCollection(Field field) throws Exception {
		return Collection.class.isAssignableFrom(field.getType());
	}

	public static void makeModifiable(Field nameField) throws Exception {
		nameField.setAccessible(true);
		int modifiers = nameField.getModifiers();
		Field modifierField = nameField.getClass().getDeclaredField("modifiers");
		modifiers = modifiers & ~Modifier.FINAL;
		modifierField.setAccessible(true);
		modifierField.setInt(nameField, modifiers);
	}

	public static void makeModifiable(Method nameField) throws Exception {
		nameField.setAccessible(true);
	}

	public static void set(Object o, Field fieldName, Object newValue) throws Exception {
		makeModifiable(fieldName);
		fieldName.set(o, newValue);
	}

	public static void set(Object o, Method method, Object newValue) throws Exception {
		makeModifiable(method);
		method.invoke(o, newValue);
	}

	public static void setRecursively(Object o, String fieldName, Object newValue) throws Exception {
		Field staticField = getAllFields(o.getClass()).stream().filter(u -> u.getName().equalsIgnoreCase(fieldName))
				.findFirst().get();
		makeModifiable(staticField);
		staticField.set(o, newValue);
	}

	public static void setValue(BeanAnnotationProperty<?> p, Object o, Object value) throws Exception {
		if (p.getWriteMethod() != null) {
			set(o, p.getWriteMethod(), value);
		} else {
			set(o, p.getField(), value);
		}
	}

	public static void transfer(Field f, Object source, Object dest) throws Exception {
		set(dest, f, get(source, f));
	}
}
