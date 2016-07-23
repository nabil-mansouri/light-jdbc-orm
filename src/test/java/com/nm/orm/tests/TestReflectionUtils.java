package com.nm.orm.tests;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.util.List;

import javax.persistence.Column;

import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.Test;

import com.nm.orm.utils.BeanAnnotationProperty;
import com.nm.orm.utils.ReflectionUtils;

/**
 * 
 * @author Nabil MANSOURI
 *
 */
public class TestReflectionUtils {

	@Test
	public void testShouldFindFields() throws Exception {
		Assert.assertEquals(3, ReflectionUtils.getAllFields(ExampleModel.class).size());
		Assert.assertEquals(7, ReflectionUtils.getAllFields(ExampleModelChild.class).size());
	}

	@Test
	public void testShouldFindAnnotationField() throws Exception {
		BeanInfo bi = Introspector.getBeanInfo(ExampleModel.class);
		PropertyDescriptor[] properties = bi.getPropertyDescriptors();
		for (PropertyDescriptor property : properties) {
			// One way
			System.out.println(property.getName());
			System.out.println(property.getReadMethod().getName());
			for (Annotation annotation : property.getReadMethod().getAnnotations()) {
				System.out.println(annotation);
				if (annotation instanceof Column) {
					String string = ((Column) annotation).name();
					System.out.println(string);
				}
			}
		}
	}

	@Test
	public void testShouldFindAnnotationOnParent() throws Exception {
		List<BeanAnnotationProperty<Column>> result = ReflectionUtils.findAnnotationProperty(ExampleModel.class,
				Column.class);
		Assert.assertEquals(3, result.size());
		for (BeanAnnotationProperty<Column> b : result) {
			System.out.println(b.getName());
			Assert.assertNotNull(b.getField());
			Assert.assertNotNull(b.getAnnotation());
			System.out.println(b.getAnnotation().name());
		}
	}

	@Test
	public void testShouldFindAnnotationOnChild() throws Exception {
		List<BeanAnnotationProperty<Column>> result = ReflectionUtils.findAnnotationProperty(ExampleModelChild.class,
				Column.class);
		System.out.println(StringUtils.join(result, "\n"));
		Assert.assertEquals(7, result.size());
		for (BeanAnnotationProperty<Column> b : result) {
			System.out.println(b.getName());
			Assert.assertNotNull(b.getField());
			Assert.assertNotNull(b.getAnnotation());
			System.out.println(b.getAnnotation().name());
		}
	}

	@Test
	public void testShouldSetAndGetValuesOnChild() throws Exception {
		List<BeanAnnotationProperty<Column>> result = ReflectionUtils.findAnnotationProperty(ExampleModelChild.class,
				Column.class);
		System.out.println(StringUtils.join(result, "\n"));
		Assert.assertEquals(7, result.size());
		ExampleModelChild child = new ExampleModelChild();
		for (BeanAnnotationProperty<Column> b : result) {
			if (StringUtils.equalsIgnoreCase("field3", b.getName())
					|| StringUtils.equalsIgnoreCase("field6", b.getName())) {
				ReflectionUtils.setValue(b, child, Boolean.TRUE);
				Assert.assertEquals(Boolean.TRUE, ReflectionUtils.getValue(b, child));
			} else {
				ReflectionUtils.setValue(b, child, "YEAH");
				Assert.assertEquals("YEAH", ReflectionUtils.getValue(b, child));
			}
			System.out.println(ReflectionUtils.getValue(b, child));
		}
	}

}
