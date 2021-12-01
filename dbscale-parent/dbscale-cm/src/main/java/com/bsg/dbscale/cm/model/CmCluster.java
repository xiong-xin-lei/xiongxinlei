package com.bsg.dbscale.cm.model;

import java.io.Serializable;
import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;

public class CmCluster extends CmClusterBase implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    @JSONField(name = "zone")
    private String zone;

    @JSONField(name = "image_types")
    private List<String> imageTypes;

    @JSONField(name = "ha_tag")
    private String haTag;

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
        return "CmCluster [zone=" + zone + ", imageTypes=" + imageTypes + ", haTag=" + haTag + ", desc=" + desc
                + ", site=" + site + ", createdAt=" + createdAt + ", modifiedAt=" + modifiedAt + "]";
    }

}
