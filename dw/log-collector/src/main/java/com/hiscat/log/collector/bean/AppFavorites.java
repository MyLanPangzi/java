package com.hiscat.log.collector.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**收藏
 * @author hiscat
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppFavorites {
    private int id;//主键
    private int course_id;//商品id
    private int userid;//用户ID
    private String add_time;//创建时间
}
