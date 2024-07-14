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

//    @Override
//    public PlaylistWithTracksResponseDto getPlaylistWithTrackDetails(String playlistId) {
//        ObjectId objectId = new ObjectId(playlistId);
//
//        Aggregation aggregation = Aggregation.newAggregation(
//                Aggregation.match(Criteria.where("_id").is(objectId)),
//                Aggregation.unwind("tracks", true),
//                Aggregation.addFields().addField("tracks.trackObjId").withValue(ConvertOperators.ToObjectId.toObjectId("$tracks.trackId")).build(),
//                Aggregation.lookup("track", "tracks.trackObjId", "_id", "trackDetails"),
//                Aggregation.unwind("trackDetails", true),
//                Aggregation.project("_id", "name", "description", "owner", "isPublic", "isCollaborative")
//                        .and("tracks.addedAt").as("trackAddedAt")
//                        .and("tracks.addedBy").as("trackAddedBy")
//                        .and("trackDetails").as("trackDetails")
//                        .and("trackDetails.duration").as("trackDetail.duration")
//                        .and("trackDetails._id").as("trackDetail._id")
//                        .and("trackDetails.name").as("trackDetail.name")
//                        .and("trackDetails.album").as("trackDetail.album")
//                        .and("trackDetails.artists").as("trackDetail.artists"),
//                Aggregation.group("$_id")
//                        .first("name").as("name")
//                        .first("description").as("description")
//                        .first("owner").as("owner")
//                        .first("isPublic").as("isPublic")
//                        .first("isCollaborative").as("isCollaborative")
//                        .push(new BasicDBObject()
//                                .append("addedAt", "$trackAddedAt")
//                                .append("addedBy", "$trackAddedBy")
//                                .append("track", "$trackDetail")
//                        ).as("tracks")
//        );
//
//        AggregationResults<PlaylistWithTracksResponseDto> results = mongoTemplate.aggregate(aggregation, "playlist", PlaylistWithTracksResponseDto.class);
//        return results.getUniqueMappedResult();
//    }

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
