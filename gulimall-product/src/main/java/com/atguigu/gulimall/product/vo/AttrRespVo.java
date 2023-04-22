package com.atguigu.gulimall.product.vo;

import lombok.Data;

/**
 * @author QiaoJiancheng
 * @create 2023/4/20 13:48
 */
@Data
public class AttrRespVo extends AttrVo{

    private String catelogName;
    private String groupName;
    private Long[] catelogPath;
}
