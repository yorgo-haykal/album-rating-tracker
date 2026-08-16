package com.yorgohaykal.album_rating_tracker.controller;

import com.yorgohaykal.album_rating_tracker.dto.AlbumResponse;
import com.yorgohaykal.album_rating_tracker.entity.Album;
import com.yorgohaykal.album_rating_tracker.entity.ScoringWeights;
import com.yorgohaykal.album_rating_tracker.repository.AlbumRepository;
import com.yorgohaykal.album_rating_tracker.repository.ScoringWeightsRepository;
import com.yorgohaykal.album_rating_tracker.service.ScoringService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class AlbumController {

    private final AlbumRepository albumRepository;
    private final ScoringWeightsRepository scoringWeightsRepository;
    private final ScoringService scoringService;

    public AlbumController(
            AlbumRepository albumRepository,
            ScoringWeightsRepository scoringWeightsRepository,
            ScoringService scoringService
    ) {
        this.albumRepository = albumRepository;
        this.scoringWeightsRepository = scoringWeightsRepository;
        this.scoringService = scoringService;
    }

    @GetMapping("/api/albums")
    public List<AlbumResponse> getAlbums(@RequestParam Long userId) {
        List<Album> albums = albumRepository.findByUserId(userId);

        ScoringWeights weights = scoringWeightsRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalStateException(
                        "No scoring weights found for user " + userId));

        return albums.stream()
                .map(album -> toResponse(album, weights))
                .collect(Collectors.toList());
    }

    private AlbumResponse toResponse(Album album, ScoringWeights weights) {
        AlbumResponse response = new AlbumResponse();
        response.setId(album.getId());
        response.setTitle(album.getTitle());
        response.setArtist(album.getArtist());
        response.setGenre(album.getGenre());
        response.setReleaseYear(album.getReleaseYear());
        response.setDateAdded(album.getDateAdded());
        response.setSongwritingScore(album.getSongwritingScore());
        response.setProductionScore(album.getProductionScore());
        response.setCohesionScore(album.getCohesionScore());
        response.setTracklistScore(album.getTracklistScore());
        response.setReplayValueScore(album.getReplayValueScore());
        response.setEmotionalImpactScore(album.getEmotionalImpactScore());
        response.setWeightedTotal(scoringService.calculateWeightedTotal(album, weights));
        return response;
    }

}
