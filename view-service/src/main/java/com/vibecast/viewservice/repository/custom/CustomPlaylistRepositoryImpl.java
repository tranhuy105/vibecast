package com.vibecast.viewservice.repository.custom;

import com.mongodb.BasicDBObject;
import com.vibecast.viewservice.model.response.PlaylistSummary;
import com.vibecast.viewservice.model.response.PlaylistTrackResponseDto;
import com.vibecast.viewservice.model.response.PlaylistWithTracksResponseDto;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class CustomPlaylistRepositoryImpl implements CustomPlaylistRepository {
    private final MongoTemplate mongoTemplate;

    @Override
    public List<PlaylistSummary> findByOwnerUserIdWithTrackCount(String ownerId) {
        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.match(Criteria.where("owner.userId").is(ownerId)),
                Aggregation.project("id", "name", "description", "owner", "public", "collaborative", "previewImage")
                        .andExpression("size(tracks)").as("tracksCount")
        );

        AggregationResults<PlaylistSummary> results = mongoTemplate.aggregate(aggregation, "playlist", PlaylistSummary.class);
        return results.getMappedResults();
    }

    @Override
    public PlaylistSummary findPlaylistByIdWithTotalTrackCount(String playlistId) {
        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.match(Criteria.where("_id").is(playlistId)),
                Aggregation.project("id", "name", "description", "owner", "public", "collaborative", "previewImage")
                        .andExpression("size(tracks)").as("tracksCount")
        );

        AggregationResults<PlaylistSummary> results = mongoTemplate.aggregate(aggregation, "playlist", PlaylistSummary.class);
        return results.getUniqueMappedResult();
    }

    @Override
    public List<PlaylistTrackResponseDto> getPaginatedTracks(String playlistId, int limit, int offset) {
        ObjectId objectId = new ObjectId(playlistId);

        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.match(Criteria.where("_id").is(objectId)),
                Aggregation.unwind("tracks", true),
                Aggregation.addFields().addField("tracks.trackObjId").withValue(ConvertOperators.ToObjectId.toObjectId("$tracks.trackId")).build(),
                Aggregation.lookup("track", "tracks.trackObjId", "_id", "trackDetails"),
                Aggregation.skip(offset),
                Aggregation.limit(limit),
                Aggregation.unwind("trackDetails", true),
                Aggregation.project("_id", "tracks.addedAt", "tracks.addedBy", "trackDetails")
                        .and("trackDetails._id").as("track._id")
                        .and("trackDetails.name").as("track.name")
                        .and("trackDetails.duration").as("track.duration")
                        .and("trackDetails.album").as("track.album")
                        .and("trackDetails.artists").as("track.artists")

        );

        AggregationResults<PlaylistTrackResponseDto> results = mongoTemplate.aggregate(aggregation, "playlist", PlaylistTrackResponseDto.class);
        return results.getMappedResults();
    }

}
