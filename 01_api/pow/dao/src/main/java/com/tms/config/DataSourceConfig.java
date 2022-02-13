package com.tms.config;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

/**
 * Created by son.nguyencong on 12/10/16.
 */

@Configuration
@EnableTransactionManagement
public class DataSourceConfig {

    public static final String DB_JDBC_TEMPLATE = "firstJdbcTemplate";
    public static final String DB_TRANSACTION_MANAGER = "firstTransactionManager";

    public static final String LOG_DB_JDBC_TEMPLATE = "logJdbcTemplate";
    public static final String LOG_DB_TRANSACTION_MANAGER = "logFirstTransactionManager";

    public static final String MM_JDBC_TEMPLATE = "memJdbcTemplate";
    public static final String MM_TRANSACTION_MANAGER = "secondTransactionManager";

    @Bean
    @Primary
    @ConfigurationProperties(prefix="app.datasource")
    public DataSourceProperties firstDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean
    @Primary
    @ConfigurationProperties(prefix="app.datasource.hikari-config")
    public HikariDataSource hikariDataSource() {
        HikariDataSource build = firstDataSourceProperties().initializeDataSourceBuilder().type(HikariDataSource.class)
                .build();

        return build;
    }

    @Bean
    public JdbcTemplate firstJdbcTemplate(DataSource hikariDataSource) {
        return new JdbcTemplate(hikariDataSource);
    }


    /*@Bean(name = "logDataSource")
    @ConfigurationProperties(prefix="app.datasource.hikari-config-2")
    public HikariDataSource logDataSource() {
        HikariDataSource build = firstDataSourceProperties().initializeDataSourceBuilder().type(HikariDataSource.class)
                .build();
        return build;
    }

    @Bean(name = LOG_DB_JDBC_TEMPLATE)
    @Autowired
    public JdbcTemplate logJdbcTemplate(@Qualifier("logDataSource") DataSource logDataSource) {
        return new JdbcTemplate(logDataSource);
    }*/


    @Bean(name = "memDataSource")
    @ConfigurationProperties(prefix="inmemory.datasource")
    public DataSource secondDataSource() {
        DataSource ds =  DataSourceBuilder.create().build();
        return ds;
    }


    @Bean(name = MM_JDBC_TEMPLATE)
    @Autowired
    public JdbcTemplate secondJdbcTemplate(@Qualifier("memDataSource") DataSource secondDataSource) {
        return new JdbcTemplate(secondDataSource);
    }

    /*@Bean
    public PlatformTransactionManager firstTransactionManager(DataSource hikariDataSource){
        return new DataSourceTransactionManager(hikariDataSource);
    }

    @Bean
    public PlatformTransactionManager logTransactionManager(DataSource logDataSource){
        return new DataSourceTransactionManager(logDataSource);
    }

    @Bean
    @Autowired
    public PlatformTransactionManager secondTransactionManager(@Qualifier("secondDataSource") DataSource secondDataSource){
        return new DataSourceTransactionManager(secondDataSource);
    }*/
}
