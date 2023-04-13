package com.example.demo.mapper;

import com.example.demo.entity.Userinfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserMapper {

    /**
     * 通过 id来查找用户
     * @param id 用户id
     * @return 返回这个用户
     */
    Userinfo getUserById(@Param("userId") Integer id);

    /**
     * 查询全部用户
     * @return 用户List
     */
    List<Userinfo> getUserAll();

    /**
     * 插入元素
     * @param userinfo 要插入的元素
     * @return 返回受影响的行数
     */
    int addUser(Userinfo userinfo);
}
