package com.bsg.dbscale.cm.body;

import java.io.Serializable;
import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;

public class CmClusterBody implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    @JSONField(name = "name")
    private String name;

    @JSONField(name = "zone")
    private String zone;

    @JSONField(name = "image_types")
    private List<String> imageTypes;

    @JSONField(name = "ha_tag")
    private String haTag;

    @JSONField(name = "desc")
    private String desc;

    @JSONField(name = "unschedulable")
    private Boolean unschedulable;

    @JSONField(name = "site_id")
    private String siteId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getZone() {
        return zone;
    }

    public void setZone(String zone) {
        this.zone = zone;
    }

    public List<String> getImageTypes() {
        return imageTypes;
    }

    public void setImageTypes(List<String> imageTypes) {
        this.imageTypes = imageTypes;
    }

    public String getHaTag() {
        return haTag;
    }

    public void setHaTag(String haTag) {
        this.haTag = haTag;
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

    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }

    @Override
    public String toString() {
        return "CmClusterBody [name=" + name + ", zone=" + zone + ", imageTypes=" + imageTypes + ", haTag=" + haTag
                + ", desc=" + desc + ", unschedulable=" + unschedulable + ", siteId=" + siteId + "]";
    }

}
