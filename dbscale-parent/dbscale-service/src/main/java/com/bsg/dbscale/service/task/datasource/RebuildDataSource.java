package com.bsg.dbscale.service.task.datasource;

public class RebuildDataSource {

    private String hostRelateId;
    private Boolean force;

    public String getHostRelateId() {
        return hostRelateId;
    }

    public void setHostRelateId(String hostRelateId) {
        this.hostRelateId = hostRelateId;
    }

    public Boolean getForce() {
        return force;
    }

    public void setForce(Boolean force) {
        this.force = force;
    }

    @Override
    public String toString() {
        return "RebuildDataSource [hostRelateId=" + hostRelateId + ", force=" + force + "]";
    }

}
