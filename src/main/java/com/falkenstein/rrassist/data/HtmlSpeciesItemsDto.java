package com.falkenstein.rrassist.data;

import org.jsoup.nodes.Node;

import java.util.List;

public record HtmlSpeciesItemsDto(int id, List<Node> nodes) {
}
