package com.andyshao.application.wma.neo4j.dao;

import com.andyshao.application.wma.neo4j.domain.Group;
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
 * Copyright: Copyright(c) 2021/7/27
 * Encoding: UNIX UTF-8
 *
 * @author Andy.Shao
 */
@Neo4jDao(eneity = Group.class, pk = String.class)
public interface GroupDao {
    @Neo4jSql(sql = "MERGE (n:Group {uuid: $g_uuid}) RETURN n")
    Mono<Group> saveOrUpdate(@Param("g")Group group, CompletionStage<AsyncTransaction> tx);

    @Neo4jSql(sql = "MATCH (n:Group {uuid: $g_uuid}), (m:Material {uuid: $m_uuid}) MERGE (n) -[:Include]-> (m) RETURN n")
    Mono<Group> addMaterial(@Param("g")Group group, @Param("m")Material material, CompletionStage<AsyncTransaction> tx);

    @Neo4jSql(sql = "MATCH (n:Group {uuid: $g_uuid}) -[:Include]-> (m:Material) RETURN m")
    Flux<Material> findMaterials(@Param("g")Group group, CompletionStage<AsyncTransaction> tx);

    @Neo4jSql(sql = "MATCH (n:Group {uuid: $g_uuid}) -[r:Include]-> (m:Material {uuid: $m_uuid}) DELETE r")
    Mono<Void> removeMaterial(@Param("g")Group group, @Param("m")Material material, CompletionStage<AsyncTransaction> tx);
}
