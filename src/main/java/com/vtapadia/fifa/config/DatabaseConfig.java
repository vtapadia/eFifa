package com.vtapadia.fifa.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate4.HibernateTransactionManager;
import org.springframework.orm.hibernate4.LocalSessionFactoryBean;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManager;
import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@EnableTransactionManagement
public class DatabaseConfig {

    @Value("${eFifa.db:postgresql}")
    String database;

    @Value("${eFifa.db.url}")
    String databaseUrl;

    @Value("${eFifa.db.username}")
    String databaseUsername;

    @Value("${eFifa.db.password}")
    String databasePassword;

    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        switch (database) {
            case "oracle":
                dataSource.setDriverClassName("oracle.jdbc.driver.OracleDriver");
                break;
            case "postgresql":
            default:
                dataSource.setDriverClassName("org.postgresql.Driver");
                break;
        }
        dataSource.setUrl(databaseUrl);
        dataSource.setUsername(databaseUsername);
        dataSource.setPassword(databasePassword);
        return dataSource;
    }

    @Bean
    public LocalSessionFactoryBean sessionFactory() {
        LocalSessionFactoryBean sessionFactoryBean = new LocalSessionFactoryBean();
        sessionFactoryBean.setDataSource(dataSource());
        sessionFactoryBean.setPackagesToScan("com.vtapadia.fifa.domain");
        sessionFactoryBean.setHibernateProperties(hibernateProperties());
        return sessionFactoryBean;
    }

    @Bean
    public JpaVendorAdapter jpaVendorAdapter() {
        HibernateJpaVendorAdapter adapter = new HibernateJpaVendorAdapter();
        switch (database) {
            case "oracle":
                adapter.setDatabase(Database.ORACLE);
                break;
            case "postgresql":
            default:
                adapter.setDatabase(Database.POSTGRESQL);
                //break;
        }
        adapter.setShowSql(false);
        adapter.setGenerateDdl(false);
        return adapter;
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource, JpaVendorAdapter jpaVendorAdapter) {
        LocalContainerEntityManagerFactoryBean lef = new LocalContainerEntityManagerFactoryBean();
        lef.setDataSource(dataSource);
        lef.setJpaVendorAdapter(jpaVendorAdapter);
        lef.setPackagesToScan("com.vtapadia.fifa.domain");
        return lef;
    }

    @Bean
    public HibernateTransactionManager transactionManager() {
        HibernateTransactionManager transactionManager = new HibernateTransactionManager();
        transactionManager.setSessionFactory(sessionFactory().getObject());
        return transactionManager;
    }

    @Bean
    public EntityManager entityManager() {
        return entityManagerFactory(dataSource(),jpaVendorAdapter()).getObject().createEntityManager();
    }

    public Properties hibernateProperties() {
        Properties prop = new Properties();
        switch (database) {
            case "oracle":
                prop.setProperty("hibernate.dialect","org.hibernate.dialect.Oracle10gDialect");
                break;
            case "postgresql":
            default:
                prop.setProperty("hibernate.dialect","org.hibernate.dialect.PostgreSQL9Dialect");
                break;
        }
        prop.setProperty("hibernate.show_sql","false");
        prop.setProperty("hibernate.hbm2ddl.auto","validate");
        return prop;
    }
}
