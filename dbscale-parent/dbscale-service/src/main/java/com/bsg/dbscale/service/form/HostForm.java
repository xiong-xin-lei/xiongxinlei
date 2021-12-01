package com.bsg.dbscale.service.form;

import java.io.Serializable;

public class HostForm implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private String clusterId;
    private String ip;
    private String room;
    private String seat;
    private Integer maxUnitCnt;
    private Integer maxUsage;
    private String role;
    private String description;
    private VolumePathForm volumePath;

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

    public Integer getMaxUnitCnt() {
        return maxUnitCnt;
    }

    public void setMaxUnitCnt(Integer maxUnitCnt) {
        this.maxUnitCnt = maxUnitCnt;
    }

    public Integer getMaxUsage() {
        return maxUsage;
    }

    public void setMaxUsage(Integer maxUsage) {
        this.maxUsage = maxUsage;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public VolumePathForm getVolumePath() {
        return volumePath;
    }

    public void setVolumePath(VolumePathForm volumePath) {
        this.volumePath = volumePath;
    }

    @Override
    public String toString() {
        return "HostForm [clusterId=" + clusterId + ", ip=" + ip + ", room=" + room + ", seat=" + seat + ", maxUnitCnt="
                + maxUnitCnt + ", maxUsage=" + maxUsage + ", role=" + role + ", description=" + description
                + ", volumePath=" + volumePath + "]";
    }

}
