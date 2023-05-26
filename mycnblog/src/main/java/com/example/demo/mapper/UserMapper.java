package com.example.demo.mapper;

import com.example.demo.entity.Userinfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserMapper {

    /**
     * 注册添加user用户
     * @param userinfo
     * @return 返回受影响的行数
     */
    int insertUser(Userinfo userinfo);

    Userinfo getUserByName(@Param("username") String username);

    Userinfo getAuthorById(@Param("id") Integer id);
}
