package com.bsg.dbscale.cm.response;

import java.io.Serializable;

import com.alibaba.fastjson.annotation.JSONField;

public class CmUnitRestoreResp implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    @JSONField(name = "id")
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "CmUnitRestoreResp [id=" + id + "]";
    }

}
