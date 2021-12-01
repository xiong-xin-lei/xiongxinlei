package com.bsg.dbscale.service.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DBSchemaDetailDTO extends DBSchemaDTO implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private List<TableDTO> tables;

    public DBSchemaDetailDTO() {
        this.tables = new ArrayList<>();
    }

    public List<TableDTO> getTables() {
        return tables;
    }

    public void setTables(List<TableDTO> tables) {
        this.tables = tables;
    }

    @Override
    public String toString() {
        return super.toString() + "DBSchemaDetailDTO [tables=" + tables + "]";
    }

    public class TableDTO {
        private String name;
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
            return "TableDTO [name=" + name + ", size=" + size + "]";
        }

    }

}
