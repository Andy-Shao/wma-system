package com.andyshao.application.wma.neo4j.serializer;

import com.andyshao.application.wma.neo4j.domain.Mean;
import com.andyshao.application.wma.neo4j.domain.WordType;
import com.github.andyshao.lang.NotSupportConvertException;
import com.github.andyshao.lang.StringOperation;
import com.github.andyshao.neo4j.process.serializer.Deserializer;
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
public class MeanListDeSerializer implements Deserializer<List<Mean>> {
    @Override
    public List<Mean> decode(Value value) throws NotSupportConvertException {
        if(Objects.equals(Values.NULL, value)) return null;
        final List<Object> values = value.asList();
        return values.stream()
                .map(it -> {
                    String meanStr = it.toString();
                    meanStr = StringOperation.replaceLast(meanStr, MeanListSerializer.ITEM_SPLIT, "");
                    final String[] splits = meanStr.split(MeanListSerializer.ATTRIBUTE_SPLIT);
                    final Mean mean = new Mean();
                    mean.setInterpretation(splits[0]);
                    mean.setType(WordType.valueOf(splits[1]));
                    return mean;
                })
                .collect(Collectors.toList());
    }
}
