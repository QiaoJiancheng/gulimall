package com.atguigu.gulimall.product;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/*
*   1、导入依赖：mybatis-plus
*
*   2、配置
*       配置数据源：导入数据库驱动 + 在application.yaml中配置数据源相关信息
*       配置mybatis-plus：配置Mapper文件位置(MapperScan) + Mapper映射文件位置
* */
@MapperScan("com.atguigu.gulimall.product.dao")
@SpringBootApplication
@EnableDiscoveryClient
public class ProductApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProductApplication.class, args);
    }

}
