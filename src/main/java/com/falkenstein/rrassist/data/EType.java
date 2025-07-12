package com.falkenstein.rrassist.data;

public enum EType {
    NORMAL(0),
    FIGHTING(1),
    FLYING(2),
    POISON(3),
    GROUND(4),
    ROCK(5),
    BUG(6),
    GHOST(7),
    STEEL(8),
    FIRE(10),
    WATER(11),
    GRASS(12),
    ELECTRIC(13),
    PSYCHIC(14),
    ICE(15),
    DRAGON(16),
    DARK(17),
    FAIRY(23),
    ;

    private final Integer id;

    EType(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public static EType fromId(Integer id) {
        for (EType type : values()) {
            if (type.getId().equals(id)) {
                return type;
            }
        }
        throw new IllegalArgumentException("No EType found for id: " + id);
    }
}
