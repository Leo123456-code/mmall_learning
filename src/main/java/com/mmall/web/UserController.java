package com.mmall.web;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mmall.common.Const;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;
import com.mmall.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

/**
 * ClassName: UserController
 * Description: TODO
 * Author: Leo
 * Date: 2020/3/8-2:13
 * email 1437665365@qq.com
 */
@RestController
@Slf4j
@RequestMapping("/user")
public class UserController {

    private Gson gson = new GsonBuilder().setPrettyPrinting().create();
    @Autowired
    private IUserService UserService;

    /**
     * 登录
     *
     * @param username
     * @param password
     * @param session
     * @return
     */
    @RequestMapping(value = "login.do", method = RequestMethod.POST)
    public ServerResponse<User> login(String username, String password, HttpSession session) {

        ServerResponse<User> response = UserService.login(username, password);

        if (response.isSuccess()) {
            session.setAttribute(Const.CURRENT_USER, response.getData());
        }
        return response;
    }


    /**
     * 登出
     * 移除session
     *
     * @param session
     * @return
     */
    @RequestMapping(value = "logout.do", method = RequestMethod.GET)
    public ServerResponse<String> logout(HttpSession session) {

        session.removeAttribute(Const.CURRENT_USER);

        return ServerResponse.createBySuccess();
    }


}


