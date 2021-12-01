package com.bsg.dbscale.dbscale.query;

import java.io.Serializable;

public class OperateLogQuery implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private String siteId;
    private Long start;
    private Long end;

    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }

    public Long getStart() {
        return start;
    }

    public void setStart(Long start) {
        this.start = start;
    }

    public Long getEnd() {
        return end;
    }

    public void setEnd(Long end) {
        this.end = end;
    }

    @Override
    public String toString() {
        return "OperateLogQuery [siteId=" + siteId + ", start=" + start + ", end=" + end + "]";
    }

}
