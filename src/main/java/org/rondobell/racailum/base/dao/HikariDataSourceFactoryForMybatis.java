package org.rondobell.racailum.base.dao;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.ibatis.datasource.DataSourceFactory;

import javax.sql.DataSource;
import java.util.Properties;

public class HikariDataSourceFactoryForMybatis implements DataSourceFactory {

	protected DataSource dataSource;

	@Override
	public void setProperties(Properties properties) {
		HikariConfig config = new HikariConfig(properties);
		dataSource = new HikariDataSource(config);
	}

	@Override
	public DataSource getDataSource() {
		return dataSource;
	}

}
