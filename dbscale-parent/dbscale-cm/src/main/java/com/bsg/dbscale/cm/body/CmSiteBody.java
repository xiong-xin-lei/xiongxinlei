package com.bsg.dbscale.cm.body;

import java.io.Serializable;

import com.alibaba.fastjson.annotation.JSONField;

public class CmSiteBody implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    @JSONField(name = "name")
    private String name;

    @JSONField(name = "region")
    private String region;

    @JSONField(name = "description")
    private String description;

    @JSONField(name = "kubeconfig")
    private String kubeconfig;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getKubeconfig() {
        return kubeconfig;
    }

    public void setKubeconfig(String kubeconfig) {
        this.kubeconfig = kubeconfig;
    }

    @Override
    public String toString() {
        return "CmSiteBody [name=" + name + ", region=" + region + ", description=" + description + ", kubeconfig="
                + kubeconfig + "]";
    }

}
