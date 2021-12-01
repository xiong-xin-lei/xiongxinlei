package com.bsg.dbscale.cm.body;

import java.io.Serializable;
import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;

public class CmServiceRoleBody implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    @JSONField(name = "exec_unit_id")
    private List<String> execUnitIds;

    @JSONField(name = "master")
    private CmRoleInfo master;

    @JSONField(name = "slave")
    private CmRoleInfo slave;

    public List<String> getExecUnitIds() {
        return execUnitIds;
    }

    public void setExecUnitIds(List<String> execUnitIds) {
        this.execUnitIds = execUnitIds;
    }

    public CmRoleInfo getMaster() {
        return master;
    }

    public void setMaster(CmRoleInfo master) {
        this.master = master;
    }

    public CmRoleInfo getSlave() {
        return slave;
    }

    public void setSlave(CmRoleInfo slave) {
        this.slave = slave;
    }

    @Override
    public String toString() {
        return "CmServiceRoleBody [execUnitIds=" + execUnitIds + ", master=" + master + ", slave=" + slave + "]";
    }

    public class CmRoleInfo {
        @JSONField(name = "unit_id")
        private List<String> unitIds;

        public List<String> getUnitIds() {
            return unitIds;
        }

        public void setUnitIds(List<String> unitIds) {
            this.unitIds = unitIds;
        }

        @Override
        public String toString() {
            return "CmRoleInfo [unitIds=" + unitIds + "]";
        }

    }

}
