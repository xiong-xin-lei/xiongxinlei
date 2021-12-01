package com.bsg.dbscale.dao.domain;

import java.io.Serializable;
import java.util.Date;

public class RoleDO implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * 唯一标识
     */
    private String id;

    /**
     * 角色名称
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

    /**
     * 是否为系统资源
     */
    private Boolean sys;

    /**
     * 创建时间
     */
    private Date gmtCreate;

    /**
     * 创建者
     */
    private String creator;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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

    public Boolean getSys() {
        return sys;
    }

    public void setSys(Boolean sys) {
        this.sys = sys;
    }

    public Date getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    @Override
    public String toString() {
        return "RoleDO [id=" + id + ", name=" + name + ", description=" + description + ", manager=" + manager
                + ", dataScope=" + dataScope + ", sys=" + sys + ", gmtCreate=" + gmtCreate + ", creator=" + creator
                + "]";
    }

}
