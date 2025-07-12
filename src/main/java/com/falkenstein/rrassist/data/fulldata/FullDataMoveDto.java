package com.falkenstein.rrassist.data.fulldata;

public record FullDataMoveDto(
        int ID,
        String name,
        int power,
        int type,
        int accuracy,
        int pp,
        int priority,
        int secondaryEffectChance,
        int target,
        int split,
        String description
) {
}
