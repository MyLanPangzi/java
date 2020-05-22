package com.hiscat.gmallweb.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hiscat.gmallweb.entity.StartupLog;
import com.hiscat.gmallweb.vo.DauTotalVO;
import com.hiscat.gmallweb.vo.HourDau;

import java.util.List;

/**
 * @author hiscat
 */
public interface StartupLogService extends IService<StartupLog> {

    /**
     * 统计当日日活
     *
     * @param date 日期
     * @return 日活列表
     */
    List<DauTotalVO> getTotalDau(String date);

    /**
     * 统计当日分时日活
     *
     * @param id   统计类型
     * @param date 日期
     * @return 分时日活
     */
    HourDau hourDau(String id, String date);
}
