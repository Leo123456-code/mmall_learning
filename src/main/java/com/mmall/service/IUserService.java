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

    //注册
    ServerResponse<String> register(User user);

    //效验
    ServerResponse<String> checkVaild(String str, String type);

    //找回密码返回密码接口
    ServerResponse<String> selectQuestion(String username);

    //找回密码答案
    ServerResponse<String> selectAnswer(String username, String question, String answer);

    //重置密码(传入用户名,新密码,Token)
    ServerResponse<String> forgetRestPassword(String username, String passwordNew, String forgetToken);

    //登录状态重置密码
    ServerResponse<String> restPassord(String passwordNew, String passwordOld, User user);

    //更新用户信息
    ServerResponse<User> updateInformation(User user);

    //获取用户信息
    ServerResponse<User> getInformation(Integer userId);


}
