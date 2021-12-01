package com.bsg.dbscale.service.dto;

import java.io.Serializable;

public class DBSchemaDTO implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private String name;
    private String characterSet;
    private Long size;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCharacterSet() {
        return characterSet;
    }

    public void setCharacterSet(String characterSet) {
        this.characterSet = characterSet;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    @Override
    public String toString() {
        return "DBSchemaDTO [name=" + name + ", characterSet=" + characterSet + ", size=" + size + "]";
    }

}
