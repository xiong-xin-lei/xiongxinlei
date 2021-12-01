package com.bsg.dbscale.service.dto;

import java.io.Serializable;

public class RedisServGroupDTO extends ServGroupDTO implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private Integer redisServCnt;

    public Integer getRedisServCnt() {
        return redisServCnt;
    }

    public void setRedisServCnt(Integer redisServCnt) {
        this.redisServCnt = redisServCnt;
    }

    @Override
    public String toString() {
        return super.toString() + "RedisServGroupDTO [redisServCnt=" + redisServCnt + "]";
    }

}
