package com.example.java_gobang.mapper;

import com.example.java_gobang.entity.User;
import com.example.java_gobang.entity.vo.UserVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.ArrayList;

@Mapper
public interface UserMapper {

    /**
     * 添加一条用户信息
     * @param user 要添加的用户
     * @return 返回受影响的行数
     */
    int insertUser(User user);

    /**
     * 根据用户名查询用户信息, 用于登录验证
     * @param username 用户名
     * @return 返回查询到的用户信息
     */
    User selectUserByName(@Param("username") String username);

    /**
     * 通过id寻找用户
     * @param id 用户id
     * @return 返回查找到的用户
     */
    User selectUserById(@Param("id") Integer id);

    /**
     * 胜方修改数据
     * @param id 胜方用户id
     */
    void userWinUpdate(@Param("id") Integer id);

    /**
     * 败方修改数据
     * @param id 败方id
     */
    void userLoseUpdate(@Param("id") Integer id);

    ArrayList<Integer> selectUserListByCharacter(@Param("userName") String userCharacter);

    UserVO selectUserVOById(@Param("id") Integer id);

}
