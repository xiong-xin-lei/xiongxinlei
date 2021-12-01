package com.bsg.dbscale.service.dto;

import java.io.Serializable;

public class ServGroupStateBaseDTO extends ServGroupBaseDTO implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private Boolean meetExpectation;
    private DisplayDTO state;
    private TaskBaseDTO task;

    public Boolean getMeetExpectation() {
        return meetExpectation;
    }

    public void setMeetExpectation(Boolean meetExpectation) {
        this.meetExpectation = meetExpectation;
    }

    public DisplayDTO getState() {
        return state;
    }

    public void setState(DisplayDTO state) {
        this.state = state;
    }

    public TaskBaseDTO getTask() {
        return task;
    }

    public void setTask(TaskBaseDTO task) {
        this.task = task;
    }

    @Override
    public String toString() {
        return super.toString() + "ServGroupStateBaseDTO [meetExpectation=" + meetExpectation + ", state=" + state
                + ", task=" + task + "]";
    }

}
