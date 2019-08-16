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
import org.springframework.context.annotation.Bean;

/**
 * @author hiscat
//@Configuration
 */
@Slf4j
@AllArgsConstructor
public class FlatFileWriterConfig {
    private JobBuilderFactory jobBuilderFactory;
    private StepBuilderFactory stepBuilderFactory;
    private ItemReader<? extends Customer> reader;
    private FlatFileItemWriter<Customer> writer;

    @Bean
    Job job() {
        return jobBuilderFactory.get("flatFileWriterJob")
                .start(step())
                .build();
    }

    @Bean
    Step step() {

        return stepBuilderFactory.get("flatFileWriterJobStep")
                .<Customer, Customer>chunk(10)
                .reader(reader)
                .writer(this.writer)
                .build();
    }

}
