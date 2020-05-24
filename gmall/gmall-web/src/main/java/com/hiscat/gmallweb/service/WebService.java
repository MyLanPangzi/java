package com.hiscat.gmallweb.service;

import com.hiscat.gmallweb.vo.DauTotalVO;
import com.hiscat.gmallweb.vo.HourCount;

import java.util.List;

/**
 * @author hiscat
 */
public interface WebService   {

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
    HourCount hourDau(String id, String date);
}
