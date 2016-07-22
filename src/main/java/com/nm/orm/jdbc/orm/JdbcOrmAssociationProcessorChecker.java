package com.nm.orm.jdbc.orm;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

import com.nm.orm.jdbc.meta.ColumnFilter;
import com.nm.orm.jdbc.meta.MetaInformationAssociation;
import com.nm.orm.jdbc.meta.MetaRepository;
import com.nm.orm.utils.ReflectionUtils;

/**
 * 
 * @author MANSOURI Nabil <nabil.mansouri.3@gmail.com>
 *
 */
public abstract class JdbcOrmAssociationProcessorChecker extends JdbcOrmAssociationProcessor {

	@Override
	@SuppressWarnings("unchecked")
	protected final void onFoundedList(Object o, List<Object> founded, MetaInformationAssociation context) throws Exception {
		onFoundedListBefore(o, founded, context);
		Object attached = ReflectionUtils.get(o, context.getField());
		Collection<Object> attachedList = Arrays.asList(attached);
		if (attached instanceof Collection) {
			attachedList = (Collection<Object>) attached;
		}
		// ITERATE ATTACHED
		for (final Object a : attachedList) {
			final MapSqlParameterSource map1 = MetaRepository.getOrCreate(a).getMap(a, ColumnFilter.onlyIds());
			Optional<Object> opt = founded.stream().filter(new Predicate<Object>() {
				public boolean test(Object t) {
					try {
						final MapSqlParameterSource map2 = MetaRepository.getOrCreate(t).getMap(t, ColumnFilter.onlyIds());
						return JdbcOrmUtils.compareIds(map1, map2);
					} catch (Exception e) {
						throw new IllegalArgumentException(e);
					}
				}
			}).findFirst();
			if (!opt.isPresent()) {
				notPersisted(a, context);
			}
		}
		// ITERATE PERSISTED
		for (final Object a : founded) {
			final MapSqlParameterSource map1 = MetaRepository.getOrCreate(a).getMap(a, ColumnFilter.onlyIds());
			Optional<Object> opt = attachedList.stream().filter(new Predicate<Object>() {
				public boolean test(Object t) {
					try {
						final MapSqlParameterSource map2 = MetaRepository.getOrCreate(t).getMap(t, ColumnFilter.onlyIds());
						return JdbcOrmUtils.compareIds(map1, map2);
					} catch (Exception e) {
						throw new IllegalArgumentException(e);
					}
				}
			}).findFirst();
			if (!opt.isPresent()) {
				notAttach(a, context);
			}
		}
		onFoundedListAfter(o, founded, context);
	}

	protected abstract void notPersisted(Object o, MetaInformationAssociation context) throws Exception;

	protected abstract void notAttach(Object o, MetaInformationAssociation context) throws Exception;

	protected void onFoundedListBefore(Object root, List<Object> founded, MetaInformationAssociation context) throws Exception {

	}

	protected void onFoundedListAfter(Object root, List<Object> founded, MetaInformationAssociation context) throws Exception {

	}
}
