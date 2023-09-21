package com.example.java_gobang.service;

import com.example.java_gobang.common.AppVariable;
import com.example.java_gobang.entity.User;
import com.example.java_gobang.entity.vo.UserVO;
import com.example.java_gobang.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    public int addUserService(User user) {
        // TODO 需要给注册密码进行加密
        return userMapper.insertUser(user);
    }

    public boolean getUserPasswordService(String username, String password, HttpServletRequest request) {
        // TODO 数据库中的最终密码，要进行解密
        User user = userMapper.selectUserByName(username);
        if (user == null) {
            return false;
        }
        boolean isLogin = password.equals(user.getPassword());
        if (isLogin) {
            // 登录成功后添加 session
            HttpSession session = request.getSession(true);
            user.setPassword("");
            session.setAttribute(AppVariable.USER_SESSION_KEY, user);
            // TODO 每次登录成功后更新数据库密码盐值
        }
        return isLogin;
    }

    public User getUserByIdService(Integer userId) {
        return userMapper.selectUserById(userId);
    }

    public ArrayList<Integer> getUserIdListByCharacter(String userCharacter) {
        return userMapper.selectUserListByCharacter(userCharacter);
    }

    public ArrayList<User> getUserVOListById(ArrayList<Integer> userIdList) {
        ArrayList<UserVO> userVOList = new ArrayList<>();
        for (Integer integer : userIdList) {
            UserVO userVO = (UserVO) userMapper.selectUserById(integer);
            // 如果根据登录用户id和传输用户id两个属性从followinfo表中查找到的值等于1，则赋值true，否则赋值false

            userVO.setWhetherFollow();
            userVOList.add();
        }
        return userVOList;
    }


}
