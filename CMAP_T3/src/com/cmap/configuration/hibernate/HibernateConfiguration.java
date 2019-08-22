package com.cmap.configuration.hibernate;

import java.beans.PropertyVetoException;
import java.util.Properties;
import javax.sql.DataSource;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import com.cmap.annotation.Log;
import com.mchange.v2.c3p0.ComboPooledDataSource;

@Configuration
@EnableTransactionManagement
@ComponentScan({ "com.cmap.configuration" })
@PropertySource(value = { "classpath:application.properties" })
public class HibernateConfiguration {
	@Log
	private static Logger log;

    @Autowired
    private Environment environment;

    @Bean
    @Primary
    public LocalSessionFactoryBean sessionFactory() {
    	LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
        sessionFactory.setDataSource(dataSource());
        sessionFactory.setPackagesToScan(new String[] { "com.cmap.model", "com.cmap.plugin.module" });
        sessionFactory.setHibernateProperties(hibernateProperties());
        sessionFactory.setEntityInterceptor(new HibernateInterceptor());
        return sessionFactory;
     }

    @Bean
    public DataSource dataSource() {
    	ComboPooledDataSource dataSource = new ComboPooledDataSource();
    	try {
			dataSource.setDriverClass(environment.getRequiredProperty("primary.jdbc.driverClassName"));
			dataSource.setJdbcUrl(environment.getRequiredProperty("primary.jdbc.url"));
			dataSource.setUser(environment.getRequiredProperty("primary.jdbc.username"));
			dataSource.setPassword(environment.getRequiredProperty("primary.jdbc.password"));
			dataSource.setMaxPoolSize(Integer.parseInt(environment.getRequiredProperty("hibernate.c3p0.max_pool_size")));
			dataSource.setMinPoolSize(Integer.parseInt(environment.getRequiredProperty("hibernate.c3p0.min_pool_size")));
			dataSource.setInitialPoolSize(Integer.parseInt(environment.getRequiredProperty("hibernate.c3p0.initial_pool_size")));
			dataSource.setMaxIdleTime(Integer.parseInt(environment.getRequiredProperty("hibernate.c3p0.max_idle_time")));
			dataSource.setAcquireRetryAttempts(Integer.parseInt(environment.getRequiredProperty("hibernate.c3p0.acquire_retry_attempts")));
			dataSource.setMaxStatements(Integer.parseInt(environment.getRequiredProperty("hibernate.c3p0.max_statements")));
			dataSource.setIdleConnectionTestPeriod(Integer.parseInt(environment.getRequiredProperty("hibernate.c3p0.idle_connection_test_period")));
			dataSource.setCheckoutTimeout(Integer.parseInt(environment.getRequiredProperty("hibernate.c3p0.timeout")));
			dataSource.setTestConnectionOnCheckout(new Boolean(environment.getRequiredProperty("hibernate.c3p0.test_connection_on_checkout")));
			dataSource.setStatementCacheNumDeferredCloseThreads(
			        Integer.parseInt(environment.getRequiredProperty("hibernate.c3p0.statement_cache_num_deferred_close_threads")));
			dataSource.setAcquireIncrement(Integer.parseInt(environment.getRequiredProperty("hibernate.c3p0.acquire_increment")));
			dataSource.setBreakAfterAcquireFailure(new Boolean(environment.getRequiredProperty("hibernate.c3p0.break_after_acquire_failure")));
			dataSource.setPreferredTestQuery(environment.getRequiredProperty("hibernate.c3p0.preferred_test_query"));

		} catch (IllegalStateException | PropertyVetoException e) {
			log.error(e.toString(), e);
		}

    	/*
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(environment.getRequiredProperty("jdbc.driverClassName"));
        dataSource.setUrl(environment.getRequiredProperty("jdbc.url"));
        dataSource.setUsername(environment.getRequiredProperty("jdbc.username"));
        dataSource.setPassword(environment.getRequiredProperty("jdbc.password"));
        */
        return dataSource;
    }

    @Bean(name = "secondSessionFactory")
    public LocalSessionFactoryBean secondSessionFactory() {
        LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
        sessionFactory.setDataSource(secondDataSource());
        sessionFactory.setPackagesToScan(new String[] { "com.cmap.model", "com.cmap.plugin.module" });
        sessionFactory.setHibernateProperties(hibernateProperties());
        sessionFactory.setEntityInterceptor(new HibernateInterceptor());
        return sessionFactory;
     }

    @Bean
    public DataSource secondDataSource() {
        ComboPooledDataSource dataSource = new ComboPooledDataSource();
        try {
            dataSource.setDriverClass(environment.getRequiredProperty("secondary.jdbc.driverClassName"));
            dataSource.setJdbcUrl(environment.getRequiredProperty("secondary.jdbc.url"));
            dataSource.setUser(environment.getRequiredProperty("secondary.jdbc.username"));
            dataSource.setPassword(environment.getRequiredProperty("secondary.jdbc.password"));
            dataSource.setMaxPoolSize(Integer.parseInt(environment.getRequiredProperty("hibernate.c3p0.max_pool_size")));
            dataSource.setMinPoolSize(Integer.parseInt(environment.getRequiredProperty("hibernate.c3p0.min_pool_size")));
            dataSource.setInitialPoolSize(Integer.parseInt(environment.getRequiredProperty("hibernate.c3p0.initial_pool_size")));
            dataSource.setMaxIdleTime(Integer.parseInt(environment.getRequiredProperty("hibernate.c3p0.max_idle_time")));
            dataSource.setAcquireRetryAttempts(Integer.parseInt(environment.getRequiredProperty("hibernate.c3p0.acquire_retry_attempts")));
            dataSource.setMaxStatements(Integer.parseInt(environment.getRequiredProperty("hibernate.c3p0.max_statements")));
            dataSource.setIdleConnectionTestPeriod(Integer.parseInt(environment.getRequiredProperty("hibernate.c3p0.idle_connection_test_period")));
            dataSource.setCheckoutTimeout(Integer.parseInt(environment.getRequiredProperty("hibernate.c3p0.timeout")));
            dataSource.setTestConnectionOnCheckout(new Boolean(environment.getRequiredProperty("hibernate.c3p0.test_connection_on_checkout")));
            dataSource.setStatementCacheNumDeferredCloseThreads(
                    Integer.parseInt(environment.getRequiredProperty("hibernate.c3p0.statement_cache_num_deferred_close_threads")));
            dataSource.setAcquireIncrement(Integer.parseInt(environment.getRequiredProperty("hibernate.c3p0.acquire_increment")));
            dataSource.setBreakAfterAcquireFailure(new Boolean(environment.getRequiredProperty("hibernate.c3p0.break_after_acquire_failure")));
            dataSource.setPreferredTestQuery(environment.getRequiredProperty("hibernate.c3p0.preferred_test_query"));

        } catch (IllegalStateException | PropertyVetoException e) {
            log.error(e.toString(), e);
        }

        /*
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(environment.getRequiredProperty("jdbc.driverClassName"));
        dataSource.setUrl(environment.getRequiredProperty("jdbc.url"));
        dataSource.setUsername(environment.getRequiredProperty("jdbc.username"));
        dataSource.setPassword(environment.getRequiredProperty("jdbc.password"));
        */
        return dataSource;
    }

    private Properties hibernateProperties() {
        Properties properties = new Properties();
        properties.put("hibernate.dialect", environment.getRequiredProperty("hibernate.dialect"));
        properties.put("hibernate.show_sql", environment.getRequiredProperty("hibernate.show_sql"));
        properties.put("hibernate.format_sql", environment.getRequiredProperty("hibernate.format_sql"));
        properties.put("hibernate.ejb.interceptor", environment.getRequiredProperty("hibernate.ejb.interceptor"));
        return properties;
    }

    @Bean
    public HibernateTransactionManager transactionManager() {
        HibernateTransactionManager transactionManager = new HibernateTransactionManager();
        transactionManager.setSessionFactory(sessionFactory().getObject());
        return transactionManager;
    }
}
