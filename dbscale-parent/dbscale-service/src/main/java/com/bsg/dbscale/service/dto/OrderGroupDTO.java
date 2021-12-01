package com.bsg.dbscale.service.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class OrderGroupDTO extends IdentificationDTO implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * 
     */
    private String category;
    private BusinessSubsystemBaseDTO businessSubsystem;
    private IdentificationDTO site;
    private IdentificationDTO businessArea;
    private DisplayDTO sysArchitecture;
    private DisplayDTO createType;
    private DisplayDTO state;
    private String msg;
    private Boolean highAvailable;
    private UserDTO owner;
    private InfoDTO created;
    private InfoDTO modified;
    private List<OrderDTO> orders;
    private String servGroupId;
    private String taskId;

    public OrderGroupDTO() {
        this.orders = new ArrayList<>();
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public BusinessSubsystemBaseDTO getBusinessSubsystem() {
        return businessSubsystem;
    }

    public void setBusinessSubsystem(BusinessSubsystemBaseDTO businessSubsystem) {
        this.businessSubsystem = businessSubsystem;
    }

    public IdentificationDTO getSite() {
        return site;
    }

    public void setSite(IdentificationDTO site) {
        this.site = site;
    }

    public IdentificationDTO getBusinessArea() {
        return businessArea;
    }

    public void setBusinessArea(IdentificationDTO businessArea) {
        this.businessArea = businessArea;
    }

    public DisplayDTO getSysArchitecture() {
        return sysArchitecture;
    }

    public void setSysArchitecture(DisplayDTO sysArchitecture) {
        this.sysArchitecture = sysArchitecture;
    }

    public DisplayDTO getCreateType() {
        return createType;
    }

    public void setCreateType(DisplayDTO createType) {
        this.createType = createType;
    }

    public DisplayDTO getState() {
        return state;
    }

    public void setState(DisplayDTO state) {
        this.state = state;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Boolean getHighAvailable() {
        return highAvailable;
    }

    public void setHighAvailable(Boolean highAvailable) {
        this.highAvailable = highAvailable;
    }

    public UserDTO getOwner() {
        return owner;
    }

    public void setOwner(UserDTO owner) {
        this.owner = owner;
    }

    public InfoDTO getCreated() {
        return created;
    }

    public void setCreated(InfoDTO created) {
        this.created = created;
    }

    public InfoDTO getModified() {
        return modified;
    }

    public void setModified(InfoDTO modified) {
        this.modified = modified;
    }

    public List<OrderDTO> getOrders() {
        return orders;
    }

    public void setOrders(List<OrderDTO> orders) {
        this.orders = orders;
    }

    public String getServGroupId() {
        return servGroupId;
    }

    public void setServGroupId(String servGroupId) {
        this.servGroupId = servGroupId;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    @Override
    public String toString() {
        return super.toString() + "OrderGroupDTO [category=" + category + ", businessSubsystem=" + businessSubsystem
                + ", site=" + site + ", businessArea=" + businessArea + ", sysArchitecture=" + sysArchitecture
                + ", createType=" + createType + ", state=" + state + ", msg=" + msg + ", highAvailable="
                + highAvailable + ", owner=" + owner + ", created=" + created + ", modified=" + modified + ", orders="
                + orders + ", servGroupId=" + servGroupId + ", taskId=" + taskId + "]";
    }

    public class UserDTO {
        private String username;
        private String name;
        private String telephone;
        private String company;

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getTelephone() {
            return telephone;
        }

        public void setTelephone(String telephone) {
            this.telephone = telephone;
        }

        public String getCompany() {
            return company;
        }

        public void setCompany(String company) {
            this.company = company;
        }

        @Override
        public String toString() {
            return "UserDTO [username=" + username + ", name=" + name + ", telephone=" + telephone + ", company="
                    + company + "]";
        }

    }
}
