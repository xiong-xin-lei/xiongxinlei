package com.bsg.dbscale.service.dto;

import java.io.Serializable;

public class MysqlUnitDTO extends UnitBaseDTO implements Serializable {

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
        return "MysqlUnitDTO [replication=" + replication + "]";
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
        private DisplayDTO slaveRunning;
        private DisplayDTO slaveIoRunning;
        private String slaveIoState;
        private DisplayDTO slaveSqlRunning;
        private String slaveSqlState;
        private Integer secondsBehindMaster;
        private String masterLogFile;
        private String relayMasterLogFile;
        private Integer readMasterLogPos;
        private Integer execMasterLogPos;
        private String relayLogFile;
        private Integer relayLogPos;
        private String lastIoError;
        private String lastSqlError;
        private DisplayDTO archMode;

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

        public DisplayDTO getSlaveRunning() {
            return slaveRunning;
        }

        public void setSlaveRunning(DisplayDTO slaveRunning) {
            this.slaveRunning = slaveRunning;
        }

        public DisplayDTO getSlaveIoRunning() {
            return slaveIoRunning;
        }

        public void setSlaveIoRunning(DisplayDTO slaveIoRunning) {
            this.slaveIoRunning = slaveIoRunning;
        }

        public String getSlaveIoState() {
            return slaveIoState;
        }

        public void setSlaveIoState(String slaveIoState) {
            this.slaveIoState = slaveIoState;
        }

        public DisplayDTO getSlaveSqlRunning() {
            return slaveSqlRunning;
        }

        public void setSlaveSqlRunning(DisplayDTO slaveSqlRunning) {
            this.slaveSqlRunning = slaveSqlRunning;
        }

        public String getSlaveSqlState() {
            return slaveSqlState;
        }

        public void setSlaveSqlState(String slaveSqlState) {
            this.slaveSqlState = slaveSqlState;
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

        public DisplayDTO getArchMode() {
            return archMode;
        }

        public void setArchMode(DisplayDTO archMode) {
            this.archMode = archMode;
        }

        @Override
        public String toString() {
            return "UnSingelReplication [role=" + role + ", masterIp=" + masterIp + ", masterPort=" + masterPort
                    + ", slaveRunning=" + slaveRunning + ", slaveIoRunning=" + slaveIoRunning + ", slaveIoState="
                    + slaveIoState + ", slaveSqlRunning=" + slaveSqlRunning + ", slaveSqlState=" + slaveSqlState
                    + ", secondsBehindMaster=" + secondsBehindMaster + ", masterLogFile=" + masterLogFile
                    + ", relayMasterLogFile=" + relayMasterLogFile + ", readMasterLogPos=" + readMasterLogPos
                    + ", execMasterLogPos=" + execMasterLogPos + ", relayLogFile=" + relayLogFile + ", relayLogPos="
                    + relayLogPos + ", lastIoError=" + lastIoError + ", lastSqlError=" + lastSqlError + ", archMode="
                    + archMode + "]";
        }

    }

}
