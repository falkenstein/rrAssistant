package com.falkenstein.rrassist.monotypeanalysis.rating;

import java.util.List;

public record PivotingEvaluationDto(
        int score,
        List<PivotingIssuesDto> pivotingIssues
) {
}
