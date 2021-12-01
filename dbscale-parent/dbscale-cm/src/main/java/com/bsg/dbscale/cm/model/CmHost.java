package com.bsg.dbscale.cm.model;

import java.io.Serializable;
import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;

public class CmHost implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    @JSONField(name = "id")
    private String id;

    @JSONField(name = "name")
    private String name;

    @JSONField(name = "ip")
    private String ip;

    @JSONField(name = "desc")
    private String desc;

    @JSONField(name = "labels")
    private Label label;

    @JSONField(name = "spec")
    private Spec spec;

    @JSONField(name = "status")
    private Status status;

    @JSONField(name = "created_at")
    private String createdAt;

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

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Label getLabel() {
        return label;
    }

    public void setLabel(Label label) {
        this.label = label;
    }

    public Spec getSpec() {
        return spec;
    }

    public void setSpec(Spec spec) {
        this.spec = spec;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "CmHost [id=" + id + ", name=" + name + ", ip=" + ip + ", desc=" + desc + ", label=" + label + ", spec="
                + spec + ", status=" + status + ", createdAt=" + createdAt + "]";
    }

    public class Label {
        @JSONField(name = "room")
        private String room;

        @JSONField(name = "seat")
        private String seat;

        @JSONField(name = "cluster_id")
        private String clusterId;

        @JSONField(name = "cluster_name")
        private String clusterName;

        @JSONField(name = "storage_remote_id")
        private String storageRemoteId;

        @JSONField(name = "storage_remote_name")
        private String storageRemoteName;

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

        public String getClusterId() {
            return clusterId;
        }

        public void setClusterId(String clusterId) {
            this.clusterId = clusterId;
        }

        public String getClusterName() {
            return clusterName;
        }

        public void setClusterName(String clusterName) {
            this.clusterName = clusterName;
        }

        public String getStorageRemoteId() {
            return storageRemoteId;
        }

        public void setStorageRemoteId(String storageRemoteId) {
            this.storageRemoteId = storageRemoteId;
        }

        public String getStorageRemoteName() {
            return storageRemoteName;
        }

        public void setStorageRemoteName(String storageRemoteName) {
            this.storageRemoteName = storageRemoteName;
        }

        @Override
        public String toString() {
            return "Label [room=" + room + ", seat=" + seat + ", clusterId=" + clusterId + ", clusterName="
                    + clusterName + ", storageRemoteId=" + storageRemoteId + ", storageRemoteName=" + storageRemoteName
                    + "]";
        }

    }

    public class Spec {

        @JSONField(name = "usage_limit")
        private UsageLimit usageLimit;

        @JSONField(name = "role")
        private String role;

        @JSONField(name = "unschedulable")
        private Boolean unschedulable;

        @JSONField(name = "storages")
        private List<Storage> storages;

        public UsageLimit getUsageLimit() {
            return usageLimit;
        }

        public void setUsageLimit(UsageLimit usageLimit) {
            this.usageLimit = usageLimit;
        }

        public String getRole() {
            return role;
        }

        public void setRole(String role) {
            this.role = role;
        }

        public Boolean getUnschedulable() {
            return unschedulable;
        }

        public void setUnschedulable(Boolean unschedulable) {
            this.unschedulable = unschedulable;
        }

        public List<Storage> getStorages() {
            return storages;
        }

        public void setStorages(List<Storage> storages) {
            this.storages = storages;
        }

        @Override
        public String toString() {
            return "Spec [usageLimit=" + usageLimit + ", role=" + role + ", unschedulable=" + unschedulable
                    + ", storages=" + storages + "]";
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
                return "UsageLimit [cpu=" + cpu + ", memory=" + memory + ", storage=" + storage + ", unit=" + unit
                        + "]";
            }

        }

        public class Storage {

            @JSONField(name = "performance")
            private String performance;

            @JSONField(name = "paths")
            private List<String> paths;

            public String getPerformance() {
                return performance;
            }

            public void setPerformance(String performance) {
                this.performance = performance;
            }

            public List<String> getPaths() {
                return paths;
            }

            public void setPaths(List<String> paths) {
                this.paths = paths;
            }

            @Override
            public String toString() {
                return "Storage [performance=" + performance + ", paths=" + paths + "]";
            }

        }
    }

    public class Status {
        @JSONField(name = "phase")
        private String phase;

        @JSONField(name = "resource_soft_limit")
        private List<String> resourceSoftLimit;

        @JSONField(name = "allocatable")
        private Usage allocatable;

        @JSONField(name = "capacity")
        private Usage capacity;

        @JSONField(name = "node_info")
        private NodeInfo nodeInfo;

        @JSONField(name = "role")
        private String role;

        @JSONField(name = "unschedulable")
        private Boolean unschedulable;

        @JSONField(name = "node_condition")
        private String nodeCondition;

        @JSONField(name = "agent_condition")
        private String agentCondition;

        public String getPhase() {
            return phase;
        }

        public void setPhase(String phase) {
            this.phase = phase;
        }

        public List<String> getResourceSoftLimit() {
            return resourceSoftLimit;
        }

        public void setResourceSoftLimit(List<String> resourceSoftLimit) {
            this.resourceSoftLimit = resourceSoftLimit;
        }

        public Usage getAllocatable() {
            return allocatable;
        }

        public void setAllocatable(Usage allocatable) {
            this.allocatable = allocatable;
        }

        public Usage getCapacity() {
            return capacity;
        }

        public void setCapacity(Usage capacity) {
            this.capacity = capacity;
        }

        public NodeInfo getNodeInfo() {
            return nodeInfo;
        }

        public void setNodeInfo(NodeInfo nodeInfo) {
            this.nodeInfo = nodeInfo;
        }

        public String getRole() {
            return role;
        }

        public void setRole(String role) {
            this.role = role;
        }

        public Boolean getUnschedulable() {
            return unschedulable;
        }

        public void setUnschedulable(Boolean unschedulable) {
            this.unschedulable = unschedulable;
        }

        public String getNodeCondition() {
            return nodeCondition;
        }

        public void setNodeCondition(String nodeCondition) {
            this.nodeCondition = nodeCondition;
        }

        public String getAgentCondition() {
            return agentCondition;
        }

        public void setAgentCondition(String agentCondition) {
            this.agentCondition = agentCondition;
        }

        @Override
        public String toString() {
            return "Status [phase=" + phase + ", resourceSoftLimit=" + resourceSoftLimit + ", allocatable="
                    + allocatable + ", capacity=" + capacity + ", nodeInfo=" + nodeInfo + ", role=" + role
                    + ", unschedulable=" + unschedulable + ", nodeCondition=" + nodeCondition + ", agentCondition="
                    + agentCondition + "]";
        }

        public class Usage {

            @JSONField(name = "milicpu")
            private Integer milicpu;

            @JSONField(name = "memory")
            private Long memory;

            @JSONField(name = "pods")
            private Integer pod;

            @JSONField(name = "units")
            private Integer unit;

            @JSONField(name = "storages")
            private List<Storage> storages;

            public Integer getMilicpu() {
                return milicpu;
            }

            public void setMilicpu(Integer milicpu) {
                this.milicpu = milicpu;
            }

            public Long getMemory() {
                return memory;
            }

            public void setMemory(Long memory) {
                this.memory = memory;
            }

            public Integer getPod() {
                return pod;
            }

            public void setPod(Integer pod) {
                this.pod = pod;
            }

            public Integer getUnit() {
                return unit;
            }

            public void setUnit(Integer unit) {
                this.unit = unit;
            }

            public List<Storage> getStorages() {
                return storages;
            }

            public void setStorages(List<Storage> storages) {
                this.storages = storages;
            }

            @Override
            public String toString() {
                return "Usage [milicpu=" + milicpu + ", memory=" + memory + ", pod=" + pod + ", unit=" + unit
                        + ", storages=" + storages + "]";
            }

            public class Storage {

                @JSONField(name = "performance")
                private String performance;

                @JSONField(name = "name")
                private String name;

                @JSONField(name = "size")
                private Long size;

                public String getPerformance() {
                    return performance;
                }

                public void setPerformance(String performance) {
                    this.performance = performance;
                }

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }

                public Long getSize() {
                    return size;
                }

                public void setSize(Long size) {
                    this.size = size;
                }

                @Override
                public String toString() {
                    return "Storage [performance=" + performance + ", name=" + name + ", size=" + size + "]";
                }

            }
        }

        public class NodeInfo {

            @JSONField(name = "architecture")
            private String architecture;

            @JSONField(name = "container_runtime_version")
            private String containerRuntimeVersion;

            @JSONField(name = "kernel_version")
            private String kernelVersion;

            @JSONField(name = "os_image")
            private String osImage;

            @JSONField(name = "operating_system")
            private String operatingSystem;

            @JSONField(name = "kubelet_version")
            private String kubeletVersion;

            @JSONField(name = "kube_proxy_version")
            private String kubeProxyVersion;

            public String getArchitecture() {
                return architecture;
            }

            public void setArchitecture(String architecture) {
                this.architecture = architecture;
            }

            public String getContainerRuntimeVersion() {
                return containerRuntimeVersion;
            }

            public void setContainerRuntimeVersion(String containerRuntimeVersion) {
                this.containerRuntimeVersion = containerRuntimeVersion;
            }

            public String getKernelVersion() {
                return kernelVersion;
            }

            public void setKernelVersion(String kernelVersion) {
                this.kernelVersion = kernelVersion;
            }

            public String getOsImage() {
                return osImage;
            }

            public void setOsImage(String osImage) {
                this.osImage = osImage;
            }

            public String getOperatingSystem() {
                return operatingSystem;
            }

            public void setOperatingSystem(String operatingSystem) {
                this.operatingSystem = operatingSystem;
            }

            public String getKubeletVersion() {
                return kubeletVersion;
            }

            public void setKubeletVersion(String kubeletVersion) {
                this.kubeletVersion = kubeletVersion;
            }

            public String getKubeProxyVersion() {
                return kubeProxyVersion;
            }

            public void setKubeProxyVersion(String kubeProxyVersion) {
                this.kubeProxyVersion = kubeProxyVersion;
            }

            @Override
            public String toString() {
                return "NodeInfo [architecture=" + architecture + ", containerRuntimeVersion=" + containerRuntimeVersion
                        + ", kernelVersion=" + kernelVersion + ", osImage=" + osImage + ", operatingSystem="
                        + operatingSystem + ", kubeletVersion=" + kubeletVersion + ", kubeProxyVersion="
                        + kubeProxyVersion + "]";
            }

        }

    }
}
