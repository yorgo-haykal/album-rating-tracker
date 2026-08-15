package com.yorgohaykal.album_rating_tracker.repository;

import com.yorgohaykal.album_rating_tracker.entity.ScoringWeights;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ScoringWeightsRepository extends JpaRepository<ScoringWeights, Long> {

    Optional<ScoringWeights> findUserById(Long id);
}
