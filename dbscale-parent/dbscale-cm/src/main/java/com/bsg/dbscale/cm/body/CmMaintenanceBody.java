package com.bsg.dbscale.cm.body;

import java.io.Serializable;

import com.alibaba.fastjson.annotation.JSONField;

public class CmMaintenanceBody implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    @JSONField(name = "maintenance")
    private Boolean maintenance;

    @JSONField(name = "unit_id")
    private String unitId;

    public Boolean getMaintenance() {
        return maintenance;
    }

    public void setMaintenance(Boolean maintenance) {
        this.maintenance = maintenance;
    }

    public String getUnitId() {
        return unitId;
    }

    public void setUnitId(String unitId) {
        this.unitId = unitId;
    }

    @Override
    public String toString() {
        return "CmMaintenanceBody [maintenance=" + maintenance + ", unitId=" + unitId + "]";
    }

}
