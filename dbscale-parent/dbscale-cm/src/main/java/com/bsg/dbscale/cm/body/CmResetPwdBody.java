package com.bsg.dbscale.cm.body;

import java.io.Serializable;

import com.alibaba.fastjson.annotation.JSONField;

public class CmResetPwdBody implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    @JSONField(name = "type")
    private String type;

    @JSONField(name = "pass")
    private String pwd;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    @Override
    public String toString() {
        return "CmResetPwdBody [type=" + type + ", pwd=" + pwd + "]";
    }

}
