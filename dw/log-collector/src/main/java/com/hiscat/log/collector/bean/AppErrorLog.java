package com.hiscat.log.collector.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author hiscat
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppErrorLog {

    private String errorBrief;    //错误摘要
    private String errorDetail;   //错误详情
}
