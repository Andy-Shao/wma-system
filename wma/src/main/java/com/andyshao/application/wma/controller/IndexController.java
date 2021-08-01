package com.andyshao.application.wma.controller;

import com.andyshao.application.wma.service.MemoryRecordService;
import com.github.andyshao.exception.Result;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

/**
 * Title: <br>
 * Description: <br>
 * Copyright: Copyright(c) 2021/7/27
 * Encoding: UNIX UTF-8
 *
 * @author Andy.Shao
 */
@Controller
public class IndexController {
    public static final String HOME_PAGE = "homePage";
    @Value("${spring.profiles.active}")
    private String applicationLevel;
    @Autowired
    private MemoryRecordService memoryRecordService;

    @GetMapping("/application")
    @ResponseBody
    public Mono<Result<Map<String, String>>> application() {
        return Mono.create(ms -> {
            final HashMap<String, String> data = Maps.newHashMap();
            data.put("applicationName", "World Memory Assistance System");
            data.put("applicationLevel", this.applicationLevel);
            ms.success(Result.successData(data));
        });
    }

    @GetMapping("index.model")
    public Mono<String> homePage(final Model model) {
        return this.memoryRecordService.findMemoryRecords(null)
                .collectList()
                .doOnNext(it -> model.addAttribute("memoryRecords", it))
                .then(Mono.just("homePage"))
                .doOnNext(it -> {
                    model.addAttribute("systemType", this.applicationLevel);
                });
    }
}
