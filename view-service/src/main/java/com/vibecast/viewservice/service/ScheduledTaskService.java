package com.vibecast.viewservice.service;

import com.vibecast.viewservice.model.persistance.Playlist;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.BulkOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class ScheduledTaskService {
    private final CacheService cacheService;
    private final MongoTemplate mongoTemplate;

    @Scheduled(cron = "0 0 0 * * ?") // Runs daily at midnight
    public void updatePlaylistSaveCounts() {
        Set<String> keys = cacheService.getAllPlaylistSavedCountKeys();

        BulkOperations bulkOps = mongoTemplate.bulkOps(BulkOperations.BulkMode.UNORDERED, Playlist.class);

        for (String key : keys) {
            String playlistId = key.split(":")[2];
            Long count = cacheService.getPlaylistSaveCount(playlistId);

            Update update = new Update();
            update.inc("savedCount", count != null ? count.intValue() : 0);

            bulkOps.updateOne(
                    Query.query(Criteria.where("id").is(playlistId)),
                    update
            );

            cacheService.removePlaylistSaveCount(playlistId);
        }

        bulkOps.execute();
    }
}
