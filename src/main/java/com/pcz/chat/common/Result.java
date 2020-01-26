package com.pcz.chat.common;

import lombok.Data;

/**
 * 200: 成功
 * 500: 错误
 * 501: 验证错误
 * 502: 用户token错误
 * 555: 异常
 *
 * @author picongzhi
 */
@Data
public class Result {
    private Integer status;
    private String message;
    private Object data;

    public Result() {
    }

    public Result(Integer status, String message, Object data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public Result(Object data) {
        this.status = 200;
        this.message = "OK";
        this.data = data;
    }

    public static Result build(Integer status, String message, Object data) {
        return new Result(status, message, data);
    }

    public static Result ok(Object data) {
        return new Result(data);
    }

    public static Result ok() {
        return new Result(null);
    }

    public static Result errorMessage(String message) {
        return new Result(500, message, null);
    }

    public static Result errorMap(Object data) {
        return new Result(501, "error", data);
    }

    public static Result errorTokenMessage(String message) {
        return new Result(502, message, null);
    }

    public static Result errorException(String message) {
        return new Result(555, message, null);
    }
}
