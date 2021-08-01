package com.andyshao.application.wma.neo4j.serializer;

import com.github.andyshao.lang.NotSupportConvertException;
import com.github.andyshao.neo4j.process.serializer.Serializer;
import org.neo4j.driver.Value;
import org.neo4j.driver.Values;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Title: <br>
 * Description: <br>
 * Copyright: Copyright(c) 2021/7/27
 * Encoding: UNIX UTF-8
 *
 * @author Andy.Shao
 */
public class WordListSerializer implements Serializer<List<String>> {
    public static final String SPLIT = ",";

    @Override
    public Value encode(List<String> data) throws NotSupportConvertException {
        if(Objects.isNull(data)) return Values.NULL;
        final String str = data.stream()
                .map(it -> it + SPLIT)
                .collect(Collectors.joining());
        return Values.value(str);
    }
}
