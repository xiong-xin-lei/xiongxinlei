package com.bsg.dbscale.service.dto;

import java.io.Serializable;
import java.util.Map;

public class CmhaServDTO extends ServDTO implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private Map<String, Topology> topology;

    public Map<String, Topology> getTopology() {
        return topology;
    }

    public void setTopology(Map<String, Topology> topology) {
        this.topology = topology;
    }

    @Override
    public String toString() {
        return "CmhaServDTO [topology=" + topology + "]";
    }

    public class Topology {
        private DisplayDTO state;
        private DisplayDTO role;
        private String masterIp;
        private DisplayDTO runningState;
        private DisplayDTO ioThread;
        private DisplayDTO sqlThread;
        private DisplayDTO replMode;
        private Long replErrCounter;
        private Boolean candidate;
        private Boolean maintain;
        private Boolean isolate;

        public DisplayDTO getState() {
            return state;
        }

        public void setState(DisplayDTO state) {
            this.state = state;
        }

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

        public DisplayDTO getRunningState() {
            return runningState;
        }

        public void setRunningState(DisplayDTO runningState) {
            this.runningState = runningState;
        }

        public DisplayDTO getIoThread() {
            return ioThread;
        }

        public void setIoThread(DisplayDTO ioThread) {
            this.ioThread = ioThread;
        }

        public DisplayDTO getSqlThread() {
            return sqlThread;
        }

        public void setSqlThread(DisplayDTO sqlThread) {
            this.sqlThread = sqlThread;
        }

        public DisplayDTO getReplMode() {
            return replMode;
        }

        public void setReplMode(DisplayDTO replMode) {
            this.replMode = replMode;
        }

        public Long getReplErrCounter() {
            return replErrCounter;
        }

        public void setReplErrCounter(Long replErrCounter) {
            this.replErrCounter = replErrCounter;
        }

        public Boolean getCandidate() {
            return candidate;
        }

        public void setCandidate(Boolean candidate) {
            this.candidate = candidate;
        }

        public Boolean getMaintain() {
            return maintain;
        }

        public void setMaintain(Boolean maintain) {
            this.maintain = maintain;
        }

        public Boolean getIsolate() {
            return isolate;
        }

        public void setIsolate(Boolean isolate) {
            this.isolate = isolate;
        }

        @Override
        public String toString() {
            return "Topology [state=" + state + ", role=" + role + ", masterIp=" + masterIp + ", runningState="
                    + runningState + ", ioThread=" + ioThread + ", sqlThread=" + sqlThread + ", replMode=" + replMode
                    + ", replErrCounter=" + replErrCounter + ", candidate=" + candidate + ", maintain=" + maintain
                    + ", isolate=" + isolate + "]";
        }

    }

}
