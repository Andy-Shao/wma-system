package com.andyshao.application.wma.service;

import com.andyshao.application.wma.controller.domain.MemoryRecordInfo;
import com.andyshao.application.wma.controller.domain.PageInfo;
import com.andyshao.application.wma.neo4j.dao.MemoryRecordDao;
import com.andyshao.application.wma.neo4j.dao.PageDao;
import com.andyshao.application.wma.neo4j.domain.MemoryRecord;
import com.github.andyshao.lang.AutoIncreaseArray;
import com.github.andyshao.lang.StringOperation;
import com.github.andyshao.neo4j.annotation.Neo4jTransaction;
import com.github.andyshao.util.CollectionOperation;
import com.github.andyshao.util.EntityOperation;
import com.google.common.collect.Sets;
import org.neo4j.driver.async.AsyncTransaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.HashSet;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.atomic.AtomicReference;

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
    @Autowired
    private PageDao pageDao;
    @Autowired
    private PageService pageService;

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
    public Mono<Void> saveOrUpdateMemoryRecordOpt(MemoryRecordInfo recordInfo, CompletionStage<AsyncTransaction> tx) {
        if(StringOperation.isTrimEmptyOrNull(recordInfo.getUuid())) recordInfo.setUuid(UUID.randomUUID().toString());
        omitRepeatItem(recordInfo.getPageSequence());
        return this.memoryRecordDao.saveOrUpdateOpt(EntityOperation.copyProperties(recordInfo, new MemoryRecord()), tx)
                .then();
    }

    private void omitRepeatItem(AutoIncreaseArray<String> pageSequence) {
        if(CollectionOperation.isEmptyOrNull(pageSequence)) return;
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
    public Mono<String> removeRecord(String uuid, CompletionStage<AsyncTransaction> tx) {
        return this.memoryRecordDao.findRecordById(uuid, tx)
                .flatMap(memoryRecord -> {
                    if(CollectionOperation.isEmptyOrNull(memoryRecord.getPageSequence())) {
                        return this.memoryRecordDao.removeMemoryRecord(uuid, tx)
                                .then(Mono.just("Remove Success!"));
                    }
                    else {
                        return Mono.just("Record includes pages, consequently it cannot be eliminated.");
                    }
                });
    }

    @Neo4jTransaction
    public Mono<MemoryRecordInfo> addPage(final String recordId, String pageId,final CompletionStage<AsyncTransaction> tx) {
        final AtomicReference<String> pageUuid = new AtomicReference<>();
        if(StringOperation.isTrimEmptyOrNull(pageId)) pageUuid.set(UUID.randomUUID().toString());
        else pageUuid.set(pageId);
        if(StringOperation.isTrimEmptyOrNull(pageId)) pageId = UUID.randomUUID().toString();

        return this.memoryRecordDao.findRecordById(recordId, tx)
                .flatMap(memoryRecord -> {
                    return this.pageDao.findOrCreateById(pageUuid.get(), tx)
                            .flatMap(page -> {
                                AutoIncreaseArray<String> pageSequence = memoryRecord.getPageSequence();
                                if(Objects.isNull(pageSequence)) {
                                    pageSequence = new AutoIncreaseArray<>();
                                    memoryRecord.setPageSequence(pageSequence);
                                }
                                if(!pageSequence.contains(page.getUuid())) {
//                                    pageSequence.add(page.getUuid());
                                    pageSequence.addHead(page.getUuid());
                                    return this.memoryRecordDao.saveOrUpdateOpt(memoryRecord, tx);
                                }
                                else return Mono.just(memoryRecord);
                            });
                })
                .map(memoryRecord -> EntityOperation.copyProperties(memoryRecord, new MemoryRecordInfo()));
    }

    @Neo4jTransaction
    public Mono<String> removePage(final String recordId, final String pageId, final CompletionStage<AsyncTransaction> tx) {
        return this.pageDao.findGroups(pageId, tx)
                .collectList()
                .flatMap(groups -> {
                    if(CollectionOperation.isEmptyOrNull(groups)) {
                        return this.pageDao.removePageById(pageId, tx)
                                .then(this.memoryRecordDao.findRecordById(recordId, tx))
                                .flatMap(record -> {
                                    final AutoIncreaseArray<String> pageSequence = record.getPageSequence();
                                    final int index = pageSequence.indexOf(pageId);
                                    if(index != -1) {
                                        pageSequence.remove(index);
                                        return this.memoryRecordDao.saveOrUpdateOpt(record, tx)
                                                .then(Mono.just("Delete Success!"));
                                    }
                                    return Mono.just("Does not need to be left out!");
                                });
                    }
                    else {
                        return Mono.just("Page includes groups, therefore it cannot be omitted!");
                    }
                });
    }

    @Neo4jTransaction
    public Mono<PageInfo> studyPage(String recordId, final CompletionStage<AsyncTransaction> tx) {
        return this.memoryRecordDao.findRecordById(recordId, tx)
                .flatMap(record -> {
                    final AutoIncreaseArray<String> pageSequence = record.getPageSequence();
                    if(CollectionOperation.isEmptyOrNull(pageSequence)) return Mono.empty();
                    final int studyNumber = record.getStudyNumber();
                    if(studyNumber == 0) return Mono.empty();
                    final String pageUuid = pageSequence.get(0);

                    return this.pageService.getPageInfo(pageUuid, tx);
                });
    }

    @Neo4jTransaction
    public Mono<PageInfo> finishStudyPage(String recordId, final CompletionStage<AsyncTransaction> tx) {
        return this.memoryRecordDao.findRecordById(recordId, tx)
                .flatMap(record -> {
                    int studyNumber = record.getStudyNumber();
                    if(studyNumber == 0) return Mono.empty();
                    int newStudyNumber = Math.max(0, studyNumber - 1);
                    record.setStudyNumber(newStudyNumber);

                    final AutoIncreaseArray<String> pageSequence = record.getPageSequence();
                    if(CollectionOperation.isEmptyOrNull(pageSequence)) {
                        record.setPageSequence(new AutoIncreaseArray<>());
                        return this.memoryRecordDao.saveOrUpdateOpt(record, tx).then(Mono.empty());
                    }
                    if(pageSequence.size() > 1) {
                        pageSequence.move(0, pageSequence.size() - 1);
                    }

                    if(newStudyNumber == 0) {
                        return this.memoryRecordDao.saveOrUpdateOpt(record, tx)
                                .then(Mono.empty());
                    }
                    final String newPageId = pageSequence.get(0);
                    return this.memoryRecordDao.saveOrUpdateOpt(record, tx)
                            .then(this.pageService.getPageInfo(newPageId, tx));
                });
    }

    @Neo4jTransaction
    public Mono<PageInfo> restudyPage(String recordId, CompletionStage<AsyncTransaction> tx) {
        return this.memoryRecordDao.findRecordById(recordId, tx)
                .flatMap(record -> {
                    final int studyNumber = record.getStudyNumber();
                    if(studyNumber == 0) return Mono.empty();
                    int newStudyNumber = Math.max(0, studyNumber - 1);
                    record.setStudyNumber(newStudyNumber);

                    final AutoIncreaseArray<String> pageSequence = record.getPageSequence();
                    if(CollectionOperation.isEmptyOrNull(pageSequence)){
                        record.setPageSequence(new AutoIncreaseArray<>());
                        return this.memoryRecordDao.saveOrUpdateOpt(record, tx).then(Mono.empty());
                    }
                    if(pageSequence.size() > 1) {
                        pageSequence.move(0, Math.min(newStudyNumber, pageSequence.size() - 1));
                    }

                    if(newStudyNumber == 0) {
                        return this.memoryRecordDao.saveOrUpdateOpt(record, tx)
                                .then(Mono.empty());
                    }
                    final String newPageId = pageSequence.get(0);
                    return this.memoryRecordDao.saveOrUpdateOpt(record, tx)
                            .then(this.pageService.getPageInfo(newPageId, tx));
                });
    }

    @Neo4jTransaction
    public Mono<PageInfo> moveStudyPageToAnotherRecord(
            String originRecordId,
            String targetRecordId,
            String pageId,
            String moveType,
            final CompletionStage<AsyncTransaction> tx) {
        if(Objects.equals(originRecordId, targetRecordId)) return this.pageService.getPageInfo(pageId, tx);
        return this.memoryRecordDao.findRecordById(targetRecordId, tx)
                .flatMap(targetRecord -> {
                    AutoIncreaseArray<String> pageSequence = targetRecord.getPageSequence();
                    if(Objects.isNull(pageSequence)) {
                        pageSequence = new AutoIncreaseArray<>();
                        targetRecord.setPageSequence(pageSequence);
                    }
                    if(Objects.equals(moveType, "head")) pageSequence.addHead(pageId);
                    else if(Objects.equals(moveType, "tail")) pageSequence.addTail(pageId);
                    return this.memoryRecordDao.saveOrUpdateOpt(targetRecord, tx)
                            .then();
                })
                .then(this.memoryRecordDao.findRecordById(originRecordId, tx))
                .<PageInfo>flatMap(originRecord -> {
                    final int studyNumber = originRecord.getStudyNumber();
                    int newStudyNumber = Math.max(0, studyNumber - 1);
                    originRecord.setStudyNumber(newStudyNumber);

                    final AutoIncreaseArray<String> pageSequence = originRecord.getPageSequence();
                    if(CollectionOperation.isEmptyOrNull(pageSequence)) {
                        return this.memoryRecordDao.saveOrUpdateOpt(originRecord, tx)
                                .then(Mono.empty());
                    }
                    pageSequence.remove(pageId);

                    if(newStudyNumber == 0) {
                        return this.memoryRecordDao.saveOrUpdateOpt(originRecord, tx)
                                .then(Mono.empty());
                    }
                    if(!pageSequence.isEmpty()) {
                        final String newPageId = pageSequence.get(0);
                        return this.memoryRecordDao.saveOrUpdateOpt(originRecord, tx)
                                .then(this.pageService.getPageInfo(newPageId, tx));
                    }
                    else return this.memoryRecordDao.saveOrUpdateOpt(originRecord, tx)
                            .then(Mono.empty());
                });
    }

    @Neo4jTransaction
    public Mono<MemoryRecordInfo> movePage(final String recordId, String pageId, int destination, final CompletionStage<AsyncTransaction> tx) {
        if(destination < 0) throw new IndexOutOfBoundsException("destination less than 0");
        return this.memoryRecordDao.findRecordById(recordId, tx)
                .flatMap(record -> {
                    final AutoIncreaseArray<String> pageSequence = record.getPageSequence();
                    final int index = pageSequence.indexOf(pageId);
                    pageSequence.move(index, destination);
                    return this.memoryRecordDao.saveOrUpdateOpt(record, tx);
                })
                .map(record -> EntityOperation.copyProperties(record, new MemoryRecordInfo()));
    }
}
