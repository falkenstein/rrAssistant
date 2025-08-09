package com.falkenstein.rrassist.monotypeanalysis.rating;

import com.falkenstein.rrassist.data.processed.SpeciesDto;

public record DurabilityEvaluationDto(
        int score,
        SpeciesDto physicalTank,
        SpeciesDto secondaryPhysicalTank,
        SpeciesDto specialTank,
        SpeciesDto secondarySpecialTank
) {

    @Override
    public String toString() {
        return "Durability: " + score + " (" + physicalTank + ", " + secondaryPhysicalTank + ", " + specialTank + ", " + secondarySpecialTank + ")";
    }
}
