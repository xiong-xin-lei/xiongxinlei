package com.bsg.dbscale.service.dto;

public class StatisticsDTO<T> {
    private T used;
    private T capacity;

    public T getUsed() {
        return used;
    }

    public void setUsed(T used) {
        this.used = used;
    }

    public T getCapacity() {
        return capacity;
    }

    public void setCapacity(T capacity) {
        this.capacity = capacity;
    }

    @Override
    public String toString() {
        return "StatisticsDTO [used=" + used + ", capacity=" + capacity + "]";
    }

}
