package com.bsg.dbscale.service.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ServGroupDTO extends ServGroupStateBaseDTO implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * 
     */
    private ArchBaseDTO arch;
    private ScaleBaseDTO scale;
    private ImageDTO image;
    private DisplayDTO diskType;
    private Integer dataSize;
    private Integer logSize;
    private List<String> addresses;

    public ServGroupDTO() {
        this.addresses = new ArrayList<>();
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

    public ImageDTO getImage() {
        return image;
    }

    public void setImage(ImageDTO image) {
        this.image = image;
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

    public List<String> getAddresses() {
        return addresses;
    }

    public void setAddresses(List<String> addresses) {
        this.addresses = addresses;
    }

    @Override
    public String toString() {
        return super.toString() + "ServGroupDTO [arch=" + arch + ", scale=" + scale + ", image=" + image + ", diskType="
                + diskType + ", dataSize=" + dataSize + ", logSize=" + logSize + ", addresses=" + addresses + "]";
    }

    public class ImageDTO {
        private String type;
        private VersionDTO version;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public VersionDTO getVersion() {
            return version;
        }

        public void setVersion(VersionDTO version) {
            this.version = version;
        }

        @Override
        public String toString() {
            return "ImageDTO [type=" + type + ", version=" + version + "]";
        }

    }
}
