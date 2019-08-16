package com.hiscat.batch.config;

import lombok.AllArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;

//@Configuration
@AllArgsConstructor
public class SplitConfig {

    private JobBuilderFactory jobBuilderFactory;

    private StepBuilderFactory stepBuilderFactory;

    @Bean
    Job job() {
        return jobBuilderFactory.get("splitJob2")
                .start(flow1())
                .split(new SimpleAsyncTaskExecutor()).add(flow2())
                .end()
                .build();
    }

    @Bean
    Flow flow1() {
        return new FlowBuilder<Flow>("flow1")
                .start(stepBuilderFactory.get("flow1 step1")
                        .tasklet(((contribution, chunkContext) -> {
                            System.out.printf("step:%s, thread: %s\n", chunkContext.getStepContext().getStepName(), Thread.currentThread().getName());
                            return RepeatStatus.FINISHED;
                        }))
                        .build()
                )
                .build();
    }

    @Bean
    Flow flow2() {
        return new FlowBuilder<Flow>("flow2")
                .start(stepBuilderFactory.get("flow2 step1")
                        .tasklet(((contribution, chunkContext) -> {
                            System.out.printf("step:%s, thread: %s\n", chunkContext.getStepContext().getStepName(), Thread.currentThread().getName());
                            return RepeatStatus.FINISHED;
                        }))
                        .build()
                )
                .build();
    }

}
