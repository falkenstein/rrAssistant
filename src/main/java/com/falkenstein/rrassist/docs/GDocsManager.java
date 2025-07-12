package com.falkenstein.rrassist.docs;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.docs.v1.Docs;
import com.google.api.services.docs.v1.DocsScopes;
import com.google.api.services.docs.v1.model.*;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.GoogleCredentials;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;

@Service
public class GDocsManager {

    private HttpCredentialsAdapter authorize() throws IOException {
        GoogleCredentials credentials = GoogleCredentials.fromStream(new FileInputStream("credentials.json"))
                .createScoped(List.of(DocsScopes.DOCUMENTS));
        credentials.refreshIfExpired();
        return new HttpCredentialsAdapter(credentials);
    }

    private List<ParagraphDto> readMonotypeTwoDocument() throws GeneralSecurityException, IOException {
        NetHttpTransport transport = GoogleNetHttpTransport.newTrustedTransport();
        GsonFactory factory = GsonFactory.getDefaultInstance();
        Docs docsService = new Docs.Builder(transport, factory, authorize())
                .setApplicationName("RRAssist")
                .build();
        String documentId = "10HJkWRr-rGj1B4r8RSIEjtp5ZGZkhQzkp17Tqv5MTeY";
        Document document = docsService.documents().get(documentId).execute();
        return document.getBody().getContent().stream()
                .filter(it -> it.getParagraph() != null || it.getTable() != null)
                .map(it -> new ParagraphDto(
                        setupParagraphStyle(it),
                        setupText(it),
                        getPokemonNamesFromTable(it)
                ))
                .toList();
    }

    private String setupParagraphStyle(StructuralElement element) {
        if (element.getParagraph() != null && element.getParagraph().getParagraphStyle().getNamedStyleType() != null) {
            return element.getParagraph().getParagraphStyle().getNamedStyleType();
        } else if (element.getTable() != null) {
            return "TABLE";
        } else {
            return "UNKNOWN";
        }
    }

    private String setupText(StructuralElement element) {
        if (element.getParagraph() == null) {
            return null;
        }
        TextRun textRun = element.getParagraph().getElements().getFirst().getTextRun();
        if (textRun != null) {
            return textRun.getContent().trim();
        } else {
            return null;
        }
    }

    private List<String> getPokemonNamesFromTable(StructuralElement tableElement) {
        List<String> names = new ArrayList<>();
        if (tableElement == null || tableElement.getTable() == null) return names;
        for (TableRow row : tableElement.getTable().getTableRows()) {
            for (TableCell cell : row.getTableCells()) {
                if (cell.getContent() == null) continue;
                for (StructuralElement content : cell.getContent()) {
                    if (content.getParagraph() == null || content.getParagraph().getElements() == null) continue;
                    for (ParagraphElement element : content.getParagraph().getElements()) {
                        if (element.getTextRun() != null && element.getTextRun().getContent() != null) {
                            String text = element.getTextRun().getContent().trim();
                            if (!text.isBlank() && !text.equals("\n")) {
                                names.add(text.replace("\n", "").trim());
                            }
                        }
                    }
                }
            }
        }
        return names;
    }

    public List<MonotypeThreeDocsDto> composePlannedMonotypes() throws GeneralSecurityException, IOException {
        List<ParagraphDto> paragraphs = readMonotypeTwoDocument();
        List<String> types = paragraphs.stream().filter(it -> it.style().equals("HEADING_2")).map(ParagraphDto::text).toList();
        List<List<String>> pokemon = paragraphs.stream()
                .filter(it -> it.style().equals("TABLE"))
                .map(ParagraphDto::table)
                .map(this::filterOnlyPokemon)
                .toList();
        List<MonotypeThreeDocsDto> fetchedData = new ArrayList<>();
        for (int i = 0; i < 18; i++) {
            fetchedData.add(new MonotypeThreeDocsDto(types.get(i), pokemon.get(i)));
        }
        return fetchedData;
    }

    private List<String> filterOnlyPokemon(List<String> input) {
        List<String> filtered = new ArrayList<>();
        for (int i = 3; i < input.size(); i++) { // We intentionally drop the first three elements of the list.
            if (!input.get(i).equals("Early") && !input.get(i).equals("Visitor") && !input.get(i).equals("Late")) {
                filtered.add(input.get(i));
            }
        }
        return filtered;
    }

}
