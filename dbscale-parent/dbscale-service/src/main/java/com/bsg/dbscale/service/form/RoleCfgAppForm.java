package com.bsg.dbscale.service.form;

import java.io.Serializable;
import java.util.List;

public class RoleCfgAppForm implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * 可见功能编码
     */
    private List<Long> appIds;

    public List<Long> getAppIds() {
        return appIds;
    }

    public void setAppIds(List<Long> appIds) {
        this.appIds = appIds;
    }

    @Override
    public String toString() {
        return "RoleCfgAppForm [appIds=" + appIds + "]";
    }

}
