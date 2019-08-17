package com.hiscat.batch.config;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;

/**
 * @author hiscat
//@Configuration
 */
@Slf4j
@AllArgsConstructor
public class JobLauncherConfig {
    private JobBuilderFactory jobBuilderFactory;
    private StepBuilderFactory stepBuilderFactory;

    @Bean
    Job launcherJob() {
        return jobBuilderFactory.get("launcherJob")
                .start(step1())
                .build();
    }

    @Bean
    Step step1() {
        return stepBuilderFactory
                .get("launcherJobStep")
                .tasklet((contribution, chunkContext) -> {
                    Object jobParameter = chunkContext.getStepContext().getJobParameters().get("jobParameter");
                    LOGGER.info("job parameter : {}", jobParameter);
                    return RepeatStatus.FINISHED;
                })
                .build();
    }

}
