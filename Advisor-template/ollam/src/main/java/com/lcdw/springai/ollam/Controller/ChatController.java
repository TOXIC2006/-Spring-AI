package com.lcdw.springai.ollam.Controller;

import com.lcdw.springai.ollam.Service.service;
import com.lcdw.springai.ollam.advisor.PrintAdvisor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("chat")
public class ChatController {

  private final service chatService;

  public   PrintAdvisor printAdvisor;
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

  @GetMapping("Stream-flux")
  public ResponseEntity<Flux<String>> streamFLux(
          @RequestParam(value = "q", required = true) String message) {

     return   new ResponseEntity<>(chatService.fluxResponse( message), HttpStatus.OK);

  }





}
