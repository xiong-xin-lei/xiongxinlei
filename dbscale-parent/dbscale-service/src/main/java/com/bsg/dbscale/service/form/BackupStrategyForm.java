package com.bsg.dbscale.service.form;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class BackupStrategyForm implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private String servGroupId;
    private String backupStorageType;
    private String type;
    private List<String> tables;
    private String cronExpression;
    private Integer fileRetentionNum;
    private Boolean enabled;
    private String description;

    public BackupStrategyForm() {
        this.tables = new ArrayList<>();
    }

    public String getServGroupId() {
        return servGroupId;
    }

    public void setServGroupId(String servGroupId) {
        this.servGroupId = servGroupId;
    }

    public String getBackupStorageType() {
        return backupStorageType;
    }

    public void setBackupStorageType(String backupStorageType) {
        this.backupStorageType = backupStorageType;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<String> getTables() {
        return tables;
    }

    public void setTables(List<String> tables) {
        this.tables = tables;
    }

    public String getCronExpression() {
        return cronExpression;
    }

    public void setCronExpression(String cronExpression) {
        this.cronExpression = cronExpression;
    }

    public Integer getFileRetentionNum() {
        return fileRetentionNum;
    }

    public void setFileRetentionNum(Integer fileRetentionNum) {
        this.fileRetentionNum = fileRetentionNum;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "BackupStrategyForm [servGroupId=" + servGroupId + ", backupStorageType=" + backupStorageType + ", type="
                + type + ", tables=" + tables + ", cronExpression=" + cronExpression + ", fileRetentionNum="
                + fileRetentionNum + ", enabled=" + enabled + ", description=" + description + "]";
    }

}
