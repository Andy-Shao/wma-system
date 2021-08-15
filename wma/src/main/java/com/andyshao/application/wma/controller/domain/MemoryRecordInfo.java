package com.andyshao.application.wma.controller.domain;

import com.github.andyshao.lang.AutoIncreaseArray;
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
public class MemoryRecordInfo {
    private String uuid;
    private String description;
    private String currentPageId;
    private AutoIncreaseArray<String> pageSequence;
}
