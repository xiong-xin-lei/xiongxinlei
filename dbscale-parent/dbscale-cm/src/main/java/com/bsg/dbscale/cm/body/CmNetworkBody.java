package com.bsg.dbscale.cm.body;

import java.io.Serializable;
import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;

public class CmNetworkBody implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    @JSONField(name = "site_id")
    private String siteId;

    @JSONField(name = "zone")
    private String zone;

    @JSONField(name = "name")
    private String name;

    @JSONField(name = "topology")
    private List<String> topologys;

    @JSONField(name = "desc")
    private String desc;

    @JSONField(name = "unschedulable")
    private Boolean unschedulable;

    @JSONField(name = "ip_summary")
    private IpSummary ipSummary;

    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }

    public String getZone() {
        return zone;
    }

    public void setZone(String zone) {
        this.zone = zone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getTopologys() {
        return topologys;
    }

    public void setTopologys(List<String> topologys) {
        this.topologys = topologys;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Boolean getUnschedulable() {
        return unschedulable;
    }

    public void setUnschedulable(Boolean unschedulable) {
        this.unschedulable = unschedulable;
    }

    public IpSummary getIpSummary() {
        return ipSummary;
    }

    public void setIpSummary(IpSummary ipSummary) {
        this.ipSummary = ipSummary;
    }

    @Override
    public String toString() {
        return "CmNetworkBody [siteId=" + siteId + ", zone=" + zone + ", name=" + name + ", topologys=" + topologys
                + ", desc=" + desc + ", unschedulable=" + unschedulable + ", ipSummary=" + ipSummary + "]";
    }

    public class IpSummary {
        @JSONField(name = "start_ip")
        private String startIp;

        @JSONField(name = "end_ip")
        private String endIp;

        @JSONField(name = "gateway")
        private String gateway;

        @JSONField(name = "prefix")
        private Integer prefix;

        @JSONField(name = "vlan")
        private Integer vlan;

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

        public Integer getPrefix() {
            return prefix;
        }

        public void setPrefix(Integer prefix) {
            this.prefix = prefix;
        }

        public Integer getVlan() {
            return vlan;
        }

        public void setVlan(Integer vlan) {
            this.vlan = vlan;
        }

        @Override
        public String toString() {
            return "IpSummary [startIp=" + startIp + ", endIp=" + endIp + ", gateway=" + gateway + ", prefix=" + prefix
                    + ", vlan=" + vlan + "]";
        }

    }

}
