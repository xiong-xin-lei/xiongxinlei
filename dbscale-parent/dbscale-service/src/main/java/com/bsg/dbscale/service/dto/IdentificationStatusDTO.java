package com.bsg.dbscale.service.dto;

public class IdentificationStatusDTO extends IdentificationDTO {
    private Boolean enabled;

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public String toString() {
        return super.toString() + "IdentificationStatusDTO [enabled=" + enabled + "]";
    }

}
