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
public class AppActiveBackground {
    private String activeSource;//1=upgrade,2=download(下载),3=plugin_upgrade
}
