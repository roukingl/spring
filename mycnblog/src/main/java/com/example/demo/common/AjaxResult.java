package com.example.demo.common;

import lombok.Data;

import java.io.Serializable;

@Data
public class AjaxResult implements Serializable {
    // 状态码
    private int statusCode;
    // 状态码描述
    private String statusCodeDescription;
    // 返回的数据
    private Object data;

    /**
     * 操作成功调用的函数
     */
    public static AjaxResult success(Object data) {
        AjaxResult ajaxResult = new AjaxResult();
        ajaxResult.setStatusCode(200);
        ajaxResult.setStatusCodeDescription("");
        ajaxResult.setData(data);
        return ajaxResult;
    }

    public static AjaxResult success(int statusCode, Object data) {
        AjaxResult ajaxResult = new AjaxResult();
        ajaxResult.setStatusCode(statusCode);
        ajaxResult.setStatusCodeDescription("");
        ajaxResult.setData(data);
        return ajaxResult;
    }

    public static AjaxResult success(int statusCode, String statusCodeDescription, Object data) {
        AjaxResult ajaxResult = new AjaxResult();
        ajaxResult.setStatusCode(statusCode);
        ajaxResult.setStatusCodeDescription(statusCodeDescription);
        ajaxResult.setData(data);
        return ajaxResult;
    }

    /**
     * 失败调用的函数
     */
    public static AjaxResult fail(int statusCode, String statusCodeDescription) {
        AjaxResult ajaxResult = new AjaxResult();
        ajaxResult.setStatusCode(statusCode);
        ajaxResult.setStatusCodeDescription(statusCodeDescription);
        ajaxResult.setData(null);
        return ajaxResult;
    }

    public static AjaxResult fail(int statusCode, String statusCodeDescription, Object data) {
        AjaxResult ajaxResult = new AjaxResult();
        ajaxResult.setStatusCode(statusCode);
        ajaxResult.setStatusCodeDescription(statusCodeDescription);
        ajaxResult.setData(data);
        return ajaxResult;
    }
}
