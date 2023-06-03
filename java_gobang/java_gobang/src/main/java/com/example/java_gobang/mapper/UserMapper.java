package com.example.java_gobang.mapper;

import com.example.java_gobang.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserMapper {

    /**
     * 添加一条用户信息
     * @param user 要添加的用户
     * @return 返回受影响的行数
     */
    int insertUser(User user);

    /**
     * 根据用户名查询用户信息
     * @param username 用户名
     * @return 返回查询到的用户信息
     */
    User selectUserByName(@Param("username") String username);

}
