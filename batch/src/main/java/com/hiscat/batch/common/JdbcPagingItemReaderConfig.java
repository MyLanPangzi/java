package com.hiscat.batch.common;

import com.hiscat.batch.entity.Customer;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.Order;
import org.springframework.batch.item.database.builder.JdbcPagingItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.util.Map;

/**
 * @author hiscat
 */
@Configuration
public class JdbcPagingItemReaderConfig {

    @Bean
    JdbcPagingItemReader<Customer> customerJdbcPagingItemReader(DataSource dataSource) throws Exception {
        JdbcPagingItemReader<Customer> reader = new JdbcPagingItemReaderBuilder<Customer>()
                .name("reader")
                .dataSource(dataSource)
                .fetchSize(100)
                .fromClause("from customer")
                .selectClause("id, name, birthday")
                .sortKeys(Map.of("id", Order.ASCENDING))
                .rowMapper(
                        (rs, i) -> Customer.builder()
                                .id(rs.getInt("id"))
                                .name(rs.getString("name"))
                                .birthday(rs.getDate("birthday"))
                                .build()
                )
                .build();
        reader.afterPropertiesSet();
        return reader;
    }
}
