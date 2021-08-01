package com.andyshao.application.wma.domain;

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
public class GroupInfo {
    private String uuid;
    private List<MaterialInfo> materials;
}
