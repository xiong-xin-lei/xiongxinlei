package com.bsg.dbscale.cm.model;

import java.io.Serializable;
import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;

public class CmDBSchema implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    @JSONField(name = "name")
    private String name;

    @JSONField(name = "character_set")
    private String characterSet;

    @JSONField(name = "size")
    private Long size;

    @JSONField(name = "tables")
    private List<Table> tables;

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

    public List<Table> getTables() {
        return tables;
    }

    public void setTables(List<Table> tables) {
        this.tables = tables;
    }

    @Override
    public String toString() {
        return "CmDBSchema [name=" + name + ", characterSet=" + characterSet + ", size=" + size + ", tables=" + tables
                + "]";
    }

    public class Table {
        @JSONField(name = "name")
        private String name;

        @JSONField(name = "size")
        private Long size;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Long getSize() {
            return size;
        }

        public void setSize(Long size) {
            this.size = size;
        }

        @Override
        public String toString() {
            return "Table [name=" + name + ", size=" + size + "]";
        }

    }
}
