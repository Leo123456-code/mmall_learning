package com.mmall.dao;

import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;
import org.apache.ibatis.annotations.Param;

public interface UserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Integer id);


    int updateByPrimaryKey(User record);

    //查询名字是否唯一
    int checkUsername(String username);

    //根据姓名和密码查询是否存在
    User selectLogin(@Param("username") String username, @Param("password") String password);

    //查询邮箱是否唯一
    int checkEmail(String email);

    //根据名字查找问题
    String selectQuestionByUsername(String username);

    //根据用户名,密码，邮箱查找答案
    //如果答案的个数大于0，表示答案正确
    int checkAnswer(@Param("username") String username,
                    @Param("question") String question, @Param("answer") String answer);

    //重置密码
    int updatePasswordByUsername(@Param("username") String username, @Param("passwordNew") String passwordNew);

    //根据userId和Password 查询是否存在
    int checkPasswordByUserId(@Param("password") String password, @Param("userId") Integer userId);

    //更新密码
    int updateByPrimaryKeySelective(User record);

    //查找是否有相同的email
    int checkEmailByUserId(@Param("email") String email, @Param("userId") Integer userId);

    //获取用户信息
    User selectInformationByUserId(@Param("userId") Integer userId);
}