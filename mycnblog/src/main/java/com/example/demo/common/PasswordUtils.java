package com.example.demo.common;

import org.springframework.util.DigestUtils;

import java.util.UUID;

public class PasswordUtils {

    /**
     * 加密(加盐)
     * @param password 用户输入的密码
     * @return 用于数据库存储的密码
     */
    public static String passwordEncrypt(String password) {
        // 1. 得到盐值
        String salt = UUID.randomUUID().toString().replace("-", "");
        // 2. 得到 md5 加密后的密码, 需要和盐一起加密
        String saltPassword = DigestUtils.md5DigestAsHex((salt + password).getBytes());
        // 返回数据库中存储的密码，也就是前三十二位为盐值，分隔符，后三十二位md5加密密码
        String finalPassword = salt + "$" + saltPassword;
        return finalPassword;
    }

    /**
     * 以固定的盐值得到最终密码
     * @param password 用户输入的明文密码
     * @param salt 从数据库密码中得到的盐值
     * @return 返回最终密码和数据库密码比较
     */
    public static String passwordEncrypt(String password, String salt) {
        String saltPassword = DigestUtils.md5DigestAsHex((salt + password).getBytes());
        String finalPassword = salt + "$" + saltPassword;
        return finalPassword;
    }

    /**
     * 检查数据库密码和经过加密的用户明文密码是否相同
     * @param password 用户输入明文密码
     * @param finalPassword 数据库中密码
     * @return 相等返回true，不相等返回false
     */
    public static boolean passwordDecrypt(String password, String finalPassword) {
        // 得到盐值
        String salt = finalPassword.split("\\$")[0];
        String decryptPassword = PasswordUtils.passwordEncrypt(password, salt);
        return decryptPassword.equals(finalPassword);
    }

}
