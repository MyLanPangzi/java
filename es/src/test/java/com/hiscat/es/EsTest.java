package com.hiscat.es;

import io.searchbox.client.JestClient;
import io.searchbox.client.JestClientFactory;
import io.searchbox.client.config.HttpClientConfig;
import io.searchbox.core.Bulk;
import io.searchbox.core.Index;
import io.searchbox.core.Search;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Arrays;

public class EsTest {

    static JestClientFactory FACTORY;
    static JestClient jestClient;

    @BeforeAll
    static void init() {
        FACTORY = new JestClientFactory();
        FACTORY.setHttpClientConfig(new HttpClientConfig.Builder("http://hadoop102:9200").build());
        jestClient = FACTORY.getObject();
    }

    @AfterAll
    static void close() throws IOException {
        jestClient.close();
    }


    @Test
    void testSearch() throws IOException {
        SearchSourceBuilder builder = new SearchSourceBuilder();
        builder
                .query(
                        new BoolQueryBuilder()
                                .filter(new TermQueryBuilder("color", "red"))
                );

        String query = builder.toString();
        System.out.println(query);
        jestClient.execute(
                new Search.Builder(query)
                        .addIndex("car")
                        .addType("doc")
                        .build()
        )
                .getHits(Car.class)
                .stream()
                .map(e -> e.source)
                .forEach(System.out::println);

        jestClient.execute(
                new Search.Builder("{\"query\":{\"bool\":{\"filter\":[{\"term\":{\"color\":{\"value\":\"blue\",\"boost\":1.0}}}],\"adjust_pure_negative\":true,\"boost\":1.0}}}")
                        .addIndex("car")
                        .addType("doc")
                        .build()
        )
                .getHits(Car.class)
                .forEach(e -> {
                    System.out.printf("index=%s,type=%s,id=%s,parent=%s,routing=%s,score=%s,highlight=%s,sort=%s,score=%s,explanation=%s",
                            e.index, e.type, e.id, e.parent,
                            e.routing, e.score,
                            e.highlight, e.sort, e.score, e.explanation);
                });
    }

    @Test
    void testBulk() throws IOException {
        Bulk bulk = new Bulk.Builder()
                .addAction(Arrays.asList(
                        new Index.Builder(Car.builder().brand("aodi").color("red").build()).id("2").build(),
                        new Index.Builder(Car.builder().brand("aodi").color("blue").build()).id("3").build()
                ))
                .defaultIndex("car")
                .defaultType("doc")
                .build();
        jestClient.execute(bulk);
    }

    @Test
    void testIndex() throws IOException {
        Car car = Car.builder()
                .brand("aodi")
                .color("red")
                .build();
        Index request = new Index.Builder(car)
                .index("car")
                .type("doc")
                .id("1")
                .build();
        jestClient.execute(request);
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    static class Car {
        private String brand;
        private String color;
    }

    @Test
    void testJest() throws IOException {
        Index index = new Index.Builder("{\n" +
                "  \"name\":\"zhangsan\",\n" +
                "  \"age\":17\n" +
                "}")
                .index("test5")
                .type("_doc")
                .id("2")
                .build();

        jestClient.execute(index);

    }
}
