package com.atguigu.product;


import com.atguigu.gulimall.product.ProductApplication;
import com.atguigu.gulimall.product.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.UUID;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ProductApplication.class)
public class ProductApplicationTests {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Test
    public void testFindPath() {
        Long[] categoryPath = categoryService.findCategoryPath(225L);
        log.info("完整路径：{}", Arrays.asList(categoryPath));
    }

    @Test
    public void testSpringRedisTemplate() {
        ValueOperations<String, String> ops = stringRedisTemplate.opsForValue();
        ops.set("hello", UUID.randomUUID().toString());

        log.info("之前保存的数据：{}", ops.get("hello"));
    }

    @Autowired
    private RedissonClient redissonClient;

    @Test
    public void redisson() {
        log.info("{}", redissonClient);
    }

    @Test
    public void redissonTest() {
        RLock lock = redissonClient.getLock("my-lock");
        lock.lock();
        try {
            log.info("加锁成功，执行业务。加锁线程ID是：{}",Thread.currentThread().getId());
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }finally {
            lock.unlock();
        }
    }

}
