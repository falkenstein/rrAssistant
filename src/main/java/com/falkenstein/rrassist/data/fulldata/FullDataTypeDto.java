package com.falkenstein.rrassist.data.fulldata;

import java.util.List;

public record FullDataTypeDto(
        String name,
        String color,
        List<Integer> matchup
) {
}
