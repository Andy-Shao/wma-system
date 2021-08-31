package com.andyshao.application.wma.utils;

import com.andyshao.application.wma.IntegrationTest;
import com.andyshao.application.wma.controller.domain.MemoryRecordInfo;
import com.andyshao.application.wma.controller.domain.PageInfo;
import com.andyshao.application.wma.service.MemoryRecordService;
import com.andyshao.application.wma.service.PageService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Title: <br>
 * Description: <br>
 * Copyright: Copyright(c) 2021/8/31
 * Encoding: UNIX UTF-8
 *
 * @author Andy.Shao
 */
public class FindingEmptyNode extends IntegrationTest {
    @Autowired
    private MemoryRecordService memoryRecordService;
    @Autowired
    private PageService pageService;

    @Test
    public void findEmptyNodes() {
        final List<String> pages = this.pageService.getPages(null)
                .map(PageInfo::getUuid)
                .collectList()
                .block();
        final List<MemoryRecordInfo> records = this.memoryRecordService.findMemoryRecords(null)
                .collectList()
                .block();
        records.forEach(record -> {
            record.getPageSequence()
                    .forEach(pageId -> {
                        pages.remove(pageId);
                    });
        });
        System.out.println(pages);
    }

    @Test
    public void addEmptyNodes() {
        this.memoryRecordService.addPage("88023deb-1734-40b6-87af-2b3ba60227df", "2ab7593e-d3de-4184-9cc4-1dcbe6c902e3", null)
                .block();
    }
}
