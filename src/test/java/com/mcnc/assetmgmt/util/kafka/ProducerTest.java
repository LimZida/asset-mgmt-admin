package com.mcnc.assetmgmt.util.kafka;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.TimeUnit;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.Matchers.containsString;

@SpringBootTest
class ProducerTest {
    @Autowired
    private Producer producer;

    @Test
    void create() throws InterruptedException {
        producer.create("say hello");
    }

}