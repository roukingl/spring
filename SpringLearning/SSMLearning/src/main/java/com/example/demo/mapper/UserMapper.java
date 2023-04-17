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
     * 通过倒序id查询元素
     * @param sort 倒序的关键字 使用$
     * @return 返回所有查询的元素
     */
    List<Userinfo> getUserAllByIdSortDesc(@Param("sort") String sort);

    /**
     * 插入元素
     * @param userinfo 要插入的元素
     * @return 返回受影响的行数
     */
    int addUser(Userinfo userinfo);

    /**
     * 添加元素并返回 id
     * @param userinfo 添加的元素
     * @return 返回受影响的行数
     */
    int addUserGetId(Userinfo userinfo);

    /**
     * 修改元素并返回受影响的行数
     * @param userinfo 添加的元素
     * @return 返回受影响的行数
     */
    int updateUserName(Userinfo userinfo);

    /**
     * 删除 id 的 元素
     * @param id 要删除元素的 id
     * @return 返回受影响的行数
     */
    int deleteById(@Param("id") Integer id);

    List<Userinfo> getUserByName(@Param("username") String username);

    int addUser2(Userinfo userinfo);

    int addUser3(Userinfo userinfo);

    int deleteUsersById(List<Integer> ids);
}
