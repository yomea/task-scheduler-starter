package com.hangu.task.model;

/**
 * @author wuzhenhong
 * @date 2023/8/7 17:05
 */
public class ApiResult<T> {

    private static final int SUCCESS_CODE = 200;

    private int code;

    private String msg;

    private T data;

    public ApiResult(String msg, int code) {

        this.msg = msg;
        this.code = code;
    }

    public ApiResult(String msg, int code, T data) {
        this(msg, code);
        this.data = data;
    }

    public ApiResult(int code, T data) {
        this.code = code;
        this.data = data;
    }

    public static <T> ApiResult<T> fail(String msg, int code) {
        return new ApiResult<>(msg, code);
    }

    public static <T> ApiResult<T> success() {

        return new ApiResult<>(SUCCESS_CODE, null);
    }
    public static <T> ApiResult<T> success(T data) {

        return new ApiResult<>(SUCCESS_CODE, data);
    }

    public boolean isSuccess() {
        return SUCCESS_CODE == this.code;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public T getData() {
        return data;
    }
}
