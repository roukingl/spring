package com.example.java_gobang.service;

import com.example.java_gobang.common.AppVariable;
import com.example.java_gobang.component.MatchQueue;
import com.example.java_gobang.component.OnlineUserState;
import com.example.java_gobang.entity.User;
import com.example.java_gobang.entity.vo.UserVO;
import com.example.java_gobang.entity.vo.UserVOHall;
import com.example.java_gobang.mapper.FollowMapper;
import com.example.java_gobang.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
            // TODO 可以使用内连接 inner join
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
        List<Integer> followedIdList = followMapper.selectFollowedId(userId);
        // 得到信息
        List<User> followedList = new ArrayList<>();
        for (int followedId : followedIdList) {
            followedList.add(userMapper.selectUserById(followedId));
        }
        return followedList;
    }

    public List<UserVO> getFansUserService(User user) {
        int followId = user.getId();
        // 需要把粉丝界面的玩家从大厅状态中移除
        onlineUserState.exitSessionHall(followId);
        // 如果正在匹配就需要把玩家从匹配队列中移除
        matchQueue.removeUserFromQueue(user);

        // 得到关注该用户的所有用户id，查找对应的信息后还要添加有没有互关的数据字段
        List<Integer> fansIdList = followMapper.selectFansId(followId);
        // 在得到该用户关注的所有用户id放到set集合中，如果粉丝id在该集合中就关注信息赋值为true
        Set<Integer> followedIdSet = new HashSet<>(followMapper.selectFollowedId(user.getId()));
        // TODO 可以使用内连接 inner join
        // 得到信息
        List<UserVO> fansVOList = new ArrayList<>();
        for (int fansId : fansIdList) {
            UserVO userVO = userMapper.selectUserVOById(fansId);
            userVO.setWhetherFollow(followedIdSet.contains(fansId));
            fansVOList.add(userVO);
        }
        return fansVOList;
    }

    // 我们规定，在 UserVOHall 中字段 userStatus 为 1 是表示用户界面为 大厅 和 房间 界面，需要检查大厅状态 Map session存在赋值, 表示准备好的状态
    // 当用户在 搜索，关注，粉丝 界面时，userStatus 为 2 表示未准备的状态，检查大厅状态 Map中session不存在赋值
    // 当用户在开始游戏房间界面，userStatus 为 3 ，表示游玩中，检查大厅状态 Map 不存在， 游戏房间状态 Map 存在
    // userStatus 为 0 时表示该字段还没赋值，显示未知状态
    public List<UserVOHall> getFollowedUserHallService(int userId) {
        // 使用内连接 inner join
        List<UserVOHall> followedUserVOHallList = followMapper.selectFollowedUserHallList(userId);
        setUserVOHallStatus(followedUserVOHallList);
        return followedUserVOHallList;
    }

    public List<UserVOHall> getFansUserHallService(int followedId) {
        // 使用内连接 inner join
        List<UserVOHall> fansUserVOHallList = followMapper.selectFansUserHallList(followedId);
        setUserVOHallStatus(fansUserVOHallList);
        return fansUserVOHallList;
    }

    private void setUserVOHallStatus(List<UserVOHall> userVOHallList) {
        for (UserVOHall userVOHall : userVOHallList) {
            // 查询状态 Map 设置 userStatus字段数据
            int userVOHallId = userVOHall.getId();
            if (onlineUserState.getSessionRoom(userVOHallId) != null) {
                // 设置成 3 表示游戏中
                userVOHall.setUserStatus(3);
            } else if (onlineUserState.getSessionHall(userVOHallId) != null) {
                // 在大厅准备着，赋值 1
                userVOHall.setUserStatus(1);
            } else {
                // 其他情况, 赋值 2 表示未准备
                userVOHall.setUserStatus(2);
            }
        }
    }
}
