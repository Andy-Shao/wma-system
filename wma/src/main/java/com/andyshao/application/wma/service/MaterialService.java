package com.andyshao.application.wma.service;

import com.andyshao.application.wma.controller.domain.MaterialInfo;
import com.andyshao.application.wma.neo4j.dao.MaterialDao;
import com.andyshao.application.wma.neo4j.domain.Material;
import com.github.andyshao.lang.StringOperation;
import com.github.andyshao.neo4j.annotation.Neo4jTransaction;
import com.github.andyshao.util.EntityOperation;
import org.neo4j.driver.async.AsyncTransaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;
import java.util.concurrent.CompletionStage;

/**
 * Title: <br>
 * Description: <br>
 * Copyright: Copyright(c) 2021/8/15
 * Encoding: UNIX UTF-8
 *
 * @author Andy.Shao
 */
@Service
public class MaterialService {
    @Autowired
    private MaterialDao materialDao;

    @Neo4jTransaction
    public Mono<MaterialInfo> saveOrUpdate(MaterialInfo materialInfo, CompletionStage<AsyncTransaction> tx) {
        if(StringOperation.isTrimEmptyOrNull(materialInfo.getUuid())) materialInfo.setUuid(UUID.randomUUID().toString());
        return this.materialDao.saveOrUpdate(EntityOperation.copyProperties(materialInfo, new Material()), tx)
                .map(it -> EntityOperation.copyProperties(it, new MaterialInfo()));
    }

    @Neo4jTransaction
    public Flux<MaterialInfo> matchMaterialByWord(String word, CompletionStage<AsyncTransaction> tx) {
        return this.materialDao.matchByWord(word, tx)
                .map(it -> EntityOperation.copyProperties(it, new MaterialInfo()));
    }

    @Neo4jTransaction
    public Mono<MaterialInfo> findById(String uuid, CompletionStage<AsyncTransaction> tx) {
        return this.materialDao.findByPk(uuid, tx)
                .map(it -> EntityOperation.copyProperties(it, new MaterialInfo()));
    }
}
