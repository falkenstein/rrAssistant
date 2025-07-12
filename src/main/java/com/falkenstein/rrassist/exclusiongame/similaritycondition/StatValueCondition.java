package com.falkenstein.rrassist.exclusiongame.similaritycondition;

import com.falkenstein.rrassist.data.EStat;
import com.falkenstein.rrassist.data.processed.SpeciesDto;

import java.util.List;

import static java.lang.Math.abs;

public class StatValueCondition extends SimilarityCondition {

    private final EStat stat;
    private final int minRange;
    private final int maxRange;

    public StatValueCondition(EStat stat, int minRange, int maxRange) {
        this.stat = stat;
        this.minRange = minRange;
        this.maxRange = maxRange;
    }

    @Override
    public boolean isMetBy(SpeciesDto clickedSpecies) {
        return clickedSpecies.stats().get(stat) >= minRange && clickedSpecies.stats().get(stat) <= maxRange;
    }

    @Override
    public String successExplanation(SpeciesDto clickedSpecies) {
        return clickedSpecies.name() + " " + stat.name().toLowerCase() + ": " + clickedSpecies.stats().get(stat) + ".";
    }

    @Override
    public String failedExplanation(SpeciesDto clickedSpecies) {
        return clickedSpecies.name() + " " + stat.name().toLowerCase() + ": " + clickedSpecies.stats().get(stat) + ".";
    }
}
