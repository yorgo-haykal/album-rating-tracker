package com.yorgohaykal.album_rating_tracker.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class ScoringWeightsResponse {

    private BigDecimal songwritingWeight;
    private BigDecimal productionWeight;
    private BigDecimal cohesionWeight;
    private BigDecimal tracklistWeight;
    private BigDecimal replayValueWeight;
    private BigDecimal emotionalImpactWeight;

}
