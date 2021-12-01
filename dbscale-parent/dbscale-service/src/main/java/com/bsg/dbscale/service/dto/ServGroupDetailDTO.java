package com.bsg.dbscale.service.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ServGroupDetailDTO extends ServGroupStateBaseDTO implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * 
     */
    private List<ServDTO> servs;

    public ServGroupDetailDTO() {
        this.servs = new ArrayList<>();
    }

    public List<ServDTO> getServs() {
        return servs;
    }

    public void setServs(List<ServDTO> servs) {
        this.servs = servs;
    }

    @Override
    public String toString() {
        return super.toString() + "ServGroupDetailDTO [servs=" + servs + "]";
    }

}
