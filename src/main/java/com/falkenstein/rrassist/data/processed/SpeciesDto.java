package com.falkenstein.rrassist.data.processed;

import com.falkenstein.rrassist.data.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public record SpeciesDto(
        int id,
        String name,
        Map<EStat, Integer> stats,
        List<String> abilities,
        List<EType> types,
        List<MoveDto> tmMoves,
        List<MoveDto> eggMoves,
        Map<MoveDto, Integer> levelUpMoves,
        String spriteUrl,
        Map<EType, EResistance> typeResists,
        int ancestorId,
        List<EForm> forms,
        Map<EType, EMatchup> matchups
) {

    @Override
    public String toString() {
        if (forms == null || forms.isEmpty()) {
            return name;
        } else {
            return name + "-" + forms.stream().map(Enum::name).collect(Collectors.joining());
        }
    }

    public boolean resistsType(EType type) {
        return typeResists.get(type).isResistant();
    }

    public boolean weakToType(EType type) {
        return typeResists.get(type).isWeak();
    }

    public Integer getStat(EStat stat) {
        return stats.getOrDefault(stat, 0);
    }
}
