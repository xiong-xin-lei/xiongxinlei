package com.bsg.dbscale.service.dto;

import java.io.Serializable;
import java.util.List;

public class ClusterDTO extends IdentificationDTO implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private List<DefServDTO> imageTypes;
    private String haTag;
    private Boolean enabled;
    private String description;
    private IdentificationDTO site;
    private IdentificationDTO businessArea;
    private InfoDTO created;

    public List<DefServDTO> getImageTypes() {
        return imageTypes;
    }

    public void setImageTypes(List<DefServDTO> imageTypes) {
        this.imageTypes = imageTypes;
    }

    public String getHaTag() {
        return haTag;
    }

    public void setHaTag(String haTag) {
        this.haTag = haTag;
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
        return super.toString() + "ClusterDTO [imageTypes=" + imageTypes + ", haTag=" + haTag + ", enabled=" + enabled
                + ", description=" + description + ", site=" + site + ", businessArea=" + businessArea + ", created="
                + created + "]";
    }

}
