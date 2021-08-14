package com.andyshao.application.wma.neo4j.dao.impl;

import com.andyshao.application.wma.neo4j.domain.MemoryRecord;
import com.github.andyshao.lang.AutoIncreaseArray;
import com.github.andyshao.neo4j.annotation.Neo4jSqlClip;
import com.github.andyshao.neo4j.process.sql.Sql;
import com.github.andyshao.reflect.annotation.Param;
import com.google.common.collect.Maps;
import org.neo4j.driver.Value;
import org.neo4j.driver.Values;
import org.neo4j.driver.internal.value.NullValue;

import java.util.Map;
import java.util.Objects;

/**
 * Title: <br>
 * Description: <br>
 * Copyright: Copyright(c) 2021/8/14
 * Encoding: UNIX UTF-8
 *
 * @author Andy.Shao
 */
public final class MemoryRecordDaoImpl {

    @Neo4jSqlClip
    public static Sql saveOrUpdateOpt(@Param("mr") MemoryRecord memoryRecord) {
        final Sql sql = new Sql();
        sql.setParameters(Maps.newHashMap());
        final Map<String, Value> parameters = sql.getParameters();
        final StringBuilder sqlString = new StringBuilder();
        sqlString.append("MERGE (n:MemoryRecord {uuid: $mr_uuid}) ");
        sqlString.append("ON CREATE SET n.currentPageId = $mr_currentPageId, n.pageSequence = $mr_pageSequence, n.description = $mr_description ");
        final String currentPageId = memoryRecord.getCurrentPageId();
        final String description = memoryRecord.getDescription();
        final AutoIncreaseArray<String> pageSequence = memoryRecord.getPageSequence();
        { // ON MATCH SET
            if(Objects.nonNull(description) || Objects.nonNull(pageSequence) || Objects.nonNull(currentPageId)) {
                sqlString.append("ON MATCH SET ");
                boolean hasHeader = false;
                if(Objects.nonNull(description)) {
                    hasHeader = true;
                    sqlString.append("n.currentPageId = $mr_currentPageId ");
                }
                if(Objects.nonNull(pageSequence)) {
                    if(hasHeader) sqlString.append(", ");
                    else hasHeader = true;
                    sqlString.append("n.pageSequence = $mr_pageSequence ");
                }
                if(Objects.nonNull(currentPageId)) {
                    if(hasHeader) sqlString.append(", ");
                    else hasHeader = true;
                    sqlString.append("n.description = $mr_description ");
                }
            }
        }
        sqlString.append("RETURN n");
        parameters.put("mr_uuid", Values.value(memoryRecord.getUuid()));
        parameters.put("mr_currentPageId", Objects.isNull(currentPageId) ? NullValue.NULL: Values.value(currentPageId));
        parameters.put("mr_pageSequence", Values.value(pageSequence));
        parameters.put("mr_description", Objects.isNull(description)? NullValue.NULL: Values.value(description));
        sql.setSql(sqlString.toString());
        return sql;
    }
}
