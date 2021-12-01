package com.bsg.dbscale.service.form;

import java.io.Serializable;

public class DictForm implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * 字典名称
     */
    private String name;

    /**
     * 顺序
     */
    private Integer sequence;

    /**
     * 获取字典名称
     * 
     * @return name 字典名称
     */
    public String getName() {
        return name;
    }

    /**
     * 设置字典名称
     * 
     * @param name
     *            字典名称
     */
    public void setName(String name) {
        this.name = name;
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
        return "DictForm [name=" + name + ", sequence=" + sequence + "]";
    }

}
