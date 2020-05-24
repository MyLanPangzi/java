package com.hiscat.gmallweb.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author hiscat
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DauTotalVO {
    private String id;
    private String name;
    private Double value;

}
