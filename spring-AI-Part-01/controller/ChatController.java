package com.lcdw.springai.springfirstai.controller;

import com.lcdw.springai.springfirstai.Model.Tuf;
import com.lcdw.springai.springfirstai.service.chatservice;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("chat")
public class ChatController {

//    private final ChatClient openAiChatClient;
//    private final ChatClient ollamaChatClient;
//    private final ChatClient geminiChatClient;
//
//    public ChatController(
//            @Qualifier("openAiChatClient") ChatClient openAiChatClient,
//            @Qualifier("ollamaChatClient") ChatClient ollamaChatClient,
//            @Qualifier("geminiChatClient") ChatClient geminiChatClient) {
//
//        this.openAiChatClient = openAiChatClient;
//        this.ollamaChatClient = ollamaChatClient;
//        this.geminiChatClient = geminiChatClient;
//    }
    @Autowired
     private chatservice chatservice;


   private  ChatClient chatClient;
    public ChatController( ChatClient.Builder builder) {
        this.chatClient = builder.build();
    }

    @GetMapping
    public ResponseEntity<List<Tuf>> chat(@RequestParam(value = "q", required = true) String message) {
        try {
            // Using Gemini as default for this example, but you can choose based on input
//            var geminiResponse = geminiChatClient.prompt(message).call().content();
//            var ollamaResponse = ollamaChatClient.prompt(message).call().content();
////            var openAiResponse = openAiChatClient.prompt(message).call().content();

            return ResponseEntity.ok().body(chatservice.Chat(message));
        } catch (Exception e) {
            System.out.println("Error occurred while processing chat request: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(null);
        }
    }
}
