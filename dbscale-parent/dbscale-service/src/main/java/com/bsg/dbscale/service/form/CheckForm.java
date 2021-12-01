package com.bsg.dbscale.service.form;

import java.io.Serializable;

public class CheckForm implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private String pwd;

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    @Override
    public String toString() {
        return "CheckForm [pwd=" + pwd + "]";
    }

}
