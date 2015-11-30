/*
    Available for use publically
    Check out my blog: rdquest.com
 */
package com.rdquest.ignite;

import org.apache.ignite.IgniteCache;

/**
 *
 * @author Corey
 */
public class IgniteCacheDAO {
    
    private final IgniteCache<String, String> cache;
    
    public IgniteCacheDAO(IgniteCache cache) {
        this.cache = cache;
    }
    
    public void add(String key, String value)
    {
        cache.put(key, value);
    }
    
    public void delete(String key)
    {
        cache.remove(key);
    }
    
    public void update(String key, String value)
    {
        cache.put(key, value);
    }
    
    public String get(String key)
    {
       return cache.get(key);
    }
    
}
