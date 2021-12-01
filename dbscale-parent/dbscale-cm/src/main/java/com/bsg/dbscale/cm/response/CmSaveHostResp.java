package com.bsg.dbscale.cm.response;

import java.io.Serializable;

import com.alibaba.fastjson.annotation.JSONField;

public class CmSaveHostResp implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    @JSONField(name = "host_id")
    private String hostId;

    public String getHostId() {
        return hostId;
    }

    public void setHostId(String hostId) {
        this.hostId = hostId;
    }

    @Override
    public String toString() {
        return "CmSaveHostResp [hostId=" + hostId + "]";
    }

}
