package com.bsg.dbscale.service.dto;

import java.io.Serializable;

public class ServCfgDTO implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private String key;

    private String value;

    private String range;

    private String defaultValue;

    private Boolean canSet;

    private Boolean mustRestart;

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
        return "ServCfgDTO [key=" + key + ", value=" + value + ", range=" + range + ", defaultValue=" + defaultValue
                + ", canSet=" + canSet + ", mustRestart=" + mustRestart + ", desc=" + desc + "]";
    }

}
