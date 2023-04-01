package com.atguigu.gulimall.member.feign;

import com.atguigu.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author QiaoJiancheng
 * @create 2023/4/1 12:38
 */
@FeignClient("gulimall-coupon")
public interface MemberCouponFeign {

    @RequestMapping("/coupon/coupon/coupons")
    public R memberCoupons();
}
