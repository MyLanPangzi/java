package com.hiscat.batch.config;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;

import java.util.Map;

/**
 * @author hiscat
//@Configuration
 */
@Slf4j
@AllArgsConstructor
public class ExceptionHandleConfig {
    private static final String FIRST = "first";
    private JobBuilderFactory jobBuilderFactory;
    private StepBuilderFactory stepBuilderFactory;

    @Bean
    Job job() {
        return jobBuilderFactory.get("errorHandleJob2")
                .start(step1())
                .next(step2())
                .build();
    }

    @Bean
    Step step1() {
        return stepBuilderFactory
                .get("errorHandleJobStep1")
                .tasklet(errorHandle())
                .build();
    }

    @Bean
    Step step2() {
        return stepBuilderFactory
                .get("errorHandleJobStep2")
                .tasklet(errorHandle())
                .build();
    }

    @Bean
    @StepScope
    Tasklet errorHandle() {
        return (contribution, chunkContext) -> {
            Map<String, Object> context = chunkContext.getStepContext().getStepExecutionContext();
            if (context.containsKey(FIRST)) {
                LOGGER.info("context has first run success");
                return RepeatStatus.FINISHED;
            }
            chunkContext.getStepContext().getStepExecution().getExecutionContext().put(FIRST, true);
            throw new RuntimeException("context has not first run error");
        };
    }


}
