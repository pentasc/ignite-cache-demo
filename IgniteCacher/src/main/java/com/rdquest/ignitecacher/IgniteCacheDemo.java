/*
    Available for use publically
    Check out my blog: rdquest.com
 */
package com.rdquest.ignitecacher;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
import java.util.ArrayList;
import java.util.Collection;
import javax.cache.configuration.FactoryBuilder;
import javax.sql.DataSource;
import org.apache.ignite.Ignition;
import org.apache.ignite.cache.CacheTypeMetadata;
import org.apache.ignite.cache.store.jdbc.CacheJdbcPojoStore;
import org.apache.ignite.configuration.CacheConfiguration;
import org.apache.ignite.configuration.IgniteConfiguration;

/**
 *
 * @author Corey
 */
public class IgniteCacheDemo {

    public static void main(String[] args) {

        IgniteConfiguration cfg = new IgniteConfiguration();

        CacheConfiguration ccfg = new CacheConfiguration<>();

        MysqlDataSource dataSource = new MysqlDataSource();
        dataSource.setURL("jdbc:mysql://localhost:3306");
        dataSource.setUser("ignite");
        dataSource.setPassword("ignite");


        CacheJdbcPojoStore store = new CacheJdbcPojoStore();
        store.setDataSource(dataSource);

 
        ccfg.setCacheStoreFactory(new FactoryBuilder.SingletonFactory<>(store));

        ccfg.setReadThrough(true);
        ccfg.setWriteThrough(true);

        ccfg.setWriteBehindEnabled(true);

        cfg.setCacheConfiguration(ccfg);

        Collection<CacheTypeMetadata> meta = new ArrayList<>();

        
        Ignition.start(cfg);

    }

}
