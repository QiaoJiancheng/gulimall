package com.atguigu.gulimall.product.to;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author QiaoJiancheng
 * @create 2023/4/23 11:53
 */
@Data
public class SpuBoundTo {

    private Long spuId;
    private BigDecimal buyBounds;
    private BigDecimal growBounds;
}
