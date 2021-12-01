package com.bsg.dbscale.service.dto;

import java.io.Serializable;
import java.util.List;

public class BackupStrategyDTO implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private String id;
    private ServGroupDTO servGroup;
    private DisplayDTO backupStorageType;
    private DisplayDTO type;
    private List<String> tables;
    private String cronExpression;
    private Integer fileRetentionNum;
    private Boolean enabled;
    private String description;
    private InfoDTO created;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ServGroupDTO getServGroup() {
        return servGroup;
    }

    public void setServGroup(ServGroupDTO servGroup) {
        this.servGroup = servGroup;
    }

    public DisplayDTO getBackupStorageType() {
        return backupStorageType;
    }

    public void setBackupStorageType(DisplayDTO backupStorageType) {
        this.backupStorageType = backupStorageType;
    }

    public DisplayDTO getType() {
        return type;
    }

    public void setType(DisplayDTO type) {
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

    public InfoDTO getCreated() {
        return created;
    }

    public void setCreated(InfoDTO created) {
        this.created = created;
    }

    @Override
    public String toString() {
        return "BackupStrategyDTO [id=" + id + ", servGroup=" + servGroup + ", backupStorageType=" + backupStorageType
                + ", type=" + type + ", tables=" + tables + ", cronExpression=" + cronExpression + ", fileRetentionNum="
                + fileRetentionNum + ", enabled=" + enabled + ", description=" + description + ", created=" + created
                + "]";
    }

    public class ServGroupDTO {
        private String id;
        private String name;
        private String category;
        private BusinessSubsystemBaseDTO businessSubsystem;
        private IdentificationDTO site;
        private UserBaseDTO owner;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }

        public BusinessSubsystemBaseDTO getBusinessSubsystem() {
            return businessSubsystem;
        }

        public void setBusinessSubsystem(BusinessSubsystemBaseDTO businessSubsystem) {
            this.businessSubsystem = businessSubsystem;
        }

        public IdentificationDTO getSite() {
            return site;
        }

        public void setSite(IdentificationDTO site) {
            this.site = site;
        }

        public UserBaseDTO getOwner() {
            return owner;
        }

        public void setOwner(UserBaseDTO owner) {
            this.owner = owner;
        }

        @Override
        public String toString() {
            return "ServGroupDTO [id=" + id + ", name=" + name + ", category=" + category + ", businessSubsystem="
                    + businessSubsystem + ", site=" + site + ", owner=" + owner + "]";
        }

    }

}
