package com.falkenstein.rrassist.controller;

import com.falkenstein.rrassist.monotypeanalysis.MonotypeManager;
import com.falkenstein.rrassist.monotypeanalysis.MonotypeThreeRunDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

@RestController
public class DebugController {

    @Autowired
    private MonotypeManager monotypeManager;

    @PostMapping("/debug/analyze")
    public void analyzeMonotypeTeams() throws GeneralSecurityException, IOException {
        List<MonotypeThreeRunDto> dtos = monotypeManager.composeMonotypeTeams();
        for (MonotypeThreeRunDto dto : dtos) {
            monotypeManager.rateTeam(dto);
        }
    }
}
