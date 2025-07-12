package com.falkenstein.rrassist.data;

public enum ESplit {
    PHYSICAL(0),
    SPECIAL(1),
    STATUS(2);

    private final int id;

    ESplit(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public static ESplit fromId(int id) {
        for (ESplit split : values()) {
            if (split.getId() == id) {
                return split;
            }
        }
        throw new IllegalArgumentException("Invalid ESplit ID: " + id);
    }
}
