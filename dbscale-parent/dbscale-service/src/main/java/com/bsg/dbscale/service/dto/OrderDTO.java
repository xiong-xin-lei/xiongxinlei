package com.bsg.dbscale.service.dto;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class OrderDTO implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * 
     */
    private String id;
    private DisplayDTO type;
    private VersionDTO version;
    private ArchBaseDTO arch;
    private ScaleBaseDTO scale;
    private DisplayDTO diskType;
    private Integer dataSize;
    private Integer logSize;
    private Integer port;
    private Cfg cfg;
    private Integer cnt;
    private Boolean clusterHA;
    private Boolean hostHA;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public DisplayDTO getType() {
        return type;
    }

    public void setType(DisplayDTO type) {
        this.type = type;
    }

    public VersionDTO getVersion() {
        return version;
    }

    public void setVersion(VersionDTO version) {
        this.version = version;
    }

    public ArchBaseDTO getArch() {
        return arch;
    }

    public void setArch(ArchBaseDTO arch) {
        this.arch = arch;
    }

    public ScaleBaseDTO getScale() {
        return scale;
    }

    public void setScale(ScaleBaseDTO scale) {
        this.scale = scale;
    }

    public DisplayDTO getDiskType() {
        return diskType;
    }

    public void setDiskType(DisplayDTO diskType) {
        this.diskType = diskType;
    }

    public Integer getDataSize() {
        return dataSize;
    }

    public void setDataSize(Integer dataSize) {
        this.dataSize = dataSize;
    }

    public Integer getLogSize() {
        return logSize;
    }

    public void setLogSize(Integer logSize) {
        this.logSize = logSize;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public Cfg getCfg() {
        return cfg;
    }

    public void setCfg(Cfg cfg) {
        this.cfg = cfg;
    }

    public Integer getCnt() {
        return cnt;
    }

    public void setCnt(Integer cnt) {
        this.cnt = cnt;
    }

    public Boolean getClusterHA() {
        return clusterHA;
    }

    public void setClusterHA(Boolean clusterHA) {
        this.clusterHA = clusterHA;
    }

    public Boolean getHostHA() {
        return hostHA;
    }

    public void setHostHA(Boolean hostHA) {
        this.hostHA = hostHA;
    }

    @Override
    public String toString() {
        return "OrderDTO [id=" + id + ", type=" + type + ", version=" + version + ", arch=" + arch + ", scale=" + scale
                + ", diskType=" + diskType + ", dataSize=" + dataSize + ", logSize=" + logSize + ", port=" + port
                + ", cfg=" + cfg + ", cnt=" + cnt + ", clusterHA=" + clusterHA + ", hostHA=" + hostHA + "]";
    }

    public class Cfg {
        private Map<String, Object> param;
        private BackupStrategyDTO backupStrategy;
        private List<IdentificationDTO> networks;

        public Map<String, Object> getParam() {
            return param;
        }

        public void setParam(Map<String, Object> param) {
            this.param = param;
        }

        public BackupStrategyDTO getBackupStrategy() {
            return backupStrategy;
        }

        public void setBackupStrategy(BackupStrategyDTO backupStrategy) {
            this.backupStrategy = backupStrategy;
        }

        public List<IdentificationDTO> getNetworks() {
            return networks;
        }

        public void setNetworks(List<IdentificationDTO> networks) {
            this.networks = networks;
        }

        @Override
        public String toString() {
            return "Cfg [param=" + param + ", backupStrategy=" + backupStrategy + ", networks=" + networks + "]";
        }

        public class BackupStrategyDTO {
            private DisplayDTO backupStorageType;
            private DisplayDTO type;
            private String cronExpression;
            private Integer fileRetentionNum;
            private String description;

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

            public String getDescription() {
                return description;
            }

            public void setDescription(String description) {
                this.description = description;
            }

            @Override
            public String toString() {
                return "BackupStrategyDTO [backupStorageType=" + backupStorageType + ", type=" + type
                        + ", cronExpression=" + cronExpression + ", fileRetentionNum=" + fileRetentionNum
                        + ", description=" + description + "]";
            }

        }
    }

}
