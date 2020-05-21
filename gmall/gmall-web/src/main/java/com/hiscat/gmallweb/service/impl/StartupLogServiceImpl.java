package com.hiscat.gmallweb.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hiscat.gmallweb.entity.StartupLog;
import com.hiscat.gmallweb.mapper.StartupLogMapper;
import com.hiscat.gmallweb.service.StartupLogService;
import com.hiscat.gmallweb.vo.DauTotalVO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
 * @author hiscat
 */
@Service
@AllArgsConstructor
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
        return Collections.singletonList(
                DauTotalVO
                        .builder()
                        .id("dau")
                        .name("日活")
                        .value(count.longValue())
                        .build()
        );
    }
}
