package com.gdev.core.cache;

import org.ehcache.CacheManager;
import org.ehcache.config.builders.CacheManagerBuilder;

public class EhCacheManager {

    private static CacheManager cacheManager;

    public static synchronized CacheManager getInstance() {
        if (cacheManager == null)
        {
            cacheManager = CacheManagerBuilder.newCacheManagerBuilder().build();
            cacheManager.init();
        }
        return cacheManager;
    }

    public static synchronized void close() {
        if (cacheManager != null) {
            cacheManager.close();
        }
    }
}
