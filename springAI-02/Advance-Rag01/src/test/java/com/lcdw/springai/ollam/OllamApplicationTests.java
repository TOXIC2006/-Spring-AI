package com.lcdw.springai.ollam;

import com.lcdw.springai.ollam.Service.DataLoader;
import com.lcdw.springai.ollam.Service.Datatranformer;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class OllamApplicationTests {

    private static final String PDF_FILE_NAME = "leps106.pdf";

    @Autowired
    private DataLoader dataLoader;

    @Autowired
    private Datatranformer dataTransformer;

    @Autowired
    private VectorStore vectorStore;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    @Order(1)
    void pdfEtl_isLoadedIntoTheDatabase() {
        // ---- EXTRACT ----
        List<Document> pages = dataLoader.loadDocumentsPDF();
        System.out.println("EXTRACT  -> " + pages.size() + " pages read from " + PDF_FILE_NAME);
        assertFalse(pages.isEmpty(), "the PDF reader returned nothing");
        assertTrue(pages.stream().noneMatch(p -> p.getText().isBlank()), "a page was extracted with blank text");

        // ---- TRANSFORM ----
        List<Document> chunks = dataTransformer.transform(pages);
        System.out.println("TRANSFORM-> " + chunks.size() + " chunks");
        assertTrue(chunks.size() >= pages.size(), "chunking should not lose pages");

        // ---- LOAD ----
        int purged = purgePdfRows();
        System.out.println("LOAD     -> purged " + purged + " stale rows, inserting " + chunks.size());

        int before = countRows();
        vectorStore.add(chunks);
        int after = countRows();
        System.out.println("DB ROWS  -> " + before + " before, " + after + " after");

        assertEquals(chunks.size(), after - before,
                "every chunk should land in vector_store as its own row");
        assertEquals(chunks.size(), countPdfRows(),
                "rows tagged file_name=" + PDF_FILE_NAME + " should equal the number of chunks inserted");
    }

    @Test
    @Order(2)
    void loadedPdfIsRetrievableBySimilaritySearch() {
        List<Document> hits = vectorStore.similaritySearch(SearchRequest.builder()
                .query("environment and natural resources")
                .topK(5)
                .similarityThreshold(0.3)
                .build());

        System.out.println("RETRIEVE -> " + hits.size() + " hits");
        hits.forEach(h -> System.out.println("   " + h.getMetadata().get("file_name")
                + " p." + h.getMetadata().get("page_number") + " : " + oneLine(h.getText())));

        assertFalse(hits.isEmpty(), "similarity search returned nothing - the PDF data is not queryable");
        assertTrue(hits.stream().anyMatch(h -> PDF_FILE_NAME.equals(h.getMetadata().get("file_name"))),
                "expected at least one hit from " + PDF_FILE_NAME + ", but every hit came from other data");
    }

    @Test
    @Order(3)
    void jsonEtl_isLoadedIntoTheDatabase() {
        List<Document> airports = dataLoader.loadDocumentsJson();
        System.out.println("EXTRACT  -> " + airports.size() + " airports");
        assertEquals(30, airports.size(), "airports.json holds 30 airports under the 'airports' key");

        List<Document> chunks = dataTransformer.transform(airports);

        int purged = purgeAirportRows();
        System.out.println("LOAD     -> purged " + purged + " stale rows, inserting " + chunks.size());

        int before = countRows();
        vectorStore.add(chunks);
        int after = countRows();

        assertEquals(chunks.size(), after - before, "every airport should land in vector_store");

        List<Document> hits = vectorStore.similaritySearch(SearchRequest.builder()
                .query("Which airport is in Dubai?")
                .topK(3)
                .similarityThreshold(0.3)
                .build());
        System.out.println("RETRIEVE -> " + hits.stream().map(h -> oneLine(h.getText())).toList());
        assertTrue(hits.stream().anyMatch(h -> h.getText().contains("DXB")), "expected Dubai (DXB) in the top hits");
    }

    // The two load tests keep their data so the RAG endpoints can use it. Purging first makes
    // them safe to re-run - without this every run appends another full copy of the source.
    private int purgePdfRows() {
        return jdbcTemplate.update("DELETE FROM vector_store WHERE JSON_UNQUOTE(JSON_EXTRACT(metadata, '$.file_name')) = ?",
                PDF_FILE_NAME);
    }

    private int purgeAirportRows() {
        return jdbcTemplate.update("DELETE FROM vector_store WHERE content LIKE 'code: %'");
    }

    private int countRows() {
        Integer n = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM vector_store", Integer.class);
        return n == null ? -1 : n;
    }

    private int countPdfRows() {
        Integer n = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM vector_store WHERE JSON_UNQUOTE(JSON_EXTRACT(metadata, '$.file_name')) = ?",
                Integer.class, PDF_FILE_NAME);
        return n == null ? -1 : n;
    }

    private static String oneLine(String text) {
        String flat = text.replaceAll("[^\\p{Print}]", " ").replaceAll("\\s+", " ").trim();
        return flat.length() > 110 ? flat.substring(0, 110) + "..." : flat;
    }
}
