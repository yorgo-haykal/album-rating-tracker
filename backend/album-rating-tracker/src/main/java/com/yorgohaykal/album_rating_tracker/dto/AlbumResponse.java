package com.yorgohaykal.album_rating_tracker.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class AlbumResponse {

    private Long id;
    private String title;
    private String artist;
    private String genre;
    private Integer releaseYear;
    private LocalDateTime dateAdded;
    private BigDecimal songwritingScore;
    private BigDecimal productionScore;
    private BigDecimal cohesionScore;
    private BigDecimal tracklistScore;
    private BigDecimal replayValueScore;
    private BigDecimal emotionalImpactScore;
    private BigDecimal weightedTotal;

}
