package com.andyshao.application.wma.neo4j.dao;

import com.andyshao.application.wma.IntegrationTest;
import com.andyshao.application.wma.neo4j.domain.MemoryRecord;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.neo4j.driver.Driver;
import org.neo4j.driver.async.AsyncSession;
import org.neo4j.driver.async.AsyncTransaction;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.concurrent.CompletionStage;

class MemoryRecordDaoTest extends IntegrationTest {
    @Autowired
    private MemoryRecordDao memoryRecordDao;
    @Autowired
    private Driver neo4jDriver;

    @Test
    void findRecords() {
        final AsyncSession asyncSession = this.neo4jDriver.asyncSession();
        final CompletionStage<AsyncTransaction> transaction = asyncSession.beginTransactionAsync();
        final Flux<MemoryRecord> records = this.memoryRecordDao.findRecords(transaction);

        StepVerifier.create(records.collectList())
                .assertNext(ls -> {
                    Assertions.assertThat(ls).isNotNull();
                    Assertions.assertThat(ls).isEmpty();
                })
                .verifyComplete();
    }
}