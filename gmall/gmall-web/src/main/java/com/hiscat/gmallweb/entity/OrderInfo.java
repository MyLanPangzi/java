package com.hiscat.gmallweb.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("GMALL2020_ORDER_INFO")
public class OrderInfo {
    @TableField
    private String province_id;
    @TableField
    private String consignee;
    @TableField
    private String order_comment;
    @TableField
    private String consignee_tel;
    @TableField
    private String order_status;
    @TableField
    private String payment_way;
    @TableField
    private String user_id;
    @TableField
    private String img_url;
    @TableField
    private Double total_amount;
    @TableField
    private String expire_time;
    @TableField
    private String delivery_address;
    @TableField
    private String create_time;
    @TableField
    private String operate_time;
    @TableField
    private String tracking_no;
    @TableField
    private String parent_order_id;
    @TableField
    private String out_trade_no;
    @TableField
    private String trade_body;
    @TableField
    private String create_date;
    @TableField
    private String create_hour;
}
