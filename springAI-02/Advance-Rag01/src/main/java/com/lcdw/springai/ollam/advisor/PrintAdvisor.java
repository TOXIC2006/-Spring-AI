package com.lcdw.springai.ollam.advisor;

import org.slf4j.ILoggerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClientRequest;
import org.springframework.ai.chat.client.ChatClientResponse;
import org.springframework.ai.chat.client.advisor.api.CallAdvisor;
import org.springframework.ai.chat.client.advisor.api.CallAdvisorChain;
import org.springframework.ai.chat.client.advisor.api.StreamAdvisor;
import org.springframework.ai.chat.client.advisor.api.StreamAdvisorChain;
import reactor.core.publisher.Flux;

public class PrintAdvisor implements StreamAdvisor, CallAdvisor {

    private static final Logger log = LoggerFactory.getLogger(PrintAdvisor.class);
    @Override
    public Flux<ChatClientResponse> adviseStream(ChatClientRequest chatClientRequest, StreamAdvisorChain streamAdvisorChain) {
        Flux<ChatClientResponse> ans= streamAdvisorChain.nextStream(chatClientRequest);
        return ans;

    }

    private void logger() {
    }

    @Override
    public String getName() {
        return  this.getClass().getName() ;
    }

    @Override
    public int getOrder() {
        return 0;
    }

    @Override
    public ChatClientResponse adviseCall(ChatClientRequest chatClientRequest, CallAdvisorChain callAdvisorChain) {
        this.log.info("Request: {}", chatClientRequest);

        this.log.info(  "request"+ chatClientRequest.prompt().getContents());
        ChatClientResponse chatClientResponse = callAdvisorChain.nextCall(chatClientRequest);
        this.log.info("Response:{}", chatClientResponse.
                chatResponse()
                .getResult()
                .getOutput()
                .getText());

         this.log.info("Response: Total Tokens: {}", chatClientResponse.chatResponse().getMetadata().getUsage().getTotalTokens());
        this.log.info("Response: prompt Tokens: {}", chatClientResponse.chatResponse().getMetadata().getUsage().getPromptTokens());
        this.log.info("Response: prompt Tokens: {}", chatClientResponse.chatResponse().getMetadata().getUsage().getCompletionTokens());


        return chatClientResponse;
    }

}
