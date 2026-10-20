package com.lcdw.springai.ollam.Controller;

import com.lcdw.springai.ollam.Service.service;
import com.lcdw.springai.ollam.Service.serviceimpl;
import com.lcdw.springai.ollam.advisor.PrintAdvisor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import java.security.Principal;
import java.util.Map;

@RestController
@RequestMapping("chat")
public class ChatController {

  @Autowired
  public serviceimpl serviceimpl;
  @GetMapping("test")
  public ResponseEntity<String> Chat(@RequestParam("q") String message) {
    String response = serviceimpl.getreponse(message);
    return response != null ? ResponseEntity.ok(response) : ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing the request");
  }






}
