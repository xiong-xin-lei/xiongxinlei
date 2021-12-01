package com.bsg.dbscale.service.service;

import com.bsg.dbscale.service.check.CheckResult;

public class Result {

    public static final long SUCCESS = 200;
    public static final long FAILURE = 500;

    private long code;
    private String msg;
    private Object data;

    public static Result success() {
        Result result = new Result();
        result.setCode(SUCCESS);
        return result;
    }

    public static Result success(Object object) {
        Result result = new Result();
        result.setCode(SUCCESS);
        result.setData(object);
        return result;
    }

    public static Result failure(CheckResult checkResult) {
        Result result = new Result();
        result.setCode(checkResult.getCode());
        result.setMsg(checkResult.getMsg());
        return result;
    }

    public static Result failure(String msg) {
        return failure(FAILURE, msg);
    }

    public static Result failure(long code, String msg) {
        Result result = new Result();
        result.setCode(code);
        result.setMsg(msg);
        return result;
    }

    public long getCode() {
        return code;
    }

    public void setCode(long code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "Result [code=" + code + ", msg=" + msg + ", data=" + data + "]";
    }
}
