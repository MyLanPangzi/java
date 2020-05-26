package com.hiscat.gmallweb.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hiscat.gmallweb.bean.Detail;
import com.hiscat.gmallweb.bean.Option;
import com.hiscat.gmallweb.bean.SaleDetail;
import com.hiscat.gmallweb.bean.Stat;
import com.hiscat.gmallweb.controller.SaleDetailQuery;
import com.hiscat.gmallweb.entity.OrderInfo;
import com.hiscat.gmallweb.entity.StartupLog;
import com.hiscat.gmallweb.mapper.OrderInfoMapper;
import com.hiscat.gmallweb.mapper.StartupLogMapper;
import com.hiscat.gmallweb.service.WebService;
import com.hiscat.gmallweb.vo.DauTotalVO;
import com.hiscat.gmallweb.vo.HourCount;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.Operator;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.bucket.MultiBucketsAggregation;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedLongTerms;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedStringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.aggregations.support.ValueType;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.*;

/**
 * @author hiscat
 */
@Service
@AllArgsConstructor
@Slf4j
public class WebServiceImpl implements WebService {

    StartupLogMapper startupLogMapper;

    OrderInfoMapper orderInfoMapper;

    ElasticsearchRestTemplate restTemplate;

    @Override
    public List<DauTotalVO> getTotalDau(String date) {
        Integer dauCount = startupLogMapper.selectCount(
                new QueryWrapper<StartupLog>()
                        .lambda()
                        .eq(StartupLog::getLogdate, date)
        );

        return asList(
                DauTotalVO
                        .builder()
                        .id("dau")
                        .name("新增日活")
                        .value(dauCount.doubleValue())
                        .build(),
                DauTotalVO
                        .builder()
                        .id("order_amount")
                        .name("新增交易额")
                        .value(orderInfoMapper.getDauTotal(date))
                        .build(),
                DauTotalVO
                        .builder()
                        .id("new_mid")
                        .name("新增设备")
                        .value(233D)
                        .build()
        );
    }

    @Override
    public HourCount hourDau(String id, String date) {
        Map<String, Object> today = null;
        Map<String, Object> yesterday = null;
        switch (id) {
            case "dau":
                today = getDauHourMap(date);
                yesterday = getDauHourMap(LocalDate.parse(date).minusDays(1).toString());
                break;
            case "order_amount":
                today = getOrderHourMap(date);
                yesterday = getOrderHourMap(LocalDate.parse(date).minusDays(1).toString());
                break;
            default:

        }

        return HourCount.builder()
                .today(today)
                .yesterday(yesterday)
                .build();
    }

    @Override
    public SaleDetail saleDetail(SaleDetailQuery query) {
        NativeSearchQuery searchQuery = new NativeSearchQuery(
                new BoolQueryBuilder()
                        .filter(new TermQueryBuilder("dt", query.getDate()))
                        .must(new MatchQueryBuilder("sku_name", query.getKeyword()).operator(Operator.AND))
        ).setPageable(PageRequest.of(query.getStartpage(), query.getSize()));
        String countByAge = "countByAge";
        String countByGender = "countByGender";
        searchQuery.setAggregations(asList(
                new TermsAggregationBuilder(countByAge, ValueType.LONG).field("user_age"),
                new TermsAggregationBuilder(countByGender, ValueType.LONG).field("user_gender")
        ));

        AggregatedPage<Detail> agg = restTemplate.queryForPage(searchQuery, Detail.class);
        return SaleDetail.builder()
                .stat(asList(
                        genderAgg2Stat(agg.getAggregation(countByGender)),
                        ageAgg2Stat(agg.getAggregation(countByAge))
                ))
                .total(agg.getTotalElements())
                .detail(agg.getContent())
                .build();
    }

    private Stat ageAgg2Stat(Aggregation aggregation) {
        ParsedLongTerms ageAgg = (ParsedLongTerms) aggregation;
        Map<String, Long> collect = ageAgg.getBuckets().stream()
                .collect(groupingBy(bucket -> {
                            long age = bucket.getKeyAsNumber().longValue();
                            if (age < 20) {
                                return "20岁以下";
                            } else if (age < 30) {
                                return "20岁到30岁";
                            } else {
                                return "30岁以上";
                            }
                        },
                        summingLong(MultiBucketsAggregation.Bucket::getDocCount)
                ));
        long sum = collect.values().stream().mapToLong(value -> value).sum();
        List<Option> options = collect.entrySet()
                .stream()
                .map(entry -> Option.builder().name(entry.getKey()).value(entry.getValue().doubleValue() / sum).build())
                .collect(toList());
        return Stat.builder()
                .title("年龄占比")
                .options(options)
                .build();
    }

    private Stat genderAgg2Stat(Aggregation agg) {
        ParsedStringTerms genderAgg = (ParsedStringTerms) agg;
        long f = genderAgg.getBucketByKey("F").getDocCount();
        long m = genderAgg.getBucketByKey("M").getDocCount();
        return Stat.builder()
                .options(asList(
                        Option.builder().name("女").value(f * 1.0 / (m + f)).build(),
                        Option.builder().name("男").value(m * 1.0 / (m + f)).build()
                ))
                .title("用户性别占比")
                .build();
    }

    private Map<String, Object> getOrderHourMap(String date) {
        Map<String, Object> result = new HashMap<>();
        this.orderInfoMapper.selectMaps(
                new QueryWrapper<OrderInfo>()
                        .select("sum(total_amount) total_amount", "create_hour")
                        .eq("create_date", date)
                        .groupBy("create_hour")
        ).forEach(e -> {
            result.put(e.get("CREATE_HOUR").toString(), e.get("TOTAL_AMOUNT"));
        });
        return result;
    }

    private Map<String, Object> getDauHourMap(String date) {
        Map<String, Object> result = new HashMap<>();
        this.startupLogMapper.selectMaps(
                new QueryWrapper<StartupLog>()
                        .select("loghour", "count(*) cnt")
                        .eq("logdate", date)
                        .groupBy("loghour")
        ).forEach(e -> {
            result.put(e.get("LOGHOUR").toString(), e.get("CNT"));
        });
        return result;
    }
}
