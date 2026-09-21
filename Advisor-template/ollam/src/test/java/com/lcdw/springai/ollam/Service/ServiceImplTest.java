package com.lcdw.springai.ollam.Service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ai.chat.client.ChatClient;

import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ServiceImplTest {

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private ChatClient.Builder chatClientBuilder;

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private ChatClient chatClient;

    @Test
    void testChat() {
        when(chatClientBuilder.defaultAdvisors(any(), any())).thenReturn(chatClientBuilder);
        when(chatClientBuilder.build()).thenReturn(chatClient);
        when(chatClient.prompt("Hello").call().content()).thenReturn("Hi!");

        serviceimpl serviceImpl = new serviceimpl(chatClientBuilder);
        String response = serviceImpl.chat("Hello");

        assertNotNull(response);
    }

    @Test
    void testChatTemplate() {
        when(chatClientBuilder.defaultAdvisors(any(), any())).thenReturn(chatClientBuilder);
        when(chatClientBuilder.build()).thenReturn(chatClient);
        when(chatClient.prompt()
                .system(any(Consumer.class))
                .user(any(Consumer.class))
                .call()
                .content()).thenReturn("AI response");

        serviceimpl serviceImpl = new serviceimpl(chatClientBuilder);
        String response = serviceImpl.chattemplate("Java");

        assertNotNull(response);
    }
}
