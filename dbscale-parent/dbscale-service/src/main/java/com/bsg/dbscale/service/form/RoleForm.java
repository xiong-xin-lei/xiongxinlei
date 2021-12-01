package com.bsg.dbscale.service.form;

import java.io.Serializable;

public class RoleForm implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * 角色名
     */
    private String name;

    /**
     * 描述
     */
    private String description;

    /**
     * 是否为管理角色
     */
    private Boolean manager;

    /**
     * 可见数据范围
     */
    private String dataScope;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getManager() {
        return manager;
    }

    public void setManager(Boolean manager) {
        this.manager = manager;
    }

    public String getDataScope() {
        return dataScope;
    }

    public void setDataScope(String dataScope) {
        this.dataScope = dataScope;
    }

    @Override
    public String toString() {
        return "RoleForm [name=" + name + ", description=" + description + ", manager=" + manager + ", dataScope="
                + dataScope + "]";
    }

}
