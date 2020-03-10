package com.mmall.service.impl;

import com.mmall.common.Const;
import com.mmall.common.ServerResponse;
import com.mmall.common.TokenCache;
import com.mmall.dao.UserMapper;
import com.mmall.pojo.User;
import com.mmall.service.IUserService;
import com.mmall.util.MD5Util;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * ClassName: UserServiceImpl
 * Description: TODO
 * Author: Leo
 * Date: 2020/3/8-2:24
 * email 1437665365@qq.com
 */
@Service("userService")
@Slf4j
public class UserServiceImpl implements IUserService {
    @Autowired
    private UserMapper userMapper;

    /**
     * @return java.lang.Object
     * @Description 登录实现类
     * @Author Leo
     * @Date 2:27 2020/3/8
     * @Param [username, password]
     */
    @Override
    public ServerResponse<User> login(String username, String password) {
        int count = userMapper.checkUsername(username);
        if (count == 0) {
            return ServerResponse.createByErrorMessage("用户名不存在");
        }
        ////TODO 密码登录
        String md5Password = MD5Util.MD5EncodeUtf8(password);
        User user = userMapper.selectLogin(username, md5Password);
        if (user == null) {
            return ServerResponse.createByErrorMessage("用户名或密码错误");
        }
        user.setPassword(StringUtils.EMPTY);
        return ServerResponse.createBySuccess("登录成功", user);
    }

    /**
     * @return com.mmall.common.ServerResponse<java.lang.String>
     * @Description 注册实现类
     * @Author Leo
     * @Date 19:15 2020/3/8
     * @Param [user]
     */
    @Override
    public ServerResponse<String> register(User user) {
//        int resultCount = userMapper.checkUsername(user.getUsername());
//        if(resultCount > 0){
//            return ServerResponse.createByErrorMessage("用户名已存在");
//        }
//        int emailCount = userMapper.checkEmail(user.getEmail());
//        if(emailCount > 0){
//            return ServerResponse.createByErrorMessage("邮箱已存在");
//        }
        ServerResponse vaildResponse = this.checkVaild(user.getUsername(), Const.USERNAME);
        if (!vaildResponse.isSuccess()) {
            return vaildResponse;
        }
        vaildResponse = this.checkVaild(user.getEmail(), Const.EMAIL);
        if (!vaildResponse.isSuccess()) {
            return vaildResponse;
        }

        //设置角色为普通用户
        user.setRole(Const.Role.ROLE_CUSTOMER);
        //颜值加密
        user.setPassword(MD5Util.MD5EncodeUtf8(user.getPassword()));
        int selective = userMapper.insertSelective(user);
        if (selective == 0) {
            return ServerResponse.createByErrorMessage("注册失败");
        }

        return ServerResponse.createBySuccess("注册成功");
    }

    /**
     * @return com.mmall.common.ServerResponse<java.lang.String>
     * @Description //当用户不存在时返回校验成功
     * @Author Leo
     * @Date 19:52 2020/3/8
     * @Param [str, type] type 参数类别,str 实际参数
     */
    @Override
    public ServerResponse<String> checkVaild(String str, String type) {
        //判断是否有空字符串
        if (StringUtils.isNotBlank(type)) {
            //开始校验
            if (Const.USERNAME.equals(type)) {
                int resultCount = userMapper.checkUsername(str);
                if (resultCount > 0) {
                    return ServerResponse.createByErrorMessage("用户名已存在");
                }
            }
            if (Const.EMAIL.equals(type)) {
                int emailCount = userMapper.checkEmail(str);
                if (emailCount > 0) {
                    return ServerResponse.createByErrorMessage("邮箱已存在");
                }
            }
        } else {
            return ServerResponse.createByErrorMessage("参数错误");
        }
        return ServerResponse.createBySuccessMessage("校验成功");
    }

    /**
     * @return com.mmall.common.ServerResponse<com.mmall.pojo.User>
     * @Description //把问题返回给前端
     * @Author Leo
     * @Date 20:49 2020/3/8
     * @Param [username]
     */
    @Override
    public ServerResponse<String> selectQuestion(String username) {
        ServerResponse<String> vaildResponse = this.checkVaild(username, Const.USERNAME);
        if (vaildResponse.isSuccess()) {
            //用户不存在
            return ServerResponse.createByErrorMessage("用户不存在");
        }
        //查找question
        String question = userMapper.selectQuestionByUsername(username);
        if (StringUtils.isNotBlank(question)) {
            return ServerResponse.createBySuccess(question);
        }

        return ServerResponse.createByErrorMessage("找回密码的问题是空的");
    }

    /**
     * @return com.mmall.common.ServerResponse<java.lang.String>
     * @Description 找回密码答案
     * @Author Leo
     * @Date 21:14 2020/3/8
     * @Param [username, password, email]
     */
    @Override
    public ServerResponse<String> selectAnswer(String username, String question, String answer) {
        int resultCount = userMapper.checkAnswer(username, question, answer);
        if (resultCount > 0) {
            //说明问题及这个问题的答案是这个用户的，并且是正确的
            String forgetToken = UUID.randomUUID().toString();
            //Token 放入缓存
            TokenCache.setKey(TokenCache.TOKEN_PREFIX + username, forgetToken);
            return ServerResponse.createBySuccess(forgetToken);
        }

        return ServerResponse.createByErrorMessage("问题答案错误");
    }

    /**
     * @return com.mmall.common.ServerResponse<java.lang.String>
     * @Description 重置密码
     * @Author Leo
     * @Date 21:57 2020/3/8
     * @Param [username, passwordNew, forgetToken]
     */
    @Override
    public ServerResponse<String> forgetRestPassword(String username, String passwordNew, String forgetToken) {
        if (StringUtils.isBlank(forgetToken)) {
            return ServerResponse.createByErrorMessage("参数错误,Token 需要传递");
        }
        ServerResponse<String> vaildResponse = this.checkVaild(username, Const.USERNAME);
        if (vaildResponse.isSuccess()) {
            return ServerResponse.createByErrorMessage("用户不存在");
        }
        String token = TokenCache.getKey(TokenCache.TOKEN_PREFIX + username);
        if (StringUtils.isBlank(token)) {
            return ServerResponse.createByErrorMessage("Token无效或过期");
        }
        //equals判断
        if (StringUtils.equals(forgetToken, token)) {
            String md5Password = MD5Util.MD5EncodeUtf8(passwordNew);
            int passwordCount = userMapper.updatePasswordByUsername(username, md5Password);
            if (passwordCount > 0) {
                return ServerResponse.createBySuccess("修改密码成功");
            }
        } else {
            return ServerResponse.createByErrorMessage("Token错误,请重新获取重置密码的Token");
        }
        return ServerResponse.createByErrorMessage("修改密码失败");
    }

    /**
     * @return com.mmall.common.ServerResponse<java.lang.String>
     * @Description //登录状态重置密码
     * @Author Leo
     * @Date 23:01 2020/3/8
     * @Param [passwordNew, passwordOld, user]
     */
    @Override
    public ServerResponse<String> restPassord(String passwordNew, String passwordOld, User user) {
        //防止横向越权,要校验一下这个用户的旧密码,一定要指定是这个用户
        int passwordCount = userMapper.checkPasswordByUserId(MD5Util.MD5EncodeUtf8(passwordOld), user.getId());
        if (passwordCount == 0) {
            return ServerResponse.createByErrorMessage("旧密码错误");
        }
        user.setPassword(MD5Util.MD5EncodeUtf8(passwordNew));
        int updateCount = userMapper.updateByPrimaryKeySelective(user);
        if (updateCount > 0) {
            return ServerResponse.createBySuccess("密码更新成功");
        }
        return ServerResponse.createByErrorMessage("密码更新失败");
    }

    /**
     * @return com.mmall.common.ServerResponse<com.mmall.pojo.User>
     * @Description //更新用户信息
     * @Author Leo
     * @Date 23:37 2020/3/8
     * @Param [user]
     */
    @Override
    public ServerResponse<User> updateInformation(User user) {
        //username不能被更新
        //email不能和别人相同
        int emailCount = userMapper.checkEmailByUserId(user.getEmail(), user.getId());
        if (emailCount > 0) {
            return ServerResponse.createByErrorMessage("你的邮箱和别人相同,请更换");
        }

        User updateUser = new User();
        updateUser.setId(user.getId());
        updateUser.setEmail(user.getEmail());
        updateUser.setPhone(user.getPhone());
        updateUser.setQuestion(user.getQuestion());
        updateUser.setAnswer(user.getAnswer());

        int updateCount = userMapper.updateByPrimaryKeySelective(updateUser);
        if (updateCount > 0) {
            return ServerResponse.createBySuccessMessage("个人信息更新成功");
        }

        return ServerResponse.createByErrorMessage("个人信息更新失败");
    }

    /**
     * @return com.mmall.common.ServerResponse<com.mmall.pojo.User>
     * @Description //获取用户信息
     * @Author Leo
     * @Date 0:11 2020/3/9
     * @Param [userId]
     */
    @Override
    public ServerResponse<User> getInformation(Integer userId) {
        User user = userMapper.selectInformationByUserId(userId);
        if (user == null) {
            return ServerResponse.createByErrorMessage("找不到当前用户,请注册");
        }
        //密码隐藏
        user.setPassword(StringUtils.EMPTY);

        return ServerResponse.createBySuccess(user);
    }


}
