package com.mmall.web;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
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
 * Description: 前台用户控制层
 * Author: Leo
 * Date: 2020/3/8-2:13
 * email 1437665365@qq.com
 */
@RestController
@Slf4j
@RequestMapping("/user/")
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
    @RequestMapping(value = "logout.do", method = RequestMethod.POST)
    public ServerResponse<String> logout(HttpSession session) {

        session.removeAttribute(Const.CURRENT_USER);

        return ServerResponse.createBySuccess();
    }


    /**
     * @return com.mmall.common.ServerResponse<java.lang.String>
     * @Description 注册
     * @Author Leo
     * @Date 19:30 2020/3/8
     * @Param [user]
     */
    @RequestMapping(value = "register.do", method = RequestMethod.POST)
    public ServerResponse<String> register(User user) {

        ServerResponse<String> response = UserService.register(user);
        return response;
    }

    /**
     * @return com.mmall.common.ServerResponse<java.lang.String>
     * @Description 效验接口
     * @Author Leo
     * @Date 20:14 2020/3/8
     * @Param [str, type]
     */
    @RequestMapping(value = "check_vaild.do", method = RequestMethod.POST)
    public ServerResponse<String> checkVaild(String str, String type) {

        return UserService.checkVaild(str, type);
    }

    /**
     * @return com.mmall.common.ServerResponse<com.mmall.pojo.User>
     * @Description 获取用户的登录信息
     * @Author Leo
     * @Date 20:20 2020/3/8
     * @Param [session]
     */
    @RequestMapping(value = "get_user_info.do", method = RequestMethod.POST)
    public ServerResponse<User> getUserInfo(HttpSession session) {

        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user != null) {
            return ServerResponse.createBySuccess(user);
        }
        return ServerResponse.createBySuccessMessage("用户未登录,无法获取用户的信息");
    }

    /**
     * @return com.mmall.common.ServerResponse<java.lang.String>
     * @Description 找回密码问题
     * @Author Leo
     * @Date 21:10 2020/3/8
     * @Param [username]
     */
    @RequestMapping(value = "forget_get_question.do", method = RequestMethod.POST)
    public ServerResponse<String> forgetGetQuestion(String username) {

        return UserService.selectQuestion(username);
    }

    /**
     * @return com.mmall.common.ServerResponse<java.lang.String>
     * @Description 找回密码答案, 并把Token放入缓存
     * @Author Leo
     * @Date 21:50 2020/3/8
     * @Param [username, question, answer]
     */
    @RequestMapping(value = "forget_check_answer.do", method = RequestMethod.POST)
    public ServerResponse<String> forgetCacheAnswer(String username, String question, String answer) {

        return UserService.selectAnswer(username, question, answer);
    }

    /**
     * @return com.mmall.common.ServerResponse<java.lang.String>
     * @Description 重置密码
     * @Author Leo
     * @Date 22:53 2020/3/8
     * @Param [username, passwordNew, forgetToken]
     */
    @RequestMapping(value = "forget_rest_password.do", method = RequestMethod.POST)
    public ServerResponse<String> forgetRestPassword(String username, String passwordNew, String forgetToken) {

        return UserService.forgetRestPassword(username, passwordNew, forgetToken);
    }

    /**
     * @return com.mmall.common.ServerResponse<java.lang.String>
     * @Description 登录状态重置密码
     * @Author Leo
     * @Date 22:55 2020/3/8
     * @Param [session, passwordNew, passwordOld]
     */
    @RequestMapping(value = "reset_password.do", method = RequestMethod.POST)
    public ServerResponse<String> resetPassword(HttpSession session, String passwordNew, String passwordOld) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorMessage("用户未登录");
        }

        return UserService.restPassord(passwordNew, passwordOld, user);
    }


    /**
     * @return com.mmall.common.ServerResponse<com.mmall.pojo.User>
     * @Description 更新用户信息
     * @Author Leo
     * @Date 23:33 2020/3/8
     * @Param [session, user]
     */
    @RequestMapping(value = "update_information.do", method = RequestMethod.POST)
    public ServerResponse<User> updateInformation(HttpSession session, User user) {

        User currentUser = (User) session.getAttribute(Const.CURRENT_USER);
        if (currentUser == null) {
            return ServerResponse.createByErrorMessage("用户未登录");
        }
        user.setId(currentUser.getId());
        user.setUsername(currentUser.getUsername());
        ServerResponse<User> response = UserService.updateInformation(user);
        if (response.isSuccess()) {
            session.setAttribute(Const.CURRENT_USER, response.getData());
        }

        return response;
    }

    /**
     * @return com.mmall.common.ServerResponse<com.mmall.pojo.User>
     * @Description //获取用户登录信息
     * @Author Leo
     * @Date 0:21 2020/3/9
     * @Param [session]
     */
    @RequestMapping(value = "get_information.do", method = RequestMethod.POST)
    public ServerResponse<User> getInformation(HttpSession session) {
        User currentUser = (User) session.getAttribute(Const.CURRENT_USER);
        if (currentUser == null) {
            //需要强制登录
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),
                    "未登录,需要强制登录,status=10");
        }
        return UserService.getInformation(currentUser.getId());
    }


}


