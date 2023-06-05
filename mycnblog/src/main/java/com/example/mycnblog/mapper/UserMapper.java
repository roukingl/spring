package com.example.mycnblog.mapper;

import com.example.mycnblog.entity.Userinfo;
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

    /**
     * 根据用户名来查找用户，用来对接登录功能
     * @param username 需要查找的用户名
     * @return 返回查找到的用户
     */
    Userinfo getUserByName(@Param("username") String username);

    /**
     * 根据用户 id 来查找用户，用来对接展示作者信息功能
     * @param id 要查找的作者 id
     * @return 返回查找到作者的 id
     */
    Userinfo getAuthorById(@Param("id") Integer id);

    /**
     * 修改用户的 state， freeze_time，error_number, 输错五次后继续输入，修改冻结时间，增加等级，重置输入错误次数
     * @param userinfo 登录成功后要修改的用户及其数据
     * @return 返回受影响的行数
     */
    int updateUserLoginParameters(Userinfo userinfo);

    /**
     * 修改用户的 error_number和 state , 当用户输入密码错误超过五次时，需要提升用户等级和重置输入错误次数
     * @param id 要修改的用户 id
     * @param error_number 要修改的新的 error_number
     * @param state 要修改的新的 state
     */
    int updateUserError_numberState(@Param("id")Integer id, @Param("error_number") Integer error_number, @Param("state") Integer state);

    /**
     * 修改用户的 error_number，当用户输入错误时就要修改用户的输入错误次数
     * @param id 输入错误密码的用户 id
     * @param error_number 要修改的 error_number
     */
    int updateUserError_number(@Param("id")Integer id, @Param("error_number") Integer error_number);

    /**
     * 根据用户 id 来返回该用户数据库存储的加盐密码
     * @param id 用户id
     * @return 返回该用户数据库密码
     */
    String selectUserPassword(@Param("id") Integer id);

    /**
     * 根据用户 id 修改用户的密码
     * @param id 用户id
     * @param finalPassword 要修改的数据库最终密码
     * @return 返回受影响的行数
     */
    int updateUserPassword(@Param("id") Integer id, @Param("finalPassword") String finalPassword);

    // TODO 修改用户头像
}
