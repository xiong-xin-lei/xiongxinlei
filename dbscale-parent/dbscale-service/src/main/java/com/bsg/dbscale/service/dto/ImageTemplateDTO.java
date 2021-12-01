package com.bsg.dbscale.service.dto;

import java.io.Serializable;

public class ImageTemplateDTO implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private String key;
    private String value;
    private String defaultValue;
    private String range;
    private Boolean canSet;
    private Boolean mustRestart;
    private String description;

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

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public String getRange() {
        return range;
    }

    public void setRange(String range) {
        this.range = range;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "ImageTemplateDTO [key=" + key + ", value=" + value + ", defaultValue=" + defaultValue + ", range="
                + range + ", canSet=" + canSet + ", mustRestart=" + mustRestart + ", description=" + description + "]";
    }

}
