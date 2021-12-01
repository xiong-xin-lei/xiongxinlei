package com.bsg.dbscale.dao.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class OrderGroupDO implements Serializable {

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
    private String createType;
    private String state;
    private String msg;
    private String owner;
    private Date gmtCreate;
    private String creator;
    private Date gmtModified;
    private String editor;
    private BusinessSubsystemDO businessSubsystem;
    private BusinessAreaDO businessArea;
    private List<OrderDO> orders;

    public OrderGroupDO() {
        this.orders = new ArrayList<>();
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

    public String getCreateType() {
        return createType;
    }

    public void setCreateType(String createType) {
        this.createType = createType;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
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

    public Date getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public Date getGmtModified() {
        return gmtModified;
    }

    public void setGmtModified(Date gmtModified) {
        this.gmtModified = gmtModified;
    }

    public String getEditor() {
        return editor;
    }

    public void setEditor(String editor) {
        this.editor = editor;
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

    public List<OrderDO> getOrders() {
        return orders;
    }

    public void setOrders(List<OrderDO> orders) {
        this.orders = orders;
    }

    @Override
    public String toString() {
        return "OrderGroupDO [id=" + id + ", category=" + category + ", businessSubsystemId=" + businessSubsystemId
                + ", businessAreaId=" + businessAreaId + ", sysArchitecture=" + sysArchitecture + ", name=" + name
                + ", createType=" + createType + ", state=" + state + ", msg=" + msg + ", owner=" + owner
                + ", gmtCreate=" + gmtCreate + ", creator=" + creator + ", gmtModified=" + gmtModified + ", editor="
                + editor + ", businessSubsystem=" + businessSubsystem + ", businessArea=" + businessArea + ", orders="
                + orders + "]";
    }

}
