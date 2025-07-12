package com.falkenstein.rrassist.controller;

import com.falkenstein.rrassist.controller.dto.ExcludeSpeciesDto;
import com.falkenstein.rrassist.controller.dto.GameFrontendDto;
import com.falkenstein.rrassist.exclusiongame.GameDto;
import com.falkenstein.rrassist.exclusiongame.GameManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GameController {

    @Autowired
    private GameManager gameManager;

    /**
     * Launches a completely new game.
     */
    @PostMapping("/game/new")
    public GameFrontendDto startNewGame() {
        return new GameFrontendDto(gameManager.generateNewGame());
    }

    /**
     * The only endpoint that pushes the game state properly - excludes a species.
     */
    @PostMapping("/game/exclude")
    public GameFrontendDto excludeSpecies(@RequestBody ExcludeSpeciesDto inputDto) {
        var response = gameManager.excludeSpecies(inputDto.gameId(), inputDto.speciesId(), inputDto.form());
        return new GameFrontendDto(response);
    }
}
