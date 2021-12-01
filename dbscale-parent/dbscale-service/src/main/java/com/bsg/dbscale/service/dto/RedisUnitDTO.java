package com.bsg.dbscale.service.dto;

import java.io.Serializable;

public class RedisUnitDTO extends UnitBaseDTO implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private Replication replication;

    public Replication getReplication() {
        return replication;
    }

    public void setReplication(Replication replication) {
        this.replication = replication;
    }

    @Override
    public String toString() {
        return "RedisUnitDTO [replication=" + replication + "]";
    }

    public class Replication {
        private String selfIp;
        private Integer selfPort;

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

        @Override
        public String toString() {
            return "Replication [selfIp=" + selfIp + ", selfPort=" + selfPort + "]";
        }

    }

    public class SingelReplication extends Replication {

    }

    public class UnSingelReplication extends Replication {
        private DisplayDTO role;
        private String masterIp;
        private Integer masterPort;
        private DisplayDTO state;

        public DisplayDTO getRole() {
            return role;
        }

        public void setRole(DisplayDTO role) {
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

        public DisplayDTO getState() {
            return state;
        }

        public void setState(DisplayDTO state) {
            this.state = state;
        }

        @Override
        public String toString() {
            return "UnSingelReplication [role=" + role + ", masterIp=" + masterIp + ", masterPort=" + masterPort
                    + ", state=" + state + "]";
        }

    }

}
