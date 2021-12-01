package com.bsg.dbscale.service.dto;

import java.io.Serializable;
import java.util.List;

public class UserDTO extends UserBaseDTO implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private String email;
    private String emerContact;
    private String emerTel;
    private String authType;
    private Boolean enabled;
    private Boolean ogAutoExamine;
    private Boolean ogAutoExecute;
    private List<IdentificationDTO> groups;
    private IdentificationDTO role;
    private InfoDTO created;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmerContact() {
        return emerContact;
    }

    public void setEmerContact(String emerContact) {
        this.emerContact = emerContact;
    }

    public String getEmerTel() {
        return emerTel;
    }

    public void setEmerTel(String emerTel) {
        this.emerTel = emerTel;
    }

    public String getAuthType() {
        return authType;
    }

    public void setAuthType(String authType) {
        this.authType = authType;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public Boolean getOgAutoExamine() {
        return ogAutoExamine;
    }

    public void setOgAutoExamine(Boolean ogAutoExamine) {
        this.ogAutoExamine = ogAutoExamine;
    }

    public Boolean getOgAutoExecute() {
        return ogAutoExecute;
    }

    public void setOgAutoExecute(Boolean ogAutoExecute) {
        this.ogAutoExecute = ogAutoExecute;
    }

    public List<IdentificationDTO> getGroups() {
        return groups;
    }

    public void setGroups(List<IdentificationDTO> groups) {
        this.groups = groups;
    }

    public IdentificationDTO getRole() {
        return role;
    }

    public void setRole(IdentificationDTO role) {
        this.role = role;
    }

    public InfoDTO getCreated() {
        return created;
    }

    public void setCreated(InfoDTO created) {
        this.created = created;
    }

    @Override
    public String toString() {
        return super.toString() + "UserDTO [email=" + email + ", emerContact=" + emerContact + ", emerTel=" + emerTel
                + ", authType=" + authType + ", enabled=" + enabled + ", ogAutoExamine=" + ogAutoExamine
                + ", ogAutoExecute=" + ogAutoExecute + ", groups=" + groups + ", role=" + role + ", created=" + created
                + "]";
    }

}
