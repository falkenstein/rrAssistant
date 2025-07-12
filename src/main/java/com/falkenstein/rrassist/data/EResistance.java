package com.falkenstein.rrassist.data;

public enum EResistance {
    STANDARD,
    IMMUNE,
    WEAK,
    VERY_WEAK,
    RESISTANT,
    VERY_RESISTANT,
    ;

    public boolean isWeak() {
        return this == WEAK || this == VERY_WEAK;
    }

    public boolean isResistant() {
        return this == RESISTANT || this == VERY_RESISTANT || this == IMMUNE;
    }
}
