package com.bsg.dbscale.cm.model;

import java.io.Serializable;

import com.alibaba.fastjson.annotation.JSONField;

public class CmRemoteStorage extends CmRemoteStorageBase implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    @JSONField(name = "vendor")
    private String vendor;

    @JSONField(name = "model")
    private String model;

    @JSONField(name = "type")
    private String type;

    @JSONField(name = "capacity")
    private Long capacity;

    @JSONField(name = "used")
    private Long used;

    @JSONField(name = "unschedulable")
    private Boolean unschedulable;

    @JSONField(name = "state")
    private String state;

    @JSONField(name = "desc")
    private String desc;

    @JSONField(name = "auth")
    private Auth auth;

    @JSONField(name = "site")
    private CmSiteBase site;

    @JSONField(name = "created_at")
    private String createdAt;

    public String getVendor() {
        return vendor;
    }

    public void setVendor(String vendor) {
        this.vendor = vendor;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

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

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Auth getAuth() {
        return auth;
    }

    public void setAuth(Auth auth) {
        this.auth = auth;
    }

    public CmSiteBase getSite() {
        return site;
    }

    public void setSite(CmSiteBase site) {
        this.site = site;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return super.toString() + "CmRemoteStorage [vendor=" + vendor + ", model=" + model + ", type=" + type
                + ", capacity=" + capacity + ", used=" + used + ", unschedulable=" + unschedulable + ", state=" + state
                + ", desc=" + desc + ", auth=" + auth + ", site=" + site + ", createdAt=" + createdAt + "]";
    }

    public class Auth {
        @JSONField(name = "ip")
        private String ip;

        @JSONField(name = "port")
        private Integer port;

        public String getIp() {
            return ip;
        }

        public void setIp(String ip) {
            this.ip = ip;
        }

        public Integer getPort() {
            return port;
        }

        public void setPort(Integer port) {
            this.port = port;
        }

        @Override
        public String toString() {
            return "Auth [ip=" + ip + ", port=" + port + "]";
        }

    }

}
