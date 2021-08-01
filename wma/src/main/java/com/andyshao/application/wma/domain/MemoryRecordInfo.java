package com.andyshao.application.wma.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;

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
    private Set<String> pageSequence;
}
