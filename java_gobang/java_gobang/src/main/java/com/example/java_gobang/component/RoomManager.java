package com.example.java_gobang.component;

import com.example.java_gobang.entity.Room;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
public class RoomManager {
    // 有两个映射，一个是房间id和房间的映射，一个是用户id和房间id的映射
    private final HashMap<String, Room> rooms = new HashMap<>();
    private final HashMap<Integer, String> userIdToRoomId = new HashMap<>();

    public void addRoom(Room room, int userId1, int userId2) {
        rooms.put(room.getRoomId(), room);
        userIdToRoomId.put(userId1, room.getRoomId());
        userIdToRoomId.put(userId2, room.getRoomId());
    }

    public void removeRoom(String roomId, int userId1, int userId2) {
        rooms.remove(roomId);
        userIdToRoomId.remove(userId1);
        userIdToRoomId.remove(userId2);
    }

    public Room getRoomByRoomId(String roomId) {
        return rooms.get(roomId);
    }

    public Room getRoomByUserId(Integer userId) {
        String roomId = userIdToRoomId.get(userId);
        if (roomId == null) {
            return null;
        }
        return rooms.get(roomId);
    }
}
