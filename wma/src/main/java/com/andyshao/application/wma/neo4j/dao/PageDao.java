package com.andyshao.application.wma.neo4j.dao;

import com.andyshao.application.wma.neo4j.domain.Group;
import com.andyshao.application.wma.neo4j.domain.Page;
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
@Neo4jDao(eneity = Page.class, pk = String.class)
public interface PageDao {
    @Neo4jSql(sql = "MATCH (n:Page) RETURN n")
    Flux<Page> pages(CompletionStage<AsyncTransaction> tx);

    @Neo4jSql(sql = "MATCH (n:Page {uuid: $id}) RETURN n")
    Mono<Page> findByPk(@Param("id")String uuid, CompletionStage<AsyncTransaction> tx);

    @Neo4jSql(sql = "MERGE (n:Page {uuid: $p_uuid}) RETURN n")
    Mono<Page> saveOrUpdate(@Param("p")Page page, CompletionStage<AsyncTransaction> tx);

    @Neo4jSql(sql = "MATCH (n:Page {uuid: $p_uuid}), (g:Group {uuid: $g_uuid}) MERGE (n) -[:Own]-> (g) RETURN n")
    Mono<Page> addGroup(@Param("p") Page page, @Param("g")Group group, CompletionStage<AsyncTransaction> tx);

    @Neo4jSql(sql = "MATCH (n:Page {uuid: $p_uuid}) -[:Own]-> (g:Group) RETURN g")
    Flux<Group> findGroups(@Param("p") Page page, CompletionStage<AsyncTransaction> tx);

    @Neo4jSql(sql = "MATCH (n:Page {uuid: $p_uuid}) -[r:Own]-> (g:Group {uuid: $g_uuid}) DELETE r")
    Mono<Void> removeGroup(@Param("p") Page page, @Param("g")Group group, CompletionStage<AsyncTransaction> tx);
}
