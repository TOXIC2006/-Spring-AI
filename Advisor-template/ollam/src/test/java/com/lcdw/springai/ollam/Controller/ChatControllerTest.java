package com.lcdw.springai.ollam.Controller;

import com.lcdw.springai.ollam.Service.service;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ChatControllerTest {

    @Mock
    private service chatService;

    @Test
    void testSimpleChat() {
        when(chatService.chat("Hello")).thenReturn("Hi there!");

        ChatController controller = new ChatController(chatService);
        ResponseEntity<String> response = controller.chat("Hello");

        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
    }

    @Test
    void testChatTemplate() {
        when(chatService.chattemplate("Java")).thenReturn("Java is a programming language.");

        ChatController controller = new ChatController(chatService);
        ResponseEntity<String> response = controller.chatTemplate("Java");

        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
    }

    @Test
    void testEndpoint() {
        ChatController controller = new ChatController(chatService);
        ResponseEntity<String> response = controller.test();

        assertNotNull(response);
        assertEquals("Test endpoint is working!", response.getBody());
    }
}
