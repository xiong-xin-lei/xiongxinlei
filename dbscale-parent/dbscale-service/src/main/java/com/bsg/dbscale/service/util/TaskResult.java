package com.bsg.dbscale.service.util;

public class TaskResult {

    private String state;
    private String msg;
    private Object obj;

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getObj() {
        return obj;
    }

    public void setObj(Object obj) {
        this.obj = obj;
    }

    @Override
    public String toString() {
        return "TaskResult [state=" + state + ", msg=" + msg + ", obj=" + obj + "]";
    }

}
