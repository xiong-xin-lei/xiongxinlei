package com.bsg.dbscale.service.form;

import java.io.Serializable;
import java.util.List;

public class NetworkForm implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * 业务区
     */
    private String businessAreaId;

    /**
     * 网段名
     */
    private String name;

    /**
     * 起始地址
     */
    private String startIp;

    /**
     * 结束地址
     */
    private String endIp;

    /**
     * 网关
     */
    private String gateway;

    /**
     * 掩码
     */
    private Integer netmask;

    /**
     * VLAN ID
     */
    private Integer vlan;

    /**
     * 拓扑
     */
    private List<String> topologys;

    /**
     * 是否可用
     */
    private Boolean enabled;

    /**
     * 描述
     */
    private String description;

    public String getBusinessAreaId() {
        return businessAreaId;
    }

    public void setBusinessAreaId(String businessAreaId) {
        this.businessAreaId = businessAreaId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStartIp() {
        return startIp;
    }

    public void setStartIp(String startIp) {
        this.startIp = startIp;
    }

    public String getEndIp() {
        return endIp;
    }

    public void setEndIp(String endIp) {
        this.endIp = endIp;
    }

    public String getGateway() {
        return gateway;
    }

    public void setGateway(String gateway) {
        this.gateway = gateway;
    }

    public Integer getNetmask() {
        return netmask;
    }

    public void setNetmask(Integer netmask) {
        this.netmask = netmask;
    }

    public Integer getVlan() {
        return vlan;
    }

    public void setVlan(Integer vlan) {
        this.vlan = vlan;
    }

    public List<String> getTopologys() {
        return topologys;
    }

    public void setTopologys(List<String> topologys) {
        this.topologys = topologys;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "NetworkForm [businessAreaId=" + businessAreaId + ", name=" + name + ", startIp=" + startIp + ", endIp="
                + endIp + ", gateway=" + gateway + ", netmask=" + netmask + ", vlan=" + vlan + ", topologys="
                + topologys + ", enabled=" + enabled + ", description=" + description + "]";
    }

}
