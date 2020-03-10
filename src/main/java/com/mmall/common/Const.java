package com.mmall.common;

/**
 * ClassName: Const
 * Description: 常量类
 * Author: Leo
 * Date: 2020/3/8-3:47
 * email 1437665365@qq.com
 */
public class Const {

    public static final String CURRENT_USER = "currentUser";

    public static final String USERNAME = "username";

    public static final String EMAIL = "email";

    public interface Role {
        int ROLE_CUSTOMER = 0; //普通用户
        int ROLE_ADMIN = 1;//管理员
    }


}
