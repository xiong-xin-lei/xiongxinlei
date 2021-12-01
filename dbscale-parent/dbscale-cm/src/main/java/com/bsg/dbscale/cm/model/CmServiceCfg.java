package com.bsg.dbscale.cm.model;

import java.io.Serializable;

import com.alibaba.fastjson.annotation.JSONField;

public class CmServiceCfg implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    @JSONField(name = "key")
    private String key;

    @JSONField(name = "value")
    private String value;

    @JSONField(name = "range")
    private String range;

    @JSONField(name = "default")
    private String defaultValue;

    @JSONField(name = "canSet")
    private Boolean canSet;

    @JSONField(name = "mustRestart")
    private Boolean mustRestart;

    @JSONField(name = "desc")
    private String desc;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getRange() {
        return range;
    }

    public void setRange(String range) {
        this.range = range;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public Boolean getCanSet() {
        return canSet;
    }

    public void setCanSet(Boolean canSet) {
        this.canSet = canSet;
    }

    public Boolean getMustRestart() {
        return mustRestart;
    }

    public void setMustRestart(Boolean mustRestart) {
        this.mustRestart = mustRestart;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    @Override
    public String toString() {
        return "CmServiceCfg [key=" + key + ", value=" + value + ", range=" + range + ", defaultValue=" + defaultValue
                + ", canSet=" + canSet + ", mustRestart=" + mustRestart + ", desc=" + desc + "]";
    }

}
