package com.bsg.dbscale.cm.model;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;

public class CmService implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    @JSONField(name = "id")
    private String id;

    @JSONField(name = "name")
    private String name;

    @JSONField(name = "type")
    private String type;

    @JSONField(name = "group_name")
    private String groupName;

    @JSONField(name = "group_type")
    private String groupType;

    @JSONField(name = "status")
    private Status status;

    @JSONField(name = "spec")
    private Spec spec;

    @JSONField(name = "site")
    private CmSiteBase site;

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getGroupType() {
        return groupType;
    }

    public void setGroupType(String groupType) {
        this.groupType = groupType;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Spec getSpec() {
        return spec;
    }

    public void setSpec(Spec spec) {
        this.spec = spec;
    }

    public CmSiteBase getSite() {
        return site;
    }

    public void setSite(CmSiteBase site) {
        this.site = site;
    }

    @Override
    public String toString() {
        return "CmService [id=" + id + ", name=" + name + ", type=" + type + ", groupName=" + groupName + ", groupType="
                + groupType + ", status=" + status + ", spec=" + spec + ", site=" + site + "]";
    }

    public class Spec {
        @JSONField(name = "arch")
        private CmServiceArchBase arch;

        @JSONField(name = "image")
        private CmImageBase image;

        @JSONField(name = "ports")
        private List<Port> ports;

        @JSONField(name = "options")
        private JSONObject options;

        @JSONField(name = "unit")
        private Unit unit;

        public CmServiceArchBase getArch() {
            return arch;
        }

        public void setArch(CmServiceArchBase arch) {
            this.arch = arch;
        }

        public CmImageBase getImage() {
            return image;
        }

        public void setImage(CmImageBase image) {
            this.image = image;
        }

        public List<Port> getPorts() {
            return ports;
        }

        public void setPorts(List<Port> ports) {
            this.ports = ports;
        }

        public JSONObject getOptions() {
            return options;
        }

        public void setOptions(JSONObject options) {
            this.options = options;
        }

        public Unit getUnit() {
            return unit;
        }

        public void setUnit(Unit unit) {
            this.unit = unit;
        }

        @Override
        public String toString() {
            return "Spec [arch=" + arch + ", image=" + image + ", ports=" + ports + ", options=" + options + ", unit="
                    + unit + "]";
        }

        public class Unit {

            @JSONField(name = "resources")
            private Resources resources;

            public Resources getResources() {
                return resources;
            }

            public void setResources(Resources resources) {
                this.resources = resources;
            }

            @Override
            public String toString() {
                return "Unit [resources=" + resources + "]";
            }

            public class Resources {

                @JSONField(name = "requests")
                private Requests requests;

                public Requests getRequests() {
                    return requests;
                }

                public void setRequests(Requests requests) {
                    this.requests = requests;
                }

                @Override
                public String toString() {
                    return "Resources [requests=" + requests + "]";
                }

                public class Requests extends com.bsg.dbscale.cm.model.CmService.Resources {
                }
            }
        }
    }

    public static class Port {
        @JSONField(name = "name")
        private String name;

        @JSONField(name = "port")
        private Integer port;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Integer getPort() {
            return port;
        }

        public void setPort(Integer port) {
            this.port = port;
        }

        @Override
        public String toString() {
            return "Port [name=" + name + ", port=" + port + "]";
        }

    }

    public static class Resources {
        @JSONField(name = "milicpu")
        private Integer milicpu;

        @JSONField(name = "memory")
        private Long memory;

        @JSONField(name = "storage")
        private Storage storage;

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

        public Storage getStorage() {
            return storage;
        }

        public void setStorage(Storage storage) {
            this.storage = storage;
        }

        @Override
        public String toString() {
            return "Resources [milicpu=" + milicpu + ", memory=" + memory + ", storage=" + storage + "]";
        }

        public class Storage {
            @JSONField(name = "mode")
            private String mode;

            @JSONField(name = "volumepath")
            private VolumePath volumePath;

            @JSONField(name = "volumes")
            private List<Volume> volumes;

            public String getMode() {
                return mode;
            }

            public void setMode(String mode) {
                this.mode = mode;
            }

            public VolumePath getVolumePath() {
                return volumePath;
            }

            public void setVolumePath(VolumePath volumePath) {
                this.volumePath = volumePath;
            }

            public List<Volume> getVolumes() {
                return volumes;
            }

            public void setVolumes(List<Volume> volumes) {
                this.volumes = volumes;
            }

            @Override
            public String toString() {
                return "Storage [mode=" + mode + ", volumePath=" + volumePath + ", volumes=" + volumes + "]";
            }

            public class Volume {
                @JSONField(name = "type")
                private String type;

                @JSONField(name = "capacity")
                private Long capacity;

                public String getType() {
                    return type;
                }

                public void setType(String type) {
                    this.type = type;
                }

                public Long getCapacity() {
                    return capacity;
                }

                public void setCapacity(Long capacity) {
                    this.capacity = capacity;
                }

                @Override
                public String toString() {
                    return "Volume [type=" + type + ", capacity=" + capacity + "]";
                }

            }

            public class VolumePath {

                @JSONField(name = "type")
                private String type;

                @JSONField(name = "performance")
                private String performance;

                public String getType() {
                    return type;
                }

                public void setType(String type) {
                    this.type = type;
                }

                public String getPerformance() {
                    return performance;
                }

                public void setPerformance(String performance) {
                    this.performance = performance;
                }

                @Override
                public String toString() {
                    return "VolumePath [type=" + type + ", performance=" + performance + "]";
                }

            }
        }
    }

    public class Status {
        @JSONField(name = "phase")
        private String phase;

        @JSONField(name = "err_msg")
        private String errMsg;

        @JSONField(name = "state")
        private String state;

        @JSONField(name = "arch")
        private CmServiceArchBase arch;

        @JSONField(name = "ports")
        private List<Port> ports;

        @JSONField(name = "units")
        private List<Unit> units;

        public String getPhase() {
            return phase;
        }

        public void setPhase(String phase) {
            this.phase = phase;
        }

        public String getErrMsg() {
            return errMsg;
        }

        public void setErrMsg(String errMsg) {
            this.errMsg = errMsg;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public CmServiceArchBase getArch() {
            return arch;
        }

        public void setArch(CmServiceArchBase arch) {
            this.arch = arch;
        }

        public List<Port> getPorts() {
            return ports;
        }

        public void setPorts(List<Port> ports) {
            this.ports = ports;
        }

        public List<Unit> getUnits() {
            return units;
        }

        public void setUnits(List<Unit> units) {
            this.units = units;
        }

        @Override
        public String toString() {
            return "Status [phase=" + phase + ", errMsg=" + errMsg + ", state=" + state + ", arch=" + arch + ", ports="
                    + ports + ", units=" + units + "]";
        }

        public class Unit {
            @JSONField(name = "id")
            private String id;

            @JSONField(name = "namespace")
            private String namespace;

            @JSONField(name = "image")
            private CmImageBase image;

            @JSONField(name = "resources")
            private Resources resources;

            @JSONField(name = "ip")
            private String ip;

            @JSONField(name = "action")
            private String action;

            @JSONField(name = "node")
            private Node node;

            @JSONField(name = "node_condition")
            private String nodeCondition;

            @JSONField(name = "state")
            private String state;

            @JSONField(name = "pod_state")
            private String podState;

            @JSONField(name = "init_start")
            private Boolean initStart;

            @JSONField(name = "replication")
            private Map<String, Replication> replication;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getNamespace() {
                return namespace;
            }

            public void setNamespace(String namespace) {
                this.namespace = namespace;
            }

            public CmImageBase getImage() {
                return image;
            }

            public void setImage(CmImageBase image) {
                this.image = image;
            }

            public Resources getResources() {
                return resources;
            }

            public void setResources(Resources resources) {
                this.resources = resources;
            }

            public String getIp() {
                return ip;
            }

            public void setIp(String ip) {
                this.ip = ip;
            }

            public String getAction() {
                return action;
            }

            public void setAction(String action) {
                this.action = action;
            }

            public Node getNode() {
                return node;
            }

            public void setNode(Node node) {
                this.node = node;
            }

            public String getNodeCondition() {
                return nodeCondition;
            }

            public void setNodeCondition(String nodeCondition) {
                this.nodeCondition = nodeCondition;
            }

            public String getState() {
                return state;
            }

            public void setState(String state) {
                this.state = state;
            }

            public String getPodState() {
                return podState;
            }

            public void setPodState(String podState) {
                this.podState = podState;
            }

            public Boolean getInitStart() {
                return initStart;
            }

            public void setInitStart(Boolean initStart) {
                this.initStart = initStart;
            }

            public Map<String, Replication> getReplication() {
                return replication;
            }

            public void setReplication(Map<String, Replication> replication) {
                this.replication = replication;
            }

            @Override
            public String toString() {
                return "Unit [id=" + id + ", namespace=" + namespace + ", image=" + image + ", resources=" + resources
                        + ", ip=" + ip + ", action=" + action + ", node=" + node + ", nodeCondition=" + nodeCondition
                        + ", state=" + state + ", podState=" + podState + ", initStart=" + initStart + ", replication="
                        + replication + "]";
            }

            public class Node {
                @JSONField(name = "id")
                private String id;

                @JSONField(name = "name")
                private String name;

                @JSONField(name = "host_ip")
                private String hostIp;

                @JSONField(name = "cluster")
                private Cluster cluster;

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

                public String getHostIp() {
                    return hostIp;
                }

                public void setHostIp(String hostIp) {
                    this.hostIp = hostIp;
                }

                public Cluster getCluster() {
                    return cluster;
                }

                public void setCluster(Cluster cluster) {
                    this.cluster = cluster;
                }

                @Override
                public String toString() {
                    return "Node [id=" + id + ", name=" + name + ", hostIp=" + hostIp + ", cluster=" + cluster + "]";
                }

                public class Cluster {
                    @JSONField(name = "id")
                    private String id;

                    @JSONField(name = "name")
                    private String name;

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

                    @Override
                    public String toString() {
                        return "Cluster [id=" + id + ", name=" + name + "]";
                    }

                }
            }

            public class Replication {
                @JSONField(name = "role")
                private String role;

                @JSONField(name = "master_ip")
                private String masterIp;

                @JSONField(name = "master_port")
                private Integer masterPort;

                @JSONField(name = "self_ip")
                private String selfIp;

                @JSONField(name = "self_port")
                private Integer selfPort;

                @JSONField(name = "slave_io_running")
                private String slaveIoRunning;

                @JSONField(name = "slave_io_state")
                private String slaveIoState;

                @JSONField(name = "slave_sql_running")
                private String slaveSqlRunning;

                @JSONField(name = "slave_sql_running_state")
                private String slaveSqlRunningState;

                @JSONField(name = "seconds_behind_master")
                private Integer secondsBehindMaster;

                @JSONField(name = "master_log_file")
                private String masterLogFile;

                @JSONField(name = "relay_master_log_file")
                private String relayMasterLogFile;

                @JSONField(name = "read_master_log_pos")
                private Integer readMasterLogPos;

                @JSONField(name = "exec_master_log_pos")
                private Integer execMasterLogPos;

                @JSONField(name = "relay_log_file")
                private String relayLogFile;

                @JSONField(name = "relay_log_pos")
                private Integer relayLogPos;

                @JSONField(name = "last_io_error")
                private String lastIoError;

                @JSONField(name = "last_sql_error")
                private String lastSqlError;

                @JSONField(name = "arch_mode")
                private String archMode;

                @JSONField(name = "replica_link_status")
                private String replicaLinkStatus;

                public String getRole() {
                    return role;
                }

                public void setRole(String role) {
                    this.role = role;
                }

                public String getMasterIp() {
                    return masterIp;
                }

                public void setMasterIp(String masterIp) {
                    this.masterIp = masterIp;
                }

                public Integer getMasterPort() {
                    return masterPort;
                }

                public void setMasterPort(Integer masterPort) {
                    this.masterPort = masterPort;
                }

                public String getSelfIp() {
                    return selfIp;
                }

                public void setSelfIp(String selfIp) {
                    this.selfIp = selfIp;
                }

                public Integer getSelfPort() {
                    return selfPort;
                }

                public void setSelfPort(Integer selfPort) {
                    this.selfPort = selfPort;
                }

                public String getSlaveIoRunning() {
                    return slaveIoRunning;
                }

                public void setSlaveIoRunning(String slaveIoRunning) {
                    this.slaveIoRunning = slaveIoRunning;
                }

                public String getSlaveIoState() {
                    return slaveIoState;
                }

                public void setSlaveIoState(String slaveIoState) {
                    this.slaveIoState = slaveIoState;
                }

                public String getSlaveSqlRunning() {
                    return slaveSqlRunning;
                }

                public void setSlaveSqlRunning(String slaveSqlRunning) {
                    this.slaveSqlRunning = slaveSqlRunning;
                }

                public String getSlaveSqlRunningState() {
                    return slaveSqlRunningState;
                }

                public void setSlaveSqlRunningState(String slaveSqlRunningState) {
                    this.slaveSqlRunningState = slaveSqlRunningState;
                }

                public Integer getSecondsBehindMaster() {
                    return secondsBehindMaster;
                }

                public void setSecondsBehindMaster(Integer secondsBehindMaster) {
                    this.secondsBehindMaster = secondsBehindMaster;
                }

                public String getMasterLogFile() {
                    return masterLogFile;
                }

                public void setMasterLogFile(String masterLogFile) {
                    this.masterLogFile = masterLogFile;
                }

                public String getRelayMasterLogFile() {
                    return relayMasterLogFile;
                }

                public void setRelayMasterLogFile(String relayMasterLogFile) {
                    this.relayMasterLogFile = relayMasterLogFile;
                }

                public Integer getReadMasterLogPos() {
                    return readMasterLogPos;
                }

                public void setReadMasterLogPos(Integer readMasterLogPos) {
                    this.readMasterLogPos = readMasterLogPos;
                }

                public Integer getExecMasterLogPos() {
                    return execMasterLogPos;
                }

                public void setExecMasterLogPos(Integer execMasterLogPos) {
                    this.execMasterLogPos = execMasterLogPos;
                }

                public String getRelayLogFile() {
                    return relayLogFile;
                }

                public void setRelayLogFile(String relayLogFile) {
                    this.relayLogFile = relayLogFile;
                }

                public Integer getRelayLogPos() {
                    return relayLogPos;
                }

                public void setRelayLogPos(Integer relayLogPos) {
                    this.relayLogPos = relayLogPos;
                }

                public String getLastIoError() {
                    return lastIoError;
                }

                public void setLastIoError(String lastIoError) {
                    this.lastIoError = lastIoError;
                }

                public String getLastSqlError() {
                    return lastSqlError;
                }

                public void setLastSqlError(String lastSqlError) {
                    this.lastSqlError = lastSqlError;
                }

                public String getArchMode() {
                    return archMode;
                }

                public void setArchMode(String archMode) {
                    this.archMode = archMode;
                }

                public String getReplicaLinkStatus() {
                    return replicaLinkStatus;
                }

                public void setReplicaLinkStatus(String replicaLinkStatus) {
                    this.replicaLinkStatus = replicaLinkStatus;
                }

                @Override
                public String toString() {
                    return "Replication [role=" + role + ", masterIp=" + masterIp + ", masterPort=" + masterPort
                            + ", selfIp=" + selfIp + ", selfPort=" + selfPort + ", slaveIoRunning=" + slaveIoRunning
                            + ", slaveIoState=" + slaveIoState + ", slaveSqlRunning=" + slaveSqlRunning
                            + ", slaveSqlRunningState=" + slaveSqlRunningState + ", secondsBehindMaster="
                            + secondsBehindMaster + ", masterLogFile=" + masterLogFile + ", relayMasterLogFile="
                            + relayMasterLogFile + ", readMasterLogPos=" + readMasterLogPos + ", execMasterLogPos="
                            + execMasterLogPos + ", relayLogFile=" + relayLogFile + ", relayLogPos=" + relayLogPos
                            + ", lastIoError=" + lastIoError + ", lastSqlError=" + lastSqlError + ", archMode="
                            + archMode + ", replicaLinkStatus=" + replicaLinkStatus + "]";
                }

            }
        }
    }

}
