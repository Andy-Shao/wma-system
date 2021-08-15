package com.andyshao.application.wma.controller;

import com.andyshao.application.wma.controller.vo.PageSearchParams;
import com.andyshao.application.wma.controller.domain.PageInfo;
import com.andyshao.application.wma.service.PageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

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

    @PostMapping("/getPageByIds")
    @ResponseBody
    public Flux<PageInfo> getPageByIds(@RequestBody PageSearchParams params) {
        return this.pageService.getPageByIds(params, null);
    }

    @GetMapping("/getPage/{id}")
    public Mono<PageInfo> getPageById(@PathVariable("id")String pageId) {
        return this.pageService.getPageInfo(pageId, null);
    }

    @PutMapping("/addGroup")
    @ResponseBody
    public Mono<PageInfo> addGroup(@RequestParam("pageId")String pageId,
                                   @RequestParam(value = "groupId", required = false) String groupId) {
        return this.pageService.addGroup(pageId, groupId, null);
    }

    @DeleteMapping("/removeGroup")
    @ResponseBody
    public Mono<Void> removeGroup(@RequestParam("pageId")String pageId, @RequestParam("groupId")String groupId) {
        return this.pageService.removeGroup(pageId, groupId, null);
    }
}
