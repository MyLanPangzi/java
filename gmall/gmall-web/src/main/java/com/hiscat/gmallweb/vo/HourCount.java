package com.hiscat.gmallweb.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * @author hiscat
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HourCount {
    private Map<String, Object> yesterday;
    private Map<String, Object> today;
}
