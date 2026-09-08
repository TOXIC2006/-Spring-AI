package com.lcdw.springai.ollam;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("chat")
public class ChatController {

     ChatClient chatClient;
      // constructor injection
      ChatController(ChatClient.Builder chatClient) {
          this.chatClient = chatClient.build();
      }

      @GetMapping
        public ResponseEntity<String> chat(@RequestParam(value = "q") String message) {

           var  respone= chatClient.prompt(message).call().content();
            return ResponseEntity.ok().body("You said: " + message + "\n" + " and the AI said: " + respone);

      }
}
