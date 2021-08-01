package com.andyshao.application.wma.neo4j.domain;

import com.github.andyshao.neo4j.annotation.Neo4jEntity;
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
public class Group {
    public static final String MATERIAL_RELATIONSHIP = "Include";
    private String uuid;
}
