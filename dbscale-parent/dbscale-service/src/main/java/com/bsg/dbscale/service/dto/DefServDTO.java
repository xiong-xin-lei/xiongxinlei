package com.bsg.dbscale.service.dto;

import java.io.Serializable;

public class DefServDTO implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * 服务代码
     */
    private String code;

    /**
     * 服务名称
     */
    private String name;

    /**
     * 是否是有状态服务
     */
    private Boolean stateful;

    /**
     * 是否可用
     */
    private Boolean enabled;

    /**
     * 描述
     */
    private String description;

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

    public Boolean getStateful() {
        return stateful;
    }

    public void setStateful(Boolean stateful) {
        this.stateful = stateful;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "DefServDTO [code=" + code + ", name=" + name + ", stateful=" + stateful + ", enabled=" + enabled
                + ", description=" + description + "]";
    }

}
