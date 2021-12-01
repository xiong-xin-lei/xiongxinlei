package com.bsg.dbscale.dao.domain;

import java.io.Serializable;
import java.util.Date;

public class OperateLogDO implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private Long id;
    private String objType;
    private String objName;
    private String description;
    private String siteId;
    private Date gmtCreate;
    private String creator;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getObjType() {
        return objType;
    }

    public void setObjType(String objType) {
        this.objType = objType;
    }

    public String getObjName() {
        return objName;
    }

    public void setObjName(String objName) {
        this.objName = objName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
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
        return "OperateLogDO [id=" + id + ", objType=" + objType + ", objName=" + objName + ", description="
                + description + ", siteId=" + siteId + ", gmtCreate=" + gmtCreate + ", creator=" + creator + "]";
    }

}
