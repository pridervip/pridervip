package com.pridervip.service.impl;



import com.pridervip.entity.User;
import com.pridervip.mapper.UserMapper;
import com.pridervip.service.UserService;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserServiceImpl implements UserService{
@Autowired
    UserMapper userMapper;
    /**
     * 登录
     * 根据用户名和密码进行查询
     */
    @Override
    public User login(String phone_mobile, String login_password) {
        return userMapper.findByUserNameAndPassword(phone_mobile, login_password);
    }
    /**
     * 注册
     * 增加用户
     */
    @Override
    public void register(User user) {
        userMapper.addUser(user);
    }
    /**
     * 根据用户名查询
     */
    @Override
    public User findByUserName(String phone_mobile) {

        return userMapper.judgeUser(phone_mobile);
    }
}
