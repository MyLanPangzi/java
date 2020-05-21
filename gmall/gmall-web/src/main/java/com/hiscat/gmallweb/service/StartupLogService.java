package com.hiscat.gmallweb.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hiscat.gmallweb.entity.StartupLog;
import com.hiscat.gmallweb.vo.DauTotalVO;

import java.util.List;

/**
 * @author hiscat
 */
public interface StartupLogService  extends IService<StartupLog> {

    /**
     * @param date 日期
     * @return 日活列表
     */
    List<DauTotalVO> getTotalDau(String date);
}
