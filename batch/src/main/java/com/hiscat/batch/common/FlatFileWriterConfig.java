package com.hiscat.batch.common;

import com.alibaba.fastjson.JSONObject;
import com.hiscat.batch.entity.Customer;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

/**
 * @author hiscat
 */
@Configuration
public class FlatFileWriterConfig {
    @Bean
    FlatFileItemWriter<Customer> customerFlatFileItemWriter() throws Exception {
        FlatFileItemWriter<Customer> writer = new FlatFileItemWriterBuilder<Customer>()
                .name("flatFileWriter")
                .resource(new ClassPathResource("customers.txt"))
                .lineAggregator(JSONObject::toJSONString)
                .build();
        writer.afterPropertiesSet();
        return writer;
    }
}
