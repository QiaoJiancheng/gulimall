package com.atguigu.gulimall.member;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@MapperScan("com.atguigu.gulimall.member.dao")
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients("com.atguigu.gulimall.member.feign")
public class MemberApplication {

    public static void main(String[] args) {
        SpringApplication.run(MemberApplication.class, args);
    }

}

/**
 * 远程调用测试：
 *  1、导入 openfeign
 *  2、引导类开启feign功能：@EnableFeignClients("这里面是扫描的feign接口所在的位置")
 *  3、由于feign是声明式，编写一个接口并标注 @FeignClient("这里面是需要调用的远程服务的应用名称")
 *  4、将远程服务的 controller方法复制，并补全url链接，以 / 开始
 */
