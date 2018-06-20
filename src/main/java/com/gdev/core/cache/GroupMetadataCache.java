package com.gdev.core.cache;

import com.gdev.core.cache.model.GroupMetadata;
import org.ehcache.Cache;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.config.units.MemoryUnit;
import org.ehcache.expiry.Duration;
import org.ehcache.expiry.Expirations;

import java.util.concurrent.TimeUnit;

public class GroupMetadataCache {

    private static Cache cache = null;

    public static synchronized Cache getInstance() {
        if (cache == null)
        {
            cache =  EhCacheManager.getInstance().createCache("GroupMetadaCache",
                    CacheConfigurationBuilder.newCacheConfigurationBuilder(String.class, GroupMetadata.class,
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

        Cache cache = GroupMetadataCache.getInstance();
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
