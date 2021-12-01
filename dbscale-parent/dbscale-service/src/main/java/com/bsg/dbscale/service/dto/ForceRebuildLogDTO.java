package com.bsg.dbscale.service.dto;

import java.io.Serializable;

public class ForceRebuildLogDTO implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private Long id;
    private String unitRelateName;
    private String sourceHostIp;
    private String sourceHostName;
    private String targetHostIp;
    private String targetHostName;
    private TaskBaseDTO task;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUnitRelateName() {
        return unitRelateName;
    }

    public void setUnitRelateName(String unitRelateName) {
        this.unitRelateName = unitRelateName;
    }

    public String getSourceHostIp() {
        return sourceHostIp;
    }

    public void setSourceHostIp(String sourceHostIp) {
        this.sourceHostIp = sourceHostIp;
    }

    public String getSourceHostName() {
        return sourceHostName;
    }

    public void setSourceHostName(String sourceHostName) {
        this.sourceHostName = sourceHostName;
    }

    public String getTargetHostIp() {
        return targetHostIp;
    }

    public void setTargetHostIp(String targetHostIp) {
        this.targetHostIp = targetHostIp;
    }

    public String getTargetHostName() {
        return targetHostName;
    }

    public void setTargetHostName(String targetHostName) {
        this.targetHostName = targetHostName;
    }

    public TaskBaseDTO getTask() {
        return task;
    }

    public void setTask(TaskBaseDTO task) {
        this.task = task;
    }

    @Override
    public String toString() {
        return "ForceRebuildLogDTO [id=" + id + ", unitRelateName=" + unitRelateName + ", sourceHostIp=" + sourceHostIp
                + ", sourceHostName=" + sourceHostName + ", targetHostIp=" + targetHostIp + ", targetHostName="
                + targetHostName + ", task=" + task + "]";
    }

}
