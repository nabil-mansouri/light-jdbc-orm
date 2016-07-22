package com.nm.orm.jdbc.meta;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.List;

import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;

import com.google.common.collect.Lists;
import com.nm.orm.jdbc.meta.MetaInformationAssociation.TypeOfAssociation;
import com.nm.orm.jdbc.orm.JdbcOrmUtils;
import com.nm.orm.utils.ReflectionUtils;

/**
 * 
 * @author MANSOURI Nabil
 *
 */
public class AssociationFinder {
	private final TypeOfAssociation type;
	private final Class<? extends Annotation> annotation;

	public AssociationFinder(TypeOfAssociation type, Class<? extends Annotation> annotation) {
		super();
		this.type = type;
		this.annotation = annotation;
	}

	public List<MetaInformationAssociation> find(Class<?> clazz, MetaInformation metaInfo) throws Exception {
		List<MetaInformationAssociation> all = Lists.newArrayList();
		List<Field> fields = ReflectionUtils.getFieldHavingAnnotation(clazz, annotation);
		for (Field f : fields) {
			JoinColumns joins = ReflectionUtils.getAnnotation(f, JoinColumns.class);
			JoinColumn join = f.getAnnotation(JoinColumn.class);
			if (joins != null || join != null) {
				MetaInformationAssociation meta = new MetaInformationAssociation();
				all.add(meta);
				//
				Class<?> target = f.getType();
				if (ReflectionUtils.isCollection(f)) {
					target = ReflectionUtils.getGenericParameter(f);
				}
				meta.setTarget(target);
				meta.setField(f);
				meta.setType(type);
				//
				if (joins != null) {
					for (JoinColumn j : joins.value()) {
						Field field = JdbcOrmUtils.getFieldForColumn(clazz, j.referencedColumnName());
						meta.getTargetFields().put(j.name(), field);
					}
				} else {
					Field field = JdbcOrmUtils.getFieldForColumn(clazz, join.referencedColumnName());
					// DEFAULT IS THE UNIQUE ID COLUMN
					if (field == null) {
						field = metaInfo.getFields(ColumnFilter.onlyIds())[0];
					}
					meta.getTargetFields().put(join.name(), field);
				}
				all.add(meta);
			}
		}
		return all;
	}
}
