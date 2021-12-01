package com.bsg.dbscale.service.form;

import java.io.Serializable;
import java.util.List;

public class VolumePathForm implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private List<String> hddPaths;
    private List<String> ssdPaths;
    private String remoteStorageId;

    public List<String> getHddPaths() {
        return hddPaths;
    }

    public void setHddPaths(List<String> hddPaths) {
        this.hddPaths = hddPaths;
    }

    public List<String> getSsdPaths() {
        return ssdPaths;
    }

    public void setSsdPaths(List<String> ssdPaths) {
        this.ssdPaths = ssdPaths;
    }

    public String getRemoteStorageId() {
        return remoteStorageId;
    }

    public void setRemoteStorageId(String remoteStorageId) {
        this.remoteStorageId = remoteStorageId;
    }

    @Override
    public String toString() {
        return "VolumePathForm [hddPaths=" + hddPaths + ", ssdPaths=" + ssdPaths + ", remoteStorageId="
                + remoteStorageId + "]";
    }

}
