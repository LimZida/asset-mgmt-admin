package com.mcnc.assetmgmt.util.batch;

import com.mcnc.assetmgmt.util.common.CodeAs;
import com.mcnc.assetmgmt.util.common.Response;
import com.mcnc.assetmgmt.util.config.BatchConfig;
import com.mcnc.assetmgmt.util.exception.CustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * title : BatchScheduler
 *
 * description : 배치 스케줄러 (매일 9시마다)
 *
 * reference : 배치 코드 https://warpgate3.tistory.com/entry/Spring-Batch-Sample-Code-1
 *                     https://cocococo.tistory.com/entry/Spring-Boot-Spring-Batch-scheduler-%EC%82%AC%EC%9A%A9%ED%95%B4-%EC%9D%BC%EC%A0%95-%EC%A3%BC%EA%B8%B0%EB%A1%9C-%EC%8B%A4%ED%96%89-%EB%B0%A9%EB%B2%95
 *             cron 표현식 https://dev-coco.tistory.com/176 , https://allonsyit.tistory.com/43
 *
 * author : 임현영
 * date : 2024.02.21
 **/
@Slf4j
@Component
@RequiredArgsConstructor
public class BatchScheduler {
    private final JobLauncher jobLauncher;
    private final BatchConfig batchConfig;

    @Scheduled(cron = "0 0 9 * * *")
    public void runJob() {
        // job parameter 설정
        Map<String, JobParameter> confMap = new HashMap<>();
        confMap.put("time", new JobParameter(System.currentTimeMillis()));
        JobParameters jobParameters = new JobParameters(confMap);

        try {
            log.info("매일 9시마다 실행되는 자산관리 배치");
            jobLauncher.run(batchConfig.assetJob(), jobParameters);
        } catch (Exception e) {
            throw Response.makeFailResponse(CodeAs.ASSET_BATCH_ERROR_CODE,
                    HttpStatus.INTERNAL_SERVER_ERROR,CodeAs.ASSET_BATCH_ERROR_MESSAGE , e );
        }
    }
}
