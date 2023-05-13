package com.atguigu.gulimall.product.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

/**
 * @author QiaoJiancheng
 * @create 2023/5/5 22:34
 */
@Configuration
public class MyRedisConfig {

    @Bean(destroyMethod = "shutdown")
    public RedissonClient redisson() throws IOException {
        Config config = new Config();
        config.useSingleServer().setAddress("redis://192.168.221.128:6379");
        RedissonClient redissonClient = Redisson.create(config);
        return redissonClient;
    }
}
