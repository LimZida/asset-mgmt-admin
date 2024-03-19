package com.mcnc.assetmgmt.util.config;

import com.mcnc.assetmgmt.asset.service.HardwareService;
import com.mcnc.assetmgmt.asset.service.SoftwareService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * title : BatchConfig
 *
 * description : Batch에 대한 환경설정, 자산 노후화 및 만료 여부와 관련된 배치
 *
 * reference : 배치 코드 https://warpgate3.tistory.com/entry/Spring-Batch-Sample-Code-1
 *                     https://cocococo.tistory.com/entry/Spring-Boot-Spring-Batch-scheduler-%EC%82%AC%EC%9A%A9%ED%95%B4-%EC%9D%BC%EC%A0%95-%EC%A3%BC%EA%B8%B0%EB%A1%9C-%EC%8B%A4%ED%96%89-%EB%B0%A9%EB%B2%95
 *
 * author : 임현영
 * date : 2024.02.21
 **/

@Configuration
@EnableBatchProcessing
@RequiredArgsConstructor
@Slf4j
public class BatchConfig {
    private final HardwareService hardwareService;
    private final SoftwareService softwareService;
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;


    @Bean
    public Job assetJob() {
        return jobBuilderFactory
                .get("assetJob")
                .start(assetStep())
                .build();
    }

    @Bean
    public Step assetStep() {
        return this.stepBuilderFactory
                .get("assetStep")
                .tasklet(assetTasklet())
                .build();
    }

    @Bean
    public Tasklet assetTasklet() {
        return (stepContribution, chunkContext) -> {
            log.info("----------------------------------------------");
            log.info("assetJob 시작");
            hardwareService.oldHardware();
            softwareService.expireSoftware();
            log.info("assetJob 종료");
            log.info("----------------------------------------------");

            return RepeatStatus.FINISHED;
        };
    }
}
