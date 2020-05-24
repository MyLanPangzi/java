package com.hiscat.gmallweb.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hiscat.gmallweb.entity.OrderInfo;
import com.hiscat.gmallweb.entity.StartupLog;
import com.hiscat.gmallweb.mapper.OrderInfoMapper;
import com.hiscat.gmallweb.mapper.StartupLogMapper;
import com.hiscat.gmallweb.service.WebService;
import com.hiscat.gmallweb.vo.DauTotalVO;
import com.hiscat.gmallweb.vo.HourCount;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author hiscat
 */
@Service
@AllArgsConstructor
@Slf4j
public class WebServiceImpl implements WebService {

    StartupLogMapper startupLogMapper;

    OrderInfoMapper orderInfoMapper;

    @Override
    public List<DauTotalVO> getTotalDau(String date) {
        Integer dauCount = startupLogMapper.selectCount(
                new QueryWrapper<StartupLog>()
                        .lambda()
                        .eq(StartupLog::getLogdate, date)
        );

        return Arrays.asList(
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

        }

        return HourCount.builder()
                .today(today)
                .yesterday(yesterday)
                .build();
    }

    private Map<String, Object> getOrderHourMap(String date) {
        Map<String, Object> result = new HashMap<>();
        this.orderInfoMapper.selectMaps(
                new QueryWrapper<OrderInfo>()
                        .select("sum(total_amount) total_amount","create_hour")
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
