package com.hiscat.batch.config;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.*;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

@Slf4j
@Configuration
@AllArgsConstructor
public class ItemStreamReaderConfig {

    private JobBuilderFactory jobBuilderFactory;
    private StepBuilderFactory stepBuilderFactory;

    @Bean
    Job job() {
        return jobBuilderFactory.get("restartJob4")
                .start(step())
                .build();
    }

    @Bean
    Step step() {
        return stepBuilderFactory.get("restartJobStep")
                .<Customer, Customer>chunk(1)
                .reader(restartReader())
                .writer(System.out::println)
                .build();
    }

    @Bean
    RestartReader restartReader() {
        return new RestartReader();
    }

    static class RestartReader implements ItemStreamReader<Customer> {
        private FlatFileItemReader<Customer> flatFileItemReader;
        private ExecutionContext executionContext;
        private Integer cur;
        private Boolean restart;

        public RestartReader() {
            cur = 0;
            restart = false;
            DefaultLineMapper<Customer> mapper = new DefaultLineMapper<>();
            DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();
            tokenizer.setNames("id", "name", "birthday");
            mapper.setLineTokenizer(tokenizer);
            mapper.setFieldSetMapper(fieldSet -> Customer.builder()
                    .id(fieldSet.readInt("id"))
                    .name(fieldSet.readRawString("name"))
                    .birthday(fieldSet.readDate("birthday"))
                    .build());
            mapper.afterPropertiesSet();
            this.flatFileItemReader = new FlatFileItemReaderBuilder<Customer>()
                    .name("flatFileReader")
                    .resource(new ClassPathResource("data.csv"))
                    .linesToSkip(1)
                    .lineMapper(mapper)
                    .build();
        }

        @Override
        public Customer read() throws Exception {
            this.cur++;
            if (restart) {
                LOGGER.info("start at {}", this.cur);
                this.flatFileItemReader.setLinesToSkip(this.cur);
            }
            this.flatFileItemReader.open(this.executionContext);

            Customer result = flatFileItemReader.read();
            if (result != null && result.getName().strip().equals("wrong")) {
                throw new RuntimeException("wrong name " + result.getName());
            } else {
                cur--;
            }
            this.flatFileItemReader.update(this.executionContext);
            return result;
        }

        @Override
        public void open(ExecutionContext executionContext) throws ItemStreamException {
            this.executionContext = executionContext;
            if (executionContext.containsKey("cur")) {
                cur = executionContext.getInt("cur");
                this.restart = true;
            }
        }

        @Override
        public void update(ExecutionContext executionContext) throws ItemStreamException {
            this.executionContext.putInt("cur", this.cur);
        }

        @Override
        public void close() throws ItemStreamException {

        }
    }

}
