package com.example.java_gobang.component;

import com.example.java_gobang.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class DoubleRoomManager {

    // 每次进入自建房间页面的时候需要把用户名加入，还要创建一个新Set 表示这个房间的用户
    // 总的 Set 用于存放所有进入自建房间的玩家，一个玩家不能进入两个房间 不能重复
    private final Set<Integer> anyDoubleRoomUserId = new HashSet<>();

    // 管理每个房间用户，检查每个自建房间用户不能重复
    // key是自建房间房主的用户名，value是该自建房间中的人数
    private final Map<String, LinkedList<User>> doubleRoomListManager = new HashMap<>();

    // 再来个 Map 存储，邀请人映射房主的 Map， 用于通过第二人找到房主
    private final Map<String, String> attenderToHomeowner = new HashMap<>();


    @Autowired
    private RoomManager roomManager;

    // 当房主创建自检房间后就要调用，邀请人进入房间后也要调用该方法
    // 每当连接成功后需要把当前所有人的信息传递进来，传递过来List<User>
    public boolean enterDoubleRoom(User user, String homeownerUsername) {
        // user 是成功连接这个房间的人
        if (!anyDoubleRoomUserId.add(user.getId())) {
            // 状态添加失败，返回 false 提醒用户重新进入房间
            anyDoubleRoomUserId.remove(user.getId());
            return false;
        }

        if (!doubleRoomListManager.containsKey(homeownerUsername)) {
            // 没有查到房间队列说明是房间刚创建
            LinkedList<User> linkedList = new LinkedList<>();
            linkedList.add(user);
            doubleRoomListManager.put(homeownerUsername, linkedList);
            anyDoubleRoomUserId.add(user.getId());
        } else {
            // 查得到房间队列，说明是邀请人刚刚进入房间，在该队列中插入邀请人的信息, 还要邀请人映射房主
            doubleRoomListManager.get(homeownerUsername).add(user);
            attenderToHomeowner.put(user.getUsername(), homeownerUsername);
            anyDoubleRoomUserId.add(user.getId());
        }
        return true;
    }

    // 退出房间
    // 参数是退出那个用户的对象
    public void exitDoubleRoom(User user) {
        // 把退出的用户从状态Map和房间队列中删除， 分3种情况 1. 退出的是房主 2.退出的是邀请人，3. 退出的是最后一个人，需要关闭资源
        if (doubleRoomListManager.get(user.getUsername()).size() == 1) {
            // 一个人房主退出， 清理状态
            anyDoubleRoomUserId.remove(user.getId());
            doubleRoomListManager.remove(user.getUsername());
        } else {
            if (attenderToHomeowner.containsKey(user.getUsername())) {
                // 退出的是邀请人
                anyDoubleRoomUserId.remove(user.getId());
                // 找到房主姓名再找到链表，然后尾删
                doubleRoomListManager.get(attenderToHomeowner.get(user.getUsername())).removeLast();
                attenderToHomeowner.remove(user.getUsername());
                anyDoubleRoomUserId.remove(user.getId());
            } else if (attenderToHomeowner.containsValue(user.getUsername())) {
                // 两个人房主退出，转让房主
                LinkedList<User> linkedList = doubleRoomListManager.remove(user.getUsername());
                User homeowner = linkedList.removeFirst();
                doubleRoomListManager.put(linkedList.getFirst().getUsername(), linkedList);
                attenderToHomeowner.remove(linkedList.getFirst().getUsername());
                anyDoubleRoomUserId.remove(homeowner.getId());

            }
        }
    }

    // 检查是不是房主,是房主返回真，不是房主返回假
    public boolean isHomeowner(User user) {
        if (anyDoubleRoomUserId.contains(user.getId()) && doubleRoomListManager.containsKey(user.getUsername())) {
            return true;
        }
        return false;
    }

    public List<User> getDoubleUser(User user) {
        return doubleRoomListManager.get(user.getUsername());
    }

}
