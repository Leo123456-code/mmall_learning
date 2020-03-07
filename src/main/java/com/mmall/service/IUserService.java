package com.mmall.service;

import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;

/**
 * ClassName: IUserService
 * Description: User用户接口
 * Author: Leo
 * Date: 2020/3/8-2:23
 * email 1437665365@qq.com
 */
public interface IUserService {
    //登录接口
    ServerResponse<User> login(String username, String password);
}
