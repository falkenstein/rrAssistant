package com.falkenstein.rrassist.data;

import com.falkenstein.rrassist.data.fulldata.FullDataCompleteDto;
import com.falkenstein.rrassist.data.processed.SpeciesDto;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SpeciesDataManagerTest {

    private final SpeciesDataManager speciesDataManager = new SpeciesDataManager(new TypeEffectivenessManager());

    @Test
    void testLoadFullData() throws IOException {
        List<SpeciesDto> allSpecies = speciesDataManager.loadFullSpeciesData();
        System.out.println(allSpecies.stream().filter(it -> it.name().contains("Dodrio")).toList());
    }

    @Test
    void testTypeMatchup() throws IOException {
        FullDataCompleteDto fullDataDto = speciesDataManager.loadCompleteData();
        Map<EType, EResistance> resists = speciesDataManager.composeTypeResists(List.of(EType.GRASS, EType.POISON), fullDataDto.types());
        assertEquals(EResistance.RESISTANT, resists.get(EType.WATER));
        assertEquals(EResistance.WEAK, resists.get(EType.PSYCHIC));
        assertEquals(EResistance.VERY_RESISTANT, resists.get(EType.GRASS));
    }

    @Test
    void testToStringOverride() {
        SpeciesDto species = new SpeciesDto(
                1,
                "Bulbasaur",
                Map.of(EStat.HP, 45, EStat.ATTACK, 49, EStat.DEFENSE, 49, EStat.SPECIAL_ATTACK, 65, EStat.SPECIAL_DEFENSE, 65, EStat.SPEED, 45),
                List.of("Overgrow", "Chlorophyll"),
                List.of(EType.GRASS, EType.POISON),
                List.of(),
                List.of(),
                Map.of(),
                "https://example.com/sprite.png",
                Map.of(EType.GRASS, EResistance.RESISTANT, EType.POISON, EResistance.RESISTANT),
                0,
                null,
                null
        );
        assertEquals("Bulbasaur", species.toString());
    }

    @Test
    void testLoadedMoves() throws IOException {
        List<SpeciesDto> allSpecies = speciesDataManager.loadFullSpeciesData();
        var bulbasaurMoves = allSpecies.getFirst().levelUpMoves();
        assertTrue(bulbasaurMoves.keySet().stream().anyMatch(it -> it.name().equals("Leech Seed")));
    }

    @Test
    void testStabCoverage() throws IOException {
        List<SpeciesDto> allSpecies = speciesDataManager.loadFullSpeciesData();
        System.out.println(allSpecies.getFirst().matchups());
    }
}