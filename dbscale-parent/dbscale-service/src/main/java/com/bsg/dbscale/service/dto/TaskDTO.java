package com.bsg.dbscale.service.dto;

import java.io.Serializable;

public class TaskDTO extends TaskBaseDTO implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private IdentificationDTO site;
    private DisplayDTO objType;
    private String objId;
    private String objName;
    private Boolean block;
    private String startDateTime;
    private String endDateTime;
    private UserBaseDTO owner;
    private InfoDTO created;

    public IdentificationDTO getSite() {
        return site;
    }

    public void setSite(IdentificationDTO site) {
        this.site = site;
    }

    public DisplayDTO getObjType() {
        return objType;
    }

    public void setObjType(DisplayDTO objType) {
        this.objType = objType;
    }

    public String getObjId() {
        return objId;
    }

    public void setObjId(String objId) {
        this.objId = objId;
    }

    public String getObjName() {
        return objName;
    }

    public void setObjName(String objName) {
        this.objName = objName;
    }

    public Boolean getBlock() {
        return block;
    }

    public void setBlock(Boolean block) {
        this.block = block;
    }

    public String getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(String startDateTime) {
        this.startDateTime = startDateTime;
    }

    public String getEndDateTime() {
        return endDateTime;
    }

    public void setEndDateTime(String endDateTime) {
        this.endDateTime = endDateTime;
    }

    public UserBaseDTO getOwner() {
        return owner;
    }

    public void setOwner(UserBaseDTO owner) {
        this.owner = owner;
    }

    public InfoDTO getCreated() {
        return created;
    }

    public void setCreated(InfoDTO created) {
        this.created = created;
    }

    @Override
    public String toString() {
        return super.toString() + "TaskDTO [site=" + site + ", objType=" + objType + ", objId=" + objId + ", objName="
                + objName + ", block=" + block + ", startDateTime=" + startDateTime + ", endDateTime=" + endDateTime
                + ", owner=" + owner + ", created=" + created + "]";
    }

}
