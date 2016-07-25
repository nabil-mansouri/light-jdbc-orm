package com.nm.orm.jdbc.orm;

import java.util.Collection;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import com.google.common.collect.Lists;
import com.nm.orm.jdbc.meta.MetaInformationAssociation;
import com.nm.orm.jdbc.meta.MetaRepository;
import com.nm.orm.jdbc.select.RowMapperObject;

/**
 * 
 * @author MANSOURI Nabil <nabil.mansouri.3@gmail.com>
 *
 */
public abstract class JdbcOrmAssociationProcessor {
	public void process(Object o, NamedParameterJdbcTemplate template) throws Exception {
		Collection<MetaInformationAssociation> assoc = MetaRepository.getOrCreate(o).getAssociationsCollection();
		for (MetaInformationAssociation a : assoc) {
			if (isOk(a)) {
				String table = JdbcOrmUtils.getFullTableName(a.getTarget());
				// BUILD QUERY
				MapSqlParameterSource map = a.getMap(o);
				// SEARCH ONLY IF NULL
				if (map.getValues().values().stream().filter(u -> u != null).findAny().isPresent()) {
					List<String> ands = Lists.newArrayList();
					for (String s : map.getValues().keySet()) {
						ands.add(String.format("%s = :%s", s, s));
					}
					String where = StringUtils.join(ands, " AND ");
					String sql = String.format("SELECT * FROM %s WHERE %s", table, where);
					@SuppressWarnings("unchecked")
					Class<Object> cl = (Class<Object>) a.getTarget();
					RowMapperObject<Object> mapper = new RowMapperObject<Object>(cl);
					List<Object> founded = template.query(sql, map, mapper);
					onFoundedList(o, founded, a);
				} else {
					onFoundedList(o, Lists.newArrayList(), a);
				}
			}
		}
	}

	protected abstract boolean isOk(MetaInformationAssociation a) throws Exception;

	protected abstract void onFoundedList(Object root, List<Object> founded, MetaInformationAssociation context) throws Exception;

}
