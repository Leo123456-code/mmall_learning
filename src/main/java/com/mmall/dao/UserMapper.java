package com.mmall.dao;

import com.mmall.pojo.User;

public interface UserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);
    //查询名字是否唯一
    int checkUsername(String username);
    //根据姓名和密码查询是否存在
    User selectLogin(String username,String password);
}