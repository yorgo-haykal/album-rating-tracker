package com.yorgohaykal.album_rating_tracker.controller;

import com.yorgohaykal.album_rating_tracker.dto.ScoringWeightsResponse;
import com.yorgohaykal.album_rating_tracker.dto.UpdateWeightsRequest;
import com.yorgohaykal.album_rating_tracker.entity.ScoringWeights;
import com.yorgohaykal.album_rating_tracker.repository.ScoringWeightsRepository;
import com.yorgohaykal.album_rating_tracker.service.ScoringService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/weights")
@AllArgsConstructor
public class WeightsController {

    private final ScoringWeightsRepository scoringWeightsRepository;
    private final ScoringService scoringService;

    @GetMapping
    public ScoringWeightsResponse getWeights(Authentication authentication) {
        Long userId = (Long) authentication.getPrincipal();

        ScoringWeights weights = scoringWeightsRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalStateException(
                        "No scoring weights found for user " + userId));

        return toResponse(weights);
    }

    @PutMapping
    public ResponseEntity<ScoringWeightsResponse> updateWeights(
            @Valid @RequestBody UpdateWeightsRequest request,
            Authentication authentication
    ) {
        Long userId = (Long) authentication.getPrincipal();

        ScoringWeights weights = scoringWeightsRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalStateException(
                        "No scoring weights found for user " + userId));

        weights.setSongwritingWeight(request.getSongwritingWeight());
        weights.setProductionWeight(request.getProductionWeight());
        weights.setCohesionWeight(request.getCohesionWeight());
        weights.setTracklistWeight(request.getTracklistWeight());
        weights.setReplayValueWeight(request.getReplayValueWeight());
        weights.setEmotionalImpactWeight(request.getEmotionalImpactWeight());

        scoringService.validateWeightsSumTo100(weights);

        ScoringWeights saved = scoringWeightsRepository.save(weights);

        return ResponseEntity.ok(toResponse(saved));
    }

    private ScoringWeightsResponse toResponse(ScoringWeights weights) {
        ScoringWeightsResponse response = new ScoringWeightsResponse();
        response.setSongwritingWeight(weights.getSongwritingWeight());
        response.setProductionWeight(weights.getProductionWeight());
        response.setCohesionWeight(weights.getCohesionWeight());
        response.setTracklistWeight(weights.getTracklistWeight());
        response.setReplayValueWeight(weights.getReplayValueWeight());
        response.setEmotionalImpactWeight(weights.getEmotionalImpactWeight());
        return response;
    }
}
