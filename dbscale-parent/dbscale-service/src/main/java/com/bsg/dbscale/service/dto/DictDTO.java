package com.bsg.dbscale.service.dto;

import java.io.Serializable;

public class DictDTO implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * 字典代码
     */
    private String code;

    /**
     * 字典名称
     */
    private String name;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "DictDTO [code=" + code + ", name=" + name + "]";
    }

}
