package com.example.demo.service;

import com.example.demo.common.AjaxResult;
import com.example.demo.common.AppVariable;
import com.example.demo.common.FreezeTimeEnum;
import com.example.demo.common.PasswordUtils;
import com.example.demo.entity.Userinfo;
import com.example.demo.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    private final Object lock = new Object();

    /**
     * 用户登录的逻辑
     * @param password 登录密码
     * @param userinfo 根据用户名查找到的用户
     * @param request 请求变量
     * @return 返回AjaxResult格式
     * @throws ParseException 异常忽略
     */
    public AjaxResult userLoginService(String password, Userinfo userinfo, HttpServletRequest request) throws ParseException {

        HashMap<Integer, Integer> freezeTimeMap = FreezeTimeEnum.getFreezeTimeList();
        int errorNumber = userinfo.getError_number();
        int state = userinfo.getState();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        if (errorNumber == 5) {
            // 1. 检差当前的 error_num 当等于5时，根据state来设置冻结时间
            int freeTimeDiffer = freezeTimeMap.get(state);
            LocalDateTime freezeTime = LocalDateTime.now().plusSeconds(freeTimeDiffer);
            userinfo.setFreeze_time(freezeTime);
            userinfo.setError_number(errorNumber + 1);
            userinfo.setState(state < 2 ? state + 1 : 2);
            int row = userMapper.updateUserLoginParameters(userinfo);
            Thread thread = new Thread(() -> {
                synchronized (lock) {
                    try {
                        lock.wait(FreezeTimeEnum.getFreezeTimeList().get(state) * 1000);
                        int i = userMapper.updateUserError_number(userinfo.getId(), 0);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
            thread.start();
            return AjaxResult.fail(1, "账户已冻结，请 " +
                    (freeTimeDiffer >= 60 ? (freeTimeDiffer / 60) + "分钟后" : freeTimeDiffer + "秒后") + "重试");
        } else if (errorNumber > 5) {
            // 2. error_number 大于 5 时，返回剩余冻结时间
            String[] times = userinfo.getFreeze_time().toString().split("T");
            Date date = simpleDateFormat.parse(times[0] + " " + times[1]);
            long time = (date.getTime() + freezeTimeMap.get(state) - System.currentTimeMillis()) / 1000;
            String responseTime;
            if (time > 60) {
                responseTime = (int) (time / 60) + "分钟  ";
                responseTime += time % 60 + "秒";
            } else {
                responseTime = (int) time + "秒";
            }
            return AjaxResult.fail(1, "账号已被冻结，请 " + responseTime + " 后重试");
        }

        // 3. 没达到设定的 5 次就再次判断密码是否正确
        if (!PasswordUtils.passwordDecrypt(password, userinfo.getPassword())) {
            // 隐藏敏感信息   密码错误 再次增加 error_number
            userMapper.updateUserError_number(userinfo.getId(), errorNumber + 1);
            return AjaxResult.fail(-1, "非法访问");
        }

        // 密码正确，清除 state 为0，error_number为0
        int row = userMapper.updateUserError_numberState(userinfo.getId(), 0, 0);
        HttpSession session = request.getSession(true);
        userinfo.setPassword("");
        session.setAttribute(AppVariable.USER_SESSION_KEY, userinfo);
        return AjaxResult.success(row);
    }

    /**
     * 添加用户，用于注册
     * @param userinfo 要添加的用户
     * @return 返回受影响的行数
     */
    public int insertUserService(Userinfo userinfo) {
        userinfo.setPassword(PasswordUtils.passwordEncrypt(userinfo.getPassword()));
        return userMapper.insertUser(userinfo);
    }

    /**
     * service层 根据用户名查找用户
     * @param username 用户名
     * @return 返回数据库中的用户信息
     */
    public Userinfo getUserByNameService(String username) {
        return userMapper.getUserByName(username);
    }

    /**
     * service层 根据用户 id 查找作者
     * @param id 用户 id
     * @return 返回数据库中的作者信息
     */
    public Userinfo getAuthorByIdService(Integer id) {
        return userMapper.getAuthorById(id);
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
