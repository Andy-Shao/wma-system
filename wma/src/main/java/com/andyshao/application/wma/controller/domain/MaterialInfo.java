package com.andyshao.application.wma.controller.domain;

import com.andyshao.application.wma.controller.validation.annotation.NoEmptyItemList;
import com.andyshao.application.wma.neo4j.domain.Mean;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Title: <br>
 * Description: <br>
 * Copyright: Copyright(c) 2021/7/28
 * Encoding: UNIX UTF-8
 *
 * @author Andy.Shao
 */
@Getter
@Setter
public class MaterialInfo {
    private String uuid;
    @NoEmptyItemList(message = "wordList contains empty item")
    @NotEmpty(message = "wordList is required")
    private List<String> wordList;
    private List<Mean> meansList;
}
