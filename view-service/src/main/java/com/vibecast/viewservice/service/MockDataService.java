package com.vibecast.viewservice.service;

import com.vibecast.viewservice.model.persistance.*;
import com.vibecast.viewservice.model.persistance.ref.*;
import com.vibecast.viewservice.repository.*;
import com.vibecast.viewservice.repository.PlaylistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.github.javafaker.Faker;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class MockDataService {
    private final PlaylistRepository playlistRepository;
    private final AlbumRepository albumRepository;
    private final ArtistRepository artistRepository;
    private final TrackRepository trackRepository;

    private final Faker faker = new Faker();
    private final Map<String, String> albumIds = new HashMap<>();
    private final Map<String, String> artistIds = new HashMap<>();
    private final Map<String, String> trackIds = new HashMap<>();
    private final Map<String, String> userIds = new HashMap<>();

    public void generateMockData() {
        generateMockArtists();
        generateMockAlbums();
        generateMockTracks();
        generateMockPlaylists();
//        generateMockUserCollections();
    }

    private void generateMockPlaylists() {
        for (int i = 0; i < 50; i++) {
            Playlist playlist = new Playlist(
                    null,
                    faker.lorem().sentence(),
                    faker.lorem().paragraph(),
                    generateMockOwner(),
                    faker.bool().bool(),
                    faker.bool().bool(),
                    0,
                    generateMockPlaylistTracks(),
                    generateMockImage()
            );
            playlistRepository.save(playlist);
        }
    }

    private void generateMockAlbums() {
        for (int i = 0; i < 50; i++) {
            Album album = new Album(
                    null,
                    faker.music().genre(),
                    LocalDate.now(),
                    generateMockImage(),
                    generateMockArtistRefs()
            );
            Album savedAlbum = albumRepository.save(album);
            albumIds.put(savedAlbum.getId(), savedAlbum.getName());
        }
    }

    private void generateMockArtists() {
        for (int i = 0; i < 10; i++) {
            String name;
            do {
                name = faker.artist().name();
            } while (artistRepository.existsByName(name));

            Artist artist = new Artist(
                    null,
                    name,
                    generateMockGenres(),
                    faker.lorem().paragraph(),
                    generateMockImage(),
                    generateMockImage()
            );
            Artist savedArtist = artistRepository.save(artist);
            artistIds.put(artist.getName(), savedArtist.getId());
        }
    }

    private List<PlaylistTrackRef> generateMockPlaylistTracks() {
        List<PlaylistTrackRef> tracks = new ArrayList<>();
        List<String> trackIdList = new ArrayList<>(trackIds.values());

        for (int i = 0; i < faker.random().nextInt(5,30); i++) {
            String trackId = trackIdList.get(faker.random().nextInt(trackIdList.size()));
            tracks.add(new PlaylistTrackRef(
                    LocalDateTime.now(),
                    generateMockOwner(),
                    trackId
            ));
        }
        return tracks;
    }

    private void generateMockTracks() {
        for (int i = 0; i < 500; i++) {
            Track track = new Track(
                    null,
                    faker.music().instrument(),
                    faker.random().nextLong(100000) + 100000,
                    generateMockAlbumRef(),
                    generateMockArtistRefs()
            );
            Track savedTrack = trackRepository.save(track);
            trackIds.put(track.getName(), savedTrack.getId());
        }
    }



    private OwnerRef generateMockOwner() {
        String userId = faker.idNumber().valid();
        String name = faker.name().fullName();
        userIds.put(name, userId);
        return new OwnerRef(userId, name);
    }

    private Image generateMockImage() {
        return new Image(faker.internet().url(), faker.number().numberBetween(100, 1000), faker.number().numberBetween(100, 1000));
    }

    private List<ArtistRef> generateMockArtistRefs() {
        List<ArtistRef> artistRefs = new ArrayList<>();
        List<Map.Entry<String, String>> shuffledArtistIds = new ArrayList<>(artistIds.entrySet());
        Collections.shuffle(shuffledArtistIds);

        int numArtists = faker.number().numberBetween(1, 4);
        for (int i = 0; i < numArtists && i < shuffledArtistIds.size(); i++) {
            Map.Entry<String, String> entry = shuffledArtistIds.get(i);
            artistRefs.add(new ArtistRef(entry.getValue(), entry.getKey()));
        }

        return artistRefs;
    }

    private List<String> generateMockGenres() {
        List<String> genres = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            genres.add(faker.music().genre());
        }
        return genres;
    }

    private AlbumRef generateMockAlbumRef() {
        List<Map.Entry<String, String>> albumList = new ArrayList<>(albumIds.entrySet());
        Collections.shuffle(albumList);

        Map.Entry<String, String> selectedAlbum = albumList.get(0);

        String albumId = selectedAlbum.getKey();
        String albumName = selectedAlbum.getValue();

        return new AlbumRef(albumId, albumName, generateMockImage());
    }
}
