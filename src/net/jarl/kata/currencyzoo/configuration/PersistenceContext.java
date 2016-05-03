package net.jarl.kata.currencyzoo.configuration;

import java.util.Properties;
import java.util.stream.Stream;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import net.jarl.kata.currencyzoo.model.Entities;

@EnableJpaRepositories(basePackageClasses={Entities.class})
@EnableTransactionManagement
@Configuration
public class PersistenceContext {

    private static final String MODEL_PKG = "net.jarl.kata.currencyzoo.model";

    @Configuration
    @PropertySource("classpath:application.properties")
    public static class ApplicationProperties {}

    @Bean(destroyMethod="close")
    public DataSource dataSource( Environment env ) {
        HikariConfig config = new HikariConfig();
        config.setDriverClassName( env.getRequiredProperty( "db.driver" ) );
        config.setJdbcUrl( env.getRequiredProperty( "db.url" ) );
        config.setUsername( env.getRequiredProperty( "db.username" ) );
        config.setPassword( env.getRequiredProperty( "db.password" ) );

        return new HikariDataSource( config );
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(
        DataSource ds,
        Environment env )
    {
        LocalContainerEntityManagerFactoryBean factoryBean =
            new LocalContainerEntityManagerFactoryBean();
        factoryBean.setDataSource( ds );
        factoryBean.setJpaVendorAdapter( new HibernateJpaVendorAdapter() );
        factoryBean.setPackagesToScan( MODEL_PKG );

        Properties jpaProperties = new Properties();
        jpaProperties.put( "hibernate.enable_lazy_load_no_trans", "true" );
        Stream.of(
            "hibernate.hbm2ddl.auto",
            "hibernate.implicit_naming_strategy",
            "hibernate.physical_naming_strategy",
            "hibernate.show_sql",
            "hibernate.format_sql"
        ).forEach( p -> jpaProperties.put( p, env.getRequiredProperty( p ) ) );

        factoryBean.setJpaProperties( jpaProperties );

        return factoryBean;
    }

    @Bean
    public JpaTransactionManager transactionManager( EntityManagerFactory emf ) {
        JpaTransactionManager txManager = new JpaTransactionManager();
        txManager.setEntityManagerFactory( emf );
        return txManager;
    }
}
