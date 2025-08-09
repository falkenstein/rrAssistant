package com.falkenstein.rrassist.monotypeanalysis;

import com.falkenstein.rrassist.data.EStat;
import com.falkenstein.rrassist.data.EType;
import com.falkenstein.rrassist.data.SpeciesDataManager;
import com.falkenstein.rrassist.data.TypeEffectivenessManager;
import com.falkenstein.rrassist.data.processed.SpeciesDto;
import com.falkenstein.rrassist.docs.GDocsManager;
import com.falkenstein.rrassist.docs.MonotypeThreeDocsDto;
import com.falkenstein.rrassist.monotypeanalysis.rating.CoverageEvaluationDto;
import com.falkenstein.rrassist.monotypeanalysis.rating.DurabilityEvaluationDto;
import com.falkenstein.rrassist.monotypeanalysis.rating.PivotingEvaluationDto;
import com.falkenstein.rrassist.monotypeanalysis.rating.PivotingIssuesDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.*;
import java.util.stream.Stream;

@Service
public class MonotypeManager {

    private final GDocsManager gDocsManager;
    private final SpeciesDataManager speciesDataManager;
    private final TypeEffectivenessManager typeEffectivenessManager;

    @Autowired
    public MonotypeManager(GDocsManager gDocsManager, SpeciesDataManager speciesDataManager, TypeEffectivenessManager typeEffectivenessManager) {
        this.gDocsManager = gDocsManager;
        this.speciesDataManager = speciesDataManager;
        this.typeEffectivenessManager = typeEffectivenessManager;
    }

    public List<MonotypeThreeRunDto> composeMonotypeTeams() throws GeneralSecurityException, IOException {
        var fetchedDocs = gDocsManager.composePlannedMonotypes();
        var allSpecies = speciesDataManager.loadFullSpeciesData();
        return fetchedDocs.stream().map(it -> composeMonotypeTeam(it, allSpecies)).toList();
    }

    private MonotypeThreeRunDto composeMonotypeTeam(MonotypeThreeDocsDto docsDto, List<SpeciesDto> allSpecies) {
        var type = EType.valueOf(docsDto.type().toUpperCase());
        var species = docsDto.pokemon().stream()
                .map(name -> findGivenSpecies(name, allSpecies))
                .toList();
        return new MonotypeThreeRunDto(
                type,
                species,
                species.subList(0, 4),
                species.subList(4, 6),
                species.subList(6, 8),
                species.subList(8, 10),
                species.subList(10, 12)
        );
    }

    private SpeciesDto findGivenSpecies(String name, List<SpeciesDto> allSpecies) {
        String baseName = name.split("/")[0].trim();
        String formName = name.contains("/") ? name.split("/")[1].trim() : null;
        SpeciesDto found = allSpecies.stream()
                .filter(species -> species.name().equalsIgnoreCase(baseName)
                        && (formName == null || species.forms().stream().anyMatch(form -> form.name().equalsIgnoreCase(formName))))
                .findFirst().orElse(null);
        if (found == null) {
            System.err.println("No species found for name: " + name);
        }
        return found;
    }

    public String rateTeam(MonotypeThreeRunDto team) {
        PivotingEvaluationDto pivotingEvaluation = rateTeamPivoting(team);
        DurabilityEvaluationDto durabilityEvaluation = rateTeamDurability(team);
        CoverageEvaluationDto coverageEvaluation = rateTeamCoverage(team);
        int totalScore = pivotingEvaluation.score() + durabilityEvaluation.score() + coverageEvaluation.score();
        System.out.println(team.type() + ": " + totalScore + ": " + pivotingEvaluation + ", " + durabilityEvaluation + ", " + coverageEvaluation);
        return "Team rating logic not implemented yet.";
    }

    /**
     * Rates the pivoting issues of a team based on its members.
     * Returns a PivotingEvaluationDto containing the score and the issues found.
     * <p>
     * The score is calculated as follows:
     * - 12 points for no pivoting issues
     * - 11 points for 1 pivoting issue
     * - 10 points for 2 pivoting issues
     * - etc.
     * <p>
     * The issues are collected in a list of PivotingIssuesDto, which contains the species and the type it has issues with.
     */
    private PivotingEvaluationDto rateTeamPivoting(MonotypeThreeRunDto team) {
        List<PivotingIssuesDto> pivotingIssues = new ArrayList<>();
        for (SpeciesDto species : team.core()) { // Core compares to the whole team.
            pivotingIssues.addAll(compareMemberAgainstSelectedTeamParts(species, List.of(team.species())));
        }
        for (SpeciesDto species : team.visitor()) { // Visitors also compare to the whole team.
            pivotingIssues.addAll(compareMemberAgainstSelectedTeamParts(species, List.of(team.species())));
        }
        for (SpeciesDto species : team.pure()) { // Same for pures.
            pivotingIssues.addAll(compareMemberAgainstSelectedTeamParts(species, List.of(team.species())));
        }
        for (SpeciesDto species : team.early()) { // Early pokemon don't compare to the late ones.
            pivotingIssues.addAll(compareMemberAgainstSelectedTeamParts(species, List.of(team.core(), team.pure(), team.visitor(), team.early())));
        }
        for (SpeciesDto species : team.late()) { // Vice versa - late pokemon don't compare to the early ones.
            pivotingIssues.addAll(compareMemberAgainstSelectedTeamParts(species, List.of(team.core(), team.pure(), team.visitor(), team.late())));
        }
        // Now that we have the issues, we need to calculate the rating.
        long score = 12 - pivotingIssues.stream().map(PivotingIssuesDto::species).distinct().count();
        return new PivotingEvaluationDto((int) score, pivotingIssues);
    }

    /**
     * Compares a team member against the selected team parts to find pivoting issues.
     */
    private List<PivotingIssuesDto> compareMemberAgainstSelectedTeamParts(SpeciesDto member, List<List<SpeciesDto>> teamParts) {
        List<SpeciesDto> mergedTeam = teamParts.stream().flatMap(List::stream).distinct().filter(it -> it != member).toList();
        return teamMemberHasPivots(member, mergedTeam);
    }

    /**
     * Checks if a team member has pivoting issues against the entire team.
     */
    private List<PivotingIssuesDto> teamMemberHasPivots(SpeciesDto species, List<SpeciesDto> team) {
        List<PivotingIssuesDto> pivotingIssues = new ArrayList<>();
        List<EType> vulnerableToTypes = species.typeResists().entrySet().stream()
                .filter(it -> it.getValue().isWeak()).map(Map.Entry::getKey).toList();
        for (EType type : vulnerableToTypes) {
            if (team.stream().noneMatch(it -> it.resistsType(type))) {
                pivotingIssues.add(new PivotingIssuesDto(species, type));
            }
        }
        return pivotingIssues;
    }

    /**
     * Rates the durability of a team based on its physical and special tanks.
     * Returns a DurabilityEvaluationDto containing the score and the best tanks.
     */
    private DurabilityEvaluationDto rateTeamDurability(MonotypeThreeRunDto team) {
        List<SpeciesDto> noEarlyAndLate = Stream.of(team.core(), team.pure(), team.visitor())
                .flatMap(List::stream)
                .toList();
        List<SpeciesDto> physicalTanks = findSpeciesWithFittingBulk(EStat.DEFENSE, noEarlyAndLate);
        List<SpeciesDto> specialTanks = findSpeciesWithFittingBulk(EStat.SPECIAL_DEFENSE, noEarlyAndLate);
        SpeciesDto physicalTank = physicalTanks.isEmpty() ? null : physicalTanks.getFirst();
        SpeciesDto secondaryPhysicalTank = physicalTanks.size() > 1 ? physicalTanks.get(1) : null;
        SpeciesDto specialTank = specialTanks.isEmpty() ? null : specialTanks.getFirst();
        SpeciesDto secondarySpecialTank = specialTanks.size() > 1 ? specialTanks.get(1) : null;
        int score = 0;
        if (physicalTank != null) {
            score += 4;
        }
        if (secondaryPhysicalTank != null) {
            score += 2;
        }
        if (specialTank != null) {
            score += 4;
        }
        if (secondarySpecialTank != null) {
            score += 2;
        }
        return new DurabilityEvaluationDto(score, physicalTank, secondaryPhysicalTank, specialTank, secondarySpecialTank);
    }

    /**
     * Finds species that have sufficient bulk. Can be searched for both physical and special defense.
     */
    private List<SpeciesDto> findSpeciesWithFittingBulk(EStat stat, List<SpeciesDto> species) {
        return species.stream()
                .filter(it -> (it.getStat(stat) * it.getStat(EStat.HP)) >= 8000)
                .sorted(Comparator.comparing(it -> -1 * it.getStat(stat))) // Manually reversing via -1.
                .toList();
    }

    /**
     * Rates the coverage of a team based on its members.
     *
     * The score is calculated as follows:
     * - 6 points for full coverage (no missing types)
     * - 5 points for 1 missing type
     * - 4 points for 2 missing types
     * - etc.
     */
    private CoverageEvaluationDto rateTeamCoverage(MonotypeThreeRunDto team) {
        List<SpeciesDto> noEarlyAndLate = Stream.of(team.core(), team.pure(), team.visitor())
                .flatMap(List::stream)
                .toList();
        List<EType> typesOfTheTeam = noEarlyAndLate.stream().map(SpeciesDto::types).flatMap(List::stream).distinct().toList();
        List<EType> coveredTypes = typeEffectivenessManager.getSuperEffectiveCoverage(typesOfTheTeam);
        List<EType> missingTypes = Arrays.stream(EType.values())
                .filter(type -> !coveredTypes.contains(type))
                .toList();
        return new CoverageEvaluationDto(Math.max(0, 6 - missingTypes.size()), missingTypes);
    }
}
