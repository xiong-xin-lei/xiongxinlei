package com.bsg.dbscale.service.form;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class OrderGroupForm implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private String category;
    private String businessAreaId;
    private String businessSubsystemId;
    private String sysArchitecture;
    private String name;
    private Boolean highAvailable;
    private List<OrderForm> orders;

    public OrderGroupForm() {
        this.orders = new ArrayList<>();
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getBusinessAreaId() {
        return businessAreaId;
    }

    public void setBusinessAreaId(String businessAreaId) {
        this.businessAreaId = businessAreaId;
    }

    public String getBusinessSubsystemId() {
        return businessSubsystemId;
    }

    public void setBusinessSubsystemId(String businessSubsystemId) {
        this.businessSubsystemId = businessSubsystemId;
    }

    public String getSysArchitecture() {
        return sysArchitecture;
    }

    public void setSysArchitecture(String sysArchitecture) {
        this.sysArchitecture = sysArchitecture;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getHighAvailable() {
        return highAvailable;
    }

    public void setHighAvailable(Boolean highAvailable) {
        this.highAvailable = highAvailable;
    }

    public List<OrderForm> getOrders() {
        return orders;
    }

    public void setOrders(List<OrderForm> orders) {
        this.orders = orders;
    }

    @Override
    public String toString() {
        return "OrderGroupForm [category=" + category + ", businessAreaId=" + businessAreaId + ", businessSubsystemId="
                + businessSubsystemId + ", sysArchitecture=" + sysArchitecture + ", name=" + name + ", highAvailable="
                + highAvailable + ", orders=" + orders + "]";
    }

}
