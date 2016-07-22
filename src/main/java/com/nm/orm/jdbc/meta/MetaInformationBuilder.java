package com.nm.orm.jdbc.meta;

import java.io.Serializable;
import java.lang.reflect.Field;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.nm.orm.jdbc.meta.MetaInformationAssociation.TypeOfAssociation;
import com.nm.orm.utils.ReflectionUtils;

/**
 * 
 * @author MANSOURI Nabil
 *
 */
public class MetaInformationBuilder {
	private AssociationFinder assocOneToOne = new AssociationFinder(TypeOfAssociation.OneToOne, OneToOne.class);
	private AssociationFinder assocOneToMany = new AssociationFinder(TypeOfAssociation.OneToMany, OneToMany.class);

	public MetaInformation build(Class<?> clazz) throws Exception {
		MetaInformation meta = new MetaInformation();
		buildColumns(clazz, meta);
		buildAssociation(clazz, meta);
		// MUST BE AFTER
		buildUniqGroup(clazz, meta);
		return meta;
	}

	private void buildColumns(Class<?> clazz, MetaInformation meta) {
		for (Field f : ReflectionUtils.getAllFieldsRecursive(clazz)) {
			Column column = f.getAnnotation(Column.class);
			Id id = f.getAnnotation(Id.class);
			GeneratedValue generated = f.getAnnotation(GeneratedValue.class);
			if (column != null) {
				MetaInformationColumn col = new MetaInformationColumn();
				col.setColumn(column);
				col.setField(f);
				col.setId(id);
				col.setGenerated(generated);
				meta.add(col);
			}
		}
	}

	private void buildAssociation(Class<?> clazz, MetaInformation meta) throws Exception {
		meta.addAll(assocOneToOne.find(clazz, meta));
		meta.addAll(assocOneToMany.find(clazz, meta));
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
