package com.example.java_gobang.common;

import lombok.Data;

import java.io.Serializable;

@Data
public class AjaxResult implements Serializable {
    // 状态码
    private int status;
    // 状态码描述
    private String statusMsg;
    // 返回的数据
    private Object data;

    /**
     * 操作成功调用的函数
     */
    public static AjaxResult success(Object data) {
        AjaxResult ajaxResult = new AjaxResult();
        ajaxResult.setStatus(200);
        ajaxResult.setStatusMsg("");
        ajaxResult.setData(data);
        return ajaxResult;
    }

    public static AjaxResult success(int status, Object data) {
        AjaxResult ajaxResult = new AjaxResult();
        ajaxResult.setStatus(status);
        ajaxResult.setStatusMsg("");
        ajaxResult.setData(data);
        return ajaxResult;
    }

    public static AjaxResult success(int status, String statusMsg, Object data) {
        AjaxResult ajaxResult = new AjaxResult();
        ajaxResult.setStatus(status);
        ajaxResult.setStatusMsg(statusMsg);
        ajaxResult.setData(data);
        return ajaxResult;
    }

    /**
     * 失败调用的函数
     */
    public static AjaxResult fail(int status, String statusMsg) {
        AjaxResult ajaxResult = new AjaxResult();
        ajaxResult.setStatus(status);
        ajaxResult.setStatusMsg(statusMsg);
        ajaxResult.setData(null);
        return ajaxResult;
    }

    public static AjaxResult fail(int status, String statusMsg, Object data) {
        AjaxResult ajaxResult = new AjaxResult();
        ajaxResult.setStatus(status);
        ajaxResult.setStatusMsg(statusMsg);
        ajaxResult.setData(data);
        return ajaxResult;
    }
}
