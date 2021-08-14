package com.andyshao.application.wma.neo4j.dao;

import com.andyshao.application.wma.neo4j.dao.impl.MemoryRecordDaoImpl;
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
@Neo4jDao(eneity = MemoryRecord.class, pk = String.class, clipClass = MemoryRecordDaoImpl.class)
public interface MemoryRecordDao {
    @Neo4jSql(sql = "MATCH (n:MemoryRecord) RETURN n")
    Flux<MemoryRecord> findRecords(CompletionStage<AsyncTransaction> tx);

    @Neo4jSql(sql = "MATCH (n:MemoryRecord {uuid: $id}) RETURN n")
    Mono<MemoryRecord> findRecordById(@Param("id") String uuid, CompletionStage<AsyncTransaction> tx);

    @Neo4jSql(isUseSqlClip = true, sqlClipName = "saveOrUpdateOpt")
    Mono<MemoryRecord> saveOrUpdateOpt(@Param("mr")MemoryRecord memoryRecord, CompletionStage<AsyncTransaction> tx);

    @Neo4jSql(sql = "MATCH (n:MemoryRecord {uuid: $id}) DELETE n")
    Mono<Void> removeMemoryRecord(@Param("id")String uuid, CompletionStage<AsyncTransaction> tx);
}
