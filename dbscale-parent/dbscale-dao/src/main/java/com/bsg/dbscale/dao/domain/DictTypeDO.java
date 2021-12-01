package com.bsg.dbscale.dao.domain;

import java.io.Serializable;
import java.util.List;

public class DictTypeDO implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * 字典类型编码
     */
    private String code;

    /**
     * 字典类型项称
     */
    private String name;

    /**
     * 字典类型集合
     */
    private List<DictDO> dicts;

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

    public List<DictDO> getDicts() {
        return dicts;
    }

    public void setDicts(List<DictDO> dicts) {
        this.dicts = dicts;
    }

    @Override
    public String toString() {
        return "DictTypeDO [code=" + code + ", name=" + name + ", dicts=" + dicts + "]";
    }

}
