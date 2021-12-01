package com.bsg.dbscale.service.form;

import java.io.Serializable;

public class UserForm implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 姓名
     */
    private String name;

    /**
     * 联系电话
     */
    private String telephone;

    /**
     * 邮件
     */
    private String email;

    /**
     * 单位
     */
    private String company;

    /**
     * 紧急联系人
     */
    private String emerContact;

    /**
     * 紧急联系人电话
     */
    private String emerTel;

    /**
     * 角色编码
     */
    private String roleId;

    private Boolean ogAutoExamine;
    private Boolean ogAutoExecute;

    /**
     * 获取用户名
     * 
     * @return username 用户名
     */
    public String getUsername() {
        return username;
    }

    /**
     * 设置用户名
     * 
     * @param username
     *            用户名
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * 获取密码
     * 
     * @return password 密码
     */
    public String getPassword() {
        return password;
    }

    /**
     * 设置密码
     * 
     * @param password
     *            密码
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * 获取姓名
     * 
     * @return name 姓名
     */
    public String getName() {
        return name;
    }

    /**
     * 设置姓名
     * 
     * @param name
     *            姓名
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获取联系电话
     * 
     * @return telephone 联系电话
     */
    public String getTelephone() {
        return telephone;
    }

    /**
     * 设置联系电话
     * 
     * @param telephone
     *            联系电话
     */
    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    /**
     * 获取邮件
     * 
     * @return email 邮件
     */
    public String getEmail() {
        return email;
    }

    /**
     * 设置邮件
     * 
     * @param email
     *            邮件
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * 获取单位
     * 
     * @return company 单位
     */
    public String getCompany() {
        return company;
    }

    /**
     * 设置单位
     * 
     * @param company
     *            单位
     */
    public void setCompany(String company) {
        this.company = company;
    }

    /**
     * 获取紧急联系人
     * 
     * @return emerContact 紧急联系人
     */
    public String getEmerContact() {
        return emerContact;
    }

    /**
     * 设置紧急联系人
     * 
     * @param emerContact
     *            紧急联系人
     */
    public void setEmerContact(String emerContact) {
        this.emerContact = emerContact;
    }

    /**
     * 获取紧急联系人电话
     * 
     * @return emerTel 紧急联系人电话
     */
    public String getEmerTel() {
        return emerTel;
    }

    /**
     * 设置紧急联系人电话
     * 
     * @param emerTel
     *            紧急联系人电话
     */
    public void setEmerTel(String emerTel) {
        this.emerTel = emerTel;
    }

    /**
     * 获取角色编码
     * 
     * @return roleId 角色编码
     */
    public String getRoleId() {
        return roleId;
    }

    /**
     * 设置角色编码
     * 
     * @param roleId
     *            角色编码
     */
    public void setRoleId(String roleId) {
        this.roleId = roleId;
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

    @Override
    public String toString() {
        return "UserForm [username=" + username + ", password=" + password + ", name=" + name + ", telephone="
                + telephone + ", email=" + email + ", company=" + company + ", emerContact=" + emerContact
                + ", emerTel=" + emerTel + ", roleId=" + roleId + ", ogAutoExamine=" + ogAutoExamine
                + ", ogAutoExecute=" + ogAutoExecute + "]";
    }

}
