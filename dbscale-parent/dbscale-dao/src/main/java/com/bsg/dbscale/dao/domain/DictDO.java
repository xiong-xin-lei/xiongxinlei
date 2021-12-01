package com.bsg.dbscale.dao.domain;

import java.io.Serializable;
import java.util.Date;

public class DictDO implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * 字典类型编码
     */
    private String dictTypeCode;

    /**
     * 字典编码
     */
    private String code;

    /**
     * 字典名称
     */
    private String name;

    /**
     * 显示顺序
     */
    private Integer sequence;

    /**
     * 创建时间
     */
    private Date gmtCreate;

    /**
     * 创建者
     */
    private String creator;

    public String getDictTypeCode() {
        return dictTypeCode;
    }

    public void setDictTypeCode(String dictTypeCode) {
        this.dictTypeCode = dictTypeCode;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getSequence() {
        return sequence;
    }

    public void setSequence(Integer sequence) {
        this.sequence = sequence;
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

    @Override
    public String toString() {
        return "DictDO [dictTypeCode=" + dictTypeCode + ", code=" + code + ", name=" + name + ", sequence=" + sequence
                + ", gmtCreate=" + gmtCreate + ", creator=" + creator + "]";
    }

}
