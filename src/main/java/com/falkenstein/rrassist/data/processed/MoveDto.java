package com.falkenstein.rrassist.data.processed;

import com.falkenstein.rrassist.data.ESplit;
import com.falkenstein.rrassist.data.EType;

public record MoveDto(
        String name,
        int power,
        EType type,
        int accuracy,
        String description,
        ESplit split
) {

    @Override
    public String toString() {
        return name;
    }
}
