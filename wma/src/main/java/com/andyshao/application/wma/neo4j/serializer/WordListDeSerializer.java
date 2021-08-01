package com.andyshao.application.wma.neo4j.serializer;

import com.github.andyshao.lang.NotSupportConvertException;
import com.github.andyshao.neo4j.process.serializer.Deserializer;
import org.neo4j.driver.Value;
import org.neo4j.driver.Values;

import java.util.Arrays;
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
public class WordListDeSerializer implements Deserializer<List<String>> {
    @Override
    public List<String> decode(Value value) throws NotSupportConvertException {
        if(Objects.equals(Values.NULL, value)) return null;
        final String str = value.asString();
        return Arrays.stream(str.split(WordListSerializer.SPLIT))
                .collect(Collectors.toList());
    }
}
