package com.yorgohaykal.album_rating_tracker.service;

import com.yorgohaykal.album_rating_tracker.entity.Album;
import com.yorgohaykal.album_rating_tracker.entity.ScoringWeights;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class ScoringService {

    private static final BigDecimal HUNDRED = BigDecimal.valueOf(100);
    private static final BigDecimal WEIGHT_SUM_TOLERANCE = BigDecimal.valueOf(0.01);

    public BigDecimal calculateWeightedTotal(Album album, ScoringWeights weights) {
        BigDecimal total = BigDecimal.ZERO;

        total = total.add(weights.getSongwritingWeight().multiply(album.getSongwritingScore()));
        total = total.add(weights.getProductionWeight().multiply(album.getProductionScore()));
        total = total.add(weights.getCohesionWeight().multiply(album.getCohesionScore()));
        total = total.add(weights.getTracklistWeight().multiply(album.getTracklistScore()));
        total = total.add(weights.getReplayValueWeight().multiply(album.getReplayValueScore()));
        total = total.add(weights.getEmotionalImpactWeight().multiply(album.getEmotionalImpactScore()));

        return total.divide(HUNDRED, 2, RoundingMode.HALF_UP);
    }

    public void validateWeightsSumTo100(ScoringWeights weights) {
        BigDecimal sum = weights.getSongwritingWeight()
                .add(weights.getProductionWeight())
                .add(weights.getCohesionWeight())
                .add(weights.getTracklistWeight())
                .add(weights.getReplayValueWeight())
                .add(weights.getEmotionalImpactWeight());

        BigDecimal difference = sum.subtract(HUNDRED).abs();

        if (difference.compareTo(WEIGHT_SUM_TOLERANCE) > 0) {
            throw new IllegalArgumentException("Weights sum must be 100%, got: " + sum);
        }
    }

    public ScoringWeights createDefaultWeights() {
        ScoringWeights weights = new ScoringWeights();
        weights.setSongwritingWeight(new BigDecimal("16.67"));
        weights.setProductionWeight(new BigDecimal("16.67"));
        weights.setCohesionWeight(new BigDecimal("16.67"));
        weights.setTracklistWeight(new BigDecimal("16.67"));
        weights.setReplayValueWeight(new BigDecimal("16.67"));
        weights.setEmotionalImpactWeight(new BigDecimal("16.65"));
        return weights;
    }
}
