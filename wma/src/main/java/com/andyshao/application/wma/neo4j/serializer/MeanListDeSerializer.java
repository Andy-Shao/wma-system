package com.andyshao.application.wma.neo4j.serializer;

import com.andyshao.application.wma.neo4j.domain.Means;
import com.andyshao.application.wma.neo4j.domain.WordType;
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
public class MeanListDeSerializer implements Deserializer<List<Means>> {
    @Override
    public List<Means> decode(Value value) throws NotSupportConvertException {
        if(Objects.equals(Values.NULL, value)) return null;
        final String str = value.asString();
        return Arrays.stream(str.split(MeanListSerializer.ITEM_SPLIT))
                .map(mean -> {
                    final String[] splits = mean.split(MeanListSerializer.ATTRIBUTE_SPLIT);
                    final Means means = new Means();
                    means.setInterpretation(splits[0]);
                    means.setType(WordType.valueOf(splits[1]));
                    return means;
                })
                .collect(Collectors.toList());
    }
}
