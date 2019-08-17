package com.hiscat.batch.config;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.SkipListener;
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
//@Configuration
 */
@Slf4j
@AllArgsConstructor
public class SkipListenerConfig {
    private static final String ERROR_NUM = "23";
    private JobBuilderFactory jobBuilderFactory;
    private StepBuilderFactory stepBuilderFactory;

    @Bean
    Job job() {
        return jobBuilderFactory.get("skipListenerJob1")
                .start(step1())
                .build();
    }

    @Bean
    Step step1() {
        return stepBuilderFactory
                .get("skipListenerJobStep")
                .<String, String>chunk(10)
                .reader(new ListItemReader<>(IntStream.rangeClosed(1, 100).mapToObj(String::valueOf).collect(toList())))
                .writer(System.out::println)
                .processor((ItemProcessor<String, String>) item -> {
                    LOGGER.info("process item {}", item);
                    if (ERROR_NUM.equalsIgnoreCase(item)) {
                        LOGGER.info("process item {} fail will skip ", item);
                        throw new RuntimeException("error item " + item);
                    }
                    return Integer.valueOf("-" + item).toString();
                })
                .faultTolerant()
                .skip(RuntimeException.class)
                .skipLimit(10)
                .listener(skipListener())
                .build();
    }

    @Bean
    SkipListener<? super String, ? super String> skipListener() {
        return new SkipListener<>() {
            @Override
            public void onSkipInRead(Throwable t) {

            }

            @Override
            public void onSkipInWrite(String item, Throwable t) {

            }

            @Override
            public void onSkipInProcess(String item, Throwable t) {
                LOGGER.error("item : {} is skipped e:{}", item, t.getMessage(), t);
            }
        };
    }

}
