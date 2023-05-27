package com.example.demo.mapper;

import com.example.demo.entity.Userinfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserMapper {

    /**
     * 注册添加user用户
     * @param userinfo 要插入的用户
     * @return 返回受影响的行数
     */
    int insertUser(Userinfo userinfo);

    Userinfo getUserByName(@Param("username") String username);

    Userinfo getAuthorById(@Param("id") Integer id);

    /**
     * 得到该用户的state来判断设置多少的冻结时间
     * @param id 该用户的id
     * @return 返回的state
     */
    int getUserState(@Param("id") Integer id);

    /**
     * 修改用户的 state， freezetime，errornum
     * @param userinfo 要修改的数据用户
     * @return 返回受影响的行数
     */
    int updateUser(Userinfo userinfo);

    /**
     * 修改用户的 errornum
     * @param id 要修改的用户 id
     * @param errornum 要修改的 新的errornum
     */
    void updateUserErrornumState(@Param("id")Integer id, @Param("errornum") Integer errornum, @Param("state") Integer state);

    void updateUserErrornum(@Param("id")Integer id, @Param("errornum") Integer errornum);

}
