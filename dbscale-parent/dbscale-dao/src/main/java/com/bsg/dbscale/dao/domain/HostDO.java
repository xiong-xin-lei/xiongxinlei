package com.bsg.dbscale.dao.domain;

import java.io.Serializable;
import java.util.Date;

public class HostDO implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private String id;
    private String clusterId;
    private String ip;
    private String room;
    private String seat;
    private String hddPath;
    private String ssdPath;
    private String remoteStorageId;
    private Integer maxUsage;
    private String role;
    private String relateId;
    private String description;
    private Date gmtCreate;
    private String creator;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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

    public String getHddPath() {
        return hddPath;
    }

    public void setHddPath(String hddPath) {
        this.hddPath = hddPath;
    }

    public String getSsdPath() {
        return ssdPath;
    }

    public void setSsdPath(String ssdPath) {
        this.ssdPath = ssdPath;
    }

    public String getRemoteStorageId() {
        return remoteStorageId;
    }

    public void setRemoteStorageId(String remoteStorageId) {
        this.remoteStorageId = remoteStorageId;
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

    public String getRelateId() {
        return relateId;
    }

    public void setRelateId(String relateId) {
        this.relateId = relateId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    @Override
    public String toString() {
        return "HostDO [id=" + id + ", clusterId=" + clusterId + ", ip=" + ip + ", room=" + room + ", seat=" + seat
                + ", hddPath=" + hddPath + ", ssdPath=" + ssdPath + ", remoteStorageId=" + remoteStorageId
                + ", maxUsage=" + maxUsage + ", role=" + role + ", relateId=" + relateId + ", description="
                + description + ", gmtCreate=" + gmtCreate + ", creator=" + creator + "]";
    }

}
