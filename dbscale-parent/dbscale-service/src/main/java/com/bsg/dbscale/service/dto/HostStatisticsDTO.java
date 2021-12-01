package com.bsg.dbscale.service.dto;

public class HostStatisticsDTO<T> extends StatisticsDTO<T> {
    private boolean upLimit;

    public boolean isUpLimit() {
        return upLimit;
    }

    public void setUpLimit(boolean upLimit) {
        this.upLimit = upLimit;
    }

    @Override
    public String toString() {
        return "HostStatisticsDTO [upLimit=" + upLimit + "]";
    }

}
