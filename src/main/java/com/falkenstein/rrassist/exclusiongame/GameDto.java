package com.falkenstein.rrassist.exclusiongame;

import com.falkenstein.rrassist.data.processed.SpeciesDto;
import com.falkenstein.rrassist.exclusiongame.similaritycondition.SimilarityCondition;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

public class GameDto {

    public GameDto(int id, List<GameSpeciesDto> species, SpeciesDto targetSpecies) {
        this.id = id;
        this.species = species;
        this.targetSpecies = targetSpecies;
    }

    private final int id;
    private final List<GameSpeciesDto> species;
    private final SpeciesDto targetSpecies;
    private EGameState gameState = EGameState.IN_PROGRESS;
    private String hint = "";
    private int expectedExclusions = 0;
    private SimilarityCondition currentCondition = null;
    final private List<EConditionToken> tokens = new ArrayList<>(Stream.of(
            EConditionToken.STATS,
            EConditionToken.TYPE_RESISTANCE,
            EConditionToken.TYPE_RESISTANCE,
            EConditionToken.TYPE_RESISTANCE,
            EConditionToken.TYPE_RESISTANCE,
            EConditionToken.TYPE_RESISTANCE,
            EConditionToken.TYPE_RESISTANCE,
            EConditionToken.TYPE_RESISTANCE,
            EConditionToken.TYPE_EFFECTIVENESS,
            EConditionToken.TYPE_EFFECTIVENESS,
            EConditionToken.TYPE_EFFECTIVENESS,
            EConditionToken.TYPE_EFFECTIVENESS,
            EConditionToken.TYPE_EFFECTIVENESS,
            EConditionToken.TYPE_EFFECTIVENESS,
            EConditionToken.TYPE_EFFECTIVENESS
    ).toList());

    public int getId() {
        return id;
    }

    public List<GameSpeciesDto> getSpecies() {
        return species;
    }

    public SpeciesDto getTargetSpecies() {
        return targetSpecies;
    }

    public EGameState getGameState() {
        return gameState;
    }

    public String getHint() {
        return hint;
    }

    public void setHint(String hint) {
        this.hint = hint;
    }

    public int getExpectedExclusions() {
        return expectedExclusions;
    }

    public void setExpectedExclusions(int expectedExclusions) {
        this.expectedExclusions = expectedExclusions;
    }

    public SimilarityCondition getCurrentCondition() {
        return currentCondition;
    }

    public void setCurrentCondition(SimilarityCondition currentCondition) {
        this.currentCondition = currentCondition;
    }

    /**
     * Returns only the species that have not been excluded yet.
     */
    public List<SpeciesDto> getActiveSpecies() {
        return species.stream()
                .filter(s -> s.getState() == EGameSpeciesState.AVAILABLE)
                .map(GameSpeciesDto::getSpecies)
                .toList();
    }

    public void setGameState(EGameState gameState) {
        this.gameState = gameState;
    }

    /**
     * Gets a token from the list of available conditions and also removes it from the list.
     */
    public EConditionToken popToken() {
        Random random = new Random();
        var pickedToken = tokens.get(random.nextInt(tokens.size()));
        tokens.remove(pickedToken);
        return pickedToken;
    }
}
