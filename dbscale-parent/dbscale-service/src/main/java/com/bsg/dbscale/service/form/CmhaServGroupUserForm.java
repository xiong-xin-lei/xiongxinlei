package com.bsg.dbscale.service.form;

import java.io.Serializable;

public class CmhaServGroupUserForm extends MysqlServUserForm implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private Integer maxConnection;
    private String properties;

    public Integer getMaxConnection() {
        return maxConnection;
    }

    public void setMaxConnection(Integer maxConnection) {
        this.maxConnection = maxConnection;
    }

    public String getProperties() {
        return properties;
    }

    public void setProperties(String properties) {
        this.properties = properties;
    }

    @Override
    public String toString() {
        return super.toString() + "CmhaServGroupUserForm [maxConnection=" + maxConnection + ", properties=" + properties
                + "]";
    }

}
