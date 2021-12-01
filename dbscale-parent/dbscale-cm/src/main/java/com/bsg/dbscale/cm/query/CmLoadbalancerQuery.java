package com.bsg.dbscale.cm.query;

import java.io.Serializable;

import com.alibaba.fastjson.annotation.JSONField;

public class CmLoadbalancerQuery implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    @JSONField(name = "id")
    private String id;

    @JSONField(name = "type")
    private String type;

    @JSONField(name = "site_id")
    private String siteId;

    @JSONField(name = "group_name")
    private String group;

    @JSONField(name = "group_type")
    private String groupType;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getGroupType() {
        return groupType;
    }

    public void setGroupType(String groupType) {
        this.groupType = groupType;
    }

    @Override
    public String toString() {
        return "CmLoadbalancerQuery [id=" + id + ", type=" + type + ", siteId=" + siteId + ", group=" + group
                + ", groupType=" + groupType + "]";
    }

}
