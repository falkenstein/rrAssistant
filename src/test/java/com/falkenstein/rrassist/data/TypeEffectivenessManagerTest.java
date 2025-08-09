package com.falkenstein.rrassist.data;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TypeEffectivenessManagerTest {

    private final TypeEffectivenessManager typeEffectivenessManager = new TypeEffectivenessManager();

    @Test
    public void testComposeTypeMatchups() {
        var typeMatchups = typeEffectivenessManager.composeTypeMatchups(List.of(EType.GRASS, EType.POISON));
        assertEquals(EMatchup.SUPER_EFFECTIVE, typeMatchups.get(EType.WATER));
    }

}