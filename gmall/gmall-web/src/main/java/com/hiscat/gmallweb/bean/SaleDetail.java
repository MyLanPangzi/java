package com.hiscat.gmallweb.bean;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author hiscat
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SaleDetail {
    private Long total;
    private List<Stat> stat;
    private List<Detail> detail;
}
