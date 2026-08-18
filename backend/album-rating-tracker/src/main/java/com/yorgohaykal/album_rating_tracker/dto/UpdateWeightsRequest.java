package com.yorgohaykal.album_rating_tracker.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class UpdateWeightsRequest {

    @NotNull(message = "Songwriting weight is required")
    private BigDecimal songwritingWeight;

    @NotNull(message = "Production weight is required")
    private BigDecimal productionWeight;

    @NotNull(message = "Cohesion weight is required")
    private BigDecimal cohesionWeight;

    @NotNull(message = "Tracklist weight is required")
    private BigDecimal tracklistWeight;

    @NotNull(message = "Replay value weight is required")
    private BigDecimal replayValueWeight;

    @NotNull(message = "Emotional impact weight is required")
    private BigDecimal emotionalImpactWeight;
}
