package com.bsg.dbscale.dao.domain;

import java.io.Serializable;

public class CrontabCfgDO implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * 
     */
    private String triggerName;

    /**
     * 
     */
    private String crontab;

    /**
     * 
     */
    private String description;

    /**
     * 获取
     * 
     * @return triggerName
     */
    public String getTriggerName() {
        return triggerName;
    }

    /**
     * 设置
     * 
     * @param triggerName
     */
    public void setTriggerName(String triggerName) {
        this.triggerName = triggerName;
    }

    /**
     * 获取
     * 
     * @return crontab
     */
    public String getCrontab() {
        return crontab;
    }

    /**
     * 设置
     * 
     * @param crontab
     */
    public void setCrontab(String crontab) {
        this.crontab = crontab;
    }

    /**
     * 获取
     * 
     * @return description
     */
    public String getDescription() {
        return description;
    }

    /**
     * 设置
     * 
     * @param description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "CrontabCfgDO [triggerName=" + triggerName + ", crontab=" + crontab + ", description=" + description
                + "]";
    }

}
