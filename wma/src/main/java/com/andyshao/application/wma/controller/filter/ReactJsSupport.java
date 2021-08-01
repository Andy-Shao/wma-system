package com.andyshao.application.wma.controller.filter;

import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

/**
 * Title: <br>
 * Description: <br>
 * Copyright: Copyright(c) 2021/7/31
 * Encoding: UNIX UTF-8
 *
 * @author Andy.Shao
 */
@Component
public class ReactJsSupport implements WebFilter {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        final HttpHeaders headers = exchange.getResponse().getHeaders();
        headers.add("Access-Control-Allow-Origin", "*");
        headers.add("Access-Control-Allow-Headers", "*");
        headers.add("Access-Control-Allow-Methods", "*");
        return chain.filter(exchange);
    }
}
