package com.nm.orm.jdbc.meta;

import java.util.concurrent.ConcurrentMap;

import com.google.common.collect.Maps;

/**
 * 
 * @author MANSOURI Nabil
 *
 */
public class MetaRepository {
	private static ConcurrentMap<Class<?>, MetaInformation> metas = Maps.newConcurrentMap();
	private static MetaInformationBuilder builder = new MetaInformationBuilder();

	public static MetaInformation getOrCreate(Object o) throws Exception {
		return getOrCreate(o.getClass());
	}

	public static MetaInformation getOrCreate(Class<?> clazz) throws Exception {
		if (metas.containsKey(clazz)) {
			return metas.get(clazz);
		} else {
			metas.put(clazz, builder.build(clazz));
			return metas.get(clazz);
		}
	}
}
