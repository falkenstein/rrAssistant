package com.falkenstein.rrassist.docs;

import java.util.List;

public record ParagraphDto(
        String style,
        String text,
        List<String> table
) {
}
