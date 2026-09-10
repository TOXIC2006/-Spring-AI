package com.lcdw.springai.springfirstai.RestController;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.google.genai.GoogleGenAiChatModel;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("chat")
public class controller {
     private ChatClient OPENAI_CHAT_CLIENT;
     private  ChatClient Ollama_CHAT_CLIENT;
     private ChatClient GoogleGenAi_CHAT_CLIENT;
    public controller(
            @Qualifier("OpenAiChatModel") ChatClient openAiChatModel,
            @Qualifier("OllamaChatModel") ChatClient ollamaChatModel,
            @Qualifier("GemeniChatModel") ChatClient googleGenAiChatModel) {

        this.OPENAI_CHAT_CLIENT = openAiChatModel;
        this.Ollama_CHAT_CLIENT = ollamaChatModel;
        this.GoogleGenAi_CHAT_CLIENT = googleGenAiChatModel;
    }
    // constutor in injection


     @GetMapping
     public ResponseEntity<?> chat(@RequestParam(value = "q" ,required = true) String message) {
         var response =  Ollama_CHAT_CLIENT.prompt(message).call().content();
          var reverify= Ollama_CHAT_CLIENT.prompt(response+ "verify it and add the line verifed in last").call().content();
          var gemeniresponse= GoogleGenAi_CHAT_CLIENT.prompt(message).call().content()+"gemeni in last text";
          return ResponseEntity.ok().body("You said: " + message    + " and the AI said: " + gemeniresponse );
     }
}
