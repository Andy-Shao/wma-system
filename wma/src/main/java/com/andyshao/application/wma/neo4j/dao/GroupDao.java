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
    @Neo4jSql(sql = "MERGE (n:Group {uuid: $id}) RETURN n")
    Mono<Group> findOrCreateById(@Param("id")String uuid, CompletionStage<AsyncTransaction> tx);

    @Neo4jSql(sql = "MATCH (n:Group {uuid: $gId}), (m:Material {uuid: $mId}) MERGE (n) -[:Include]-> (m) RETURN n")
    Mono<Group> addMaterial(@Param("gId")String groupId, @Param("mId")String materialId, CompletionStage<AsyncTransaction> tx);

    @Neo4jSql(sql = "MATCH (n:Group {uuid: $gId}) -[:Include]-> (m:Material) RETURN m")
    Flux<Material> findMaterials(@Param("gId")String groupId, CompletionStage<AsyncTransaction> tx);

    @Neo4jSql(sql = "MATCH (n:Group {uuid: $gId}) -[r:Include]-> (m:Material {uuid: $mId}) DELETE r")
    Mono<Void> removeMaterial(@Param("gId")String groupId, @Param("mId")String materialId, CompletionStage<AsyncTransaction> tx);

    @Neo4jSql(sql = "MATCH (n:Group {uuid: $gId}) DELETE n")
    Mono<Void> removeGroupById(@Param("gId")String groupId, CompletionStage<AsyncTransaction> tx);
}
