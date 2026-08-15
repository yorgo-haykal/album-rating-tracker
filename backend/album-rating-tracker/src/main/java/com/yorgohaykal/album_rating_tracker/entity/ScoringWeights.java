package com.yorgohaykal.album_rating_tracker.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "scoring_weights")
@Getter
@Setter
@NoArgsConstructor
public class ScoringWeights {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private AppUser user;

    @Column(name = "songwriting_weight", nullable = false)
    private BigDecimal songwritingWeight;

    @Column(name = "production_weight", nullable = false)
    private BigDecimal productionWeight;

    @Column(name = "cohesion_weight", nullable = false)
    private BigDecimal cohesionWeight;

    @Column(name = "tracklist_weight", nullable = false)
    private BigDecimal tracklistWeight;

    @Column(name = "replay_value_weight", nullable = false)
    private BigDecimal replayValueWeight;

    @Column(name = "emotional_impact_weight", nullable = false)
    private BigDecimal emotionalImpactWeight;
}