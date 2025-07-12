package com.falkenstein.rrassist.exclusiongame.similaritycondition;

import com.falkenstein.rrassist.data.processed.MoveDto;
import com.falkenstein.rrassist.data.processed.SpeciesDto;

import java.util.ArrayList;
import java.util.List;

/**
 * Describes two pokemon sharing a common trait.
 */
public abstract class SimilarityCondition {

    public SimilarityCondition() {
    }

    public abstract boolean isMetBy(SpeciesDto clickedSpecies);

    public abstract String successExplanation(SpeciesDto clickedSpecies);

    public abstract String failedExplanation(SpeciesDto clickedSpecies);

    protected List<MoveDto> getAllKnownMoves(SpeciesDto queriedSpecies) {
        final List<MoveDto> mergedList = new ArrayList<>();
        mergedList.addAll(queriedSpecies.eggMoves());
        mergedList.addAll(queriedSpecies.tmMoves());
        mergedList.addAll(queriedSpecies.levelUpMoves().keySet());
        return mergedList.stream().distinct().toList();
    }
}
