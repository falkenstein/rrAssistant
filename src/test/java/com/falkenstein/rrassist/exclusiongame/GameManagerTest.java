package com.falkenstein.rrassist.exclusiongame;

import com.falkenstein.rrassist.data.EStat;
import com.falkenstein.rrassist.data.SpeciesDataManager;
import com.falkenstein.rrassist.data.TypeEffectivenessManager;
import com.falkenstein.rrassist.data.processed.SpeciesDto;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GameManagerTest {

    private SpeciesDataManager speciesDataManager = new SpeciesDataManager(new TypeEffectivenessManager());
    private GameManager gameManager = new GameManager(speciesDataManager); // No need to mock anything.

    GameManagerTest() throws IOException {
    }

    @Test
    public void testOptimalTolerance() throws IOException {
        List<SpeciesDto> firstTwentySpecies = speciesDataManager.loadFullSpeciesData().stream().limit(20).toList();
        int[] optimalTolerance = gameManager.getOptimalStatRange(firstTwentySpecies.getFirst(), firstTwentySpecies, EStat.SPEED);
        assertEquals(43, optimalTolerance[0]);
        assertEquals(97, optimalTolerance[1]);
    }
}