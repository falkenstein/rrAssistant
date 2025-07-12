package com.falkenstein.rrassist.data;

import java.util.List;
import java.util.Map;

public record BasicSpeciesDto(int id, String name, List<EType> types, List<String> abilities, Map<EStat, Integer> stats, String sprite) {
}
