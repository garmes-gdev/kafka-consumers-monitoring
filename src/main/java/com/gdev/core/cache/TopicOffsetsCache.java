package com.gdev.core.cache;

import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.config.units.MemoryUnit;
import org.ehcache.expiry.Duration;
import org.ehcache.expiry.Expirations;

import org.ehcache.Cache;
import java.util.concurrent.TimeUnit;

public class TopicOffsetsCache {

    private static Cache cache = null;

    public static synchronized org.ehcache.Cache getInstance() {
        if (cache == null)
        {
            cache =  EhCacheManager.getInstance().createCache("TopicOffsetCache",  CacheConfigurationBuilder.newCacheConfigurationBuilder(String.class, Long.class,
                    ResourcePoolsBuilder.newResourcePoolsBuilder().heap(200, MemoryUnit.MB).offheap(300, MemoryUnit.MB)
                    ).withExpiry(Expirations.timeToLiveExpiration(Duration.of(1, TimeUnit.HOURS)))
            );
        }
        return cache;
    }

    public static synchronized void close() {
        EhCacheManager.close();
    }


    public static void main(String[] args) {

        Cache cache = TopicOffsetsCache.getInstance();
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
