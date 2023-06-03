package com.example.demo.config;

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

    public static AjaxResult success(int statusCode, Object data) {
        AjaxResult ajaxResult = new AjaxResult();
        ajaxResult.setStatus(statusCode);
        ajaxResult.setStatusMsg("");
        ajaxResult.setData(data);
        return ajaxResult;
    }

    public static AjaxResult success(int statusCode, String statusCodeDescription, Object data) {
        AjaxResult ajaxResult = new AjaxResult();
        ajaxResult.setStatus(statusCode);
        ajaxResult.setStatusMsg(statusCodeDescription);
        ajaxResult.setData(data);
        return ajaxResult;
    }

    /**
     * 失败调用的函数
     */
    public static AjaxResult fail(int statusCode, String statusCodeDescription) {
        AjaxResult ajaxResult = new AjaxResult();
        ajaxResult.setStatus(statusCode);
        ajaxResult.setStatusMsg(statusCodeDescription);
        ajaxResult.setData(null);
        return ajaxResult;
    }

    public static AjaxResult fail(int statusCode, String statusCodeDescription, Object data) {
        AjaxResult ajaxResult = new AjaxResult();
        ajaxResult.setStatus(statusCode);
        ajaxResult.setStatusMsg(statusCodeDescription);
        ajaxResult.setData(data);
        return ajaxResult;
    }
}
