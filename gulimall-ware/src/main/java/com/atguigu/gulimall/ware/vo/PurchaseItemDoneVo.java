package com.atguigu.gulimall.ware.vo;

import lombok.Data;

/**
 * @author QiaoJiancheng
 * @create 2023/4/25 12:03
 */
@Data
public class PurchaseItemDoneVo {

    private Long itemId;
    private Integer status;
    private String reason;
}
