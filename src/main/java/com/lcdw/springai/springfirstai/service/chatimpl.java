package com.lcdw.springai.springfirstai.service;

import com.lcdw.springai.springfirstai.Model.Tuf;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class chatimpl  implements chatservice {

    private final ChatClient chatClient;

    public chatimpl(ChatClient.Builder builder) {
        this.chatClient = builder.build();
    }



    @Override
    public List<Tuf> Chat(String message) {
        Tuf er= new Tuf();
        er.setTitle("Error");
        er.setDescription("An error occurred while processing the request.");
        er.setYearofpublication(2026);

         try{

              List<Tuf> response = chatClient.prompt(message)
                     .call()
                     .entity(new ParameterizedTypeReference<List<Tuf>>() {
                     });

             return response;
         }catch(Exception e){
               return List.of(er);
         }


    }
}
