package com.bsg.dbscale.service.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ServDTO implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private String id;
    private String relateId;
    private DisplayDTO type;
    private VersionDTO version;
    private ArchBaseDTO arch;
    private ScaleBaseDTO scale;
    private DisplayDTO diskType;
    private Integer dataSize;
    private Integer logSize;
    private DisplayDTO state;
    private List<UnitBaseDTO> units;

    public ServDTO() {
        this.units = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRelateId() {
        return relateId;
    }

    public void setRelateId(String relateId) {
        this.relateId = relateId;
    }

    public DisplayDTO getType() {
        return type;
    }

    public void setType(DisplayDTO type) {
        this.type = type;
    }

    public VersionDTO getVersion() {
        return version;
    }

    public void setVersion(VersionDTO version) {
        this.version = version;
    }

    public ArchBaseDTO getArch() {
        return arch;
    }

    public void setArch(ArchBaseDTO arch) {
        this.arch = arch;
    }

    public ScaleBaseDTO getScale() {
        return scale;
    }

    public void setScale(ScaleBaseDTO scale) {
        this.scale = scale;
    }

    public DisplayDTO getDiskType() {
        return diskType;
    }

    public void setDiskType(DisplayDTO diskType) {
        this.diskType = diskType;
    }

    public Integer getDataSize() {
        return dataSize;
    }

    public void setDataSize(Integer dataSize) {
        this.dataSize = dataSize;
    }

    public Integer getLogSize() {
        return logSize;
    }

    public void setLogSize(Integer logSize) {
        this.logSize = logSize;
    }

    public DisplayDTO getState() {
        return state;
    }

    public void setState(DisplayDTO state) {
        this.state = state;
    }

    public List<UnitBaseDTO> getUnits() {
        return units;
    }

    public void setUnits(List<UnitBaseDTO> units) {
        this.units = units;
    }

    @Override
    public String toString() {
        return "ServDTO [id=" + id + ", relateId=" + relateId + ", type=" + type + ", version=" + version + ", arch="
                + arch + ", scale=" + scale + ", diskType=" + diskType + ", dataSize=" + dataSize + ", logSize="
                + logSize + ", state=" + state + ", units=" + units + "]";
    }

}
