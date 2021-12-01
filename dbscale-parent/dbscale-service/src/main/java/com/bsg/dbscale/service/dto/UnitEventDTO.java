package com.bsg.dbscale.service.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class UnitEventDTO implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private List<EventDTO> units;
    private List<EventDTO> pods;
    private List<EventDTO> networkClaims;
    private List<EventDTO> volumePathDatas;
    private List<EventDTO> volumePathLogs;

    public UnitEventDTO() {
        this.units = new ArrayList<>();
        this.pods = new ArrayList<>();
        this.networkClaims = new ArrayList<>();
        this.volumePathDatas = new ArrayList<>();
        this.volumePathLogs = new ArrayList<>();
    }

    public List<EventDTO> getUnits() {
        return units;
    }

    public void setUnits(List<EventDTO> units) {
        this.units = units;
    }

    public List<EventDTO> getPods() {
        return pods;
    }

    public void setPods(List<EventDTO> pods) {
        this.pods = pods;
    }

    public List<EventDTO> getNetworkClaims() {
        return networkClaims;
    }

    public void setNetworkClaims(List<EventDTO> networkClaims) {
        this.networkClaims = networkClaims;
    }

    public List<EventDTO> getVolumePathDatas() {
        return volumePathDatas;
    }

    public void setVolumePathDatas(List<EventDTO> volumePathDatas) {
        this.volumePathDatas = volumePathDatas;
    }

    public List<EventDTO> getVolumePathLogs() {
        return volumePathLogs;
    }

    public void setVolumePathLogs(List<EventDTO> volumePathLogs) {
        this.volumePathLogs = volumePathLogs;
    }

    @Override
    public String toString() {
        return "UnitEventDTO [units=" + units + ", pods=" + pods + ", networkClaims=" + networkClaims
                + ", volumePathDatas=" + volumePathDatas + ", volumePathLogs=" + volumePathLogs + "]";
    }

}
