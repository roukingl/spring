package com.example.demo.service;

import com.example.demo.entity.Userinfo;
import com.example.demo.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    public int insertUserService(Userinfo userinfo) {
        return userMapper.insertUser(userinfo);
    }

    public Userinfo getUserByNameService(String username) {
        return userMapper.getUserByName(username);
    }

    public Userinfo getAuthorByIdService(Integer id) {
        return userMapper.getAuthorById(id);
    }

    /**
     * service 层
     * 得到 id 用户的state
     * @param id 用户 id
     * @return 返回的 state
     */
    public int getUserStateService(Integer id) {
        return userMapper.getUserState(id);
    }

    /**
     * service 层
     *  修改 state， freezetime， errornum
     * @param userinfo 要修改的数据用户
     * @return 返回受影响的行数
     */
    public int updateStateFreezetimeErrornum(Userinfo userinfo) {
        return userMapper.updateUser(userinfo);
    }

    /**
     * service 层
     * 修改 errornum
     * @param id 要修改的用户 id
     * @param newErrornum 新的errornum
     */
    public void updateUserErrornumState(Integer id, Integer newErrornum, Integer newState) {
        userMapper.updateUserErrornumState(id, newErrornum, newState);
    }

    public void updateUserErrornum(Integer id, Integer newErrornum) {
        userMapper.updateUserErrornum(id, newErrornum);
    }


    /**
     * service层 根据用户 id 来查找密码
     * @param id 用户 id
     * @return 返回数据库中的加密密码
     */
    public String getUserPassword(Integer id) {
        return userMapper.selectUserPassword(id);
    }

    /**
     * service层 修改当前用户的密码
     * @param userinfo 当前用户
     * @return 返回受影响的行数
     */
    public int updateUserPasswordService(Userinfo userinfo) {
        return userMapper.updateUserPassword(userinfo.getId(), userinfo.getPassword());
    }
}
