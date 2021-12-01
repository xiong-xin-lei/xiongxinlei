package com.bsg.dbscale.service.check;

import java.io.Serializable;

public class CheckResult implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public static final long SUCCESS = 200;
    public static final long FAILURE = 500;

    private long code;
    private String msg;

    public static CheckResult success() {
        CheckResult checkResult = new CheckResult();
        checkResult.setCode(SUCCESS);
        return checkResult;
    }

    public static CheckResult failure(String msg) {
        return failure(FAILURE, msg);
    }

    public static CheckResult failure(long code, String msg) {
        CheckResult checkResult = new CheckResult();
        checkResult.setCode(code);
        checkResult.setMsg(msg);
        return checkResult;
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

    @Override
    public String toString() {
        return "CheckResult [code=" + code + ", msg=" + msg + "]";
    }

}
