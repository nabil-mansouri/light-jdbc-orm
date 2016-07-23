package com.nm.orm.jdbc.meta;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.List;

import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;

import com.google.common.collect.Lists;
import com.nm.orm.jdbc.meta.MetaInformationAssociation.TypeOfAssociation;
import com.nm.orm.jdbc.orm.JdbcOrmUtils;
import com.nm.orm.utils.BeanAnnotationProperty;
import com.nm.orm.utils.ReflectionUtils;

/**
 * 
 * @author MANSOURI Nabil
 *
 */
public class AssociationFinder<T extends Annotation> {
	private final TypeOfAssociation type;
	private final Class<T> annotation;

	public AssociationFinder(TypeOfAssociation type, Class<T> annotation) {
		super();
		this.type = type;
		this.annotation = annotation;
	}

	public List<MetaInformationAssociation> find(Class<?> clazz, MetaInformation metaInfo) throws Exception {
		List<MetaInformationAssociation> all = Lists.newArrayList();
		List<BeanAnnotationProperty<T>> fields = ReflectionUtils.findAnnotationProperty(clazz, annotation);
		for (BeanAnnotationProperty<T> f : fields) {
			BeanAnnotationProperty<JoinColumns> bJoins = ReflectionUtils.findAnnotationProperty(clazz, f.getField(),
					JoinColumns.class);
			BeanAnnotationProperty<JoinColumn> bJoin = ReflectionUtils.findAnnotationProperty(clazz, f.getField(),
					JoinColumn.class);
			if (bJoins.founded() || bJoin.founded()) {
				MetaInformationAssociation meta = new MetaInformationAssociation();
				all.add(meta);
				//
				Class<?> target = f.getField().getType();
				if (ReflectionUtils.isCollection(f.getField())) {
					target = ReflectionUtils.getGenericParameter(f.getField());
				}
				meta.setTarget(target);
				meta.setField(f.getField());
				meta.setType(type);
				//
				if (bJoins.founded()) {
					for (JoinColumn j : bJoins.getAnnotation().value()) {
						Field field = JdbcOrmUtils.getFieldForColumn(clazz, j.referencedColumnName());
						meta.getTargetFields().put(j.name(), field);
					}
				} else {
					Field field = JdbcOrmUtils.getFieldForColumn(clazz, bJoin.getAnnotation().referencedColumnName());
					// DEFAULT IS THE UNIQUE ID COLUMN
					if (field == null) {
						field = metaInfo.getFields(ColumnFilter.onlyIds())[0];
					}
					meta.getTargetFields().put(bJoin.getAnnotation().name(), field);
				}
				all.add(meta);
			}
		}
		return all;
	}
}
