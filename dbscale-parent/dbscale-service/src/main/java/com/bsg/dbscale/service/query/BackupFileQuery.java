package com.bsg.dbscale.service.query;

import java.io.Serializable;

public class BackupFileQuery implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private String siteId;
    private String unitId;
    private String servGroupId;
    private Boolean success;
    private Boolean external;
    private String type;

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

    public String getServGroupId() {
        return servGroupId;
    }

    public void setServGroupId(String servGroupId) {
        this.servGroupId = servGroupId;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
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

    @Override
    public String toString() {
        return "BackupFileQuery [siteId=" + siteId + ", unitId=" + unitId + ", servGroupId=" + servGroupId
                + ", success=" + success + ", external=" + external + ", type=" + type + "]";
    }

}
