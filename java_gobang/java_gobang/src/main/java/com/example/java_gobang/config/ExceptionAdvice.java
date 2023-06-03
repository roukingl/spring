package com.example.java_gobang.config;

import com.example.java_gobang.common.AjaxResult;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
@ResponseBody
public class ExceptionAdvice {

    @ExceptionHandler(Exception.class)
    public AjaxResult exceptionHandler(Exception exception) {
        return AjaxResult.fail(-2, "Exception异常");
    }

    @ExceptionHandler(RuntimeException.class)
    public AjaxResult runtimeExceptionHandler(RuntimeException runtimeException) {
        return AjaxResult.fail(-2, "运行时异常");
    }

    @ExceptionHandler(NullPointerException.class)
    public AjaxResult nullExceptionHandler(NullPointerException nullPointerException) {
        return AjaxResult.fail(-2, "空指针异常");
    }

    // TODO 出现运行时异常的处理场所
}
