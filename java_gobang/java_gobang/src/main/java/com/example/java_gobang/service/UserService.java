package com.example.java_gobang.service;

import com.example.java_gobang.common.AppVariable;
import com.example.java_gobang.entity.User;
import com.example.java_gobang.entity.vo.UserVO;
import com.example.java_gobang.mapper.FollowMapper;
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

    @Autowired
    private FollowMapper followMapper;

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

    
    public ArrayList<UserVO> getUserVOListById(ArrayList<Integer> userIdList, int userId) {
        ArrayList<UserVO> userVOList = new ArrayList<>();
        for (int followedId : userIdList) {
            // 从数据库中得到用户的信息，然后在用得到的用户id和登录用户id查询是否有关注关系，返回关注主体用户id判断是否是登录用户id，是
            // 就说明是已关注，就往whetherFollow字段赋值true
            UserVO userVO = userMapper.selectUserVOById(followedId);
            userVO.setWhetherFollow(followMapper.judgementIsFollow(userId, followedId) == userId);
            userVOList.add(userVO);
        }
        return userVOList;
    }


}
