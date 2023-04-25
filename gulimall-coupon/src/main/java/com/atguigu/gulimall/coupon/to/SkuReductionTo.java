package com.atguigu.gulimall.coupon.to;


import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author QiaoJiancheng
 * @create 2023/4/23 11:57
 */
@Data
public class SkuReductionTo {

    private Long skuId;
    private int fullCount;
    private BigDecimal discount;
    private int countStatus;
    private BigDecimal fullPrice;
    private BigDecimal reducePrice;
    private int priceStatus;
    private List<MemberPrice> memberPrice;
}
