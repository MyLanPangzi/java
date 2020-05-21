package com.hiscat.gmallweb.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author hiscat
 */
@Data
@TableName("GMALL2020_DAU")
public class StartupLog {
    @TableId
    private String mid;
    @TableField
    private String uid;
    @TableField
    private String appid;
    @TableField
    private String area;
    @TableField
    private String os;
    @TableField
    private String ch;
    @TableField
    private String type;
    @TableField
    private String vs;
    @TableId("LOGDATE")
    private String logdate;
    @TableField("LOGHOUR")
    private String loghour;
    @TableField
    private Long ts;
}
