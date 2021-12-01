package com.bsg.dbscale.service.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class NetworkDTO extends IdentificationDTO implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private String startIp;
    private String endIp;
    private String gateway;
    private Integer netmask;
    private Integer vlan;
    private Integer ipTotal;
    private Integer ipUsed;
    private List<DisplayDTO> topologys;
    private Boolean enabled;
    private String description;
    private IdentificationDTO site;
    private IdentificationDTO businessArea;
    private InfoDTO created;

    public NetworkDTO() {
        topologys = new ArrayList<>();
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

    public Integer getIpTotal() {
        return ipTotal;
    }

    public void setIpTotal(Integer ipTotal) {
        this.ipTotal = ipTotal;
    }

    public Integer getIpUsed() {
        return ipUsed;
    }

    public void setIpUsed(Integer ipUsed) {
        this.ipUsed = ipUsed;
    }

    public List<DisplayDTO> getTopologys() {
        return topologys;
    }

    public void setTopologys(List<DisplayDTO> topologys) {
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

    public IdentificationDTO getSite() {
        return site;
    }

    public void setSite(IdentificationDTO site) {
        this.site = site;
    }

    public IdentificationDTO getBusinessArea() {
        return businessArea;
    }

    public void setBusinessArea(IdentificationDTO businessArea) {
        this.businessArea = businessArea;
    }

    public InfoDTO getCreated() {
        return created;
    }

    public void setCreated(InfoDTO created) {
        this.created = created;
    }

    @Override
    public String toString() {
        return super.toString() + "NetworkDTO [startIp=" + startIp + ", endIp=" + endIp + ", gateway=" + gateway
                + ", netmask=" + netmask + ", vlan=" + vlan + ", ipTotal=" + ipTotal + ", ipUsed=" + ipUsed
                + ", topologys=" + topologys + ", enabled=" + enabled + ", description=" + description + ", site="
                + site + ", businessArea=" + businessArea + ", created=" + created + "]";
    }

}
