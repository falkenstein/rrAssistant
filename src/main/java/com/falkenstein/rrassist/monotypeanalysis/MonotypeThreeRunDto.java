package com.falkenstein.rrassist.monotypeanalysis;

import com.falkenstein.rrassist.data.EType;
import com.falkenstein.rrassist.data.processed.SpeciesDto;

import java.util.List;

public record MonotypeThreeRunDto(
        EType type, List<SpeciesDto> species, List<SpeciesDto> core, List<SpeciesDto> pure, List<SpeciesDto> early, List<SpeciesDto> visitor,
        List<SpeciesDto> late
) {
}
