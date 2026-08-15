package com.yorgohaykal.album_rating_tracker.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "album")
@Getter
@Setter
@NoArgsConstructor
public class Album {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private AppUser user;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String artist;

    private String genre;

    @Column(name = "release_year")
    private Integer releaseYear;

    @Column(name = "date_added", nullable = false, updatable = false)
    private LocalDateTime dateAdded = LocalDateTime.now();

    @Column(name = "songwriting_score", nullable = false)
    private BigDecimal songwritingScore;

    @Column(name = "production_score", nullable = false)
    private BigDecimal productionScore;

    @Column(name = "cohesion_score", nullable = false)
    private BigDecimal cohesionScore;

    @Column(name = "tracklist_score", nullable = false)
    private BigDecimal tracklistScore;

    @Column(name = "replay_value_score", nullable = false)
    private BigDecimal replayValueScore;

    @Column(name = "emotional_impact_score", nullable = false)
    private BigDecimal emotionalImpactScore;
}