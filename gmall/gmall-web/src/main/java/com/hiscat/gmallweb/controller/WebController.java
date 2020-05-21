package com.hiscat.gmallweb.controller;

import com.hiscat.gmallweb.service.StartupLogService;
import com.hiscat.gmallweb.vo.DauTotalVO;
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

    StartupLogService startupLogService;

    @GetMapping("/realtime-total")
    public List<DauTotalVO> total(String date) {
        return startupLogService.getTotalDau(date);
    }
}
