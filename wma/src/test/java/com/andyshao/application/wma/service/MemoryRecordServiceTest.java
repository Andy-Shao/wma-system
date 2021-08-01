package com.andyshao.application.wma.service;

import com.andyshao.application.wma.IntegrationTest;
import com.andyshao.application.wma.domain.MemoryRecordInfo;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

class MemoryRecordServiceTest extends IntegrationTest {
    @Autowired
    private MemoryRecordService service;

    @Test
    void findMemoryRecords() {
        final Flux<MemoryRecordInfo> memoryRecords = this.service.findMemoryRecords(null);
        StepVerifier.create(memoryRecords.collectList())
                .assertNext(ls -> {
                    Assertions.assertThat(ls).isNotNull();
                    Assertions.assertThat(ls).isEmpty();
                })
                .verifyComplete();
    }
}