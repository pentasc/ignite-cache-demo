/*
    Available for use publically
    Check out my blog: rdquest.com
 */
package com.rdquest.ignitecacher;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
import java.util.Scanner;
import javax.cache.configuration.Factory;
import javax.cache.configuration.FactoryBuilder;
import org.apache.ignite.CacheConfig;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.Ignition;
import org.apache.ignite.Students;
import org.apache.ignite.StudentsKey;
import org.apache.ignite.cache.CacheAtomicityMode;
import org.apache.ignite.cache.store.CacheStore;
import org.apache.ignite.cache.store.jdbc.CacheJdbcPojoStore;
import org.apache.ignite.configuration.CacheConfiguration;
import org.apache.ignite.transactions.Transaction;

/**
 *
 * @author Corey
 */
public class IgniteCacheDemo {

    public static void main(String[] args) {

//        CacheJdbcPojoStore<Long, Students> store = new CacheJdbcPojoStore();
//        store.setDataSource(dataSource);
        // Factory factory = new FactoryBuilder.SingletonFactory<>(store);
        Factory factory = new Factory<CacheStore<? super StudentsKey, ? super Students>>() {
            @Override
            public CacheStore<? super StudentsKey, ? super Students> create() {
                CacheJdbcPojoStore<StudentsKey, Students> store = new CacheJdbcPojoStore<>();

                MysqlDataSource dataSource = new MysqlDataSource();
                dataSource.setURL("jdbc:mysql://localhost:3306/caching");
                dataSource.setUser("ignite");
                dataSource.setPassword("ignite");
                store.setDataSource(dataSource);

                return store;
            }
        };

        CacheConfiguration<StudentsKey, Students> ccfg = CacheConfig.cache("StudentCache",
                factory);

        ccfg.setReadThrough(true);
        ccfg.setWriteThrough(true);
        ccfg.setAtomicityMode(CacheAtomicityMode.TRANSACTIONAL);
        ccfg.setIndexedTypes(StudentsKey.class, Students.class);

        ccfg.setWriteBehindEnabled(true);

        try (Ignite ignite = Ignition.start()) {

            try (IgniteCache<StudentsKey, Students> cache = ignite.getOrCreateCache(ccfg)) {

                Scanner reader = new Scanner(System.in);
                System.out.println("Please select a student to update (GPA):");
                int id = reader.nextInt();

                try (Transaction tx = ignite.transactions().txStart()) {
                    StudentsKey key = new StudentsKey();
                    key.setStudentid(0);
                    Students val = cache.get(key);
                    
                    System.out.println(val);
                    
                    System.out.println("Please enter updated GPA:");
                    float newGPA = reader.nextFloat();

                    val.setGpa(newGPA);

                    cache.getAndPut(key, val);

                    tx.commit();
                    
                    System.out.println("UPDATED:");
                    System.out.println(cache.get(key));
                    
                }

            }
        }
    }
}
