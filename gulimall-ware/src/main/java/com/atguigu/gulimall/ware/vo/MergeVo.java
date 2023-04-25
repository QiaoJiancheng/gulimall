package com.atguigu.gulimall.ware.vo;

import lombok.Data;

import java.util.List;

/**
 * @author QiaoJiancheng
 * @create 2023/4/24 11:12
 */
@Data
public class MergeVo {

    private Long purchaseId;
    private List<Long> items;
}
