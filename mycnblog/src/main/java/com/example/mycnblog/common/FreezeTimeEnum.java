package com.example.mycnblog.common;

import java.util.HashMap;

// 账号冻结时间列表
public class FreezeTimeEnum {

    private static final Object lock = new Object();

    private static volatile HashMap<Integer, Integer> stateMapTime = null;

    public static HashMap<Integer, Integer> getFreezeTimeList() {
        if (stateMapTime == null) {
            synchronized (lock) {
                if (stateMapTime == null) {
                    stateMapTime = new HashMap<>();
                    stateMapTime.put(0, 10);
                    stateMapTime.put(1, 60);
                    stateMapTime.put(2, 300);
                }
            }
        }
        return stateMapTime;
    }
}

