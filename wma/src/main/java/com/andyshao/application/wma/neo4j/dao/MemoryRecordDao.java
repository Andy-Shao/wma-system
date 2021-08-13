package com.andyshao.application.wma.neo4j.dao;

import com.andyshao.application.wma.neo4j.domain.MemoryRecord;
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
@Neo4jDao(eneity = MemoryRecord.class, pk = String.class)
public interface MemoryRecordDao {
    @Neo4jSql(sql = "MATCH (n:MemoryRecord) RETURN n")
    Flux<MemoryRecord> findRecords(CompletionStage<AsyncTransaction> tx);

    @Neo4jSql(sql = "MATCH (n:MemoryRecord {uuid: $id}) RETURN n")
    Mono<MemoryRecord> findRecordById(@Param("id") String uuid, CompletionStage<AsyncTransaction> tx);

    @Neo4jSql(sql = "MERGE (n:MemoryRecord {uuid: $mr_uuid}) " +
            "ON CREATE SET n.currentPageId = $mr_currentPageId, n.pageSequence = $mr_pageSequence, n.description = $mr_description " +
            "ON MATCH SET n.currentPageId = $mr_currentPageId, n.pageSequence = $mr_pageSequence, n.description = $mr_description " +
            "RETURN n")
    Mono<MemoryRecord> saveOrUpdate(@Param("mr")MemoryRecord memoryRecord, CompletionStage<AsyncTransaction> tx);

    @Neo4jSql(sql = "MATCH (n:MemoryRecord {uuid: $id}) DELETE n")
    Mono<Void> removeMemoryRecord(@Param("id")String uuid, CompletionStage<AsyncTransaction> tx);
}
