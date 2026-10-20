package com.lcdw.springai.ollam.Service;

import reactor.core.publisher.Flux;

import java.util.List;

public interface service {
   String getreponse(String message);
   void savedata(List<String> list);
}
