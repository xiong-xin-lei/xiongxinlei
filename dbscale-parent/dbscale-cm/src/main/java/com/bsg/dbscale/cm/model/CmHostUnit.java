package com.bsg.dbscale.cm.model;

import java.io.Serializable;
import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;

public class CmHostUnit implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    @JSONField(name = "id")
    private String id;

    @JSONField(name = "image")
    private Image image;

    @JSONField(name = "resources")
    private Resources resources;

    @JSONField(name = "ip")
    private String ip;

    @JSONField(name = "node")
    private Node node;

    @JSONField(name = "state")
    private String state;

    @JSONField(name = "pod_state")
    private String podState;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
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

    public Node getNode() {
        return node;
    }

    public void setNode(Node node) {
        this.node = node;
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

    @Override
    public String toString() {
        return "CmHostUnit [id=" + id + ", image=" + image + ", resources=" + resources + ", ip=" + ip + ", node="
                + node + ", state=" + state + ", podState=" + podState + "]";
    }

    public static class Image {
        @JSONField(name = "id")
        private String id;

        @JSONField(name = "major")
        private Integer major;

        @JSONField(name = "minor")
        private Integer minor;

        @JSONField(name = "patch")
        private Integer patch;

        @JSONField(name = "build")
        private Integer build;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public Integer getMajor() {
            return major;
        }

        public void setMajor(Integer major) {
            this.major = major;
        }

        public Integer getMinor() {
            return minor;
        }

        public void setMinor(Integer minor) {
            this.minor = minor;
        }

        public Integer getPatch() {
            return patch;
        }

        public void setPatch(Integer patch) {
            this.patch = patch;
        }

        public Integer getBuild() {
            return build;
        }

        public void setBuild(Integer build) {
            this.build = build;
        }

        @Override
        public String toString() {
            return "Image [id=" + id + ", major=" + major + ", minor=" + minor + ", patch=" + patch + ", build=" + build
                    + "]";
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

    public class Node {
        @JSONField(name = "id")
        private String id;

        @JSONField(name = "name")
        private String name;

        @JSONField(name = "host_ip")
        private String hostIp;

        @JSONField(name = "cluster")
        private CmClusterBase cluster;

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

        public CmClusterBase getCluster() {
            return cluster;
        }

        public void setCluster(CmClusterBase cluster) {
            this.cluster = cluster;
        }

        @Override
        public String toString() {
            return "Node [id=" + id + ", name=" + name + ", hostIp=" + hostIp + ", cluster=" + cluster + "]";
        }

    }

}
