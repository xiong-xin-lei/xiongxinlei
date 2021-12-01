package com.bsg.dbscale.cm.query;

import java.io.Serializable;

import com.alibaba.fastjson.annotation.JSONField;

public class CmNodeQuery implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    @JSONField(name = "arch")
    private String arch;

    @JSONField(name = "os")
    private String os;

    @JSONField(name = "role")
    private String role;

    public String getArch() {
        return arch;
    }

    public void setArch(String arch) {
        this.arch = arch;
    }

    public String getOs() {
        return os;
    }

    public void setOs(String os) {
        this.os = os;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "CmNodeQuery [arch=" + arch + ", os=" + os + ", role=" + role + "]";
    }

}
