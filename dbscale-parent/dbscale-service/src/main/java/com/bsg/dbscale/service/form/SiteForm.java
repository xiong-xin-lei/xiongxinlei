package com.bsg.dbscale.service.form;

import java.io.Serializable;

public class SiteForm implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private String name;
    private String region;
    private String description;
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
        return "SiteForm [name=" + name + ", region=" + region + ", description=" + description + ", kubeconfig="
                + kubeconfig + "]";
    }

}
