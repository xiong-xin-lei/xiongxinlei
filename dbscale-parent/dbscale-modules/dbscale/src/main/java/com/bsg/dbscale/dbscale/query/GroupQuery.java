package com.bsg.dbscale.dbscale.query;

import java.io.Serializable;

public class GroupQuery implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "GroupQuery [name=" + name + "]";
    }

}
