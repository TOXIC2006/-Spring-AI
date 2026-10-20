package com.lcdw.springai.ollam.Service;

import org.springframework.ai.document.Document;
import org.springframework.ai.reader.ExtractedTextFormatter;
import org.springframework.ai.reader.JsonReader;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.reader.pdf.config.PdfDocumentReaderConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DataLoaderimpl  implements DataLoader {

    @Value("classpath:/airports.json")
    private Resource airportResource;

    @Override
    public List<Document> loadDocumentsJson() {
        JsonReader reader = new JsonReader(airportResource, "code", "name", "city", "country");
        return reader.get("/airports");
    }

    @Override
    public List<Document> loadDocumentsPDF() {
        // ParagraphPdfDocumentReader needs a TOC/bookmarks in the PDF; leps106.pdf has none.
        PagePdfDocumentReader pdfReader = new PagePdfDocumentReader("classpath:/leps106.pdf",
                PdfDocumentReaderConfig.builder()
                        .withPageTopMargin(0)
                        .withPageExtractedTextFormatter(ExtractedTextFormatter.builder()
                                .withNumberOfTopTextLinesToDelete(0)
                                .build())
                        .withPagesPerDocument(1)
                        .build());
        return pdfReader.read();
    }
}
