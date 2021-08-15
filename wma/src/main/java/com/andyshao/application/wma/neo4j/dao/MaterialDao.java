package com.andyshao.application.wma.neo4j.dao;

import com.andyshao.application.wma.neo4j.domain.Material;
import com.github.andyshao.neo4j.annotation.Neo4jDao;
import com.github.andyshao.neo4j.annotation.Neo4jSql;
import com.github.andyshao.reflect.annotation.Param;
import org.neo4j.driver.async.AsyncTransaction;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletionStage;

/**
 * Title: <br>
 * Description: <br>
 * Copyright: Copyright(c) 2021/7/28
 * Encoding: UNIX UTF-8
 *
 * @author Andy.Shao
 */
@Neo4jDao(eneity = Material.class, pk = String.class)
public interface MaterialDao {
    @Neo4jSql(sql = "MATCH (n:Material {uuid: $id}) RETURN n")
    Mono<Material> findByPk(@Param("id")String uuid, CompletionStage<AsyncTransaction> tx);

    @Neo4jSql(sql = "MERGE (n:Material {uuid: $m_uuid}) " +
            "ON CREATE SET n.wordList = $m_wordList, n.meansList = $m_meansList, n.wordKey = $m_wordKey " +
            "ON MATCH SET n.wordList = $m_wordList, n.meansList = $m_meansList, n.wordKey = $m_wordKey " +
            "RETURN n")
    Mono<Material> saveOrUpdate(@Param("m")Material material, CompletionStage<AsyncTransaction> tx);

    @Neo4jSql(sql = "MATCH (n:Material {uuid: $m_uuid}) DELETE n")
    Mono<Void> removeMaterial(@Param("m")Material material, CompletionStage<AsyncTransaction> tx);

    @Neo4jSql(sql = "MATCH (n:Material) WHERE n.wordKey CONTAINS $w RETURN n")
    Flux<Material> matchByWord(@Param("w")String word, CompletionStage<AsyncTransaction> tx);
}
