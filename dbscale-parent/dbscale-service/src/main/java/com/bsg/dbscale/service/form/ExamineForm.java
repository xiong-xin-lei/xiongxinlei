package com.bsg.dbscale.service.form;

import java.io.Serializable;

public class ExamineForm implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * 状态
     */
    private String state;

    /**
     * 审批信息
     */
    private String msg;

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    /**
     * 获取审批信息
     * 
     * @return msg 审批信息
     */
    public String getMsg() {
        return msg;
    }

    /**
     * 设置审批信息
     * 
     * @param msg
     *            审批信息
     */
    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {
        return "ExamineForm [state=" + state + ", msg=" + msg + "]";
    }

}
