package com.bsg.dbscale.cm.query;

import java.io.Serializable;

import com.alibaba.fastjson.annotation.JSONField;

public class CmBackupFileQuery implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    @JSONField(name = "id")
    private String id;

    @JSONField(name = "site_id")
    private String siteId;

    @JSONField(name = "unit_id")
    private String unitId;

    @JSONField(name = "service_id")
    private String serviceId;

    @JSONField(name = "status")
    private String status;

    @JSONField(name = "external")
    private Boolean external;

    @JSONField(name = "type")
    private String type;

    @JSONField(name = "start")
    private Long start;

    @JSONField(name = "end")
    private Long end;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }

    public String getUnitId() {
        return unitId;
    }

    public void setUnitId(String unitId) {
        this.unitId = unitId;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Boolean getExternal() {
        return external;
    }

    public void setExternal(Boolean external) {
        this.external = external;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getStart() {
        return start;
    }

    public void setStart(Long start) {
        this.start = start;
    }

    public Long getEnd() {
        return end;
    }

    public void setEnd(Long end) {
        this.end = end;
    }

    @Override
    public String toString() {
        return "CmBackupFileQuery [id=" + id + ", siteId=" + siteId + ", unitId=" + unitId + ", serviceId=" + serviceId
                + ", status=" + status + ", external=" + external + ", type=" + type + ", start=" + start + ", end="
                + end + "]";
    }

}
