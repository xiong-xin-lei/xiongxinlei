package com.bsg.dbscale.cm.model;

import java.io.Serializable;

import com.alibaba.fastjson.annotation.JSONField;

public class CmRemoteStoragePool implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    @JSONField(name = "id")
    private String id;

    @JSONField(name = "name")
    private String name;

    @JSONField(name = "performance")
    private String performance;

    @JSONField(name = "capacity")
    private Long capacity;

    @JSONField(name = "used")
    private Long used;

    @JSONField(name = "unschedulable")
    private Boolean unschedulable;

    @JSONField(name = "desc")
    private String desc;

    @JSONField(name = "storage")
    private CmRemoteStorageBase remoteStorage;

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

    public String getPerformance() {
        return performance;
    }

    public void setPerformance(String performance) {
        this.performance = performance;
    }

    public Long getCapacity() {
        return capacity;
    }

    public void setCapacity(Long capacity) {
        this.capacity = capacity;
    }

    public Long getUsed() {
        return used;
    }

    public void setUsed(Long used) {
        this.used = used;
    }

    public Boolean getUnschedulable() {
        return unschedulable;
    }

    public void setUnschedulable(Boolean unschedulable) {
        this.unschedulable = unschedulable;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public CmRemoteStorageBase getRemoteStorage() {
        return remoteStorage;
    }

    public void setRemoteStorage(CmRemoteStorageBase remoteStorage) {
        this.remoteStorage = remoteStorage;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "CmRemoteStoragePool [id=" + id + ", name=" + name + ", performance=" + performance + ", capacity="
                + capacity + ", used=" + used + ", unschedulable=" + unschedulable + ", desc=" + desc
                + ", remoteStorage=" + remoteStorage + ", createdAt=" + createdAt + "]";
    }

}
