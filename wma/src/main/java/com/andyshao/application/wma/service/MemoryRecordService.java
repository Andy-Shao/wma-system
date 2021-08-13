package com.andyshao.application.wma.service;

import com.andyshao.application.wma.domain.MemoryRecordInfo;
import com.andyshao.application.wma.neo4j.dao.MemoryRecordDao;
import com.andyshao.application.wma.neo4j.domain.MemoryRecord;
import com.github.andyshao.lang.AutoIncreaseArray;
import com.github.andyshao.lang.StringOperation;
import com.github.andyshao.neo4j.annotation.Neo4jTransaction;
import com.github.andyshao.util.EntityOperation;
import com.google.common.collect.Sets;
import org.neo4j.driver.async.AsyncTransaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.HashSet;
import java.util.UUID;
import java.util.concurrent.CompletionStage;

/**
 * Title: <br>
 * Description: <br>
 * Copyright: Copyright(c) 2021/7/29
 * Encoding: UNIX UTF-8
 *
 * @author Andy.Shao
 */
@Service
public class MemoryRecordService {
    @Autowired
    private MemoryRecordDao memoryRecordDao;

    @Neo4jTransaction
    public Flux<MemoryRecordInfo> findMemoryRecords(CompletionStage<AsyncTransaction> tx) {
        return this.memoryRecordDao.findRecords(tx)
                .map(memoryRecord -> EntityOperation.copyProperties(memoryRecord, new MemoryRecordInfo()));
    }

    @Neo4jTransaction
    public Mono<MemoryRecordInfo> findRecordById(String uuid, CompletionStage<AsyncTransaction> tx) {
        return this.memoryRecordDao.findRecordById(uuid, tx)
                .map(it -> EntityOperation.copyProperties(it, new MemoryRecordInfo()));
    }

    @Neo4jTransaction
    public Mono<Void> saveOrUpdateMemoryRecord(MemoryRecordInfo recordInfo, CompletionStage<AsyncTransaction> tx) {
        if(StringOperation.isTrimEmptyOrNull(recordInfo.getUuid())) recordInfo.setUuid(UUID.randomUUID().toString());
        omitRepeatItem(recordInfo.getPageSequence());
        return this.memoryRecordDao.saveOrUpdate(EntityOperation.copyProperties(recordInfo, new MemoryRecord()), tx)
                .then();
    }

    private void omitRepeatItem(AutoIncreaseArray<String> pageSequence) {
        final HashSet<String> tmp = Sets.newHashSet();
        for(int i=0; i<pageSequence.size();) {
            final String item = pageSequence.get(i);
            if(tmp.contains(item)) {
                pageSequence.remove(i);
            }
            else {
                tmp.add(item);
                i++;
            }
        }
    }

    @Neo4jTransaction
    public Mono<Void> removeRecord(String uuid, CompletionStage<AsyncTransaction> tx) {
        return this.memoryRecordDao.removeMemoryRecord(uuid, tx);
    }
}
