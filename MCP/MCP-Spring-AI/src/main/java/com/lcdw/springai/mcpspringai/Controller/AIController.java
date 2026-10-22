package com.lcdw.springai.mcpspringai.Controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ai")
public class AIController {

    private final ChatClient chatClient;

    public AIController(ChatClient.Builder chatClientBuilder, ToolCallbackProvider toolCallbackProvider) {
        this.chatClient = chatClientBuilder.defaultTools(toolCallbackProvider).build();
    }

    @PostMapping("/response")
    public ResponseEntity<String> getAIResponse(@RequestParam("query") String query) {
        String response = chatClient.prompt(query).call().content();

        return ResponseEntity.ok(response);
    }
}