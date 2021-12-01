package com.bsg.dbscale.cm.model;

import java.io.Serializable;

import com.alibaba.fastjson.annotation.JSONField;

public class CmSite extends CmSiteBase implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    @JSONField(name = "region")
    private String region;

    @JSONField(name = "image_registry")
    private ImageRegistryInfo imageRegistry;

    @JSONField(name = "monitor_server")
    private MonitorServerInfo monitorServer;

    @JSONField(name = "monitor_ui")
    private MonitorUiInfo monitorUi;

    @JSONField(name = "service_discovery")
    private ServiceDiscoveryInfo serviceDiscovery;

    @JSONField(name = "dashboard")
    private DashboardInfo dashboard;

    @JSONField(name = "kubernetes")
    private Kubernetes kubernetes;

    @JSONField(name = "loadbalancer")
    private String loadbalancer;

    @JSONField(name = "state")
    private String state;

    @JSONField(name = "desc")
    private String desc;

    @JSONField(name = "created_at")
    private String createdAt;

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public ImageRegistryInfo getImageRegistry() {
        return imageRegistry;
    }

    public void setImageRegistry(ImageRegistryInfo imageRegistry) {
        this.imageRegistry = imageRegistry;
    }

    public MonitorServerInfo getMonitorServer() {
        return monitorServer;
    }

    public void setMonitorServer(MonitorServerInfo monitorServer) {
        this.monitorServer = monitorServer;
    }

    public MonitorUiInfo getMonitorUi() {
        return monitorUi;
    }

    public void setMonitorUi(MonitorUiInfo monitorUi) {
        this.monitorUi = monitorUi;
    }

    public ServiceDiscoveryInfo getServiceDiscovery() {
        return serviceDiscovery;
    }

    public void setServiceDiscovery(ServiceDiscoveryInfo serviceDiscovery) {
        this.serviceDiscovery = serviceDiscovery;
    }

    public DashboardInfo getDashboard() {
        return dashboard;
    }

    public void setDashboard(DashboardInfo dashboard) {
        this.dashboard = dashboard;
    }

    public Kubernetes getKubernetes() {
        return kubernetes;
    }

    public void setKubernetes(Kubernetes kubernetes) {
        this.kubernetes = kubernetes;
    }

    public String getLoadbalancer() {
        return loadbalancer;
    }

    public void setLoadbalancer(String loadbalancer) {
        this.loadbalancer = loadbalancer;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "CmSite [region=" + region + ", imageRegistry=" + imageRegistry + ", monitorServer=" + monitorServer
                + ", monitorUi=" + monitorUi + ", serviceDiscovery=" + serviceDiscovery + ", dashboard=" + dashboard
                + ", kubernetes=" + kubernetes + ", loadbalancer=" + loadbalancer + ", state=" + state + ", desc="
                + desc + ", createdAt=" + createdAt + "]";
    }

    public class ImageRegistryInfo {
        @JSONField(name = "type")
        private String type;

        @JSONField(name = "scheme")
        private String scheme;

        @JSONField(name = "domain")
        private String domain;

        @JSONField(name = "port")
        private String port;

        @JSONField(name = "project_name")
        private String projectName;

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

        public String getProjectName() {
            return projectName;
        }

        public void setProjectName(String projectName) {
            this.projectName = projectName;
        }

        @Override
        public String toString() {
            return "ImageRegistryInfo [type=" + type + ", scheme=" + scheme + ", domain=" + domain + ", port=" + port
                    + ", projectName=" + projectName + "]";
        }

    }

    public class MonitorServerInfo extends Info {
    }

    public class MonitorUiInfo extends Info {
    }

    public class ServiceDiscoveryInfo extends Info {
    }

    public class DashboardInfo extends Info {

        @JSONField(name = "token_sa_name")
        private String tokenSaName;

        @JSONField(name = "certificate_name")
        private String certificateName;

        @JSONField(name = "token")
        private String token;

        @JSONField(name = "certificate")
        private String certificate;

        public String getTokenSaName() {
            return tokenSaName;
        }

        public void setTokenSaName(String tokenSaName) {
            this.tokenSaName = tokenSaName;
        }

        public String getCertificateName() {
            return certificateName;
        }

        public void setCertificateName(String certificateName) {
            this.certificateName = certificateName;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public String getCertificate() {
            return certificate;
        }

        public void setCertificate(String certificate) {
            this.certificate = certificate;
        }

        @Override
        public String toString() {
            return "DashboardInfo [tokenSaName=" + tokenSaName + ", certificateName=" + certificateName + ", token="
                    + token + ", certificate=" + certificate + "]";
        }

    }

    public class Info {

        @JSONField(name = "type")
        private String type;

        @JSONField(name = "scheme")
        private String scheme;

        @JSONField(name = "service_name")
        private String serviceName;

        @JSONField(name = "port")
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

        public String getServiceName() {
            return serviceName;
        }

        public void setServiceName(String serviceName) {
            this.serviceName = serviceName;
        }

        public String getPort() {
            return port;
        }

        public void setPort(String port) {
            this.port = port;
        }

        @Override
        public String toString() {
            return "Info [type=" + type + ", scheme=" + scheme + ", serviceName=" + serviceName + ", port=" + port
                    + "]";
        }

    }

    public class Kubernetes {

        @JSONField(name = "version")
        private String version;

        @JSONField(name = "type")
        private String type;

        @JSONField(name = "domain")
        private String domain;

        @JSONField(name = "port")
        private Integer port;

        @JSONField(name = "storage_mode")
        private String storageMode;

        @JSONField(name = "network_mode")
        private String networkMode;

        @JSONField(name = "endpoint_mode")
        private String endpointMode;

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
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

        public String getEndpointMode() {
            return endpointMode;
        }

        public void setEndpointMode(String endpointMode) {
            this.endpointMode = endpointMode;
        }

        @Override
        public String toString() {
            return "Kubernetes [version=" + version + ", type=" + type + ", domain=" + domain + ", port=" + port
                    + ", storageMode=" + storageMode + ", networkMode=" + networkMode + ", endpointMode=" + endpointMode
                    + "]";
        }

    }

}
