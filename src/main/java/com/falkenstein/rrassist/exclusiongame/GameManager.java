package com.falkenstein.rrassist.exclusiongame;

import com.falkenstein.rrassist.data.*;
import com.falkenstein.rrassist.data.processed.SpeciesDto;
import com.falkenstein.rrassist.exclusiongame.similaritycondition.StatValueCondition;
import com.falkenstein.rrassist.exclusiongame.similaritycondition.TypeCoverageCondition;
import com.falkenstein.rrassist.exclusiongame.similaritycondition.TypeEffectivenessCondition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

@Service
public class GameManager {

    private List<GameDto> games = new ArrayList<>();
    private final SpeciesDataManager speciesDataManager;
    private final List<SpeciesDto> allSpecies;
    private final Random random = new Random();

    @Autowired
    public GameManager(SpeciesDataManager speciesDataManager) throws IOException {
        this.speciesDataManager = speciesDataManager;
        this.allSpecies = speciesDataManager.loadFullSpeciesData();
    }

    /**
     * Generates a new game with a random selection of species.
     * The target species is randomly selected from the picked species.
     * The game ID is simply the current size of the games list, which assumes no games are removed.
     */
    public GameDto generateNewGame() {
        int gameId = games.size(); // Effectively guarantees a sequence, but presumes that games are not removed.
        List<SpeciesDto> coppiedList = new ArrayList<>(allSpecies);
        Collections.shuffle(coppiedList);
        List<SpeciesDto> pickedSpecies = coppiedList.stream().limit(25).toList();
        List<GameSpeciesDto> gameSpecies = new ArrayList<>();
        for (int i = 0; i < pickedSpecies.size(); i++) {
            var setSpecies = new GameSpeciesDto(
                    i,
                    pickedSpecies.get(i),
                    EGameSpeciesState.AVAILABLE
            );
            gameSpecies.add(setSpecies);
        }
        GameDto newGame = new GameDto(
                gameId,
                gameSpecies,
                pickedSpecies.get(random.nextInt(25)) // The first species is the target species
        );
        addNewHintAndCondition(newGame);
        games.add(newGame);
        return newGame;
    }

    /**
     * Excludes a species from the game based on the player's choice. Also adjusts the game state accordingly.
     */
    public OnExclusionDto excludeSpecies(int gameId, int speciesId, String stringForm) {
        // First set up the variables.
        GameDto game = games.get(gameId);
        EForm form = stringForm == null || stringForm.isEmpty() ? null : EForm.valueOf(stringForm.toUpperCase());
        GameSpeciesDto clickedSpecies = game.getSpecies().stream()
                .filter(s -> s.getSpecies().id() == speciesId && (form == null || s.getSpecies().forms().contains(form)))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Species not found in game"));

        clickedSpecies.setState(EGameSpeciesState.EXCLUDED);
        final String exclusionExplanation;
        final boolean validGuess;
        if (!game.getCurrentCondition().isMetBy(clickedSpecies.getSpecies())) { // OK, the player correctly excluded a species that doesn't match the condition.
            game.setExpectedExclusions(game.getExpectedExclusions() - 1);
            exclusionExplanation = "Correct: " + game.getCurrentCondition().successExplanation(clickedSpecies.getSpecies());
            validGuess = true;
        } else if (clickedSpecies.getSpecies().equals(game.getTargetSpecies())) { // Bad luck, the player lost.
            game.setGameState(EGameState.LOST);
            exclusionExplanation = "Lost: " + game.getCurrentCondition().failedExplanation(clickedSpecies.getSpecies());
            validGuess = false;
        } else { // Means the player made a mistake, but got lucky.
            exclusionExplanation = "Lucky: " + game.getCurrentCondition().failedExplanation(clickedSpecies.getSpecies());
            validGuess = false;
        }
        if (game.getActiveSpecies().size() == 1 && game.getGameState() != EGameState.LOST) { // Means there's just one pokemon left, so the player won.
            game.setGameState(EGameState.WON);
        } else if (game.getExpectedExclusions() == 0) { // Means the player has excluded all species that match the condition.
            addNewHintAndCondition(game);
        }
        return new OnExclusionDto(
                game.getId(),
                game.getGameState(),
                game.getHint(),
                exclusionExplanation,
                game.getExpectedExclusions(),
                game.getSpecies(),
                validGuess
        );
    }

    /**
     * Adds a new hint and condition to the game based on the target species.
     * The hint is generated based on the median value of a random stat from all species.
     * The condition is set to either StatGreaterThanCondition or StatLowerThanCondition.
     */
    public void addNewHintAndCondition(GameDto game) {
        var condition = game.popToken();
        switch (condition) {
            case STATS -> setupStatCondition(game);
            case TYPE_RESISTANCE -> setupTypeEffectivenessCondition(game);
            case TYPE_EFFECTIVENESS -> setupTypeCoverageCondition(game);
        }
    }

    /**
     * Sets up a stat-based condition for the game.
     */
    private void setupStatCondition(GameDto game) {
        var targetSpecies = game.getTargetSpecies();
        var allOthers = game.getActiveSpecies().stream()
                .filter(species -> !species.equals(targetSpecies))
                .toList();
        // We always start with a stat-based hint.
        EStat randomStat = EStat.values()[random.nextInt(EStat.values().length)];
        int[] range = getOptimalStatRange(targetSpecies, allOthers, randomStat);
        StatValueCondition condition = new StatValueCondition(randomStat, range[0], range[1]);
        game.setHint("Exclude all whose " + randomStat + " is not within " + range[0] + " and " + range[1]);
        game.setCurrentCondition(condition);
        game.setExpectedExclusions((int) game.getActiveSpecies().stream().filter(it -> !condition.isMetBy(it)).count());
    }

    /**
     * Calculates tolerance for the stat condition, so that at most 6 species are excluded.
     */
    public int[] getOptimalStatRange(SpeciesDto targetSpecies, List<SpeciesDto> allOthers, EStat stat) {
        var statValue = targetSpecies.getStat(stat);
        List<Integer> allStatValues = allOthers.stream()
                .map(species -> species.getStat(stat))
                .sorted()
                .toList();
        List<Integer> fromTopList = allStatValues.stream().filter(it -> it > statValue).sorted().toList();
        final int statCeiling;
        if (fromTopList.isEmpty()) {
            statCeiling = allStatValues.getLast();
        } else {
            statCeiling = fromTopList.subList(Math.max(fromTopList.size() - 3, 0), fromTopList.size()).getFirst();
        }
        List<Integer> fromBottomList = allStatValues.stream().filter(it -> it < statValue).sorted().toList();
        final int statFloor;
        if (fromBottomList.isEmpty()) {
            statFloor = allStatValues.getFirst();
        } else {
            statFloor = allStatValues.stream().filter(it -> it < statValue).limit(3).toList().getLast();
        }
        return new int[]{statFloor, statCeiling};
    }

    /**
     * Sets up a type effectiveness condition for the game.
     * It tries to find a type that has a significant difference in resistance compared to the target species.
     */
    private void setupTypeEffectivenessCondition(GameDto game) {
        SpeciesDto targetSpecies = game.getTargetSpecies();
        List<SpeciesDto> allOthers = game.getActiveSpecies().stream()
                .filter(species -> !species.equals(targetSpecies))
                .toList();
        Map<EType, Integer> typeEffectivenessCounts = new EnumMap<>(EType.class);
        for (EType type : EType.values()) {
            typeEffectivenessCounts.put(type, getNotMatchingTypeEffectivenessCount(targetSpecies, allOthers, type));
        }
        for (int i = Math.min(4, allOthers.size() - 1); i < allOthers.size(); i++) { // We attempt to exclude the least amount of species possible.
            int finalI = i;
            List<EType> validTypes = typeEffectivenessCounts.entrySet().stream()
                    .filter(entry -> finalI >= entry.getValue() && entry.getValue() > 0)
                    .map(Map.Entry::getKey)
                    .toList();
            if (!validTypes.isEmpty()) {
                EType type = validTypes.get(random.nextInt(validTypes.size()));
                TypeEffectivenessCondition condition = new TypeEffectivenessCondition(type, targetSpecies.typeResists().get(type));
                game.setHint("Exclude all whose resistance to " + type + " is not " + targetSpecies.typeResists().get(type));
                game.setCurrentCondition(condition);
                game.setExpectedExclusions((int) game.getActiveSpecies().stream().filter(it -> !condition.isMetBy(it)).count());
                break;
            }
        }
    }

    /**
     * Returns the number of active species that match the type effectiveness of the target species.
     * This is used to determine how many species are effectively similar in terms of type resistance.
     */
    private int getNotMatchingTypeEffectivenessCount(SpeciesDto targetSpecies, List<SpeciesDto> allOthers, EType type) {
        EResistance effectiveness = targetSpecies.typeResists().get(type);
        return (int) allOthers.stream()
                .filter(species -> species.typeResists().get(type) != effectiveness)
                .count();
    }

    /**
     * Sets up a type coverage condition for the game.
     */
    private void setupTypeCoverageCondition(GameDto game) {
        SpeciesDto targetSpecies = game.getTargetSpecies();
        List<SpeciesDto> allOthers = game.getActiveSpecies().stream()
                .filter(species -> !species.equals(targetSpecies))
                .toList();
        Map<EType, Integer> typeCoverageCounts = new EnumMap<>(EType.class);
        for (EType type : EType.values()) {
            typeCoverageCounts.put(type, getNotMatchingTypeCoverageCount(targetSpecies, allOthers, type));
        }
        for (int i = Math.min(4, allOthers.size() - 1); i < allOthers.size(); i++) { // We attempt to exclude the least amount of species possible.
            int finalI = i;
            List<EType> validTypes = typeCoverageCounts.entrySet().stream()
                    .filter(entry -> finalI >= entry.getValue() && entry.getValue() > 0)
                    .map(Map.Entry::getKey)
                    .toList();
            if (!validTypes.isEmpty()) {
                EType type = validTypes.get(random.nextInt(validTypes.size()));
                TypeCoverageCondition condition = new TypeCoverageCondition(type, targetSpecies.matchups().get(type));
                game.setHint("Exclude all whose STAB against " + type + " is not " + targetSpecies.matchups().get(type));
                game.setCurrentCondition(condition);
                game.setExpectedExclusions((int) game.getActiveSpecies().stream().filter(it -> !condition.isMetBy(it)).count());
                break;
            }
        }
    }

    /**
     * Returns the number of active species that do not match the type coverage of the target species.
     * This is used to determine how many species are effectively different in terms of type coverage.
     */
    private int getNotMatchingTypeCoverageCount(SpeciesDto targetSpecies, List<SpeciesDto> allOthers, EType type) {
        EMatchup effectiveness = targetSpecies.matchups().get(type);
        return (int) allOthers.stream()
                .filter(species -> species.matchups().get(type) != effectiveness)
                .count();
    }
}
