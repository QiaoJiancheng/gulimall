package com.atguigu.product;


import com.atguigu.gulimall.product.ProductApplication;
import com.atguigu.gulimall.product.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ProductApplication.class)
public class ProductApplicationTests {

    @Autowired
    private CategoryService categoryService;

    @Test
    public void testFindPath() {
        Long[] categoryPath = categoryService.findCategoryPath(225L);
        log.info("完整路径：{}", Arrays.asList(categoryPath));
    }

}
