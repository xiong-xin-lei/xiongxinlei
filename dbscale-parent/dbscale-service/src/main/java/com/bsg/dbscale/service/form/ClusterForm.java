package com.bsg.dbscale.service.form;

import java.io.Serializable;
import java.util.List;

public class ClusterForm implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * 业务区
     */
    private String businessAreaId;

    /**
     * 集群名
     */
    private String name;

    /**
     * 高可用标签
     */
    private String haTag;

    /**
     * 软件类型
     */
    private List<String> imageTypes;

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

    public String getHaTag() {
        return haTag;
    }

    public void setHaTag(String haTag) {
        this.haTag = haTag;
    }

    public List<String> getImageTypes() {
        return imageTypes;
    }

    public void setImageTypes(List<String> imageTypes) {
        this.imageTypes = imageTypes;
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
        return "ClusterForm [businessAreaId=" + businessAreaId + ", name=" + name + ", haTag=" + haTag + ", imageTypes="
                + imageTypes + ", enabled=" + enabled + ", description=" + description + "]";
    }

}
