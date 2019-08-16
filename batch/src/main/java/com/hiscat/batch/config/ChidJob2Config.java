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
public class ChidJob2Config {
    private JobBuilderFactory jobBuilderFactory;

    private StepBuilderFactory stepBuilderFactory;

    @Bean
    Job job2() {
        return jobBuilderFactory.get("childJob3")
                .start(childJob1Step2())
                .build();
    }

    @Bean
    Step childJob1Step2() {
        return stepBuilderFactory.get("childJob1Step2")
                .tasklet(((contribution, chunkContext) -> {
                    System.out.println("childJob1Step2");
                    return RepeatStatus.FINISHED;
                }))
                .build();
    }
}
