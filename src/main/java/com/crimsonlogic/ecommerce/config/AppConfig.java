package com.crimsonlogic.ecommerce.config;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@PropertySource("classpath:application.properties")
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = "com.crimsonlogic.ecommerce.repository")
@ComponentScan(basePackages = "com.crimsonlogic.ecommerce.service")
public class AppConfig {

    @Value("${db.driver}")
    private String driver;

    @Value("${db.url}")
    private String url;

    @Value("${db.username}")
    private String username;

    @Value("${db.password}")
    private String password;

    @Value("${jpa.dialect}")
    private String dialect;

    @Value("${jpa.show-sql}")
    private String showSql;

    @Value("${jpa.format-sql}")
    private String formatSql;

    @Value("${jpa.ddl-auto}")
    private String ddlAuto;

    // ==========================================================
    // DATA SOURCE
    // ==========================================================

    @Bean
    public DataSource dataSource() {

        HikariDataSource dataSource = new HikariDataSource();

        dataSource.setDriverClassName(driver);
        dataSource.setJdbcUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);

        return dataSource;
    }


    // ==========================================================
    // ENTITY MANAGER FACTORY
    // ==========================================================

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(
            DataSource dataSource) {

        LocalContainerEntityManagerFactoryBean factory =
                new LocalContainerEntityManagerFactoryBean();

        factory.setDataSource(dataSource);

        factory.setPackagesToScan("com.crimsonlogic.ecommerce.entity");

        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();

        vendorAdapter.setShowSql(Boolean.parseBoolean(showSql));

        factory.setJpaVendorAdapter(vendorAdapter);

        Properties properties = new Properties();

        properties.put("hibernate.dialect", dialect);

        properties.put("hibernate.hbm2ddl.auto", ddlAuto);

        properties.put("hibernate.show_sql", showSql);

        properties.put("hibernate.format_sql", formatSql);

        factory.setJpaProperties(properties);

        return factory;
    }


    // ==========================================================
    // TRANSACTION MANAGER
    // ==========================================================

    @Bean
    public JpaTransactionManager transactionManager(
            EntityManagerFactory entityManagerFactory) {

        return new JpaTransactionManager(
                entityManagerFactory
        );
    }


    // ==========================================================
    // EXCEPTION TRANSLATION
    // ==========================================================

    @Bean
    public PersistenceExceptionTranslationPostProcessor
    exceptionTranslation() {

        return new PersistenceExceptionTranslationPostProcessor();
    }
    
    @Bean
    public static PropertySourcesPlaceholderConfigurer
    propertySourcesPlaceholderConfigurer() {

        return new PropertySourcesPlaceholderConfigurer();
    }
}
