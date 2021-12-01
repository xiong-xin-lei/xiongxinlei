package com.bsg.dbscale.dao.domain;

import java.io.Serializable;
import java.util.List;

public class AppDO implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private Long id;
    private String name;
    private String type;
    private String code;
    private String icon;
    private String pos;
    private Integer sequence;
    private Integer tabletopSeq;
    private Integer rowSeq;
    private Long pid;
    private List<AppDO> childrens;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Integer getSequence() {
        return sequence;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public void setSequence(Integer sequence) {
        this.sequence = sequence;
    }

    public Long getPid() {
        return pid;
    }

    public void setPid(Long pid) {
        this.pid = pid;
    }

    public String getPos() {
        return pos;
    }

    public void setPos(String pos) {
        this.pos = pos;
    }

    public Integer getTabletopSeq() {
        return tabletopSeq;
    }

    public void setTabletopSeq(Integer tabletopSeq) {
        this.tabletopSeq = tabletopSeq;
    }

    public Integer getRowSeq() {
        return rowSeq;
    }

    public void setRowSeq(Integer rowSeq) {
        this.rowSeq = rowSeq;
    }

    public List<AppDO> getChildrens() {
        return childrens;
    }

    public void setChildrens(List<AppDO> childrens) {
        this.childrens = childrens;
    }

    @Override
    public String toString() {
        return "AppDO [id=" + id + ", name=" + name + ", type=" + type + ", code=" + code + ", icon=" + icon + ", pos="
                + pos + ", sequence=" + sequence + ", tabletopSeq=" + tabletopSeq + ", rowSeq=" + rowSeq + ", pid="
                + pid + ", childrens=" + childrens + "]";
    }

}
