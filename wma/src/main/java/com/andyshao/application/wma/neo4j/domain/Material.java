package com.andyshao.application.wma.neo4j.domain;

import com.andyshao.application.wma.neo4j.serializer.MeanListDeSerializer;
import com.andyshao.application.wma.neo4j.serializer.MeanListSerializer;
import com.github.andyshao.neo4j.annotation.Deserializer;
import com.github.andyshao.neo4j.annotation.Neo4jEntity;
import com.github.andyshao.neo4j.annotation.Serializer;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

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
public class Material {
    private String uuid;
    private List<String> wordList;
    @Serializer(MeanListSerializer.class)
    @Deserializer(MeanListDeSerializer.class)
    private List<Mean> meansList;

    public String getWordKey() {
        return this.getWordList().stream()
                .map(it -> it + ',')
                .collect(Collectors.joining());
    }
}
