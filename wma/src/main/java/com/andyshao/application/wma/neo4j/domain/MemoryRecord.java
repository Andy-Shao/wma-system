package com.andyshao.application.wma.neo4j.domain;

import com.andyshao.application.wma.neo4j.serializer.WordListDeSerializer;
import com.andyshao.application.wma.neo4j.serializer.WordListSerializer;
import com.github.andyshao.lang.AutoIncreaseArray;
import com.github.andyshao.neo4j.annotation.Deserializer;
import com.github.andyshao.neo4j.annotation.Neo4jEntity;
import com.github.andyshao.neo4j.annotation.Serializer;
import lombok.Getter;
import lombok.Setter;

/**
 * Title: <br>
 * Description: <br>
 * Copyright: Copyright(c) 2021/7/27
 * Encoding: UNIX UTF-8
 *
 * @author Andy.Shao
 */
@Getter
@Setter
@Neo4jEntity
public class MemoryRecord {
    private String uuid;
    private String description;
    private String currentPageId;
    @Serializer(WordListSerializer.class)
    @Deserializer(WordListDeSerializer.class)
    private AutoIncreaseArray<String> pageSequence;
}
