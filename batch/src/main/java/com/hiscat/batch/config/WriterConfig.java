package com.hiscat.batch.config;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.context.annotation.Bean;

import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;

/**
 * @author hiscat
 */
@Slf4j
//@Configuration
@AllArgsConstructor
public class WriterConfig {

    private JobBuilderFactory jobBuilderFactory;
    private StepBuilderFactory stepBuilderFactory;

    @Bean
    Job job() {
        return jobBuilderFactory.get("writerJob")
                .start(step())
                .build();
    }

    @Bean
    Step step() {
        return stepBuilderFactory.get("writerJobStep")
                .<String , String >chunk(10)
                .reader(new ListItemReader<>(IntStream.rangeClosed(1,100).mapToObj(String::valueOf).collect(toList())))
                .writer(System.out::println)
                .build();
    }

}
