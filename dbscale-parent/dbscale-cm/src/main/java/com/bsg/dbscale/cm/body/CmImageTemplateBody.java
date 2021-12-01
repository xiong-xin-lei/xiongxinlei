package com.bsg.dbscale.cm.body;

import java.io.Serializable;

import com.alibaba.fastjson.annotation.JSONField;

public class CmImageTemplateBody implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    @JSONField(name = "can_set")
    private Boolean canSet;

    @JSONField(name = "default")
    private String defaultValue;

    @JSONField(name = "desc")
    private String desc;

    @JSONField(name = "key")
    private String key;

    @JSONField(name = "must_restart")
    private Boolean mustRestart;

    @JSONField(name = "range")
    private String range;

    @JSONField(name = "value")
    private String value;

    public Boolean getCanSet() {
        return canSet;
    }

    public void setCanSet(Boolean canSet) {
        this.canSet = canSet;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Boolean getMustRestart() {
        return mustRestart;
    }

    public void setMustRestart(Boolean mustRestart) {
        this.mustRestart = mustRestart;
    }

    public String getRange() {
        return range;
    }

    public void setRange(String range) {
        this.range = range;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "CmImageTemplateBody [canSet=" + canSet + ", defaultValue=" + defaultValue + ", desc=" + desc + ", key="
                + key + ", mustRestart=" + mustRestart + ", range=" + range + ", value=" + value + "]";
    }

}
