package com.bsg.dbscale.service.form;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class OrderForm implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private String type;
    private VersionForm version;
    private String archMode;
    private Integer unitCnt;
    private Double cpuCnt;
    private Double memSize;
    private String diskType;
    private Integer dataSize;
    private Integer logSize;
    private Integer port;
    private Cfg cfg;
    private Integer cnt;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public VersionForm getVersion() {
        return version;
    }

    public void setVersion(VersionForm version) {
        this.version = version;
    }

    public String getArchMode() {
        return archMode;
    }

    public void setArchMode(String archMode) {
        this.archMode = archMode;
    }

    public Integer getUnitCnt() {
        return unitCnt;
    }

    public void setUnitCnt(Integer unitCnt) {
        this.unitCnt = unitCnt;
    }

    public Double getCpuCnt() {
        return cpuCnt;
    }

    public void setCpuCnt(Double cpuCnt) {
        this.cpuCnt = cpuCnt;
    }

    public Double getMemSize() {
        return memSize;
    }

    public void setMemSize(Double memSize) {
        this.memSize = memSize;
    }

    public String getDiskType() {
        return diskType;
    }

    public void setDiskType(String diskType) {
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

    @Override
    public String toString() {
        return "OrderForm [type=" + type + ", version=" + version + ", archMode=" + archMode + ", unitCnt=" + unitCnt
                + ", cpuCnt=" + cpuCnt + ", memSize=" + memSize + ", diskType=" + diskType + ", dataSize=" + dataSize
                + ", logSize=" + logSize + ", port=" + port + ", cfg=" + cfg + ", cnt=" + cnt + "]";
    }

    public class Cfg {
        private Map<String, Object> param;
        private BackupStrategyForm backupStrategy;
        private List<String> networkIds;

        public Map<String, Object> getParam() {
            return param;
        }

        public void setParam(Map<String, Object> param) {
            this.param = param;
        }

        public BackupStrategyForm getBackupStrategy() {
            return backupStrategy;
        }

        public void setBackupStrategy(BackupStrategyForm backupStrategy) {
            this.backupStrategy = backupStrategy;
        }

        public List<String> getNetworkIds() {
            return networkIds;
        }

        public void setNetworkIds(List<String> networkIds) {
            this.networkIds = networkIds;
        }

        @Override
        public String toString() {
            return "Cfg [param=" + param + ", backupStrategy=" + backupStrategy + ", networkIds=" + networkIds + "]";
        }

    }

}
