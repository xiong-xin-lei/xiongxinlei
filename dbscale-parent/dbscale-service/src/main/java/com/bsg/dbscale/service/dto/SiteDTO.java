package com.bsg.dbscale.service.dto;

import java.io.Serializable;

/**
 * 站点数据传输对象
 * 
 * @author HCK
 *
 */
public class SiteDTO implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private String id;
    private String name;
    private DisplayDTO region;
    private String type;
    private String domain;
    private Integer port;
    private String version;
    private DisplayDTO state;
    private String storageMode;
    private String networkMode;
    private String description;
    // private ImageRegistryDTO imageRegistry;
    // private MonitorServerDTO monitorServer;
    // private MonitorUiDTO monitorUi;
    // private DashboardDTO dashboard;
    private InfoDTO created;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public DisplayDTO getRegion() {
        return region;
    }

    public void setRegion(DisplayDTO region) {
        this.region = region;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public DisplayDTO getState() {
        return state;
    }

    public void setState(DisplayDTO state) {
        this.state = state;
    }

    public String getStorageMode() {
        return storageMode;
    }

    public void setStorageMode(String storageMode) {
        this.storageMode = storageMode;
    }

    public String getNetworkMode() {
        return networkMode;
    }

    public void setNetworkMode(String networkMode) {
        this.networkMode = networkMode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public InfoDTO getCreated() {
        return created;
    }

    public void setCreated(InfoDTO created) {
        this.created = created;
    }

    @Override
    public String toString() {
        return "SiteDTO [id=" + id + ", name=" + name + ", region=" + region + ", type=" + type + ", domain=" + domain
                + ", port=" + port + ", version=" + version + ", state=" + state + ", storageMode=" + storageMode
                + ", networkMode=" + networkMode + ", description=" + description + ", created=" + created + "]";
    }

    public class ImageRegistryDTO extends CommonInfoDTO {
    }

    public class MonitorServerDTO extends CommonInfoDTO {
    }

    public class MonitorUiDTO extends CommonInfoDTO {
    }

    public class DashboardDTO extends CommonInfoDTO {
    }

    public class CommonInfoDTO {

        private String type;

        private String scheme;

        private String domain;

        private String port;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getScheme() {
            return scheme;
        }

        public void setScheme(String scheme) {
            this.scheme = scheme;
        }

        public String getDomain() {
            return domain;
        }

        public void setDomain(String domain) {
            this.domain = domain;
        }

        public String getPort() {
            return port;
        }

        public void setPort(String port) {
            this.port = port;
        }

        @Override
        public String toString() {
            return "CommonInfoDTO [type=" + type + ", scheme=" + scheme + ", domain=" + domain + ", port=" + port + "]";
        }

    }

}
