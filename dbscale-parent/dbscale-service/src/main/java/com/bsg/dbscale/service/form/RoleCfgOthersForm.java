package com.bsg.dbscale.service.form;

import java.io.Serializable;

public class RoleCfgOthersForm implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * 工单是否自动审批
     */
    private Boolean ogAutoExamine;

    /**
     * 工单是否自动执行
     */
    private Boolean ogAutoExecute;

    public Boolean getOgAutoExamine() {
        return ogAutoExamine;
    }

    public void setOgAutoExamine(Boolean ogAutoExamine) {
        this.ogAutoExamine = ogAutoExamine;
    }

    public Boolean getOgAutoExecute() {
        return ogAutoExecute;
    }

    public void setOgAutoExecute(Boolean ogAutoExecute) {
        this.ogAutoExecute = ogAutoExecute;
    }

    @Override
    public String toString() {
        return "RoleCfgOthersForm [ogAutoExamine=" + ogAutoExamine + ", ogAutoExecute=" + ogAutoExecute + "]";
    }

}
