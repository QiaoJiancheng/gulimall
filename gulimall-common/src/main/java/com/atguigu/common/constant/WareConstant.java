package com.atguigu.common.constant;

/**
 * @author QiaoJiancheng
 * @create 2023/4/24 11:17
 */
public class WareConstant {

    public enum PurchaseStatusEnum {
        CREATED(0, "新建"),
        ASSIGNED(1, "已分配"),
        RECEIVE(2, "已领取"),
        FINISH(3, "已完成"),
        HASERROR(4, "有错误");

        private Integer code;
        private String message;

        PurchaseStatusEnum(Integer code, String message) {
            this.code = code;
            this.message = message;
        }

        public Integer getCode() {
            return code;
        }

        public String getMessage() {
            return message;
        }
    }

    public enum PurchaseDetailStatusEnum {
        CREATED(0, "新建"),
        ASSIGNED(1, "已分配"),
        BUYING(2, "正在采购"),
        FINISH(3, "已完成"),
        HASERROR(4, "采购失败");

        private Integer code;
        private String message;

        PurchaseDetailStatusEnum(Integer code, String message) {
            this.code = code;
            this.message = message;
        }

        public Integer getCode() {
            return code;
        }

        public String getMessage() {
            return message;
        }
    }
}
