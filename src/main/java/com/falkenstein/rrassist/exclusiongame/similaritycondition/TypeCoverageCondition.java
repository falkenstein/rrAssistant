package com.falkenstein.rrassist.exclusiongame.similaritycondition;

import com.falkenstein.rrassist.data.EMatchup;
import com.falkenstein.rrassist.data.EResistance;
import com.falkenstein.rrassist.data.EType;
import com.falkenstein.rrassist.data.processed.SpeciesDto;

public class TypeCoverageCondition extends SimilarityCondition {

    private final EType type;
    private final EMatchup effectiveness;

    /**
     * Offensive condition, i.e. checks that the species can hit the type with a certain effectiveness.
     */
    public TypeCoverageCondition(EType type, EMatchup effectiveness) {
        this.type = type;
        this.effectiveness = effectiveness;
    }

    @Override
    public boolean isMetBy(SpeciesDto clickedSpecies) {
        return clickedSpecies.matchups().get(type) == effectiveness;
    }

    @Override
    public String successExplanation(SpeciesDto clickedSpecies) {
        return clickedSpecies.name() + " has best " + clickedSpecies.matchups().get(type) + " STAB vs " + type;
    }

    @Override
    public String failedExplanation(SpeciesDto clickedSpecies) {
        return clickedSpecies.name() + " has best " + clickedSpecies.matchups().get(type) + " STAB vs " + type;
    }
}
