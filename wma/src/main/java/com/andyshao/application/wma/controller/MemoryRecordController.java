package com.andyshao.application.wma.controller;

import com.andyshao.application.wma.controller.domain.MemoryRecordInfo;
import com.andyshao.application.wma.controller.domain.PageInfo;
import com.andyshao.application.wma.service.MemoryRecordService;
import com.github.andyshao.exception.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Title: <br>
 * Description: <br>
 * Copyright: Copyright(c) 2021/7/28
 * Encoding: UNIX UTF-8
 *
 * @author Andy.Shao
 */
@RestController
@RequestMapping("/memoryRecord")
public class MemoryRecordController {
    @Autowired
    private MemoryRecordService memoryRecordService;

    @GetMapping("/records")
    @ResponseBody
    public Flux<MemoryRecordInfo> getRecords() {
        return this.memoryRecordService.findMemoryRecords(null);
    }

    @GetMapping("/record/{id}")
    @ResponseBody
    public Mono<MemoryRecordInfo> findById(@PathVariable("id") String id) {
        return this.memoryRecordService.findRecordById(id, null);
    }

    @GetMapping("/study/{id}")
    @ResponseBody
    public Mono<PageInfo> studyPage(@PathVariable("id")String recordId) {
        return this.memoryRecordService.studyPage(recordId, null);
    }

    @PutMapping("/finishStudy")
    @ResponseBody
    public Mono<PageInfo> finishStudyPage(@RequestParam("recordId")String recordId) {
        return this.memoryRecordService.finishStudyPage(recordId, null);
    }

    @PutMapping("/restudyTomorrow")
    @ResponseBody
    public Mono<PageInfo> restudyPage(@RequestParam("recordId")String recordId) {
        return this.memoryRecordService.restudyPage(recordId, null);
    }

    @PostMapping("/moveStudyPage")
    public Mono<PageInfo> moveToAnotherRecord(@RequestParam("originRecordId")String originRecordId,
                                          @RequestParam("targetRecordId")String targetRecordId,
                                          @RequestParam("pageId")String pageId) {
        return this.memoryRecordService.moveStudyPageToAnotherRecord(originRecordId, targetRecordId, pageId, null);
    }

    @DeleteMapping("/remove/{id}")
    @ResponseBody
    public Mono<String> removeRecord(@PathVariable("id") String uuid) {
        return this.memoryRecordService.removeRecord(uuid, null);
    }

    @PostMapping(value = "/saveOrUpdate")
    @ResponseBody
    public Mono<Result<Void>> saveOrUpdateRecord(@RequestBody MemoryRecordInfo memoryRecordInfo) {
        return this.memoryRecordService.saveOrUpdateMemoryRecordOpt(memoryRecordInfo, null)
                .then(Mono.just(Result.success()));
    }

    @PutMapping("/addPage")
    @ResponseBody
    public Mono<MemoryRecordInfo> addPage(@RequestParam("recordId") String recordId,
                                          @RequestParam(value = "pageId", required = false) String pageId) {
        return this.memoryRecordService.addPage(recordId, pageId, null);
    }

    @DeleteMapping("/removePage")
    @ResponseBody
    public Mono<String> removePage(@RequestParam("recordId") String recordId, @RequestParam("pageId") String pageId) {
        return this.memoryRecordService.removePage(recordId, pageId, null);
    }
}
