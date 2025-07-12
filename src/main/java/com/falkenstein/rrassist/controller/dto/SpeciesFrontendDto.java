package com.falkenstein.rrassist.controller.dto;

import com.falkenstein.rrassist.data.EForm;
import com.falkenstein.rrassist.data.processed.SpeciesDto;
import com.falkenstein.rrassist.exclusiongame.EGameSpeciesState;
import com.falkenstein.rrassist.exclusiongame.GameSpeciesDto;

/**
 * Represents a species in the frontend, including its name, image URL, ID, form, and game species state.
 * This DTO is used to transfer species data to the frontend.
 */
public record SpeciesFrontendDto(
        String name,
        String imageUrl,
        int id,
        EForm form,
        EGameSpeciesState state
) {

    /**
     * Constructs a SpeciesFrontendDto from a GameSpeciesDto.
     * Used with the wrapper around Species, which includes the game state.
     */
    public SpeciesFrontendDto(
            GameSpeciesDto gameSpeciesDto
    ) {
        this(
                gameSpeciesDto.getSpecies().name(),
                gameSpeciesDto.getSpecies().spriteUrl(),
                gameSpeciesDto.getSpecies().id(),
                !gameSpeciesDto.getSpecies().forms().isEmpty() ? gameSpeciesDto.getSpecies().forms().getFirst() : null,
                gameSpeciesDto.getState()
        );
    }

    /**
     * Constructs a SpeciesFrontendDto from a SpeciesDto.
     * Used with just Species, without the wrapper.
     */
    public SpeciesFrontendDto(SpeciesDto speciesDto) {
        this(
                speciesDto.name(),
                speciesDto.spriteUrl(),
                speciesDto.id(),
                !speciesDto.forms().isEmpty() ? speciesDto.forms().getFirst() : null,
                EGameSpeciesState.AVAILABLE // Default state for a SpeciesDto
        );
    }
}
