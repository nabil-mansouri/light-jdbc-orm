package com.nm.orm.jdbc.meta;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.nm.orm.utils.ReflectionUtils;

/**
 * 
 * @author MANSOURI Nabil <nabil.mansouri.3@gmail.com>
 *
 */
public class MetaInformation {
	private Map<String, Collection<MetaInformationColumn>> uniqGroup = Maps.newHashMap();
	private Map<String, MetaInformationColumn> columns = Maps.newHashMap();
	private Map<String, MetaInformationAssociation> associations = Maps.newHashMap();

	public Collection<MetaInformationColumn> getColumnsCollection() {
		return columns.values();
	}

	public Collection<MetaInformationAssociation> getAssociationsCollection() {
		return associations.values();
	}

	public void add(MetaInformationColumn c) {
		this.columns.put(c.getField().getName(), c);
	}

	public void addUniq(String cons, MetaInformationColumn c) {
		this.uniqGroup.putIfAbsent(cons, Lists.newArrayList());
		this.uniqGroup.get(cons).add(c);
	}

	public void add(MetaInformationAssociation c) {
		this.associations.put(c.getField().getName(), c);
	}

	public void addAll(Collection<MetaInformationAssociation> c) {
		for (MetaInformationAssociation a : c) {
			this.add(a);
		}
	}

	public boolean isGenerated() {
		return this.columns.values().stream().filter(u -> u.isGenerated()).findAny().isPresent();
	}

	public String[] getNames(Class<?> clazz, ColumnFilter filter) throws Exception {
		String[] names = {};
		for (MetaInformationColumn r : this.columns.values().stream().filter(filter.toPredicate()).collect(Collectors.toList())) {
			names = ArrayUtils.add(names, r.getColumn().name());
		}
		return names;
	}

	public String[] getNames(Object o, ColumnFilter filter) throws Exception {
		String[] names = {};
		for (MetaInformationColumn r : this.columns.values().stream().filter(filter.toPredicate(o)).collect(Collectors.toList())) {
			names = ArrayUtils.add(names, r.getColumn().name());
		}
		return names;
	}

	public Field[] getFields(ColumnFilter filter) throws Exception {
		Field[] names = {};
		for (MetaInformationColumn r : this.columns.values().stream().filter(filter.toPredicate()).collect(Collectors.toList())) {
			names = ArrayUtils.add(names, r.getField());
		}
		return names;
	}

	public MapSqlParameterSource getMap(Object o, ColumnFilter filter) throws Exception {
		final MapSqlParameterSource all = new MapSqlParameterSource();
		for (MetaInformationColumn r : this.columns.values().stream().filter(filter.toPredicate(o)).collect(Collectors.toList())) {
			Object value = ReflectionUtils.get(o, r.getField());
			all.addValue(r.getColumn().name(), value);
		}
		return all;
	}

	public List<Object> getValues(Object o, ColumnFilter filter) throws Exception {
		final List<Object> all = Lists.newArrayList();
		for (MetaInformationColumn r : this.columns.values().stream().filter(filter.toPredicate(o)).collect(Collectors.toList())) {
			Object value = ReflectionUtils.get(o, r.getField());
			all.add(value);
		}
		return all;
	}

}
