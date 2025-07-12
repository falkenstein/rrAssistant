package com.falkenstein.rrassist.exclusiongame.similaritycondition;

import com.falkenstein.rrassist.data.EType;
import com.falkenstein.rrassist.data.processed.MoveDto;
import com.falkenstein.rrassist.data.processed.SpeciesDto;

import java.util.List;

public class MoveCoverageCondition extends SimilarityCondition {

    private final EType coverage;

    public MoveCoverageCondition(EType coverage) {
        this.coverage = coverage;
    }

    @Override
    public boolean isMetBy(SpeciesDto clickedSpecies) {
        return getAllKnownMoves(clickedSpecies).stream().anyMatch(move -> move.type() == coverage);
    }

    @Override
    public String successExplanation(SpeciesDto clickedSpecies) {
        List<String> learnedMovesOfType = getAllKnownMoves(clickedSpecies).stream()
                .filter(move -> move.type() == coverage)
                .map(MoveDto::name)
                .toList();
        return clickedSpecies.name() + " learns the following moves of type " + coverage.name() + ": " + String.join(", ", learnedMovesOfType) + ".";
    }

    @Override
    public String failedExplanation(SpeciesDto clickedSpecies) {
        return clickedSpecies.name() + " does not learn any moves of type " + coverage.name() + ".";
    }
}
