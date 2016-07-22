package com.nm.orm.jdbc.insert;

import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.metadata.TableMetaDataContext;
import org.springframework.jdbc.core.metadata.TableMetaDataProvider;
import org.springframework.jdbc.support.DatabaseMetaDataCallback;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.jdbc.support.nativejdbc.NativeJdbcExtractor;

import com.nm.orm.utils.ReflectionUtils;

/**
 * 
 * @author MANSOURI Nabil <nabil.mansouri.3@gmail.com>
 *
 */
public class TableMetaDataContextSybase extends TableMetaDataContext {
	@Override
	public void processMetaData(DataSource dataSource, List<String> declaredColumns, String[] generatedKeyNames) {
		try {
			final NativeJdbcExtractor nativeJdbcExtractor = (NativeJdbcExtractor) ReflectionUtils.getRecursively(this,
					"nativeJdbcExtractor");
			ReflectionUtils.setRecursively(this, "metaDataProvider",
					(TableMetaDataProvider) JdbcUtils.extractDatabaseMetaData(dataSource, new DatabaseMetaDataCallback() {
						public Object processMetaData(DatabaseMetaData databaseMetaData) throws SQLException {
							boolean accessTableColumnMetaData = isAccessTableColumnMetaData();
							TableMetaDataProvider provider = new SybaseTableMetaDataProvider(databaseMetaData);
							if (nativeJdbcExtractor != null) {
								provider.setNativeJdbcExtractor(nativeJdbcExtractor);
							}
							provider.initializeWithMetaData(databaseMetaData);
							if (accessTableColumnMetaData) {
								provider.initializeWithTableColumnMetaData(databaseMetaData, getCatalogName(), getSchemaName(),
										getTableName());
							}
							return provider;
						}
					}));
			//
			this.getTableColumns().clear();
			this.getTableColumns().addAll(reconcileColumnsToUse(declaredColumns, generatedKeyNames));
		} catch (Exception e) {
			throw new IllegalArgumentException(e);
		}
	}
}
