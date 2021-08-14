package com.andyshao.application.wma.neo4j.dao;

import com.andyshao.application.wma.neo4j.domain.Group;
import com.andyshao.application.wma.neo4j.domain.Page;
import com.github.andyshao.neo4j.annotation.Neo4jDao;
import com.github.andyshao.neo4j.annotation.Neo4jSql;
import com.github.andyshao.reflect.annotation.Param;
import org.neo4j.driver.async.AsyncTransaction;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.concurrent.CompletionStage;

/**
 * Title: <br>
 * Description: <br>
 * Copyright: Copyright(c) 2021/7/27
 * Encoding: UNIX UTF-8
 *
 * @author Andy.Shao
 */
@Neo4jDao(eneity = Page.class, pk = String.class)
public interface PageDao {
    @Neo4jSql(sql = "MATCH (n:Page) RETURN n")
    Flux<Page> pages(CompletionStage<AsyncTransaction> tx);

    @Neo4jSql(sql = "MATCH (n:Page) WHERE n.uuid IN $ls RETURN n")
    Flux<Page> findPageByIds(@Param("ls")List<String> ids, CompletionStage<AsyncTransaction> tx);

    @Neo4jSql(sql = "MATCH (n:Page {uuid: $id}) RETURN n")
    Mono<Page> findByPk(@Param("id")String uuid, CompletionStage<AsyncTransaction> tx);

    @Neo4jSql(sql = "MERGE (n:Page {uuid: $id}) RETURN n")
    Mono<Page> findOrCreateById(@Param("id")String uuid, CompletionStage<AsyncTransaction> tx);

    @Neo4jSql(sql = "MATCH (n:Page {uuid: $pId}), (g:Group {uuid: $gId}) MERGE (n) -[:Own]-> (g) RETURN n")
    Mono<Page> addGroup(@Param("pId") String pageId, @Param("gId")String groupId, CompletionStage<AsyncTransaction> tx);

    @Neo4jSql(sql = "MATCH (n:Page {uuid: $pId}) -[:Own]-> (g:Group) RETURN g")
    Flux<Group> findGroups(@Param("pId") String pageId, CompletionStage<AsyncTransaction> tx);

    @Neo4jSql(sql = "MATCH (n:Page {uuid: $pId}) -[r:Own]-> (g:Group {uuid: $gId}) DELETE r")
    Mono<Void> removeGroup(@Param("pId") String pageId, @Param("gId")String groupId, CompletionStage<AsyncTransaction> tx);

    @Neo4jSql(sql = "MATCH (n:Page {uuid: $id}) DELETE n")
    Mono<Void> removePageById(@Param("id") String uuid, CompletionStage<AsyncTransaction> tx);
}
