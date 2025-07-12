package com.falkenstein.rrassist.monotypeanalysis.rating;

import com.falkenstein.rrassist.data.processed.SpeciesDto;

public record DurabilityEvaluationDto(
        int score,
        SpeciesDto physicalTank,
        SpeciesDto secondaryPhysicalTank,
        SpeciesDto specialTank,
        SpeciesDto secondarySpecialTank
) {
}
