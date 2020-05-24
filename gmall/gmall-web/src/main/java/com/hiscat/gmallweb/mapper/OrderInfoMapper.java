package com.hiscat.gmallweb.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hiscat.gmallweb.entity.OrderInfo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * @author hiscat
 */

public interface OrderInfoMapper extends BaseMapper<OrderInfo> {
    /**
     * 获取指定日期交易总额
     *
     * @param date 日期
     * @return 累计交易额
     */
    @Select("select sum(total_amount) from gmall2020_order_info where create_date=#{date}")
    Double getDauTotal(@Param("date") String date);
}
