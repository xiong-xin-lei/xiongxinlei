package com.bsg.dbscale.cm.model;

import java.io.Serializable;
import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;

public class CmUnitEvent implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    @JSONField(name = "unit")
    private List<CmEvent> units;

    @JSONField(name = "pod")
    private List<CmEvent> pods;

    @JSONField(name = "network_claims")
    private List<CmEvent> networkClaims;

    @JSONField(name = "volume_path_data")
    private List<CmEvent> volumePathDatas;

    @JSONField(name = "volume_path_log")
    private List<CmEvent> volumePathLogs;

    public List<CmEvent> getUnits() {
        return units;
    }

    public void setUnits(List<CmEvent> units) {
        this.units = units;
    }

    public List<CmEvent> getPods() {
        return pods;
    }

    public void setPods(List<CmEvent> pods) {
        this.pods = pods;
    }

    public List<CmEvent> getNetworkClaims() {
        return networkClaims;
    }

    public void setNetworkClaims(List<CmEvent> networkClaims) {
        this.networkClaims = networkClaims;
    }

    public List<CmEvent> getVolumePathDatas() {
        return volumePathDatas;
    }

    public void setVolumePathDatas(List<CmEvent> volumePathDatas) {
        this.volumePathDatas = volumePathDatas;
    }

    public List<CmEvent> getVolumePathLogs() {
        return volumePathLogs;
    }

    public void setVolumePathLogs(List<CmEvent> volumePathLogs) {
        this.volumePathLogs = volumePathLogs;
    }

    @Override
    public String toString() {
        return "CmUnitEvent [units=" + units + ", pods=" + pods + ", networkClaims=" + networkClaims
                + ", volumePathDatas=" + volumePathDatas + ", volumePathLogs=" + volumePathLogs + "]";
    }

}
