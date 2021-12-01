package com.bsg.dbscale.service.form;

import java.io.Serializable;

public class BackupExternalForm implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private String servGroupId;

    public String getServGroupId() {
        return servGroupId;
    }

    public void setServGroupId(String servGroupId) {
        this.servGroupId = servGroupId;
    }

    @Override
    public String toString() {
        return "BackupExternalForm [servGroupId=" + servGroupId + "]";
    }

}
