package com.bsg.dbscale.cm.body;

import java.io.Serializable;
import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;

public class CmHostBody implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    @JSONField(name = "cluster_id")
    private String clusterId;

    @JSONField(name = "ip")
    private String ip;

    @JSONField(name = "room")
    private String room;

    @JSONField(name = "seat")
    private String seat;

    @JSONField(name = "volume_path")
    private VolumePath volumePath;

    @JSONField(name = "usage_limit")
    private UsageLimit usageLimit;

    @JSONField(name = "unschedulable")
    private Boolean unschedulable;

    @JSONField(name = "role")
    private String role;

    @JSONField(name = "desc")
    private String desc;

    public String getClusterId() {
        return clusterId;
    }

    public void setClusterId(String clusterId) {
        this.clusterId = clusterId;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getSeat() {
        return seat;
    }

    public void setSeat(String seat) {
        this.seat = seat;
    }

    public VolumePath getVolumePath() {
        return volumePath;
    }

    public void setVolumePath(VolumePath volumePath) {
        this.volumePath = volumePath;
    }

    public UsageLimit getUsageLimit() {
        return usageLimit;
    }

    public void setUsageLimit(UsageLimit usageLimit) {
        this.usageLimit = usageLimit;
    }

    public Boolean getUnschedulable() {
        return unschedulable;
    }

    public void setUnschedulable(Boolean unschedulable) {
        this.unschedulable = unschedulable;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    @Override
    public String toString() {
        return "CmHostBody [clusterId=" + clusterId + ", ip=" + ip + ", room=" + room + ", seat=" + seat
                + ", volumePath=" + volumePath + ", usageLimit=" + usageLimit + ", unschedulable=" + unschedulable
                + ", role=" + role + ", desc=" + desc + "]";
    }

    public class VolumePath {

        @JSONField(name = "storages")
        private List<Storage> storages;

        @JSONField(name = "storage_remote_id")
        private String storageRemoteId;

        public List<Storage> getStorages() {
            return storages;
        }

        public void setStorages(List<Storage> storages) {
            this.storages = storages;
        }

        public String getStorageRemoteId() {
            return storageRemoteId;
        }

        public void setStorageRemoteId(String storageRemoteId) {
            this.storageRemoteId = storageRemoteId;
        }

        @Override
        public String toString() {
            return "VolumePath [storages=" + storages + ", storageRemoteId=" + storageRemoteId + "]";
        }

        public class Storage {
            @JSONField(name = "paths")
            private List<String> paths;

            @JSONField(name = "performance")
            private String performance;

            public List<String> getPaths() {
                return paths;
            }

            public void setPaths(List<String> paths) {
                this.paths = paths;
            }

            public String getPerformance() {
                return performance;
            }

            public void setPerformance(String performance) {
                this.performance = performance;
            }

            @Override
            public String toString() {
                return "Storage [paths=" + paths + ", performance=" + performance + "]";
            }

        }
    }

    public class UsageLimit {
        @JSONField(name = "cpu")
        private Integer cpu;

        @JSONField(name = "memory")
        private Integer memory;

        @JSONField(name = "storage")
        private Integer storage;

        @JSONField(name = "units")
        private Integer unit;

        public Integer getCpu() {
            return cpu;
        }

        public void setCpu(Integer cpu) {
            this.cpu = cpu;
        }

        public Integer getMemory() {
            return memory;
        }

        public void setMemory(Integer memory) {
            this.memory = memory;
        }

        public Integer getStorage() {
            return storage;
        }

        public void setStorage(Integer storage) {
            this.storage = storage;
        }

        public Integer getUnit() {
            return unit;
        }

        public void setUnit(Integer unit) {
            this.unit = unit;
        }

        @Override
        public String toString() {
            return "UsageLimit [cpu=" + cpu + ", memory=" + memory + ", storage=" + storage + ", unit=" + unit + "]";
        }

    }

}
