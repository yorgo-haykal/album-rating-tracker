package com.yorgohaykal.album_rating_tracker.service;

import com.yorgohaykal.album_rating_tracker.entity.Album;
import com.yorgohaykal.album_rating_tracker.entity.ScoringWeights;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

public class ScoringServiceTest {

    private ScoringService scoringService;

    @BeforeEach
    void setUp() {
        scoringService = new ScoringService();
    }

    @Test
    void calculateWeightedTotal_withEqualWeightsAndEqualScores_returnsThatScore() {
        // If every category is scored 8 and weights are equal, the weighted
        // total should also be 8 - a sanity-check baseline case.
        Album album = albumWithAllScores(new BigDecimal("8.0"));
        ScoringWeights weights = scoringService.createDefaultWeights();

        BigDecimal result = scoringService.calculateWeightedTotal(album, weights);

        assertEquals(new BigDecimal("8.00"), result);
    }

    @Test
    void calculateWeightedTotal_withVaryingScoresAndCustomWeights_computesCorrectly() {
        Album album = new Album();
        album.setSongwritingScore(new BigDecimal("10.0"));
        album.setProductionScore(new BigDecimal("8.0"));
        album.setCohesionScore(new BigDecimal("6.0"));
        album.setTracklistScore(new BigDecimal("7.0"));
        album.setReplayValueScore(new BigDecimal("9.0"));
        album.setEmotionalImpactScore(new BigDecimal("5.0"));

        ScoringWeights weights = new ScoringWeights();
        weights.setSongwritingWeight(new BigDecimal("30.00"));   // heavily weighted
        weights.setProductionWeight(new BigDecimal("20.00"));
        weights.setCohesionWeight(new BigDecimal("10.00"));
        weights.setTracklistWeight(new BigDecimal("10.00"));
        weights.setReplayValueWeight(new BigDecimal("20.00"));
        weights.setEmotionalImpactWeight(new BigDecimal("10.00"));

        // Manually computed expected value:
        // (10*30 + 8*20 + 6*10 + 7*10 + 9*20 + 5*10) / 100
        // = (300 + 160 + 60 + 70 + 180 + 50) / 100 = 820 / 100 = 8.20
        BigDecimal result = scoringService.calculateWeightedTotal(album, weights);

        assertEquals(new BigDecimal("8.20"), result);
    }

    @Test
    void calculateWeightedTotal_withAllZeroScores_returnsZero() {
        Album album = albumWithAllScores(BigDecimal.ZERO);
        ScoringWeights weights = scoringService.createDefaultWeights();

        BigDecimal result = scoringService.calculateWeightedTotal(album, weights);

        assertEquals(new BigDecimal("0.00"), result);
    }

    @Test
    void validateWeightsSumTo100_withValidWeights_doesNotThrow() {
        ScoringWeights weights = scoringService.createDefaultWeights();

        assertDoesNotThrow(() -> scoringService.validateWeightsSumTo100(weights));
    }

    @Test
    void validateWeightsSumTo100_withWeightsSummingTo99_throwsException() {
        ScoringWeights weights = new ScoringWeights();
        weights.setSongwritingWeight(new BigDecimal("15.00"));
        weights.setProductionWeight(new BigDecimal("15.00"));
        weights.setCohesionWeight(new BigDecimal("15.00"));
        weights.setTracklistWeight(new BigDecimal("15.00"));
        weights.setReplayValueWeight(new BigDecimal("15.00"));
        weights.setEmotionalImpactWeight(new BigDecimal("24.00")); // sums to 99

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> scoringService.validateWeightsSumTo100(weights)
        );
        assertTrue(exception.getMessage().contains("99"));
    }

    @Test
    void validateWeightsSumTo100_withWeightsSummingTo101_throwsException() {
        ScoringWeights weights = new ScoringWeights();
        weights.setSongwritingWeight(new BigDecimal("20.00"));
        weights.setProductionWeight(new BigDecimal("20.00"));
        weights.setCohesionWeight(new BigDecimal("20.00"));
        weights.setTracklistWeight(new BigDecimal("20.00"));
        weights.setReplayValueWeight(new BigDecimal("15.00"));
        weights.setEmotionalImpactWeight(new BigDecimal("6.00")); // sums to 101

        assertThrows(
                IllegalArgumentException.class,
                () -> scoringService.validateWeightsSumTo100(weights)
        );
    }

    @Test
    void validateWeightsSumTo100_withDefaultWeights_isWithinTolerance() {
        // Default weights sum to exactly 100.00 (16.67 * 5 + 16.65),
        // this test guards against a future change accidentally breaking that.
        ScoringWeights weights = scoringService.createDefaultWeights();

        assertDoesNotThrow(() -> scoringService.validateWeightsSumTo100(weights));
    }

    @Test
    void createDefaultWeights_sumsToExactly100() {
        ScoringWeights weights = scoringService.createDefaultWeights();

        BigDecimal sum = weights.getSongwritingWeight()
                .add(weights.getProductionWeight())
                .add(weights.getCohesionWeight())
                .add(weights.getTracklistWeight())
                .add(weights.getReplayValueWeight())
                .add(weights.getEmotionalImpactWeight());

        assertEquals(0, new BigDecimal("100.00").compareTo(sum));
    }

    private Album albumWithAllScores(BigDecimal score) {
        Album album = new Album();
        album.setSongwritingScore(score);
        album.setProductionScore(score);
        album.setCohesionScore(score);
        album.setTracklistScore(score);
        album.setReplayValueScore(score);
        album.setEmotionalImpactScore(score);
        return album;
    }
}
