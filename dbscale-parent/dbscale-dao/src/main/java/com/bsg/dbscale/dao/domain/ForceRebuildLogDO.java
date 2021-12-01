package com.bsg.dbscale.dao.domain;

import java.io.Serializable;

public class ForceRebuildLogDO implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private Long id;
    private String unitRelateId;
    private String sourceHostRelateId;
    private String targetHostRelateId;
    private String taskId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUnitRelateId() {
        return unitRelateId;
    }

    public void setUnitRelateId(String unitRelateId) {
        this.unitRelateId = unitRelateId;
    }

    public String getSourceHostRelateId() {
        return sourceHostRelateId;
    }

    public void setSourceHostRelateId(String sourceHostRelateId) {
        this.sourceHostRelateId = sourceHostRelateId;
    }

    public String getTargetHostRelateId() {
        return targetHostRelateId;
    }

    public void setTargetHostRelateId(String targetHostRelateId) {
        this.targetHostRelateId = targetHostRelateId;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    @Override
    public String toString() {
        return "ForceRebuildLogDO [id=" + id + ", unitRelateId=" + unitRelateId + ", sourceHostRelateId="
                + sourceHostRelateId + ", targetHostRelateId=" + targetHostRelateId + ", taskId=" + taskId + "]";
    }

}
