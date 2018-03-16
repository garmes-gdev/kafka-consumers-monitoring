package com.gdev.core.cache;

import javax.cache.Cache;
import javax.cache.CacheManager;
import javax.cache.Caching;
import javax.cache.configuration.MutableConfiguration;
import javax.cache.spi.CachingProvider;

public class AppCache {

    private static javax.cache.Cache<String, String> cache ;
    private static CacheManager cacheManager;
    private static AppCache INSTANCE = null;

    private AppCache(){
        // Construct a simple local cache manager with default configuration
        CachingProvider jcacheProvider = Caching.getCachingProvider();
        this.cacheManager = jcacheProvider.getCacheManager();

        MutableConfiguration<String, String> configuration = new MutableConfiguration<>();
        configuration.setTypes(String.class, String.class);

        // create a cache using the supplied configuration
        this.cache = cacheManager.createCache("monitoring-cache", configuration);
    }

    public static synchronized Cache getInstance() {
        if (INSTANCE == null)
        {   INSTANCE = new AppCache();
        }
        return INSTANCE.cache;
    }

    public static synchronized void close() {
        if (INSTANCE != null) {
            INSTANCE.cacheManager.close();
        }
    }


    public static void main(String[] args) {

        Cache cache = AppCache.getInstance();
        // Store a value
        cache.put("key", "value1");
        // Retrieve the value and print it out
        System.out.printf("key = %s\n", cache.get("key"));
        // Store a value
        cache.put("key", "value2");
        // Retrieve the value and print it out
        System.out.printf("key = %s\n", cache.get("key"));

        // Stop the cache manager and release all resources
    }

}

