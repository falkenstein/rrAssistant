package com.falkenstein.rrassist.docs;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

class GDocsManagerTest {

    private GDocsManager manager = new GDocsManager();

    @Test
    public void testSheetConnection() throws GeneralSecurityException, IOException {
        List<MonotypeThreeDocsDto> fetched = manager.composePlannedMonotypes();
        System.out.println(fetched);
    }
}
