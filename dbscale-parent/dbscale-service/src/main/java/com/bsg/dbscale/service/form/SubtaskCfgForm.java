package com.bsg.dbscale.service.form;

import java.io.Serializable;

public class SubtaskCfgForm implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private Long timeout;
    private String description;

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
        return "SubtaskCfgForm [timeout=" + timeout + ", description=" + description + "]";
    }

}
