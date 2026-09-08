package com.lcdw.springai.springfirstai.RestController;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("chat")
public class controller {
     private ChatClient chatClient;
    public controller(ChatClient.Builder builder) {
        this.chatClient = builder.build();
    }
    // constutor in injection


     @GetMapping
     public ResponseEntity<?> chat(@RequestParam(value = "q" ,required = true) String message) {
         var response =  chatClient.prompt(message).call().content();
          return ResponseEntity.ok().body("You said: " + message + " and the AI said: " + response);
     }
}
