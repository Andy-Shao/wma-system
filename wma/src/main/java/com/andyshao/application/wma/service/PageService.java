package com.andyshao.application.wma.service;

import com.andyshao.application.wma.controller.vo.PageSearchParams;
import com.andyshao.application.wma.controller.domain.GroupInfo;
import com.andyshao.application.wma.controller.domain.MaterialInfo;
import com.andyshao.application.wma.controller.domain.PageInfo;
import com.andyshao.application.wma.neo4j.dao.GroupDao;
import com.andyshao.application.wma.neo4j.dao.MaterialDao;
import com.andyshao.application.wma.neo4j.dao.MemoryRecordDao;
import com.andyshao.application.wma.neo4j.dao.PageDao;
import com.andyshao.application.wma.neo4j.domain.Group;
import com.andyshao.application.wma.neo4j.domain.Material;
import com.andyshao.application.wma.neo4j.domain.Page;
import com.github.andyshao.lang.StringOperation;
import com.github.andyshao.neo4j.annotation.Neo4jTransaction;
import com.github.andyshao.util.CollectionOperation;
import com.github.andyshao.util.EntityOperation;
import org.neo4j.driver.async.AsyncTransaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.UUID;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

/**
 * Title: <br>
 * Description: <br>
 * Copyright: Copyright(c) 2021/7/27
 * Encoding: UNIX UTF-8
 *
 * @author Andy.Shao
 */
@Service
public class PageService {
    @Autowired
    private MemoryRecordDao memoryRecordDao;
    @Autowired
    private PageDao pageDao;
    @Autowired
    private GroupDao groupDao;
    @Autowired
    private MaterialDao materialDao;

    @Neo4jTransaction
    public Flux<PageInfo> getPages(CompletionStage<AsyncTransaction> tx) {
        return this.pageDao.pages(tx)
                .map(it -> EntityOperation.copyProperties(it, new PageInfo()));
    }

    @Neo4jTransaction
    public Mono<PageInfo> getPageInfo(String pageId, final CompletionStage<AsyncTransaction> tx) {
        if(StringOperation.isTrimEmptyOrNull(pageId)) throw new IllegalArgumentException("pageId cannot be null or empty");
        return this.pageDao.findByPk(pageId, tx)
                .flatMap(page -> {
                    return this.pageDao.findGroups(pageId, tx)
                            .flatMap(group -> {
                                return this.groupDao.findMaterials(group.getUuid(), tx)
                                        .map(material -> {
                                            final MaterialInfo materialInfo = new MaterialInfo();
                                            EntityOperation.copyProperties(material, materialInfo);
                                            return materialInfo;
                                        })
                                        .collect(Collectors.toList())
                                        .map(ls -> {
                                            final GroupInfo groupInfo = new GroupInfo();
                                            EntityOperation.copyProperties(group, groupInfo);
                                            groupInfo.setMaterials(ls);
                                            return groupInfo;
                                        });
                            })
                            .collect(Collectors.toList())
                            .map(ls -> {
                                final PageInfo pageInfo = new PageInfo();
                                EntityOperation.copyProperties(page, pageInfo);
                                pageInfo.setGroups(ls);
                                return pageInfo;
                            });
                });
    }

    @Neo4jTransaction
    public Mono<Void> savePageInfo(PageInfo p, final CompletionStage<AsyncTransaction> tx) {
        if(StringOperation.isTrimEmptyOrNull(p.getUuid())) p.setUuid(UUID.randomUUID().toString());
        final Page page = new Page();
        EntityOperation.copyProperties(p, page);
        return this.pageDao.findOrCreateById(page.getUuid(), tx)
                .thenMany(Flux.fromIterable(CollectionOperation.isEmptyOrNull(p.getGroups()) ? Collections.emptyList(): p.getGroups()))
                .flatMap(g -> {
                    if(StringOperation.isTrimEmptyOrNull(g.getUuid())) g.setUuid(UUID.randomUUID().toString());
                    final Group group = new Group();
                    EntityOperation.copyProperties(g, group);
                    return this.groupDao.findOrCreateById(group.getUuid(), tx)
                            .thenMany(Flux.fromIterable(g.getMaterials()))
                            .flatMap(m -> {
                                if(StringOperation.isTrimEmptyOrNull(m.getUuid())) m.setUuid(UUID.randomUUID().toString());
                                final Material material = new Material();
                                EntityOperation.copyProperties(m, material);
                                return this.materialDao.saveOrUpdate(material, tx)
                                        .flatMap(it -> this.groupDao.addMaterial(group.getUuid(), it.getUuid(), tx));
                            })
                            .then(Mono.just(group));
                })
                .flatMap(group -> {
                    return this.pageDao.addGroup(page.getUuid(), group.getUuid(), tx);
                })
                .then();
    }

    @Neo4jTransaction
    public Flux<PageInfo> getPageByIds(PageSearchParams params, final CompletionStage<AsyncTransaction> tx) {
        if(CollectionOperation.isEmptyOrNull(params.getPageIds()))  return Flux.empty();
        return this.pageDao.findPageByIds(params.getPageIds(), tx)
                .map(it -> EntityOperation.copyProperties(it, new PageInfo()));
    }

    @Neo4jTransaction
    public Mono<Void> removePage(String uuid, CompletionStage<AsyncTransaction> tx) {
        return this.pageDao.removePageById(uuid, tx);
    }

    @Neo4jTransaction
    public Mono<PageInfo> addGroup(final String pageId, String groupId, final CompletionStage<AsyncTransaction> tx) {
        final AtomicReference<String> groupKey = new AtomicReference<>();
        if(StringOperation.isTrimEmptyOrNull(groupId)) groupKey.set(UUID.randomUUID().toString());
        else groupKey.set(groupId);
        return this.pageDao.findByPk(pageId, tx)
                .flatMap(page -> {
                    final PageInfo pageInfo = EntityOperation.copyProperties(page, new PageInfo());
                    return this.groupDao.findOrCreateById(groupKey.get(), tx)
                            .then(this.pageDao.addGroup(pageId, groupKey.get(), tx))
                            .thenMany(this.pageDao.findGroups(pageId, tx))
                            .map(group -> EntityOperation.copyProperties(group, new GroupInfo()))
                            .collectList()
                            .map(groups -> {
                                pageInfo.setGroups(groups);
                                return pageInfo;
                            });
                });
    }

    @Neo4jTransaction
    public Mono<Void> removeGroup(String pageId, String groupId, final CompletionStage<AsyncTransaction> tx) {
        return this.pageDao.removeGroup(pageId, groupId, tx)
                .then(this.groupDao.removeGroupById(groupId, tx))
                .then();
    }
}
