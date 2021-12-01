package com.bsg.dbscale.dao.query;

import java.io.Serializable;

public class ForceRebuildLogQuery implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private String sourceHostRelateId;
    private String unitRelateId;

    public String getSourceHostRelateId() {
        return sourceHostRelateId;
    }

    public void setSourceHostRelateId(String sourceHostRelateId) {
        this.sourceHostRelateId = sourceHostRelateId;
    }

    public String getUnitRelateId() {
        return unitRelateId;
    }

    public void setUnitRelateId(String unitRelateId) {
        this.unitRelateId = unitRelateId;
    }

    @Override
    public String toString() {
        return "ForceRebuildLogQuery [sourceHostRelateId=" + sourceHostRelateId + ", unitRelateId=" + unitRelateId
                + "]";
    }

}
