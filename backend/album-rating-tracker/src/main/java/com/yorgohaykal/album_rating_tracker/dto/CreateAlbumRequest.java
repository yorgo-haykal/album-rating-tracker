package com.yorgohaykal.album_rating_tracker.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class CreateAlbumRequest {

    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "Artist is required")
    private String artist;

    private String genre;

    private Integer releaseYear;

    @NotNull(message = "Songwriting score is required")
    @DecimalMin(value = "0.0", message = "Score must be at least 0")
    @DecimalMax(value = "10.0", message = "Score must be at most 10")
    private BigDecimal songwritingScore;

    @NotNull(message = "Production score is required")
    @DecimalMin(value = "0.0")
    @DecimalMax(value = "10.0")
    private BigDecimal productionScore;

    @NotNull(message = "Cohesion score is required")
    @DecimalMin(value = "0.0")
    @DecimalMax(value = "10.0")
    private BigDecimal cohesionScore;

    @NotNull(message = "Tracklist score is required")
    @DecimalMin(value = "0.0")
    @DecimalMax(value = "10.0")
    private BigDecimal tracklistScore;

    @NotNull(message = "Replay value score is required")
    @DecimalMin(value = "0.0")
    @DecimalMax(value = "10.0")
    private BigDecimal replayValueScore;

    @NotNull(message = "Emotional impact score is required")
    @DecimalMin(value = "0.0")
    @DecimalMax(value = "10.0")
    private BigDecimal emotionalImpactScore;

}
