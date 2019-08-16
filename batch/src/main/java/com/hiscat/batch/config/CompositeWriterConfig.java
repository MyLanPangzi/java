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
import org.springframework.batch.item.support.CompositeItemWriter;
import org.springframework.batch.item.support.builder.ClassifierCompositeItemWriterBuilder;
import org.springframework.batch.item.support.builder.CompositeItemWriterBuilder;
import org.springframework.batch.item.xml.StaxEventItemWriter;
import org.springframework.context.annotation.Bean;

import javax.sql.DataSource;
import java.util.List;

/**
 * @author hiscat
//@Configuration
 */
@Slf4j
@AllArgsConstructor
public class CompositeWriterConfig {
    private DataSource dataSource;
    private JobBuilderFactory jobBuilderFactory;
    private StepBuilderFactory stepBuilderFactory;
    private ItemReader<? extends Customer> reader;
    private StaxEventItemWriter<Customer> xmlWriter;
    private FlatFileItemWriter<Customer> jsonWriter;

    @Bean
    Job job() {
        return jobBuilderFactory.get("compositeWriterJob1")
                .start(step2())
//                .next(step2())
                .build();
    }

    @Bean
    Step step2() {
        return stepBuilderFactory.get("classifierStep")
                .<Customer, Customer>chunk(10)
                .reader(reader)
                .writer(
                        new ClassifierCompositeItemWriterBuilder<Customer>()
                                .classifier(customer -> customer.getId() % 2 == 0 ? xmlWriter : jsonWriter)
                                .build()
                )
                .stream(xmlWriter)
                .stream(jsonWriter)
                .build();
    }

    @Bean
    Step step() throws Exception {
        CompositeItemWriter<Customer> compositeItemWriter = new CompositeItemWriterBuilder<Customer>()
                .delegates(List.of(xmlWriter, jsonWriter))
                .build();
        xmlWriter.afterPropertiesSet();
        jsonWriter.afterPropertiesSet();
        compositeItemWriter.afterPropertiesSet();
        return stepBuilderFactory.get("compositeWriterJobStep")
                .<Customer, Customer>chunk(10)
                .reader(reader)
                .writer(compositeItemWriter)
                .build();
    }

}
