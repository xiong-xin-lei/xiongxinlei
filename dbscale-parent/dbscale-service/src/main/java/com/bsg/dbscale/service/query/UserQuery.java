package com.bsg.dbscale.service.query;

import java.io.Serializable;

public class UserQuery implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * 角色编码
     */
    private String roleId;

    /**
     * 组别编码
     */
    private String groupId;

    /**
     * 是否可用
     */
    private Boolean enabled;

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public String toString() {
        return "UserQuery [roleId=" + roleId + ", groupId=" + groupId + ", enabled=" + enabled + "]";
    }

}
