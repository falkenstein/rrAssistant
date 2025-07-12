package com.falkenstein.rrassist.controller.dto;

import com.falkenstein.rrassist.exclusiongame.EGameState;
import com.falkenstein.rrassist.exclusiongame.GameDto;
import com.falkenstein.rrassist.exclusiongame.OnExclusionDto;

import java.util.List;

public record GameFrontendDto(
        int id,
        List<SpeciesFrontendDto> species,
        EGameState gameState,
        String hint,
        String explanation,
        int expectedExclusions,
        boolean validGuess
) {

    /**
     * Constructor for a new game.
     */
    public GameFrontendDto(
            GameDto gameDto
    ) {
        this(
                gameDto.getId(),
                gameDto.getSpecies().stream()
                        .map(SpeciesFrontendDto::new)
                        .toList(),
                gameDto.getGameState(),
                gameDto.getHint(),
                "Game started.",
                gameDto.getExpectedExclusions(),
                true
        );
    }

    public GameFrontendDto(OnExclusionDto dto) {
        this(
                dto.gameId(),
                dto.species().stream()
                        .map(SpeciesFrontendDto::new)
                        .toList(),
                dto.gameState(),
                dto.hint(),
                dto.explanation(),
                dto.expectedExclusions(),
                dto.validGuess()
        );
    }
}
