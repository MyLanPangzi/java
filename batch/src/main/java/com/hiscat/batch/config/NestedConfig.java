package com.hiscat.batch.config;

import lombok.AllArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.JobStepBuilder;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

//@Configuration
@AllArgsConstructor
public class NestedConfig {

    private JobBuilderFactory jobBuilderFactory;

    private JobLauncher jobLauncher;

    private Job job1;
    private Job job2;

    @Bean("PJob")
    Job parentJob(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return jobBuilderFactory.get("nestedJob2")
                .start(startStep(jobRepository, transactionManager))
                .build();
    }

    @Bean
    Step startStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        System.out.println(jobLauncher);
        System.out.println(jobRepository);
        System.out.println(transactionManager);
        return new JobStepBuilder(new StepBuilder("startStep"))
                .job(job1)
                .job(job2)
                .launcher(jobLauncher)
                .repository(jobRepository)
                .transactionManager(transactionManager)
                .build();
    }




}
