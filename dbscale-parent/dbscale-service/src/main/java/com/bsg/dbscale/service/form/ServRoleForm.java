package com.bsg.dbscale.service.form;

import java.io.Serializable;
import java.util.List;

public class ServRoleForm implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private List<String> master;
    private List<String> slave;

    public List<String> getMaster() {
        return master;
    }

    public void setMaster(List<String> master) {
        this.master = master;
    }

    public List<String> getSlave() {
        return slave;
    }

    public void setSlave(List<String> slave) {
        this.slave = slave;
    }

    @Override
    public String toString() {
        return "ServRoleForm [master=" + master + ", slave=" + slave + "]";
    }

}
