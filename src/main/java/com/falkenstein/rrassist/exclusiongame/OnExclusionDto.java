package com.falkenstein.rrassist.exclusiongame;

import java.util.List;

public record OnExclusionDto(
        int gameId,
        EGameState gameState,
        String hint,
        String explanation,
        int expectedExclusions,
        List<GameSpeciesDto> species,
        boolean validGuess
) {
}
