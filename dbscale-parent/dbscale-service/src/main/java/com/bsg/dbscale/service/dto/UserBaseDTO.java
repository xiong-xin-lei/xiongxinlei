package com.bsg.dbscale.service.dto;

import java.io.Serializable;

public class UserBaseDTO implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

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
        return "UserBaseDTO [username=" + username + ", name=" + name + ", telephone=" + telephone + ", company="
                + company + "]";
    }

}
