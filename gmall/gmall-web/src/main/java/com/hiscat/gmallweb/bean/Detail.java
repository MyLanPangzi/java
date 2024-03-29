package com.hiscat.gmallweb.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.elasticsearch.annotations.Document;

/**
 * @author hiscat
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(indexName = "gmall2020_sale_detail-query",type = "_doc")
public class Detail {
    private String order_detail_id;
    private String order_id;
    private String order_status;
    private String create_time;
    private String user_id;
    private String sku_id;
    private String user_gender;
    private Integer user_age;
    private String user_level;
    private Double sku_price;
    private String sku_name;
    private String dt;


}
