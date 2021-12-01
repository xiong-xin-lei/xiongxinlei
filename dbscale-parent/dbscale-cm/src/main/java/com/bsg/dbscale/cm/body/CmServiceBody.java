package com.bsg.dbscale.cm.body;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.annotation.JSONField;

public class CmServiceBody implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    @JSONField(name = "name")
    private String name;

    @JSONField(name = "group_name")
    private String group;

    @JSONField(name = "group_type")
    private String groupType;

    @JSONField(name = "arch")
    private String arch;

    @JSONField(name = "desc")
    private String desc;

    @JSONField(name = "spec")
    private Spec spec;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getGroupType() {
        return groupType;
    }

    public void setGroupType(String groupType) {
        this.groupType = groupType;
    }

    public String getArch() {
        return arch;
    }

    public void setArch(String arch) {
        this.arch = arch;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Spec getSpec() {
        return spec;
    }

    public void setSpec(Spec spec) {
        this.spec = spec;
    }

    @Override
    public String toString() {
        return "CmServiceBody [name=" + name + ", group=" + group + ", groupType=" + groupType + ", arch=" + arch
                + ", desc=" + desc + ", spec=" + spec + "]";
    }

    public class Spec {

        @JSONField(name = "image")
        private Image image;

        @JSONField(name = "arch")
        private Arch arch;

        @JSONField(name = "ports")
        private List<Port> ports;

        @JSONField(name = "options")
        private Map<String, Object> options;

        @JSONField(name = "conditions")
        private Conditions conditions;

        @JSONField(name = "unit")
        private Unit unit;

        public Image getImage() {
            return image;
        }

        public void setImage(Image image) {
            this.image = image;
        }

        public Arch getArch() {
            return arch;
        }

        public void setArch(Arch arch) {
            this.arch = arch;
        }

        public List<Port> getPorts() {
            return ports;
        }

        public void setPorts(List<Port> ports) {
            this.ports = ports;
        }

        public Map<String, Object> getOptions() {
            return options;
        }

        public void setOptions(Map<String, Object> options) {
            this.options = options;
        }

        public Conditions getConditions() {
            return conditions;
        }

        public void setConditions(Conditions conditions) {
            this.conditions = conditions;
        }

        public Unit getUnit() {
            return unit;
        }

        public void setUnit(Unit unit) {
            this.unit = unit;
        }

        @Override
        public String toString() {
            return "Spec [image=" + image + ", arch=" + arch + ", ports=" + ports + ", options=" + options
                    + ", conditions=" + conditions + ", unit=" + unit + "]";
        }

        public class Image {
            @JSONField(name = "id")
            private String id;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            @Override
            public String toString() {
                return "Image [id=" + id + "]";
            }

        }

        public class Arch {
            @JSONField(name = "mode")
            private String mode;

            @JSONField(name = "replicas")
            private Integer replicas;

            public String getMode() {
                return mode;
            }

            public void setMode(String mode) {
                this.mode = mode;
            }

            public Integer getReplicas() {
                return replicas;
            }

            public void setReplicas(Integer replicas) {
                this.replicas = replicas;
            }

            @Override
            public String toString() {
                return "Arch [mode=" + mode + ", replicas=" + replicas + "]";
            }

        }

        public class Port {
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

        public class Conditions {
            @JSONField(name = "cluster")
            private Info cluster;

            @JSONField(name = "host")
            private Info host;

            @JSONField(name = "storage_remote")
            private Info remoteStorage;

            @JSONField(name = "network")
            private Info network;

            public Info getCluster() {
                return cluster;
            }

            public void setCluster(Info cluster) {
                this.cluster = cluster;
            }

            public Info getHost() {
                return host;
            }

            public void setHost(Info host) {
                this.host = host;
            }

            public Info getRemoteStorage() {
                return remoteStorage;
            }

            public void setRemoteStorage(Info remoteStorage) {
                this.remoteStorage = remoteStorage;
            }

            public Info getNetwork() {
                return network;
            }

            public void setNetwork(Info network) {
                this.network = network;
            }

            @Override
            public String toString() {
                return "Conditions [cluster=" + cluster + ", host=" + host + ", remoteStorage=" + remoteStorage
                        + ", network=" + network + "]";
            }

            public class Info {
                @JSONField(name = "candidates_id")
                private List<String> id;

                @JSONField(name = "high_availability")
                private Boolean highAvailable;

                public List<String> getId() {
                    return id;
                }

                public void setId(List<String> id) {
                    this.id = id;
                }

                public Boolean getHighAvailable() {
                    return highAvailable;
                }

                public void setHighAvailable(Boolean highAvailable) {
                    this.highAvailable = highAvailable;
                }

                @Override
                public String toString() {
                    return "Info [id=" + id + ", highAvailable=" + highAvailable + "]";
                }

            }

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
                private Request request;

                public Request getRequest() {
                    return request;
                }

                public void setRequest(Request request) {
                    this.request = request;
                }

                @Override
                public String toString() {
                    return "Resources [request=" + request + "]";
                }

                public class Request {
                    @JSONField(name = "miliCpu")
                    private Integer miliCpu;

                    @JSONField(name = "memory")
                    private Long memory;

                    @JSONField(name = "storage")
                    private Storage storage;

                    public Integer getMiliCpu() {
                        return miliCpu;
                    }

                    public void setMiliCpu(Integer miliCpu) {
                        this.miliCpu = miliCpu;
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
                        return "Request [miliCpu=" + miliCpu + ", memory=" + memory + ", storage=" + storage + "]";
                    }

                    public class Storage {
                        @JSONField(name = "mode")
                        private String mode;

                        @JSONField(name = "volumes")
                        private List<Volume> volumes;

                        @JSONField(name = "volumepath")
                        private VolumePath volumePath;

                        public String getMode() {
                            return mode;
                        }

                        public void setMode(String mode) {
                            this.mode = mode;
                        }

                        public List<Volume> getVolumes() {
                            return volumes;
                        }

                        public void setVolumes(List<Volume> volumes) {
                            this.volumes = volumes;
                        }

                        public VolumePath getVolumePath() {
                            return volumePath;
                        }

                        public void setVolumePath(VolumePath volumePath) {
                            this.volumePath = volumePath;
                        }

                        @Override
                        public String toString() {
                            return "Storage [mode=" + mode + ", volumes=" + volumes + ", volumePath=" + volumePath
                                    + "]";
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
            }
        }
    }

}
