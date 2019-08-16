package com.hiscat.batch.config;

import com.hiscat.batch.entity.Customer;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.context.annotation.Bean;

import javax.sql.DataSource;
import java.util.Date;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;

/**
 * @author hiscat
 */
@Slf4j
//@Configuration
@AllArgsConstructor
public class JdbcBatchWriterConfig {
    private DataSource dataSource;
    private JobBuilderFactory jobBuilderFactory;
    private StepBuilderFactory stepBuilderFactory;

    @Bean
    Job job() {
        return jobBuilderFactory.get("jdbcBatchWriterJob")
                .start(step())
                .build();
    }

    @Bean
    Step step() {
        return stepBuilderFactory.get("jdbcBatchWriterJobStep")
                .<Customer, Customer>chunk(10)
                .reader(new ListItemReader<>(IntStream.rangeClosed(1, 100).mapToObj(i -> Customer.builder()
                                .id(i)
                                .name(String.valueOf(i))
                                .birthday(new Date())
                                .build())
                                .collect(toList())
                        )
                )
                .writer(jdbcWriter())
                .build();
    }

    @Bean
    ItemWriter<? super Customer> jdbcWriter() {
        return new JdbcBatchItemWriterBuilder<Customer>()
                .sql("replace into customer (id, name, birthday) values (:id, :name, :birthday);")
                .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
                .dataSource(dataSource)
                .build();
    }

}
