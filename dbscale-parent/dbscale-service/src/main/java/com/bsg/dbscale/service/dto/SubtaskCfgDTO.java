package com.bsg.dbscale.service.dto;

import java.io.Serializable;

public class SubtaskCfgDTO implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private DisplayDTO objType;

    private DisplayDTO action;

    private Long timeout;

    private String description;

    public DisplayDTO getObjType() {
        return objType;
    }

    public void setObjType(DisplayDTO objType) {
        this.objType = objType;
    }

    public DisplayDTO getAction() {
        return action;
    }

    public void setAction(DisplayDTO action) {
        this.action = action;
    }

    public Long getTimeout() {
        return timeout;
    }

    public void setTimeout(Long timeout) {
        this.timeout = timeout;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "SubtaskCfgDTO [objType=" + objType + ", action=" + action + ", timeout=" + timeout + ", description="
                + description + "]";
    }

}
