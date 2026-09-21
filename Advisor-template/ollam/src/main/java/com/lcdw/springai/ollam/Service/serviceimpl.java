package com.lcdw.springai.ollam.Service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SafeGuardAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class serviceimpl implements service {

    private final ChatClient chatClient;

    @Value("classpath:/userpromt.st")
    private Resource userresource;

    @Value("classpath:/sysprompt.st")
    private  Resource systemresurce;
    List<String> forbiddenWords = List.of("games and gambling", "adult content","cartoons and comics");
    public serviceimpl(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder
                .defaultAdvisors(new SimpleLoggerAdvisor(), new SafeGuardAdvisor(List.of("games and gambling", "adult content","cartoons and comics")))
                .build();
    }

    @Override
    public String chat(String message) {
        return chatClient.prompt(message).call().content();
    }

    @Override
    public String chattemplate(String message) {
        var ans = chatClient.prompt()
                .system(s -> s.text(systemresurce))
                .user(u -> u.text(userresource).param("subject", message))
                .call()
                .content();
        return ans;
    }
    // hello

}
