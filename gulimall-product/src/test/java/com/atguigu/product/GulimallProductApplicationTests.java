package com.atguigu.product;


import com.atguigu.gulimall.product.GulimallProductApplication;
import com.atguigu.gulimall.product.entity.BrandEntity;
import com.atguigu.gulimall.product.service.BrandService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = GulimallProductApplication.class)
public class GulimallProductApplicationTests {

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
        brandEntity.setBrandId(1L);
        brandEntity.setDescript("修改后的手机品牌描述");
        brandService.updateById(brandEntity);
    }

}
