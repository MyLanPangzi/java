package com.hiscat.gmallweb.controller;

import com.hiscat.gmallweb.bean.SaleDetail;
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

    WebService webService;

    @GetMapping("/realtime-total")
    public List<DauTotalVO> total(String date) {
        return webService.getTotalDau(date);
    }

    @GetMapping("/realtime-hours")
    public HourCount hourDau(String id, String date) {
        return webService.hourDau(id, date);
    }

    @GetMapping("/sale_detail")
    public SaleDetail saleDetail(SaleDetailQuery query) {
        return this.webService.saleDetail(query);
    }
}
