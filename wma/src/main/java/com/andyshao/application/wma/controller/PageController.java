package com.andyshao.application.wma.controller;

import com.andyshao.application.wma.domain.PageInfo;
import com.andyshao.application.wma.service.PageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

/**
 * Title: <br>
 * Description: <br>
 * Copyright: Copyright(c) 2021/8/1
 * Encoding: UNIX UTF-8
 *
 * @author Andy.Shao
 */
@RestController("/page")
public class PageController {
    @Autowired
    private PageService pageService;

    @GetMapping("/getPages")
    @ResponseBody
    public Flux<PageInfo> getPages() {
        return this.pageService.getPages(null);
    }
}
