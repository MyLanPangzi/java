package com.hiscat.batch.config;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.context.annotation.Bean;

import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;

/**
 * @author hiscat
 * //@Configuration
 */
@Slf4j
@AllArgsConstructor
public class SkipConfig {
    private static final String ERROR_NUM = "23";
    private JobBuilderFactory jobBuilderFactory;
    private StepBuilderFactory stepBuilderFactory;

    @Bean
    Job job() {
        return jobBuilderFactory.get("skipJob")
                .start(step1())
                .build();
    }

    @Bean
    Step step1() {
        return stepBuilderFactory
                .get("skipJobStep")
                .<String, String>chunk(10)
                .reader(new ListItemReader<>(IntStream.rangeClosed(1, 100).mapToObj(String::valueOf).collect(toList())))
                .writer(System.out::println)
                .processor(new ItemProcessor<>() {
                    private Integer tryCount = 0;

                    @Override
                    public String process(String item) {
                        LOGGER.info("process item {}", item);
                        if (ERROR_NUM.equalsIgnoreCase(item)) {
                            this.tryCount++;
                            if (tryCount < 3) {
                                LOGGER.info("process item {} fail will skip ", item);
                                throw new RuntimeException("retry " + tryCount + " times");
                            }
                        }
                        return Integer.valueOf("-" + item).toString();
                    }
                })
                .faultTolerant()
                .skip(RuntimeException.class)
                .skipLimit(10)
                .build();
    }

}
