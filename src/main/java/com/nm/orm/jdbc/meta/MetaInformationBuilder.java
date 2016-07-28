package com.nm.orm.jdbc.meta;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.nm.orm.jdbc.meta.MetaInformationAssociation.TypeOfAssociation;
import com.nm.orm.utils.BeanAnnotationProperty;
import com.nm.orm.utils.ReflectionUtils;

/**
 * 
 * @author MANSOURI Nabil
 *
 */
public class MetaInformationBuilder {
	private AssociationFinder<OneToOne> assocOneToOne = new AssociationFinder<>(TypeOfAssociation.OneToOne, OneToOne.class);
	private AssociationFinder<OneToMany> assocOneToMany = new AssociationFinder<>(TypeOfAssociation.OneToMany, OneToMany.class);
	private AssociationFinder<ManyToOne> assocManyToOne = new AssociationFinder<>(TypeOfAssociation.ManyToOne, ManyToOne.class);

	public MetaInformation build(Class<?> clazz) throws Exception {
		MetaInformation meta = new MetaInformation();
		buildColumns(clazz, meta);
		buildAssociation(clazz, meta);
		// MUST BE AFTER
		buildUniqGroup(clazz, meta);
		return meta;
	}

	private void buildColumns(Class<?> clazz, MetaInformation meta) throws Exception {
		for (BeanAnnotationProperty<Column> f : ReflectionUtils.findAnnotationProperty(clazz, Column.class)) {
			if (f.founded()) {
				BeanAnnotationProperty<Id> bId = ReflectionUtils.findAnnotationProperty(clazz, f.getField(), Id.class);
				BeanAnnotationProperty<GeneratedValue> bGenerated = ReflectionUtils.findAnnotationProperty(clazz, f.getField(),
						GeneratedValue.class);
				MetaInformationColumn col = new MetaInformationColumn();
				col.setColumn(f.getAnnotation());
				col.setField(f.getField());
				col.setId(bId.getAnnotation());
				col.setGenerated(bGenerated.getAnnotation());
				meta.add(col);
			}
		}
	}

	private void buildAssociation(Class<?> clazz, MetaInformation meta) throws Exception {
		meta.addAll(assocOneToOne.find(clazz, meta));
		meta.addAll(assocOneToMany.find(clazz, meta));
		meta.addAll(assocManyToOne.find(clazz, meta));
	}

	private void buildUniqGroup(Class<?> clazz, MetaInformation meta) {
		Table table = clazz.getAnnotation(Table.class);
		for (UniqueConstraint cons : table.uniqueConstraints()) {
			for (Serializable s : cons.columnNames()) {
				MetaInformationColumn metaCol = meta.getColumnsCollection().stream()
						.filter(u -> u.getColumn().name().equalsIgnoreCase(s.toString())).findFirst().get();
				metaCol.setInUniqGroup(true);
				meta.addUniq(cons.name(), metaCol);
			}
		}
	}
}
