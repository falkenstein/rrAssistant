package com.falkenstein.rrassist.monotypeanalysis.rating;

import com.falkenstein.rrassist.data.EType;

import java.util.List;
import java.util.stream.Collectors;

public record CoverageEvaluationDto(
        int score,
        List<EType> missingCoverage
) {

    @Override
    public String toString() {
        if (missingCoverage.isEmpty()) {
            return "Coverage: " + score;
        } else {
            return "Coverage: " + score + " (" +
                    missingCoverage.stream()
                            .map(EType::name)
                            .collect(Collectors.joining(", ")) + ")";
        }
    }
}
