package com.falkenstein.rrassist.exclusiongame.similaritycondition;

import com.falkenstein.rrassist.data.processed.MoveDto;
import com.falkenstein.rrassist.data.processed.SpeciesDto;

public class KnownMoveCondition extends SimilarityCondition {

    private final MoveDto move;

    public KnownMoveCondition(MoveDto move) {
        this.move = move;
    }

    @Override
    public boolean isMetBy(SpeciesDto clickedSpecies) {
        return getAllKnownMoves(clickedSpecies).contains(move);
    }

    @Override
    public String successExplanation(SpeciesDto clickedSpecies) {
        if (clickedSpecies.levelUpMoves().containsKey(move)) {
            return clickedSpecies.name() + " learns " + move.name() + " at level " + clickedSpecies.levelUpMoves().get(move) + ".";
        } else if (clickedSpecies.tmMoves().contains(move)) {
            return clickedSpecies.name() + " can learn " + move.name() + " via TM.";
        } else if (clickedSpecies.eggMoves().contains(move)) {
            return clickedSpecies.name() + " can learn " + move.name() + " as an egg move.";
        }
        throw new IllegalArgumentException("Mistake for species " + clickedSpecies.name() + ": " + move.name() + " is not a known move.");
    }

    @Override
    public String failedExplanation(SpeciesDto clickedSpecies) {
        return clickedSpecies.name() + " does not learn " + move.name() + ".";
    }
}
