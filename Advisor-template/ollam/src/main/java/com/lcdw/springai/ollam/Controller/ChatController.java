package com.lcdw.springai.ollam.Controller;

import com.lcdw.springai.ollam.Service.service;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("chat")
public class ChatController {

  private final service chatService;

  // constructor injection (ChatClient is built in serviceimpl, not here)
  public ChatController(service chatService) {
    this.chatService = chatService;
  }

  @GetMapping("/simple")
  public ResponseEntity<String> chat(@RequestParam(value = "q") String message) {
    var response = chatService.chat(message);
    return ResponseEntity.ok().body("You said: " + message + "\n" + " and the AI said: " + response);
  }

  @GetMapping("/template")
  public ResponseEntity<String> chatTemplate(
      @RequestParam(value = "q", defaultValue = "Java", required = true) String message) {
    var response = chatService.chattemplate(message);
    return ResponseEntity.ok().body("Prompt Template Response: " + response);
  }

  @GetMapping("/test")
  public ResponseEntity<String> test() {
    return ResponseEntity.ok().body("Test endpoint is working!");
  }
}
