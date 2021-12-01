package com.bsg.dbscale.service.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class HostEventDTO implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private List<EventDTO> hosts;
    private List<EventDTO> nodes;

    public HostEventDTO() {
        this.hosts = new ArrayList<>();
        this.nodes = new ArrayList<>();
    }

    public List<EventDTO> getHosts() {
        return hosts;
    }

    public void setHosts(List<EventDTO> hosts) {
        this.hosts = hosts;
    }

    public List<EventDTO> getNodes() {
        return nodes;
    }

    public void setNodes(List<EventDTO> nodes) {
        this.nodes = nodes;
    }

    @Override
    public String toString() {
        return "HostEventDTO [hosts=" + hosts + ", nodes=" + nodes + "]";
    }

}
