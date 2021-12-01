package com.bsg.dbscale.service.form;

import java.io.Serializable;

public class DBSchemaForm implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private String name;
    private String characterSet;

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

    @Override
    public String toString() {
        return "DBSchemaForm [name=" + name + ", characterSet=" + characterSet + "]";
    }

}
