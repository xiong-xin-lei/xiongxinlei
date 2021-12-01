package com.bsg.dbscale.cm.model;

import java.io.Serializable;
import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;

public class CmNetwork extends CmNetworkBase implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    @JSONField(name = "zone")
    private String zone;

    @JSONField(name = "topology")
    private List<String> topology;

    @JSONField(name = "ip_summary")
    private IpSummary ipSummary;

    @JSONField(name = "desc")
    private String desc;

    @JSONField(name = "site")
    private CmSiteBase site;

    @JSONField(name = "created_at")
    private String createdAt;

    @JSONField(name = "modified_at")
    private String modifiedAt;

    public String getZone() {
        return zone;
    }

    public void setZone(String zone) {
        this.zone = zone;
    }

    public List<String> getTopology() {
        return topology;
    }

    public void setTopology(List<String> topology) {
        this.topology = topology;
    }

    public IpSummary getIpSummary() {
        return ipSummary;
    }

    public void setIpSummary(IpSummary ipSummary) {
        this.ipSummary = ipSummary;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
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

    public String getModifiedAt() {
        return modifiedAt;
    }

    public void setModifiedAt(String modifiedAt) {
        this.modifiedAt = modifiedAt;
    }

    @Override
    public String toString() {
        return "CmNetwork [zone=" + zone + ", topology=" + topology + ", ipSummary=" + ipSummary + ", desc=" + desc
                + ", site=" + site + ", createdAt=" + createdAt + ", modifiedAt=" + modifiedAt + "]";
    }

    public class IpSummary {
        @JSONField(name = "start_ip")
        private String startIp;

        @JSONField(name = "end_ip")
        private String endIp;

        @JSONField(name = "gateway")
        private String gateway;

        @JSONField(name = "total")
        private Integer total;

        @JSONField(name = "used")
        private Integer used;

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

        public Integer getTotal() {
            return total;
        }

        public void setTotal(Integer total) {
            this.total = total;
        }

        public Integer getUsed() {
            return used;
        }

        public void setUsed(Integer used) {
            this.used = used;
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
            return "IpSummary [startIp=" + startIp + ", endIp=" + endIp + ", gateway=" + gateway + ", total=" + total
                    + ", used=" + used + ", prefix=" + prefix + ", vlan=" + vlan + "]";
        }

    }
}
