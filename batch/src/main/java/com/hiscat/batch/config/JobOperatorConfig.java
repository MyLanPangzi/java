package com.hiscat.batch.config;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.support.JobRegistryBeanPostProcessor;
import org.springframework.batch.core.converter.DefaultJobParametersConverter;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.batch.core.launch.support.SimpleJobOperator;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;

/**
 * @author hiscat
//@Configuration
 */
@Slf4j
@AllArgsConstructor
public class JobOperatorConfig implements ApplicationContextAware {
    private JobBuilderFactory jobBuilderFactory;
    private StepBuilderFactory stepBuilderFactory;
    private JobLauncher jobLauncher;
    private JobRepository jobRepository;
    private JobExplorer jobExplore;
    private ApplicationContext applicationContext;
    private JobRegistry jobRegistry;

    @Bean
    JobRegistryBeanPostProcessor jobRegistryBeanPostProcessor() throws Exception {
        JobRegistryBeanPostProcessor processor = new JobRegistryBeanPostProcessor();
        processor.setBeanFactory(applicationContext.getAutowireCapableBeanFactory());
        processor.setJobRegistry(jobRegistry);
        processor.afterPropertiesSet();
        return processor;
    }


    @Bean
    JobOperator jobOperator() throws Exception {
        SimpleJobOperator operator = new SimpleJobOperator();
        operator.setJobLauncher(jobLauncher);
        operator.setJobParametersConverter(new DefaultJobParametersConverter());
        operator.setJobRepository(jobRepository);
        operator.setJobExplorer(jobExplore);
        operator.setJobRegistry(jobRegistry);
        operator.afterPropertiesSet();

        return operator;
    }


    @Bean
    Job operatorJob() {
        return jobBuilderFactory.get("operatorJob")
                .start(step1())
                .build();
    }

    @Bean
    Step step1() {
        return stepBuilderFactory
                .get("operatorJobStep")
                .tasklet((contribution, chunkContext) -> {
                    Object jobParameter = chunkContext.getStepContext().getJobParameters().get("jobParameter");
                    LOGGER.info("job parameter : {}", jobParameter);
                    return RepeatStatus.FINISHED;
                })
                .build();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
