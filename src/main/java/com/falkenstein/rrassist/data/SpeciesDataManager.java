package com.falkenstein.rrassist.data;

import com.falkenstein.rrassist.data.fulldata.*;
import com.falkenstein.rrassist.data.processed.MoveDto;
import com.falkenstein.rrassist.data.processed.SpeciesDto;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.IOUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.*;

/**
 * Loads the data about species for RR.
 */
@Service
public class SpeciesDataManager {

    private List<String> formlessPokemon = List.of(
            "Ho-Oh",
            "Porygon-Z",
            "Ting-Lu",
            "Chien-Pao",
            "Wo-Chien",
            "Chi-Yu",
            "Jangmo-o",
            "Hakamo-o",
            "Kommo-o",
            "Urshifu-Rapid-Strike",
            "Toxtricity-Low-Key",
            "Terapagos-Terastal"
    );

    /**
     * Loads the basic species data from the resources file (rrBasicData.html).
     */
    public void loadBasicSpeciesData() throws IOException {
        // First we need to load the file from the resources.
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        InputStream inputStream = classloader.getResourceAsStream("static/rrBasicData.html");
        StringWriter writer = new StringWriter();
        IOUtils.copy(inputStream, writer, "UTF-8");
        String html = writer.toString();
        Document doc = Jsoup.parse(html);
        List<Node> allNodes = doc.childNodes().getFirst().childNodes().get(1).childNodes();
        List<Node> filteredNodes = allNodes.stream()
                .filter(this::isValidHtmlNode)
                .toList();
        List<HtmlSpeciesItemsDto> htmlSpeciesItems = new ArrayList<>();
        HtmlSpeciesItemsDto currentItem = null;
        for (Node node : filteredNodes) {
            if (node instanceof TextNode textNode) {
                try {
                    int id = Integer.parseInt(textNode.text().trim());
                    currentItem = new HtmlSpeciesItemsDto(id, new ArrayList<>());
                    htmlSpeciesItems.add(currentItem);
                } catch (NumberFormatException e) {
                    // Means the text is not a valid ID, so we instead add the node to the list.
                    if (currentItem != null) {
                        currentItem.nodes().add(node);
                    }
                }
            } else {
                if (currentItem != null) {
                    currentItem.nodes().add(node);
                }
            }
        }
        // Now we have the data in separate dtos and can proceed to parse it into pokemon species.
        List<BasicSpeciesDto> basicSpecies = htmlSpeciesItems.stream().map(this::setupBasicDto).toList();
        System.out.println(basicSpecies);
    }

    private boolean isValidHtmlNode(Node node) {
        if (node instanceof TextNode textNode) {
            // Check if the text node is not empty or just whitespace
            return !textNode.text().trim().isEmpty();
        }
        return true;
    }

    private BasicSpeciesDto setupBasicDto(HtmlSpeciesItemsDto inputDto) {
        int id = inputDto.id();
        String name = inputDto.nodes().stream().filter(it -> it instanceof TextNode textNode && !textNode.text().isEmpty()).findFirst().get().toString();
        List<EType> types = inputDto.nodes().stream()
                .filter(it -> it.hasAttr("class") && it.attr("class").equals("typeWrapper"))
                .map(it -> EType.valueOf(it.childNodes().getFirst().toString().trim().toUpperCase()))
                .toList();
        List<String> abilities = inputDto.nodes().stream()
                .filter(it -> it.hasAttr("class") && it.attr("class").contains("speciesAbilities"))
                .map(it -> it.childNodes().getFirst().toString().trim())
                .toList();
        List<Integer> statInts = inputDto.nodes().stream()
                .filter(it -> it.hasAttr("class") && it.attr("class").equals("speciesStatValue"))
                .map(it -> Integer.parseInt(it.childNodes().getFirst().toString().trim()))
                .toList();
        Map<EStat, Integer> stats = new HashMap<>();
        stats.put(EStat.HP, statInts.get(0));
        stats.put(EStat.ATTACK, statInts.get(1));
        stats.put(EStat.DEFENSE, statInts.get(2));
        stats.put(EStat.SPECIAL_ATTACK, statInts.get(3));
        stats.put(EStat.SPECIAL_DEFENSE, statInts.get(4));
        stats.put(EStat.SPEED, statInts.get(5));
        String sprite = inputDto.nodes().stream().filter(it -> it.hasAttr("class") && it.attr("class").equals("speciesSprite"))
                .map(it -> it.attr("src")).findFirst().get();
        return new BasicSpeciesDto(
                id,
                name,
                types,
                abilities,
                stats,
                sprite
        );
    }

    public FullDataCompleteDto loadCompleteData() throws IOException {
        // First we need to load the file from the resources.
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        InputStream inputStream = classloader.getResourceAsStream("static/data.json");
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return mapper.readValue(inputStream, FullDataCompleteDto.class);
    }

    /**
     * Gets the full data from the data.json file found on the RR GitHub repository.
     */
    public List<SpeciesDto> loadFullSpeciesData() throws IOException {
        FullDataCompleteDto fullDataDto = loadCompleteData();
        // Now let's process it into more readable species.
        List<FullDataAbilityDto> allAbilities = fullDataDto.abilities().values().stream().toList();
        List<SpeciesDto> species = new ArrayList<>();
        for (Map.Entry<Integer, FullDataSpeciesDto> entry : fullDataDto.species().entrySet()) {
            try {
                FullDataSpeciesDto fullDataSpecies = entry.getValue();
                Map<EStat, Integer> stats = new HashMap<>();
                stats.put(EStat.HP, fullDataSpecies.stats().get(0));
                stats.put(EStat.ATTACK, fullDataSpecies.stats().get(1));
                stats.put(EStat.DEFENSE, fullDataSpecies.stats().get(2));
                stats.put(EStat.SPEED, fullDataSpecies.stats().get(3));
                stats.put(EStat.SPECIAL_ATTACK, fullDataSpecies.stats().get(4));
                stats.put(EStat.SPECIAL_DEFENSE, fullDataSpecies.stats().get(5));
                List<String> abilities = new ArrayList<>();
                for (List<Integer> abilityList : fullDataSpecies.abilities()) {
                    int abilityId = abilityList.getFirst();
                    if (abilityId != 0) { // 0 means no ability, so we skip it.
                        FullDataAbilityDto fullDataAbility = allAbilities.stream().filter(it -> it.ID() == abilityId).findFirst().get();
                        String abilityName = fullDataAbility.names().get(abilityList.get(1));
                        abilities.add(abilityName);
                    }
                }
                List<EType> types = fullDataSpecies.type().stream().map(typeInt ->
                        Arrays.stream(EType.values()).filter(it -> it.getId().equals(typeInt)).findFirst().get()).toList();
                List<MoveDto> tmMoves = new ArrayList<>();
                if (fullDataSpecies.tmMoves() != null) {
                    tmMoves = fullDataSpecies.tmMoves().stream()
                            .filter(it -> it != 0) // 0 means no move, so we skip it.
                            .map(moveId -> {
                                FullDataMoveDto fullDataMove = fullDataDto.moves().get(moveId);
                                return new MoveDto(
                                        fullDataMove.name(),
                                        fullDataMove.power(),
                                        EType.fromId(fullDataMove.type()),
                                        fullDataMove.accuracy(),
                                        fullDataMove.description(),
                                        ESplit.fromId(fullDataMove.split())
                                );
                            }).toList();
                }
                List<MoveDto> eggMoves = new ArrayList<>();
                if (fullDataSpecies.eggMoves() != null) { // Some species might not have egg moves.
                    eggMoves = fullDataSpecies.eggMoves().stream()
                            .map(moveId -> {
                                FullDataMoveDto fullDataMove = fullDataDto.moves().get(moveId);
                                return new MoveDto(
                                        fullDataMove.name(),
                                        fullDataMove.power(),
                                        EType.fromId(fullDataMove.type()),
                                        fullDataMove.accuracy(),
                                        fullDataMove.description(),
                                        ESplit.fromId(fullDataMove.split())
                                );
                            }).toList();
                }
                Map<MoveDto, Integer> levelUpMoves = new HashMap<>();
                for (List<Integer> levelUpMove : fullDataSpecies.levelupMoves()) {
                    int moveId = levelUpMove.getFirst();
                    FullDataMoveDto fullDataMove = fullDataDto.moves().get(moveId);
                    MoveDto moveDto = new MoveDto(
                            fullDataMove.name(),
                            fullDataMove.power(),
                            EType.fromId(fullDataMove.type()),
                            fullDataMove.accuracy(),
                            fullDataMove.description(),
                            ESplit.fromId(fullDataMove.split())
                    );
                    levelUpMoves.put(moveDto, levelUpMove.get(1)); // The second element is the level at which the move is learned.
                }
                String spriteUrl = fullDataDto.sprites().get(entry.getKey());
                SpeciesDto speciesDto = new SpeciesDto(
                        fullDataSpecies.ID(),
                        fullDataSpecies.name(),
                        stats,
                        abilities,
                        types,
                        tmMoves,
                        eggMoves,
                        levelUpMoves,
                        spriteUrl,
                        composeTypeResists(types, fullDataDto.types()),
                        fullDataSpecies.ancestor(),
                        getForms(fullDataSpecies.key()),
                        composeTypeMatchups(types, fullDataDto.types())
                );
                species.add(speciesDto);
            } catch (Exception e) {
                // OK, we just proceed.
            }
        }
        return species;
    }

    public Map<EType, EResistance> composeTypeResists(List<EType> types, Map<Integer, FullDataTypeDto> typeData) {
        Map<EType, List<EMatchup>> matchups = new HashMap<>();
        for (Map.Entry<Integer, FullDataTypeDto> entry : typeData.entrySet()) {
            EType attackingType = EType.fromId(entry.getKey());
            List<Integer> definedMatchups = entry.getValue().matchup();
            for (EType defendingType : types) {
                EMatchup matchup = EMatchup.fromId(definedMatchups.get(defendingType.getId()));
                if (!matchups.containsKey(attackingType)) {
                    matchups.put(attackingType, new ArrayList<>());
                }
                matchups.get(attackingType).add(matchup);
            }
        }
        Map<EType, EResistance> resists = new HashMap<>();
        for (Map.Entry<EType, List<EMatchup>> entry : matchups.entrySet()) {
            EType type = entry.getKey();
            List<EMatchup> matchupList = entry.getValue();
            String multiplied = matchupList.stream().map(it -> it.multiplier).reduce(1.0, (a, b) -> a * b).toString();
            EResistance resist = switch (multiplied) {
                case "1.0" -> EResistance.STANDARD;
                case "0.0" -> EResistance.IMMUNE;
                case "0.5" -> EResistance.RESISTANT;
                case "0.25" -> EResistance.VERY_RESISTANT;
                case "2.0" -> EResistance.WEAK;
                case "4.0" -> EResistance.VERY_WEAK;
                default -> throw new IllegalArgumentException("Unknown resistance multiplier: " + multiplied);
            };
            resists.put(type, resist);
        }
        return resists;
    }

    /**
     * Composes the offensive type matchups for the given types.
     */
    public Map<EType, EMatchup> composeTypeMatchups(List<EType> types, Map<Integer, FullDataTypeDto> typeData) {
        Map<EType, List<EMatchup>> matchups = new HashMap<>();
        for (EType attackingType : types) {
            FullDataTypeDto entry = typeData.get(attackingType.getId());
            List<Integer> definedMatchups = entry.matchup();
            for (EType defendingType : types) {
                EMatchup matchup = EMatchup.fromId(definedMatchups.get(defendingType.getId()));
                if (!matchups.containsKey(attackingType)) {
                    matchups.put(attackingType, new ArrayList<>());
                }
                matchups.get(attackingType).add(matchup);
            }
        }
        Map<EType, EMatchup> results = new HashMap<>();
        for (Map.Entry<EType, List<EMatchup>> entry : matchups.entrySet()) {
            EType type = entry.getKey();
            List<EMatchup> matchupList = entry.getValue();
            String max = matchupList.stream().map(it -> it.multiplier).sorted().toList().getLast().toString();
            EMatchup effectiveness = switch (max) {
                case "1.0" -> EMatchup.STANDARD;
                case "0.0" -> EMatchup.IMMUNE;
                case "0.5" -> EMatchup.NOT_VERY_EFFECTIVE;
                case "2.0" -> EMatchup.SUPER_EFFECTIVE;
                default -> throw new IllegalArgumentException("Unknown resistance multiplier: " + max);
            };
            results.put(type, effectiveness);
        }
        return results;
    }

    private List<EForm> getForms(String speciesKey) {
        try {
            if (!speciesKey.contains("-") || formlessPokemon.contains(speciesKey)) {
                // If the species name does not contain a form, we assume it's the base form.
                return Collections.emptyList();
            }
            String[] formsText = speciesKey.split("-")[1].split("-");
            return Arrays.stream(formsText).map(it -> EForm.valueOf(it.toUpperCase())).toList();
        } catch (Exception e) {
            throw new IllegalArgumentException("On purpose ignored species: " + speciesKey, e);
        }
    }
}
