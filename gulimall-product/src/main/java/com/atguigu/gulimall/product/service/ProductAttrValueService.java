package com.atguigu.gulimall.product.service;

import com.atguigu.gulimall.product.vo.BaseAttrs;
import com.baomidou.mybatisplus.extension.service.IService;
import com.atguigu.common.utils.PageUtils;
import com.atguigu.gulimall.product.entity.ProductAttrValueEntity;

import java.util.List;
import java.util.Map;

/**
 * spu属性值
 *
 * @author qiaoJiancheng
 * @email qiaoJiancheng@gmail.com
 * @date 2023-03-31 14:43:24
 */
public interface ProductAttrValueService extends IService<ProductAttrValueEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void save(Long id, List<BaseAttrs> baseAttrs);

    List<ProductAttrValueEntity> baseAttrListForSpu(Long spuId);

    void updateBySpuId(Long spuId, List<ProductAttrValueEntity> productAttrValueEntities);
}

