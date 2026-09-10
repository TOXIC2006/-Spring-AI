package com.lcdw.springai.springfirstai.pack;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.google.genai.GoogleGenAiChatModel;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class config {
     public  ChatClient chatClient;
    @Bean(name="OpenAiChatModel")
    public ChatClient openAiChatModel(OpenAiChatModel openAiChatModel){
        return ChatClient.builder(openAiChatModel).build();
    }

   @Bean(name="OllamaChatModel")
   public ChatClient ollamaChatModel( OllamaChatModel ollamaChatModel){
        return ChatClient.builder(ollamaChatModel).build();

   }
   @Bean(name="GemeniChatModel")
   public ChatClient gemeniChatModel(GoogleGenAiChatModel googleGenAiChatModel){
        return ChatClient.builder(googleGenAiChatModel).build();
   }

}
