package com.cmap.configuration.hibernate;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import org.apache.tomcat.dbcp.dbcp2.BasicDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.PropertySource;

@PropertySource(value = { "classpath:application.properties" })
public class ConnectionFactory {
	private static Logger log = LoggerFactory.getLogger(ConnectionFactory.class);

	private static BasicDataSource dataSource;

	private ConnectionFactory() {
	}

	public static Connection getConnection() throws SQLException {
		try {
			if (dataSource == null) {
				Properties prop = new Properties();
				final String propFileName = "application.properties";

				InputStream inputStream = ConnectionFactory.class.getClassLoader().getResourceAsStream(propFileName);

				try {
					if (inputStream != null) {
						prop.load(inputStream);
					} else {
						throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
					}

					dataSource = new BasicDataSource();
					dataSource.setDriverClassName(prop.getProperty("jdbc.driverClassName"));
					dataSource.setUrl(prop.getProperty("jdbc.url"));
					dataSource.setUsername(prop.getProperty("jdbc.username"));
					dataSource.setPassword(prop.getProperty("jdbc.password"));

				} catch (Exception e) {
					throw e;

				} finally {
					inputStream.close();
				}
			}

		} catch (Exception e) {
			if (log != null) {
				log.error(e.toString(), e);
			}
		}

		return dataSource.getConnection();
	}
}