package com.lcdw.springai.ollam.Service;

import org.springframework.ai.document.Document;

import java.util.List;

public interface DataLoader {
    List<Document> loadDocumentsJson();
    List<Document> loadDocumentsPDF();
}