package com.bsg.dbscale.cm.response;

import java.io.Serializable;

import com.alibaba.fastjson.annotation.JSONField;

public class CmBackupExternalResp implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    @JSONField(name = "id")
    private String id;

    @JSONField(name = "directory")
    private String directory;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDirectory() {
        return directory;
    }

    public void setDirectory(String directory) {
        this.directory = directory;
    }

    @Override
    public String toString() {
        return "CmBackupExternalResp [id=" + id + ", directory=" + directory + "]";
    }

}
