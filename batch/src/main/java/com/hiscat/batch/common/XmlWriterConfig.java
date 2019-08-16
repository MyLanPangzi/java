package com.hiscat.batch.common;

import com.hiscat.batch.entity.Customer;
import org.springframework.batch.item.xml.StaxEventItemWriter;
import org.springframework.batch.item.xml.builder.StaxEventItemWriterBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.oxm.xstream.XStreamMarshaller;

import java.util.Map;

/**
 * @author hiscat
 */
@Configuration
public class XmlWriterConfig {
    @Bean
    StaxEventItemWriter<Customer> customerStaxEventItemWriter() throws Exception {
        XStreamMarshaller marshaller = new XStreamMarshaller();
        marshaller.setAliases(Map.of("customer", Customer.class));
        var writer = new StaxEventItemWriterBuilder<Customer>()
                .name("writer")
                .resource(new ClassPathResource("customers.xml"))
                .rootTagName("customers")
                .marshaller(marshaller)
                .build();
        writer.afterPropertiesSet();
        return writer;
    }
}
