package com.vibecast.recommendservice.service;

import lombok.RequiredArgsConstructor;
import org.apache.mahout.cf.taste.model.DataModel;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class CachedDataModelService {
    private final DataModelService dataModelService;
    private final ConcurrentHashMap<String, DataModelCacheWrapper> cache = new ConcurrentHashMap<>();

    public DataModel getCachedDataModel(String filePath) throws IOException {
        // Refresh the cache if expired
        DataModelCacheWrapper cached = cache.get(filePath);
        if (cached == null || isCacheExpired(cached)) {
            // Ensure only one thread can perform the refresh at a time
            synchronized (this) {
                // Double-check if another thread has already refreshed the cache
                cached = cache.get(filePath);
                if (cached == null || isCacheExpired(cached)) {
                    DataModel model = dataModelService.createDataModel(filePath);
                    cached = new DataModelCacheWrapper(model, System.currentTimeMillis());
                    cache.put(filePath, cached);
                }
            }
        }
        return cached.dataModel();
    }

    private boolean isCacheExpired(DataModelCacheWrapper cached) {
        long cacheDuration = 24 * 60 * 60 * 1000;
        return System.currentTimeMillis() - cached.timestamp() > cacheDuration;
    }

    private record DataModelCacheWrapper(DataModel dataModel, long timestamp) {
    }
}
