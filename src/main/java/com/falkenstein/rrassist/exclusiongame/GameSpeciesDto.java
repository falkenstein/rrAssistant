package com.falkenstein.rrassist.exclusiongame;

import com.falkenstein.rrassist.data.processed.SpeciesDto;

public class GameSpeciesDto {
    private int position;
    private SpeciesDto species;
    private EGameSpeciesState state;

    public GameSpeciesDto(int position, SpeciesDto species, EGameSpeciesState state) {
        this.position = position;
        this.species = species;
        this.state = state;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public SpeciesDto getSpecies() {
        return species;
    }

    public void setSpecies(SpeciesDto species) {
        this.species = species;
    }

    public EGameSpeciesState getState() {
        return state;
    }

    public void setState(EGameSpeciesState state) {
        this.state = state;
    }
}
