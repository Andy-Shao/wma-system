package com.andyshao.application.wma.neo4j.serializer;

import com.andyshao.application.wma.neo4j.domain.Mean;
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
public class MeanListSerializer implements Serializer<List<Mean>> {
    public static final String ITEM_SPLIT = ";";
    public static final String ATTRIBUTE_SPLIT = ",";

    @Override
    public Value encode(List<Mean> data) throws NotSupportConvertException {
        if(Objects.isNull(data)) return Values.NULL;
        final List<String> value = data.stream()
                .map(mean -> {
                    return mean.getInterpretation() + ATTRIBUTE_SPLIT + mean.getType().name() + ITEM_SPLIT;
                })
                .collect(Collectors.toList());
        return Values.value(value);
    }
}
