package com.bsg.dbscale.cm.model;

import java.util.List;
import java.io.Serializable;

import com.alibaba.fastjson.annotation.JSONField;

public class CmNode implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    @JSONField(name = "metadata")
    private Metadata metadata;

    @JSONField(name = "status")
    private Status status;

    public Metadata getMetadata() {
        return metadata;
    }

    public void setMetadata(Metadata metadata) {
        this.metadata = metadata;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "CmNode [metadata=" + metadata + ", status=" + status + "]";
    }

    public class Metadata {

        @JSONField(name = "name")
        private String name;

        @JSONField(name = "creationTimestamp")
        private String creationTimestamp;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getCreationTimestamp() {
            return creationTimestamp;
        }

        public void setCreationTimestamp(String creationTimestamp) {
            this.creationTimestamp = creationTimestamp;
        }

        @Override
        public String toString() {
            return "Metadata [name=" + name + ", creationTimestamp=" + creationTimestamp + "]";
        }

    }

    public class Status {

        @JSONField(name = "capacity")
        private Resource capacity;

        @JSONField(name = "allocatable")
        private Resource allocatable;

        @JSONField(name = "conditions")
        private List<Condition> conditions;

        @JSONField(name = "addresses")
        private List<Address> addresses;

        @JSONField(name = "nodeInfo")
        private NodeInfo nodeInfo;

        public Resource getCapacity() {
            return capacity;
        }

        public void setCapacity(Resource capacity) {
            this.capacity = capacity;
        }

        public Resource getAllocatable() {
            return allocatable;
        }

        public void setAllocatable(Resource allocatable) {
            this.allocatable = allocatable;
        }

        public List<Condition> getConditions() {
            return conditions;
        }

        public void setConditions(List<Condition> conditions) {
            this.conditions = conditions;
        }

        public List<Address> getAddresses() {
            return addresses;
        }

        public void setAddresses(List<Address> addresses) {
            this.addresses = addresses;
        }

        public NodeInfo getNodeInfo() {
            return nodeInfo;
        }

        public void setNodeInfo(NodeInfo nodeInfo) {
            this.nodeInfo = nodeInfo;
        }

        @Override
        public String toString() {
            return "Status [capacity=" + capacity + ", allocatable=" + allocatable + ", conditions=" + conditions
                    + ", addresses=" + addresses + ", nodeInfo=" + nodeInfo + "]";
        }

        public class Resource {

            @JSONField(name = "milicpu")
            private Integer milicpu;

            @JSONField(name = "memory")
            private Long memory;

            @JSONField(name = "storage")
            private Long storage;

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

            public Long getStorage() {
                return storage;
            }

            public void setStorage(Long storage) {
                this.storage = storage;
            }

            @Override
            public String toString() {
                return "Resource [milicpu=" + milicpu + ", memory=" + memory + ", storage=" + storage + "]";
            }

            
        }

        public class Condition {

            @JSONField(name = "type")
            private String type;

            @JSONField(name = "status")
            private String status;

            @JSONField(name = "reason")
            private String reason;

            @JSONField(name = "message")
            private String message;

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public String getStatus() {
                return status;
            }

            public void setStatus(String status) {
                this.status = status;
            }

            public String getReason() {
                return reason;
            }

            public void setReason(String reason) {
                this.reason = reason;
            }

            public String getMessage() {
                return message;
            }

            public void setMessage(String message) {
                this.message = message;
            }

            @Override
            public String toString() {
                return "Condition [type=" + type + ", status=" + status + ", reason=" + reason + ", message=" + message
                        + "]";
            }

        }

        public class Address {

            @JSONField(name = "type")
            private String type;

            @JSONField(name = "address")
            private String address;

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public String getAddress() {
                return address;
            }

            public void setAddress(String address) {
                this.address = address;
            }

            @Override
            public String toString() {
                return "Address [type=" + type + ", address=" + address + "]";
            }

        }

        public class NodeInfo {

            @JSONField(name = "kernelVersion")
            private String kernelVersion;

            @JSONField(name = "osImage")
            private String osImage;

            @JSONField(name = "containerRuntimeVersion")
            private String containerRuntimeVersion;

            @JSONField(name = "kubeletVersion")
            private String kubeletVersion;

            @JSONField(name = "kubeProxyVersion")
            private String kubeProxyVersion;

            @JSONField(name = "operatingSystem")
            private String operatingSystem;

            @JSONField(name = "architecture")
            private String architecture;

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

            public String getContainerRuntimeVersion() {
                return containerRuntimeVersion;
            }

            public void setContainerRuntimeVersion(String containerRuntimeVersion) {
                this.containerRuntimeVersion = containerRuntimeVersion;
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

            public String getOperatingSystem() {
                return operatingSystem;
            }

            public void setOperatingSystem(String operatingSystem) {
                this.operatingSystem = operatingSystem;
            }

            public String getArchitecture() {
                return architecture;
            }

            public void setArchitecture(String architecture) {
                this.architecture = architecture;
            }

            @Override
            public String toString() {
                return "NodeInfo [kernelVersion=" + kernelVersion + ", osImage=" + osImage
                        + ", containerRuntimeVersion=" + containerRuntimeVersion + ", kubeletVersion=" + kubeletVersion
                        + ", kubeProxyVersion=" + kubeProxyVersion + ", operatingSystem=" + operatingSystem
                        + ", architecture=" + architecture + "]";
            }

        }

    }
}
