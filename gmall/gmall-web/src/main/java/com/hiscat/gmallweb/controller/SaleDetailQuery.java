package com.hiscat.gmallweb.controller;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author hiscat
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SaleDetailQuery {
    private String date;
    private Integer startpage;
    private Integer size;
    private String keyword;
}
