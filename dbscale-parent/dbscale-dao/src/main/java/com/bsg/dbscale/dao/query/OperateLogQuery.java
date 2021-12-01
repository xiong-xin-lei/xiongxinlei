package com.bsg.dbscale.dao.query;

import java.io.Serializable;
import java.util.Date;

public class OperateLogQuery implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private String siteId;
    private String objType;
    private String objName;
    private Date start;
    private Date end;

    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
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

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    @Override
    public String toString() {
        return "OperateLogQuery [siteId=" + siteId + ", objType=" + objType + ", objName=" + objName + ", start="
                + start + ", end=" + end + "]";
    }

}
