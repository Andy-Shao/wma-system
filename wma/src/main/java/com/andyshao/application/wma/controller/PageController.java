package com.andyshao.application.wma.controller;

import com.andyshao.application.wma.controller.vo.PageSearchParams;
import com.andyshao.application.wma.domain.PageInfo;
import com.andyshao.application.wma.service.PageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

/**
 * Title: <br>
 * Description: <br>
 * Copyright: Copyright(c) 2021/8/1
 * Encoding: UNIX UTF-8
 *
 * @author Andy.Shao
 */
@RestController
@RequestMapping("/page")
public class PageController {
    @Autowired
    private PageService pageService;

    @GetMapping("/getPages")
    @ResponseBody
    public Flux<PageInfo> getPages() {
        return this.pageService.getPages(null);
    }

    @PostMapping("/getPageByIds")
    @ResponseBody
    public Flux<PageInfo> getPageByIds(@RequestBody PageSearchParams params) {
        return this.pageService.getPageByIds(params, null);
    }
}
