package com.falkenstein.rrassist.data.fulldata;

import java.util.List;

public record FullDataAbilityDto(
        int ID,
        List<String> names,
        String description
) {
}
