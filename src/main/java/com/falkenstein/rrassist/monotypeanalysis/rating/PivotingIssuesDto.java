package com.falkenstein.rrassist.monotypeanalysis.rating;

import com.falkenstein.rrassist.data.EType;
import com.falkenstein.rrassist.data.processed.SpeciesDto;

/**
 * Means that the given species has issues with pivoting against the given type, i.e. no team member to resist the type.
 */
public record PivotingIssuesDto(
        SpeciesDto species,
        EType type
) {

    @Override
    public String toString() {
        return species.name() + ":" + type.name();
    }
}
