package com.falkenstein.rrassist.data.fulldata;

import java.util.List;

public record FullDataSpeciesDto(
        int ID,
        String name,
        List<Integer> stats,
        List<Integer> type,
        List<List<Integer>> abilities,
        List<List<Integer>> levelupMoves,
        List<List<Integer>> evolutions,
        List<Integer> tmMoves,
        List<Integer> tutorMoves,
        List<Integer> eggMoves,
        String key,
        int dexID,
        int ancestor
) {
}
