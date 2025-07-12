package com.falkenstein.rrassist.exclusiongame.similaritycondition;

import com.falkenstein.rrassist.data.EResistance;
import com.falkenstein.rrassist.data.EStat;
import com.falkenstein.rrassist.data.EType;
import com.falkenstein.rrassist.data.processed.SpeciesDto;

import java.util.List;
import java.util.stream.Collectors;

import static java.lang.Math.abs;

public class TypeEffectivenessCondition extends SimilarityCondition {

    private final EType type;
    private final EResistance effectiveness;

    /**
     * Creates a condition that checks if the target species resists the given type with the specified effectiveness.
     */
    public TypeEffectivenessCondition(EType type, EResistance effectiveness) {
        this.type = type;
        this.effectiveness = effectiveness;
    }

    @Override
    public boolean isMetBy(SpeciesDto clickedSpecies) {
        return clickedSpecies.typeResists().get(type) == effectiveness;
    }

    @Override
    public String successExplanation(SpeciesDto clickedSpecies) {
        return clickedSpecies.name() + " is " + clickedSpecies.typeResists().get(type) + " vs " + type;
    }

    @Override
    public String failedExplanation(SpeciesDto clickedSpecies) {
        return clickedSpecies.name() + " is " + clickedSpecies.typeResists().get(type) + " vs " + type;
    }
}
