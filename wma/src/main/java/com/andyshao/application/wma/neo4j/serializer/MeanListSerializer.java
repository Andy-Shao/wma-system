package com.andyshao.application.wma.neo4j.serializer;

import com.andyshao.application.wma.neo4j.domain.Means;
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
public class MeanListSerializer implements Serializer<List<Means>> {
    public static final String ITEM_SPLIT = ";";
    public static final String ATTRIBUTE_SPLIT = ",";

    @Override
    public Value encode(List<Means> data) throws NotSupportConvertException {
        if(Objects.isNull(data)) return Values.NULL;
        final String value = data.stream()
                .map(means -> {
                    return means.getInterpretation() + ATTRIBUTE_SPLIT + means.getType().name() + ITEM_SPLIT;
                })
                .collect(Collectors.joining());
        return Values.value(value);
    }
}
