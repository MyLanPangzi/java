package com.hiscat.batch.config;

import com.hiscat.batch.entity.Customer;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.support.builder.CompositeItemProcessorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * @author hiscat
 */
@Slf4j
@Configuration
@AllArgsConstructor
public class CompositeProcessorConfig {
    private JobBuilderFactory jobBuilderFactory;
    private StepBuilderFactory stepBuilderFactory;
    private ItemReader<? extends Customer> reader;
    private FlatFileItemWriter<Customer> jsonWriter;

    @Bean
    Job job() {
        return jobBuilderFactory.get("compositeProcessorJob1")
                .start(step2())
                .build();
    }

    @Bean
    Step step2() {
        return stepBuilderFactory.get("compositeProcessorJobStep")
                .<Customer, Customer>chunk(10)
                .reader(reader)
                .processor(
                        new CompositeItemProcessorBuilder<Customer, Customer>()
                                .delegates(
                                        List.of((Customer c) -> {
                                            c.setName(c.getName() + "A");
                                            return c;
                                        }, (Customer c) -> c.getId() % 2 == 0 ? c : null)
                                )
                                .build()
                )
                .writer(jsonWriter)
                .build();
    }

}
