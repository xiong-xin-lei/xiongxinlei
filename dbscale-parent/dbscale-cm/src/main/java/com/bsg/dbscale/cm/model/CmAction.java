package com.bsg.dbscale.cm.model;

import java.io.Serializable;

import com.alibaba.fastjson.annotation.JSONField;

public class CmAction implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    @JSONField(name = "id")
    private String id;

    @JSONField(name = "kind")
    private String kind;

    @JSONField(name = "spec")
    private String spec;

    @JSONField(name = "option")
    private String option;

    @JSONField(name = "status")
    private String status;

    @JSONField(name = "err_msg")
    private String errMsg;

    @JSONField(name = "clean")
    private Boolean clean;

    @JSONField(name = "created_at")
    private String createdAt;

    @JSONField(name = "finish_at")
    private String finishAt;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public String getSpec() {
        return spec;
    }

    public void setSpec(String spec) {
        this.spec = spec;
    }

    public String getOption() {
        return option;
    }

    public void setOption(String option) {
        this.option = option;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }

    public Boolean getClean() {
        return clean;
    }

    public void setClean(Boolean clean) {
        this.clean = clean;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getFinishAt() {
        return finishAt;
    }

    public void setFinishAt(String finishAt) {
        this.finishAt = finishAt;
    }

    @Override
    public String toString() {
        return "CmAction [id=" + id + ", kind=" + kind + ", spec=" + spec + ", option=" + option + ", status=" + status
                + ", errMsg=" + errMsg + ", clean=" + clean + ", createdAt=" + createdAt + ", finishAt=" + finishAt
                + "]";
    }

}
