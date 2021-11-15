package com.andyshao.application.wma.utils;

import com.andyshao.application.wma.IntegrationTest;
import com.andyshao.application.wma.service.MemoryRecordService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Title: <br>
 * Description: <br>
 * Copyright: Copyright(c) 2021/8/31
 * Encoding: UNIX UTF-8
 *
 * @author Andy.Shao
 */
public class MoveNode extends IntegrationTest {
    @Autowired
    private MemoryRecordService memoryRecordService;

    @Test
    public void movePage() {
        this.memoryRecordService.movePage("88023deb-1734-40b6-87af-2b3ba60227df", "154eefc8-bb5d-4c68-ab5c-c7a8ace0603a", 0, null)
                .block();
    }
}
