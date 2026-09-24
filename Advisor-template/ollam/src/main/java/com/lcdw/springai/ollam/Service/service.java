package com.lcdw.springai.ollam.Service;

import reactor.core.publisher.Flux;

public interface service {
    String chat(String message);
    String chattemplate(String message);

    Flux<String> fluxResponse(String message);
}
