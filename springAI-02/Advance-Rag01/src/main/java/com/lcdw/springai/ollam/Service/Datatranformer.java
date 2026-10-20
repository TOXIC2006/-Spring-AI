package com.lcdw.springai.ollam.Service;

import org.springframework.ai.document.Document;

import java.util.List;

public interface Datatranformer {
    List<Document> transform(List<Document> documents);
}
