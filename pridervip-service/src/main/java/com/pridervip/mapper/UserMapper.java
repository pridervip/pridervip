package com.pridervip.mapper;
import com.pridervip.entity.User;
import org.apache.ibatis.annotations.Param;

public interface UserMapper {
    //根据用户名和密码查找。mybatis中有多个参数时，需要使用@Param注解
    User findByUserNameAndPassword(@Param("phone_mobile") String phone_mobile, @Param("login_password") String login_password);
    //增加用户
    void  addUser(User user);
    //根据用户名查询
    User judgeUser(String phone_mobile);

}
