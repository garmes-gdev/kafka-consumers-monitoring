package com.gdev.core.cache;

import com.ldev.kafka.metrics.MetricsResult;
import org.ehcache.Cache;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.config.units.MemoryUnit;
import org.ehcache.expiry.Duration;
import org.ehcache.expiry.Expirations;

import java.util.concurrent.TimeUnit;

public class MonitoringMetricsCache {
    private static Cache<String, MetricsResult> cache;

    private MonitoringMetricsCache() { }

    public static synchronized Cache<String, MetricsResult> getInstance() {
        if (cache == null) {
            cache = EhCacheManager.getInstance()
                    .createCache("monitoringMetricsCache", CacheConfigurationBuilder.newCacheConfigurationBuilder(String.class, MetricsResult.class,
                            ResourcePoolsBuilder.heap(10).offheap(20, MemoryUnit.MB))
                            .withExpiry(Expirations.timeToLiveExpiration(Duration.of(1, TimeUnit.HOURS)))
                    );
        }
        return cache;
    }
}
