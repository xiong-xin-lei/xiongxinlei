package com.bsg.dbscale.service.dto;

import java.io.Serializable;

public class OperateLogDTO implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private Long id;
    private String objType;
    private String objName;
    private String description;
    private IdentificationDTO site;
    private InfoDTO created;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getObjType() {
        return objType;
    }

    public void setObjType(String objType) {
        this.objType = objType;
    }

    public String getObjName() {
        return objName;
    }

    public void setObjName(String objName) {
        this.objName = objName;
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

    public InfoDTO getCreated() {
        return created;
    }

    public void setCreated(InfoDTO created) {
        this.created = created;
    }

    @Override
    public String toString() {
        return "OperateLogDTO [id=" + id + ", objType=" + objType + ", objName=" + objName + ", description="
                + description + ", site=" + site + ", created=" + created + "]";
    }

}
