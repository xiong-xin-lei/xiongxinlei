package com.bsg.dbscale.service.dto;

import java.io.Serializable;
import java.util.List;

import com.bsg.dbscale.dao.domain.AppDO;

public class AppDTO implements Serializable {

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

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getPos() {
        return pos;
    }

    public void setPos(String pos) {
        this.pos = pos;
    }

    public Integer getSequence() {
        return sequence;
    }

    public void setSequence(Integer sequence) {
        this.sequence = sequence;
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

    public Long getPid() {
        return pid;
    }

    public void setPid(Long pid) {
        this.pid = pid;
    }

    public List<AppDO> getChildrens() {
        return childrens;
    }

    public void setChildrens(List<AppDO> childrens) {
        this.childrens = childrens;
    }

    @Override
    public String toString() {
        return "AppDTO [id=" + id + ", name=" + name + ", type=" + type + ", code=" + code + ", icon=" + icon + ", pos="
                + pos + ", sequence=" + sequence + ", tabletopSeq=" + tabletopSeq + ", rowSeq=" + rowSeq + ", pid="
                + pid + ", childrens=" + childrens + "]";
    }

}
