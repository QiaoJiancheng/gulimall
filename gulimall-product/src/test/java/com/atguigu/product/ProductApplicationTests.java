package com.atguigu.product;


import com.atguigu.gulimall.product.ProductApplication;
import com.atguigu.gulimall.product.entity.BrandEntity;
import com.atguigu.gulimall.product.service.BrandService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ProductApplication.class)
public class ProductApplicationTests {

    @Autowired
    BrandService brandService;

    @Test
     public void contextLoads() {

        BrandEntity brandEntity = new BrandEntity();

        // 添加品牌
        // brandEntity.setDescript("手机品牌");
        // brandEntity.setName("华为");
        // brandService.save(brandEntity);

        // 修改品牌描述
        // brandEntity.setBrandId(1L);
        // brandEntity.setDescript("修改后的手机品牌描述");
        // brandService.updateById(brandEntity);

        // 查询
        List<BrandEntity> brandEntityList = brandService.list(new QueryWrapper<BrandEntity>().eq("brand_id", 1L));
        brandEntityList.forEach( (item) -> System.out.println(item));
    }

}
