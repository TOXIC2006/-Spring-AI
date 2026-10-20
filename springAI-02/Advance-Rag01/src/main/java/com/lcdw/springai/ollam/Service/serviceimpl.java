package com.lcdw.springai.ollam.Service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.api.Advisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.rag.advisor.RetrievalAugmentationAdvisor;
import org.springframework.ai.rag.generation.augmentation.ContextualQueryAugmenter;
import org.springframework.ai.rag.preretrieval.query.expansion.MultiQueryExpander;
import org.springframework.ai.rag.preretrieval.query.transformation.RewriteQueryTransformer;
import org.springframework.ai.rag.retrieval.join.ConcatenationDocumentJoiner;
import org.springframework.ai.rag.retrieval.search.VectorStoreDocumentRetriever;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import org.springframework.ai.document.Document;
import java.util.List;

@Service
public class serviceimpl implements service {

    public static final String CHAT_MEMORY_CONVERSATION_ID_KEY = "chat_memory_conversation_id";

    private final ChatClient chatClient;
    private final ChatMemory chatMemory;
    private VectorStore vectorStore;

    @Value("classpath:/userpromt.st")
    private Resource userresource;

    @Value("classpath:/sysprompt.st")
    private Resource systemresurce;

    @Autowired
    public serviceimpl(ChatClient chatClient, VectorStore vectorStore, ChatMemory chatMemory) {
        this.chatClient = chatClient;
        this.vectorStore = vectorStore;
        this.chatMemory = chatMemory;
    }

//    @Override
//    public String chat(String message, String conversationId) {
//        return chatClient.prompt(message)
//                .advisors(a -> a.param(ChatMemory.CONVERSATION_ID, conversationId))
//
//                .call()
//                .content();
//    }

//    @Override
//    public String chattemplate(String message, String conversationId) {
//        String contextdata = retrieveContext(message);
//
//        Advisor retrievalAugmentationAdvisor = RetrievalAugmentationAdvisor.builder()
//                .documentRetriever(VectorStoreDocumentRetriever.builder()
//                        .similarityThreshold(0.50)
//                        .vectorStore(this.vectorStore)
//                        .build())
//                .queryAugmenter(ContextualQueryAugmenter.builder().allowEmptyContext(true).build())
//                .build();
//
//        return chatClient.prompt()
//                .advisors(retrievalAugmentationAdvisor)
////                .system(s -> s.text(systemresurce).param("documents", contextdata))
//                .user(u -> u.text(userresource).param( "query", message))
//                .call()
//                .content();
//    }

    private String retrieveContext(String message) {
        SearchRequest searchRequest = SearchRequest.builder().
                topK(5)
                .similarityThreshold(0.7).query(message).build();
         List<Document> documents = this.vectorStore.similaritySearch(searchRequest);
         List<String> content = documents.stream().map(Document::getText).toList();
         return String.join(", ", content);
    }


    @Override
    public String getreponse(String message) {
        var   adviosr = RetrievalAugmentationAdvisor.builder()
                .queryTransformers(RewriteQueryTransformer.builder().
                        chatClientBuilder(chatClient.mutate()
                                .clone()).build()
                        )
                .queryExpander(MultiQueryExpander.builder().
                        chatClientBuilder(chatClient.mutate()
                                .clone()).build())
                .documentRetriever(VectorStoreDocumentRetriever.builder()
                        .similarityThreshold(0.50)
                        .vectorStore(this.vectorStore)
                        .build())
                .documentJoiner( new ConcatenationDocumentJoiner())
                .queryAugmenter(ContextualQueryAugmenter.builder().allowEmptyContext(true).build())
                .build();
//
        return  chatClient.prompt()
                .advisors(MessageChatMemoryAdvisor.builder(chatMemory).build())
                .advisors(a -> a.param(CHAT_MEMORY_CONVERSATION_ID_KEY, "default"))// two type of adviosor
                .advisors(adviosr)
                .user(message)
                .call()
                .content();
    }

    @Override
    public void savedata(List<String> list) {
        List<Document> documentList = list. stream() .map(Document::new) .toList() ;
        this. vectorStore . add (documentList) ;
    }
}

