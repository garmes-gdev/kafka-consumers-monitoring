package com.gdev.core.cache;


import com.gdev.core.cache.model.LagDataPoint;
import org.ehcache.Cache;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.config.units.MemoryUnit;
import org.ehcache.expiry.Duration;
import org.ehcache.expiry.Expirations;

import java.util.concurrent.TimeUnit;

public class LagCache {

    private static Cache cache = null;

    public static synchronized Cache getInstance() {
        if (cache == null)
        {
            cache =  EhCacheManager.getInstance().createCache("lagCache",  CacheConfigurationBuilder.newCacheConfigurationBuilder(String.class, LagDataPoint.class,
                    ResourcePoolsBuilder.heap(10).offheap(500, MemoryUnit.MB)
                    ).withExpiry(Expirations.timeToLiveExpiration(Duration.of(12, TimeUnit.HOURS)))
            );
        }
        return cache;
    }

    public static synchronized void close() {
        EhCacheManager.close();
    }


    public static void main(String[] args) {

        Cache cache = LagCache.getInstance();
        // Store a value
        cache.put("key", 1);
        // Retrieve the value and print it out
        System.out.printf("key = %s\n", cache.get("key"));
        // Store a value
        cache.put("key", 2);
        // Retrieve the value and print it out
        System.out.printf("key = %s\n", cache.get("key"));

        // Stop the cache manager and release all resources
    }
}
