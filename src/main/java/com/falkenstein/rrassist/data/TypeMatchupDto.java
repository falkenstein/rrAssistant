package com.falkenstein.rrassist.data;

public record TypeMatchupDto(
        EType attackingType,
        EType defendingType,
        EMatchup matchup
) {
}
