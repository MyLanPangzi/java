package com.hiscat.gmallweb.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hiscat.gmallweb.entity.StartupLog;
import com.hiscat.gmallweb.mapper.StartupLogMapper;
import com.hiscat.gmallweb.service.StartupLogService;
import com.hiscat.gmallweb.vo.DauTotalVO;
import com.hiscat.gmallweb.vo.HourDau;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

/**
 * @author hiscat
 */
@Service
@AllArgsConstructor
@Slf4j
public class StartupLogServiceImpl extends ServiceImpl<StartupLogMapper, StartupLog> implements StartupLogService {

    StartupLogMapper startupLogMapper;

    @Override
    public List<DauTotalVO> getTotalDau(String date) {
        Integer count = startupLogMapper.selectCount(
                new QueryWrapper<StartupLog>()
                        .lambda()
                        .eq(StartupLog::getLogdate, date)
                        .groupBy(StartupLog::getLogdate)
        );
        return Arrays.asList(
                DauTotalVO
                        .builder()
                        .id("dau")
                        .name("日活")
                        .value(count.longValue())
                        .build(),
                DauTotalVO
                        .builder()
                        .id("new_mid")
                        .name("新增设备")
                        .value(233L)
                        .build()
        );
    }

    @Override
    public HourDau hourDau(String id, String date) {
        if ("dau".equalsIgnoreCase(id)) {
            return HourDau.builder().build();
        }
        Map<String, Object> today = new HashMap<>();
        this.listMaps(
                new QueryWrapper<StartupLog>()
                        .select("loghour", "count(*) cnt")
                        .eq("logdate", date)
                        .groupBy("loghour")
        ).forEach(e -> {
            today.put(e.get("LOGHOUR").toString(), e.get("CNT"));
        });

        Map<String, Object> yesterday = new HashMap<>();
        this.listMaps(
                new QueryWrapper<StartupLog>()
                        .select("loghour", "count(*) cnt")
                        .eq("logdate", LocalDate.parse(date).minusDays(1).toString())
                        .groupBy("loghour")
        ).forEach(e -> {
            yesterday.put(e.get("LOGHOUR").toString(), e.get("CNT"));
        });

        return HourDau.builder()
                .today(today)
                .yesterday(yesterday)
                .build();
    }
}
