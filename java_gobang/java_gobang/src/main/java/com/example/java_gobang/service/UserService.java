package com.example.java_gobang.service;

import com.example.java_gobang.common.AppVariable;
import com.example.java_gobang.component.MatchQueue;
import com.example.java_gobang.component.OnlineUserState;
import com.example.java_gobang.entity.User;
import com.example.java_gobang.entity.vo.UserVO;
import com.example.java_gobang.mapper.FollowMapper;
import com.example.java_gobang.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private FollowMapper followMapper;

    @Autowired
    private OnlineUserState onlineUserState;

    @Autowired
    private MatchQueue matchQueue;

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

    public List<UserVO> getUserVOListById(List<Integer> userIdList, User user) {
        Integer userId = user.getId();
        // 需要把搜索的玩家从大厅状态中移除
        onlineUserState.exitSessionHall(userId);
        // 如果正在匹配就需要把玩家从匹配队列中移除
        matchQueue.removeUserFromQueue(user);

        // 把搜索到的用户组织成顺序表传递到前端
        List<UserVO> userVOList = new ArrayList<>();
        for (int followedId : userIdList) {
            // 从数据库中得到用户的信息，然后在用得到的用户id和登录用户id查询是否有关注关系，返回关注主体用户id判断是否是登录用户id，是
            // 就说明是已关注，就往whetherFollow字段赋值true
            UserVO userVO = userMapper.selectUserVOById(followedId);

            Integer followUser = followMapper.judgementIsFollow(userId, followedId);
            System.out.println(followUser);
            userVO.setWhetherFollow(userId.equals(followUser));
            userVOList.add(userVO);
        }
        return userVOList;
    }

    public List<User> getFollowedUserService(User user) {
        int userId = user.getId();
        // 需要把关注界面的用户从大厅状态中移除
        onlineUserState.exitSessionHall(userId);
        // 如果正在匹配就需要把玩家从匹配队列中移除
        matchQueue.removeUserFromQueue(user);

        // 要先得到该用户所有关注的用户id
        List<Integer> followedIdList = followMapper.getFollowedId(userId);
        // 得到信息
        List<User> followedList = new ArrayList<>();
        for (int followedId : followedIdList) {
            followedList.add(userMapper.selectUserById(followedId));
        }
        return followedList;
    }

    public List<User> getFansUserService(User user) {
        int followId = user.getId();
        // 需要把粉丝界面的玩家从大厅状态中移除
        onlineUserState.exitSessionHall(followId);
        // 如果正在匹配就需要把玩家从匹配队列中移除
        matchQueue.removeUserFromQueue(user);

        // 要先得到该用户所有关注的用户id
        List<Integer> fansIdList = followMapper.getFansId(followId);
        // 得到信息
        List<User> fansList = new ArrayList<>();
        for (int fansId : fansIdList) {
            fansList.add(userMapper.selectUserById(fansId));
        }
        return fansList;
    }

}
