package com.bsg.dbscale.dao.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ServGroupDO implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private String id;
    private String category;
    private String businessSubsystemId;
    private String businessAreaId;
    private String sysArchitecture;
    private String name;
    private String msg;
    private String owner;
    private Boolean flag;
    private String orderGroupId;
    private Date gmtCreate;
    private BusinessSubsystemDO businessSubsystem;
    private BusinessAreaDO businessArea;
    private List<ServDO> servs;
    private OrderGroupDO orderGroup;

    public ServGroupDO() {
        this.servs = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getBusinessSubsystemId() {
        return businessSubsystemId;
    }

    public void setBusinessSubsystemId(String businessSubsystemId) {
        this.businessSubsystemId = businessSubsystemId;
    }

    public String getBusinessAreaId() {
        return businessAreaId;
    }

    public void setBusinessAreaId(String businessAreaId) {
        this.businessAreaId = businessAreaId;
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

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public Boolean getFlag() {
        return flag;
    }

    public void setFlag(Boolean flag) {
        this.flag = flag;
    }

    public String getOrderGroupId() {
        return orderGroupId;
    }

    public void setOrderGroupId(String orderGroupId) {
        this.orderGroupId = orderGroupId;
    }

    public Date getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public BusinessSubsystemDO getBusinessSubsystem() {
        return businessSubsystem;
    }

    public void setBusinessSubsystem(BusinessSubsystemDO businessSubsystem) {
        this.businessSubsystem = businessSubsystem;
    }

    public BusinessAreaDO getBusinessArea() {
        return businessArea;
    }

    public void setBusinessArea(BusinessAreaDO businessArea) {
        this.businessArea = businessArea;
    }

    public List<ServDO> getServs() {
        return servs;
    }

    public void setServs(List<ServDO> servs) {
        this.servs = servs;
    }

    public OrderGroupDO getOrderGroup() {
        return orderGroup;
    }

    public void setOrderGroup(OrderGroupDO orderGroup) {
        this.orderGroup = orderGroup;
    }

    @Override
    public String toString() {
        return "ServGroupDO [id=" + id + ", category=" + category + ", businessSubsystemId=" + businessSubsystemId
                + ", businessAreaId=" + businessAreaId + ", sysArchitecture=" + sysArchitecture + ", name=" + name
                + ", msg=" + msg + ", owner=" + owner + ", flag=" + flag + ", orderGroupId=" + orderGroupId
                + ", gmtCreate=" + gmtCreate + ", businessSubsystem=" + businessSubsystem + ", businessArea="
                + businessArea + ", servs=" + servs + ", orderGroup=" + orderGroup + "]";
    }

}
