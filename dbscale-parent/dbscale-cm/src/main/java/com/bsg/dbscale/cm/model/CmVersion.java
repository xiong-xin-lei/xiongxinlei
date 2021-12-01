package com.bsg.dbscale.cm.model;

import java.io.Serializable;

import com.alibaba.fastjson.annotation.JSONField;

public class CmVersion implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    @JSONField(name = "version")
    private String version;

    @JSONField(name = "commit_id")
    private String commitId;

    @JSONField(name = "build_time")
    private String buildTime;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getCommitId() {
        return commitId;
    }

    public void setCommitId(String commitId) {
        this.commitId = commitId;
    }

    public String getBuildTime() {
        return buildTime;
    }

    public void setBuildTime(String buildTime) {
        this.buildTime = buildTime;
    }

    @Override
    public String toString() {
        return "CmVersion [version=" + version + ", commitId=" + commitId + ", buildTime=" + buildTime + "]";
    }

}
