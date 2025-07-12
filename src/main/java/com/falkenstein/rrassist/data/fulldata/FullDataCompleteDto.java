package com.falkenstein.rrassist.data.fulldata;

import java.util.Map;

public record FullDataCompleteDto(
        Map<Integer, FullDataSpeciesDto> species,
        Map<Integer, FullDataTypeDto> types,
        Map<Integer, FullDataMoveDto> moves,
        Map<Integer, FullDataAbilityDto> abilities,
        Map<Integer, String> sprites
) {
}
