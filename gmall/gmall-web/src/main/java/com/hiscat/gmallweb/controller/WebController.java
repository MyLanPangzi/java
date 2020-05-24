package com.hiscat.gmallweb.controller;

import com.hiscat.gmallweb.service.WebService;
import com.hiscat.gmallweb.vo.DauTotalVO;
import com.hiscat.gmallweb.vo.HourCount;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author hiscat
 */
@RestController
@AllArgsConstructor
public class WebController {

    WebService startupLogService;

    @GetMapping("/realtime-total")
    public List<DauTotalVO> total(String date) {
        return startupLogService.getTotalDau(date);
    }

    @GetMapping("/realtime-hours")
    public HourCount hourDau(String id, String date) {
        return startupLogService.hourDau(id, date);
    }

}
