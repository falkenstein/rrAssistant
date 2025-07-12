package com.falkenstein.rrassist.monotypeanalysis.rating;

import com.falkenstein.rrassist.data.EType;

import java.util.List;

public record CoverageEvaluationDto(
        int score,
        List<EType> missingCoverage
) {
}
