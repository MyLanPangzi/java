package com.hiscat.batch.config;

import lombok.AllArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//@Configuration
@AllArgsConstructor
public class ChildJob1Config {
    private JobBuilderFactory jobBuilderFactory;

    private StepBuilderFactory stepBuilderFactory;

    @Bean
    Job job1() {
        return jobBuilderFactory.get("childJob4")
                .start(childJob1Step1())
                .build();
    }

    @Bean
    Step childJob1Step1() {
        return stepBuilderFactory.get("childJob1Step1")
                .tasklet(((contribution, chunkContext) -> {
                    System.out.println("childJob1Step1");
                    return RepeatStatus.FINISHED;
                }))
                .build();
    }


}
