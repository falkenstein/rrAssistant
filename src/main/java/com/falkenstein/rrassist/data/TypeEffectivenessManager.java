package com.falkenstein.rrassist.data;

import com.falkenstein.rrassist.data.fulldata.FullDataTypeDto;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controls matchup effectiveness between the types.
 */
@Service
public class TypeEffectivenessManager {

    /**
     * The type matchups storage.
     */
    private final List<TypeMatchupDto> matchups = List.of(
            new TypeMatchupDto(EType.NORMAL, EType.ROCK, EMatchup.NOT_VERY_EFFECTIVE),
            new TypeMatchupDto(EType.NORMAL, EType.GHOST, EMatchup.IMMUNE),
            new TypeMatchupDto(EType.NORMAL, EType.STEEL, EMatchup.NOT_VERY_EFFECTIVE),
            new TypeMatchupDto(EType.FIRE, EType.GRASS, EMatchup.SUPER_EFFECTIVE),
            new TypeMatchupDto(EType.FIRE, EType.ICE, EMatchup.SUPER_EFFECTIVE),
            new TypeMatchupDto(EType.FIRE, EType.BUG, EMatchup.SUPER_EFFECTIVE),
            new TypeMatchupDto(EType.FIRE, EType.STEEL, EMatchup.SUPER_EFFECTIVE),
            new TypeMatchupDto(EType.FIRE, EType.FIRE, EMatchup.NOT_VERY_EFFECTIVE),
            new TypeMatchupDto(EType.FIRE, EType.WATER, EMatchup.NOT_VERY_EFFECTIVE),
            new TypeMatchupDto(EType.FIRE, EType.ROCK, EMatchup.NOT_VERY_EFFECTIVE),
            new TypeMatchupDto(EType.FIRE, EType.DRAGON, EMatchup.NOT_VERY_EFFECTIVE),
            new TypeMatchupDto(EType.WATER, EType.FIRE, EMatchup.SUPER_EFFECTIVE),
            new TypeMatchupDto(EType.WATER, EType.GROUND, EMatchup.SUPER_EFFECTIVE),
            new TypeMatchupDto(EType.WATER, EType.ROCK, EMatchup.SUPER_EFFECTIVE),
            new TypeMatchupDto(EType.WATER, EType.WATER, EMatchup.NOT_VERY_EFFECTIVE),
            new TypeMatchupDto(EType.WATER, EType.GRASS, EMatchup.NOT_VERY_EFFECTIVE),
            new TypeMatchupDto(EType.WATER, EType.DRAGON, EMatchup.NOT_VERY_EFFECTIVE),
            new TypeMatchupDto(EType.ELECTRIC, EType.WATER, EMatchup.SUPER_EFFECTIVE),
            new TypeMatchupDto(EType.ELECTRIC, EType.FLYING, EMatchup.SUPER_EFFECTIVE),
            new TypeMatchupDto(EType.ELECTRIC, EType.ELECTRIC, EMatchup.NOT_VERY_EFFECTIVE),
            new TypeMatchupDto(EType.ELECTRIC, EType.GRASS, EMatchup.NOT_VERY_EFFECTIVE),
            new TypeMatchupDto(EType.ELECTRIC, EType.DRAGON, EMatchup.NOT_VERY_EFFECTIVE),
            new TypeMatchupDto(EType.ELECTRIC, EType.GROUND, EMatchup.IMMUNE),
            new TypeMatchupDto(EType.GRASS, EType.WATER, EMatchup.SUPER_EFFECTIVE),
            new TypeMatchupDto(EType.GRASS, EType.GROUND, EMatchup.SUPER_EFFECTIVE),
            new TypeMatchupDto(EType.GRASS, EType.ROCK, EMatchup.SUPER_EFFECTIVE),
            new TypeMatchupDto(EType.GRASS, EType.FIRE, EMatchup.NOT_VERY_EFFECTIVE),
            new TypeMatchupDto(EType.GRASS, EType.GRASS, EMatchup.NOT_VERY_EFFECTIVE),
            new TypeMatchupDto(EType.GRASS, EType.POISON, EMatchup.NOT_VERY_EFFECTIVE),
            new TypeMatchupDto(EType.GRASS, EType.FLYING, EMatchup.NOT_VERY_EFFECTIVE),
            new TypeMatchupDto(EType.GRASS, EType.BUG, EMatchup.NOT_VERY_EFFECTIVE),
            new TypeMatchupDto(EType.GRASS, EType.DRAGON, EMatchup.NOT_VERY_EFFECTIVE),
            new TypeMatchupDto(EType.GRASS, EType.STEEL, EMatchup.NOT_VERY_EFFECTIVE),
            new TypeMatchupDto(EType.ICE, EType.GRASS, EMatchup.SUPER_EFFECTIVE),
            new TypeMatchupDto(EType.ICE, EType.GROUND, EMatchup.SUPER_EFFECTIVE),
            new TypeMatchupDto(EType.ICE, EType.FLYING, EMatchup.SUPER_EFFECTIVE),
            new TypeMatchupDto(EType.ICE, EType.DRAGON, EMatchup.SUPER_EFFECTIVE),
            new TypeMatchupDto(EType.ICE, EType.FIRE, EMatchup.NOT_VERY_EFFECTIVE),
            new TypeMatchupDto(EType.ICE, EType.WATER, EMatchup.NOT_VERY_EFFECTIVE),
            new TypeMatchupDto(EType.ICE, EType.ICE, EMatchup.NOT_VERY_EFFECTIVE),
            new TypeMatchupDto(EType.ICE, EType.STEEL, EMatchup.NOT_VERY_EFFECTIVE),
            new TypeMatchupDto(EType.FIGHTING, EType.NORMAL, EMatchup.SUPER_EFFECTIVE),
            new TypeMatchupDto(EType.FIGHTING, EType.ICE, EMatchup.SUPER_EFFECTIVE),
            new TypeMatchupDto(EType.FIGHTING, EType.ROCK, EMatchup.SUPER_EFFECTIVE),
            new TypeMatchupDto(EType.FIGHTING, EType.DARK, EMatchup.SUPER_EFFECTIVE),
            new TypeMatchupDto(EType.FIGHTING, EType.STEEL, EMatchup.SUPER_EFFECTIVE),
            new TypeMatchupDto(EType.FIGHTING, EType.POISON, EMatchup.NOT_VERY_EFFECTIVE),
            new TypeMatchupDto(EType.FIGHTING, EType.FLYING, EMatchup.NOT_VERY_EFFECTIVE),
            new TypeMatchupDto(EType.FIGHTING, EType.PSYCHIC, EMatchup.NOT_VERY_EFFECTIVE),
            new TypeMatchupDto(EType.FIGHTING, EType.BUG, EMatchup.NOT_VERY_EFFECTIVE),
            new TypeMatchupDto(EType.FIGHTING, EType.FAIRY, EMatchup.NOT_VERY_EFFECTIVE),
            new TypeMatchupDto(EType.FIGHTING, EType.GHOST, EMatchup.IMMUNE),
            new TypeMatchupDto(EType.POISON, EType.GRASS, EMatchup.SUPER_EFFECTIVE),
            new TypeMatchupDto(EType.POISON, EType.FAIRY, EMatchup.SUPER_EFFECTIVE),
            new TypeMatchupDto(EType.POISON, EType.POISON, EMatchup.NOT_VERY_EFFECTIVE),
            new TypeMatchupDto(EType.POISON, EType.GROUND, EMatchup.NOT_VERY_EFFECTIVE),
            new TypeMatchupDto(EType.POISON, EType.ROCK, EMatchup.NOT_VERY_EFFECTIVE),
            new TypeMatchupDto(EType.POISON, EType.GHOST, EMatchup.NOT_VERY_EFFECTIVE),
            new TypeMatchupDto(EType.POISON, EType.STEEL, EMatchup.IMMUNE),
            new TypeMatchupDto(EType.GROUND, EType.FIRE, EMatchup.SUPER_EFFECTIVE),
            new TypeMatchupDto(EType.GROUND, EType.ELECTRIC, EMatchup.SUPER_EFFECTIVE),
            new TypeMatchupDto(EType.GROUND, EType.POISON, EMatchup.SUPER_EFFECTIVE),
            new TypeMatchupDto(EType.GROUND, EType.ROCK, EMatchup.SUPER_EFFECTIVE),
            new TypeMatchupDto(EType.GROUND, EType.STEEL, EMatchup.SUPER_EFFECTIVE),
            new TypeMatchupDto(EType.GROUND, EType.GRASS, EMatchup.NOT_VERY_EFFECTIVE),
            new TypeMatchupDto(EType.GROUND, EType.BUG, EMatchup.NOT_VERY_EFFECTIVE),
            new TypeMatchupDto(EType.GROUND, EType.FLYING, EMatchup.IMMUNE),
            new TypeMatchupDto(EType.FLYING, EType.GRASS, EMatchup.SUPER_EFFECTIVE),
            new TypeMatchupDto(EType.FLYING, EType.FIGHTING, EMatchup.SUPER_EFFECTIVE),
            new TypeMatchupDto(EType.FLYING, EType.BUG, EMatchup.SUPER_EFFECTIVE),
            new TypeMatchupDto(EType.FLYING, EType.ELECTRIC, EMatchup.NOT_VERY_EFFECTIVE),
            new TypeMatchupDto(EType.FLYING, EType.ROCK, EMatchup.NOT_VERY_EFFECTIVE),
            new TypeMatchupDto(EType.FLYING, EType.STEEL, EMatchup.NOT_VERY_EFFECTIVE),
            new TypeMatchupDto(EType.PSYCHIC, EType.FIGHTING, EMatchup.SUPER_EFFECTIVE),
            new TypeMatchupDto(EType.PSYCHIC, EType.POISON, EMatchup.SUPER_EFFECTIVE),
            new TypeMatchupDto(EType.PSYCHIC, EType.PSYCHIC, EMatchup.NOT_VERY_EFFECTIVE),
            new TypeMatchupDto(EType.PSYCHIC, EType.STEEL, EMatchup.NOT_VERY_EFFECTIVE),
            new TypeMatchupDto(EType.PSYCHIC, EType.DARK, EMatchup.IMMUNE),
            new TypeMatchupDto(EType.BUG, EType.GRASS, EMatchup.SUPER_EFFECTIVE),
            new TypeMatchupDto(EType.BUG, EType.PSYCHIC, EMatchup.SUPER_EFFECTIVE),
            new TypeMatchupDto(EType.BUG, EType.DARK, EMatchup.SUPER_EFFECTIVE),
            new TypeMatchupDto(EType.BUG, EType.FIRE, EMatchup.NOT_VERY_EFFECTIVE),
            new TypeMatchupDto(EType.BUG, EType.FIGHTING, EMatchup.NOT_VERY_EFFECTIVE),
            new TypeMatchupDto(EType.BUG, EType.POISON, EMatchup.NOT_VERY_EFFECTIVE),
            new TypeMatchupDto(EType.BUG, EType.FLYING, EMatchup.NOT_VERY_EFFECTIVE),
            new TypeMatchupDto(EType.BUG, EType.GHOST, EMatchup.NOT_VERY_EFFECTIVE),
            new TypeMatchupDto(EType.BUG, EType.STEEL, EMatchup.NOT_VERY_EFFECTIVE),
            new TypeMatchupDto(EType.BUG, EType.FAIRY, EMatchup.NOT_VERY_EFFECTIVE),
            new TypeMatchupDto(EType.ROCK, EType.FIRE, EMatchup.SUPER_EFFECTIVE),
            new TypeMatchupDto(EType.ROCK, EType.ICE, EMatchup.SUPER_EFFECTIVE),
            new TypeMatchupDto(EType.ROCK, EType.FLYING, EMatchup.SUPER_EFFECTIVE),
            new TypeMatchupDto(EType.ROCK, EType.BUG, EMatchup.SUPER_EFFECTIVE),
            new TypeMatchupDto(EType.ROCK, EType.FIGHTING, EMatchup.NOT_VERY_EFFECTIVE),
            new TypeMatchupDto(EType.ROCK, EType.GROUND, EMatchup.NOT_VERY_EFFECTIVE),
            new TypeMatchupDto(EType.ROCK, EType.STEEL, EMatchup.NOT_VERY_EFFECTIVE),
            new TypeMatchupDto(EType.GHOST, EType.GHOST, EMatchup.SUPER_EFFECTIVE),
            new TypeMatchupDto(EType.GHOST, EType.PSYCHIC, EMatchup.SUPER_EFFECTIVE),
            new TypeMatchupDto(EType.GHOST, EType.DARK, EMatchup.NOT_VERY_EFFECTIVE),
            new TypeMatchupDto(EType.GHOST, EType.NORMAL, EMatchup.IMMUNE),
            new TypeMatchupDto(EType.DRAGON, EType.DRAGON, EMatchup.SUPER_EFFECTIVE),
            new TypeMatchupDto(EType.DRAGON, EType.STEEL, EMatchup.NOT_VERY_EFFECTIVE),
            new TypeMatchupDto(EType.DRAGON, EType.FAIRY, EMatchup.IMMUNE),
            new TypeMatchupDto(EType.DARK, EType.GHOST, EMatchup.SUPER_EFFECTIVE),
            new TypeMatchupDto(EType.DARK, EType.PSYCHIC, EMatchup.SUPER_EFFECTIVE),
            new TypeMatchupDto(EType.DARK, EType.FIGHTING, EMatchup.NOT_VERY_EFFECTIVE),
            new TypeMatchupDto(EType.DARK, EType.DARK, EMatchup.NOT_VERY_EFFECTIVE),
            new TypeMatchupDto(EType.DARK, EType.FAIRY, EMatchup.NOT_VERY_EFFECTIVE),
            new TypeMatchupDto(EType.STEEL, EType.ICE, EMatchup.SUPER_EFFECTIVE),
            new TypeMatchupDto(EType.STEEL, EType.ROCK, EMatchup.SUPER_EFFECTIVE),
            new TypeMatchupDto(EType.STEEL, EType.FAIRY, EMatchup.SUPER_EFFECTIVE),
            new TypeMatchupDto(EType.STEEL, EType.FIRE, EMatchup.NOT_VERY_EFFECTIVE),
            new TypeMatchupDto(EType.STEEL, EType.WATER, EMatchup.NOT_VERY_EFFECTIVE),
            new TypeMatchupDto(EType.STEEL, EType.ELECTRIC, EMatchup.NOT_VERY_EFFECTIVE),
            new TypeMatchupDto(EType.STEEL, EType.STEEL, EMatchup.NOT_VERY_EFFECTIVE),
            new TypeMatchupDto(EType.FAIRY, EType.FIGHTING, EMatchup.SUPER_EFFECTIVE),
            new TypeMatchupDto(EType.FAIRY, EType.DRAGON, EMatchup.SUPER_EFFECTIVE),
            new TypeMatchupDto(EType.FAIRY, EType.DARK, EMatchup.SUPER_EFFECTIVE),
            new TypeMatchupDto(EType.FAIRY, EType.FIRE, EMatchup.NOT_VERY_EFFECTIVE),
            new TypeMatchupDto(EType.FAIRY, EType.POISON, EMatchup.NOT_VERY_EFFECTIVE),
            new TypeMatchupDto(EType.FAIRY, EType.STEEL, EMatchup.NOT_VERY_EFFECTIVE)
    );

    public EMatchup getMatchup(EType attacker, EType defender) {
        return matchups.stream()
                .filter(matchup -> matchup.attackingType() == attacker && matchup.defendingType() == defender)
                .findFirst()
                .map(TypeMatchupDto::matchup)
                .orElse(EMatchup.STANDARD);
    }

    /**
     * Composes the offensive type matchups for the given type combination.
     */
    public Map<EType, EMatchup> composeTypeMatchups(List<EType> types) {
        Map<EType, List<EMatchup>> matchups = new HashMap<>();
        for (EType attackingType : types) {
            for (EType defendingType : EType.values()) {
                EMatchup matchup = getMatchup(attackingType, defendingType);
                if (!matchups.containsKey(defendingType)) {
                    matchups.put(defendingType, new ArrayList<>());
                }
                matchups.get(defendingType).add(matchup);
            }
        }
        Map<EType, EMatchup> results = new HashMap<>();
        for (Map.Entry<EType, List<EMatchup>> entry : matchups.entrySet()) {
            EType type = entry.getKey();
            List<EMatchup> matchupList = entry.getValue();
            String max = matchupList.stream().map(it -> it.multiplier).sorted().toList().getLast().toString();
            EMatchup effectiveness = switch (max) {
                case "1.0" -> EMatchup.STANDARD;
                case "0.0" -> EMatchup.IMMUNE;
                case "0.5" -> EMatchup.NOT_VERY_EFFECTIVE;
                case "2.0" -> EMatchup.SUPER_EFFECTIVE;
                default -> throw new IllegalArgumentException("Unknown resistance multiplier: " + max);
            };
            results.put(type, effectiveness);
        }
        return results;
    }

    /**
     * Calculates which types are attacked super-effectively by the given type combination.
     */
    public List<EType> getSuperEffectiveCoverage(List<EType> attackingTypes) {
        var matchups = composeTypeMatchups(attackingTypes);
        return matchups.entrySet().stream()
                .filter(entry -> entry.getValue() == EMatchup.SUPER_EFFECTIVE)
                .map(Map.Entry::getKey)
                .toList();
    }
}
