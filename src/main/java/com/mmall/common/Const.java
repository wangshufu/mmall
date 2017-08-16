package com.mmall.common;

import com.google.common.collect.Sets;

import java.util.Set;

/**
 * Created by wangshufu on 2017/7/27.
 */
public class Const {

    public static final String CURRENT_USER = "currentUser";
    public static final String EMAIL = "email";
    public static final String USERNAME = "username";

    public interface Cart{
        int CHECKED = 1;//即购物车选中状态
        int UN_CHECKED = 0;//购物车中未选中状态

        String LIMIT_NUM_FAIL = "LIMIT_NUM_FAIL";
        String LIMIT_NUM_SUCCESS = "LIMIT_NUM_SUCCESS";
    }

    public interface ProductListOrderBy{
        Set<String> PRICE_ASC_DESC = Sets.newHashSet("price_desc","price_asc");
    }

    public interface PropertiesKey{
        //密码加盐
        public static final String password_salt = "password.salt";
        //ftp服务器前缀
        public static final String ftp_server_http_prefix = "ftp.server.http.prefix";
        //支付宝回调url
        public static final String alipay_callback_url = "alipay.callback.url";
    }


    public enum ProductStatusEnum{
        ON_SALE(1,"在线");
        private String value;
        private int code;
        ProductStatusEnum(int code,String value){
            this.code = code;
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        public int getCode() {
            return code;
        }
    }


    //角色
    public interface Role{
        int ROLE_CUSTOMER = 0; //普通用户
        int ROLE_ADMIN = 1;//管理员
    }
}
