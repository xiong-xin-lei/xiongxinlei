package com.bsg.dbscale.dao.domain;

import java.io.Serializable;

public class PrivilegeDO implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * 
     */
    private String code;

    /**
     * 描述
     */
    private String description;

    /**
     * 是否可用
     */
    private Boolean enabled;

    /**
     * 是否全局
     */
    private Boolean global;

    /**
     * 顺序
     */
    private Integer sequence;

    /**
     * 获取
     * 
     * @return code
     */
    public String getCode() {
        return code;
    }

    /**
     * 设置
     * 
     * @param code
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * 获取描述
     * 
     * @return description 描述
     */
    public String getDescription() {
        return description;
    }

    /**
     * 设置描述
     * 
     * @param description
     *            描述
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * 获取是否可用
     * 
     * @return enabled 是否可用
     */
    public Boolean getEnabled() {
        return enabled;
    }

    /**
     * 设置是否可用
     * 
     * @param enabled
     *            是否可用
     */
    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    /**
     * 获取顺序
     * 
     * @return sequence 顺序
     */
    public Integer getSequence() {
        return sequence;
    }

    /**
     * 获取是否全局
     * 
     * @return global 是否全局
     */
    public Boolean getGlobal() {
        return global;
    }

    /**
     * 设置是否全局
     * 
     * @param global
     *            是否全局
     */
    public void setGlobal(Boolean global) {
        this.global = global;
    }

    /**
     * 设置顺序
     * 
     * @param sequence
     *            顺序
     */
    public void setSequence(Integer sequence) {
        this.sequence = sequence;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "PrivilegeDO [code=" + code + ", description=" + description + ", enabled=" + enabled + ", global="
                + global + ", sequence=" + sequence + "]";
    }

}
