package com.bsg.dbscale.service.dto;

import java.io.Serializable;

public class TaskBaseDTO implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private String id;
    private DisplayDTO action;
    private DisplayDTO state;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public DisplayDTO getAction() {
        return action;
    }

    public void setAction(DisplayDTO action) {
        this.action = action;
    }

    public DisplayDTO getState() {
        return state;
    }

    public void setState(DisplayDTO state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "TaskBaseDTO [id=" + id + ", action=" + action + ", state=" + state + "]";
    }

}
