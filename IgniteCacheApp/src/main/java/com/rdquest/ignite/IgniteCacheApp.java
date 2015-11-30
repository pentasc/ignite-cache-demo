/*
    Available for use publically
    Check out my blog: rdquest.com
 */
package com.rdquest.ignite;

import java.util.Scanner;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.Ignition;
import org.apache.ignite.cache.CacheAtomicityMode;
import org.apache.ignite.configuration.CacheConfiguration;

/**
 *
 * @author Corey
 */
public class IgniteCacheApp {

    public static void main(String[] args) {

        Ignition.setClientMode(true);

        try (Ignite ignite = Ignition.start()) {
            CacheConfiguration<String, String> cfg = new CacheConfiguration<>();
            cfg.setName("CacheApp");
            cfg.setAtomicityMode(CacheAtomicityMode.TRANSACTIONAL);

            final IgniteCache<String, String> cache = ignite.getOrCreateCache(cfg);

            IgniteCacheDAO cacheDAO = new IgniteCacheDAO(cache);

            Scanner reader = new Scanner(System.in);

            while (true) {

                System.out.println("Please select an operation to perform on the cache:");
                System.out.println("Add: 0");
                System.out.println("Update: 1");
                System.out.println("Delete: 2");
                System.out.println("Get: 3");

                int selectOp = reader.nextInt();

                switch (selectOp) {

                    case 0:
                        System.out.println("Add: Enter in the following format: {KEY VALUE} (Space in between)");
                        cacheDAO.add(reader.next(), reader.next());
                        break;
                    case 1:
                        System.out.println("Update: Enter in the following format: {KEY VALUE} (Space in between)");
                        cacheDAO.update(reader.next(), reader.next());
                        break;
                    case 2:
                        System.out.println("Remove: Enter in the following format: {KEY}");
                        cacheDAO.delete(reader.next());
                        break;
                    case 3:
                        System.out.println("Get: Enter in the following format: {KEY} (Returns value)");
                        String key = reader.next();
                        System.out.println("The cached value for the " + key + " is: " + cacheDAO.get(key));
                        break;
                    default:
                        System.out.println("Please select a valid value (0-3)");

                }

            }

        }
    }
}
