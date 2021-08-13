package com.andyshao.application.wma.controller;

import com.andyshao.application.wma.domain.MemoryRecordInfo;
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

    @DeleteMapping("/remove/{id}")
    @ResponseBody
    public Mono<Void> removeRecord(@PathVariable("id") String uuid) {
        return this.memoryRecordService.removeRecord(uuid, null);
    }

    @PostMapping(value = "/saveOrUpdate")
    @ResponseBody
    public Mono<Result<Void>> saveOrUpdateRecord(@RequestBody MemoryRecordInfo memoryRecordInfo) {
        return this.memoryRecordService.saveOrUpdateMemoryRecord(memoryRecordInfo, null)
                .then(Mono.just(Result.success()));
    }
}
