package com.falkenstein.rrassist.monotypeanalysis.rating;

import java.util.List;
import java.util.stream.Collectors;

public record PivotingEvaluationDto(
        int score,
        List<PivotingIssuesDto> pivotingIssues
) {

    @Override
    public String toString() {
        if (pivotingIssues.isEmpty()) {
            return "Pivoting: " + score;
        } else {
            return "Pivoting: " + score + " (" +
                    pivotingIssues.stream()
                            .map(PivotingIssuesDto::toString)
                            .collect(Collectors.joining(", ")) + ")";
        }
    }
}
