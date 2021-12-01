package com.bsg.dbscale.service.dto;

import java.io.Serializable;

public class ServGroupBaseDTO extends IdentificationDTO implements Serializable {

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
    private Boolean highAvailable;
    private Boolean flag;
    private UserBaseDTO owner;
    private String gmtCreate;

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

    public Boolean getHighAvailable() {
        return highAvailable;
    }

    public void setHighAvailable(Boolean highAvailable) {
        this.highAvailable = highAvailable;
    }

    public Boolean getFlag() {
        return flag;
    }

    public void setFlag(Boolean flag) {
        this.flag = flag;
    }

    public UserBaseDTO getOwner() {
        return owner;
    }

    public void setOwner(UserBaseDTO owner) {
        this.owner = owner;
    }

    public String getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(String gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    @Override
    public String toString() {
        return "ServGroupBaseDTO [category=" + category + ", businessSubsystem=" + businessSubsystem + ", site=" + site
                + ", businessArea=" + businessArea + ", sysArchitecture=" + sysArchitecture + ", highAvailable="
                + highAvailable + ", flag=" + flag + ", owner=" + owner + ", gmtCreate=" + gmtCreate + "]";
    }

}
