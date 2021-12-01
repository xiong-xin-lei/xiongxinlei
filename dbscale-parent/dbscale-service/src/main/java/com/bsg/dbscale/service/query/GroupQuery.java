package com.bsg.dbscale.service.query;

import java.io.Serializable;

public class GroupQuery implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private String name;
    private String owner;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    @Override
    public String toString() {
        return "GroupQuery [name=" + name + ", owner=" + owner + "]";
    }

}
