package com.bsg.dbscale.dao.query;

import java.io.Serializable;
import java.util.Date;

public class TaskQuery implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * 站点编码
     */
    private String siteId;

    /**
     * 所属者
     */
    private String owner;

    /**
     * 对象类型
     */
    private String objType;

    /**
     * 对象编码
     */
    private String objId;

    private String actionType;

    /**
     * 起始时间
     */
    private Date startDate;

    /**
     * 结束时间
     */
    private Date endDate;

    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getObjType() {
        return objType;
    }

    public void setObjType(String objType) {
        this.objType = objType;
    }

    public String getObjId() {
        return objId;
    }

    public void setObjId(String objId) {
        this.objId = objId;
    }

    public String getActionType() {
        return actionType;
    }

    public void setActionType(String actionType) {
        this.actionType = actionType;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    @Override
    public String toString() {
        return "TaskQuery [siteId=" + siteId + ", owner=" + owner + ", objType=" + objType + ", objId=" + objId
                + ", actionType=" + actionType + ", startDate=" + startDate + ", endDate=" + endDate + "]";
    }

}
