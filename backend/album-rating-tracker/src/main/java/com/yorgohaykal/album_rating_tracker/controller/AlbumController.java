package com.yorgohaykal.album_rating_tracker.controller;

import com.yorgohaykal.album_rating_tracker.dto.AlbumResponse;
import com.yorgohaykal.album_rating_tracker.dto.CreateAlbumRequest;
import com.yorgohaykal.album_rating_tracker.entity.Album;
import com.yorgohaykal.album_rating_tracker.entity.AppUser;
import com.yorgohaykal.album_rating_tracker.entity.ScoringWeights;
import com.yorgohaykal.album_rating_tracker.repository.AlbumRepository;
import com.yorgohaykal.album_rating_tracker.repository.ScoringWeightsRepository;
import com.yorgohaykal.album_rating_tracker.service.ScoringService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@AllArgsConstructor
public class AlbumController {

    private final AlbumRepository albumRepository;
    private final ScoringWeightsRepository scoringWeightsRepository;
    private final ScoringService scoringService;

    @GetMapping("/api/albums")
    public List<AlbumResponse> getAlbums(Authentication authentication) {
        Long userId = (Long) authentication.getPrincipal();

        List<Album> albums = albumRepository.findByUserId(userId);

        ScoringWeights weights = scoringWeightsRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalStateException(
                        "No scoring weights found for user " + userId));

        return albums.stream()
                .map(album -> toResponse(album, weights))
                .collect(Collectors.toList());
    }

    @PostMapping("/api/albums")
    public ResponseEntity<AlbumResponse> createAlbum(
            @Valid @RequestBody CreateAlbumRequest request,
            Authentication authentication
    ) {
        Long userId = (Long) authentication.getPrincipal();

        AppUser userRef = new AppUser();
        userRef.setId(userId);

        Album album = new Album();
        album.setUser(userRef);
        album.setTitle(request.getTitle());
        album.setArtist(request.getArtist());
        album.setGenre(request.getGenre());
        album.setReleaseYear(request.getReleaseYear());
        album.setSongwritingScore(request.getSongwritingScore());
        album.setProductionScore(request.getProductionScore());
        album.setCohesionScore(request.getCohesionScore());
        album.setTracklistScore(request.getTracklistScore());
        album.setReplayValueScore(request.getReplayValueScore());
        album.setEmotionalImpactScore(request.getEmotionalImpactScore());

        Album savedAlbum = albumRepository.save(album);

        ScoringWeights weights = scoringWeightsRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalStateException(
                        "No scoring weights found for user " + userId));

        return ResponseEntity.status(201).body(toResponse(savedAlbum, weights));
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
