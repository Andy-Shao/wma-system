package com.andyshao.application.wma.service;

import com.andyshao.application.wma.controller.domain.GroupInfo;
import com.andyshao.application.wma.controller.domain.MaterialInfo;
import com.andyshao.application.wma.neo4j.dao.GroupDao;
import com.andyshao.application.wma.neo4j.dao.MaterialDao;
import com.github.andyshao.neo4j.annotation.Neo4jTransaction;
import com.github.andyshao.util.CollectionOperation;
import com.github.andyshao.util.EntityOperation;
import com.google.common.collect.Lists;
import org.neo4j.driver.async.AsyncTransaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletionStage;

/**
 * Title: <br>
 * Description: <br>
 * Copyright: Copyright(c) 2021/8/16
 * Encoding: UNIX UTF-8
 *
 * @author Andy.Shao
 */
@Service
public class GroupService {
    @Autowired
    private GroupDao groupDao;
    @Autowired
    private MaterialDao materialDao;

    @Neo4jTransaction
    public Mono<GroupInfo> findById(final String uuid, final CompletionStage<AsyncTransaction> tx) {
        return this.groupDao.findOrCreateById(uuid, tx)
                .flatMap(group -> {
                    final GroupInfo groupInfo = EntityOperation.copyProperties(group, new GroupInfo());
                    if(CollectionOperation.isEmptyOrNull(groupInfo.getMaterials())) groupInfo.setMaterials(Lists.newArrayList());
                    return this.groupDao.findMaterials(uuid, tx)
                            .doOnNext(material -> {
                                final MaterialInfo materialInfo = EntityOperation.copyProperties(material, new MaterialInfo());
                                groupInfo.getMaterials().add(materialInfo);
                            })
                            .then(Mono.just(groupInfo));
                });
    }
}
