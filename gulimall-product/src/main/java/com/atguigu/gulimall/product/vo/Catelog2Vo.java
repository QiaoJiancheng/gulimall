package com.atguigu.gulimall.product.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author QiaoJiancheng
 * @create 2023/5/2 14:04
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Catelog2Vo {

    private String catalogId;
    private List<Catelog3Vo> catalog3List;
    private String id;
    private String name;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Catelog3Vo {
        private String catalog2Id;
        private String id;
        private String name;
    }
}
