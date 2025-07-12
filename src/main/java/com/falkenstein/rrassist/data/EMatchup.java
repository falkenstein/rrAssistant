package com.falkenstein.rrassist.data;

public enum EMatchup {
    STANDARD(0, 1),
    IMMUNE(1, 0),
    NOT_VERY_EFFECTIVE(5, 0.5),
    SUPER_EFFECTIVE(20, 2),
    ;

    final int id;
    final double multiplier;

    public int getId() {
        return id;
    }

    EMatchup(int id, double multiplier) {
        this.id = id;
        this.multiplier = multiplier;
    }

    public static EMatchup fromId(int id) {
        for (EMatchup matchup : values()) {
            if (matchup.getId() == id) {
                return matchup;
            }
        }
        throw new IllegalArgumentException("Invalid EMatchup ID: " + id);
    }
}
