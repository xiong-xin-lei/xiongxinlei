package com.bsg.dbscale.service.form;

import java.io.Serializable;

public class ResetPwdForm implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private String type;
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
        return "ResetPwdForm [type=" + type + ", pwd=" + pwd + "]";
    }

}
