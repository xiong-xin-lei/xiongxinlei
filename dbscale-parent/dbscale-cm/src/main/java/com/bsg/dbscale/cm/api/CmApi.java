package com.bsg.dbscale.cm.api;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.bsg.dbscale.cm.body.CmBackupBody;
import com.bsg.dbscale.cm.body.CmBackupExternalBody;
import com.bsg.dbscale.cm.body.CmCfgBody;
import com.bsg.dbscale.cm.body.CmClusterBody;
import com.bsg.dbscale.cm.body.CmDBSchemaBody;
import com.bsg.dbscale.cm.body.CmDBUserBody;
import com.bsg.dbscale.cm.body.CmHostBody;
import com.bsg.dbscale.cm.body.CmImageBody;
import com.bsg.dbscale.cm.body.CmImageTemplateBody;
import com.bsg.dbscale.cm.body.CmMaintenanceBody;
import com.bsg.dbscale.cm.body.CmNetworkBody;
import com.bsg.dbscale.cm.body.CmNfsBody;
import com.bsg.dbscale.cm.body.CmProxyUserBody;
import com.bsg.dbscale.cm.body.CmRemoteStorageBody;
import com.bsg.dbscale.cm.body.CmRemoteStoragePoolBody;
import com.bsg.dbscale.cm.body.CmReplicationModeBody;
import com.bsg.dbscale.cm.body.CmReplicationSetSourceBody;
import com.bsg.dbscale.cm.body.CmResetPwdBody;
import com.bsg.dbscale.cm.body.CmServiceBody;
import com.bsg.dbscale.cm.body.CmServiceRoleBody;
import com.bsg.dbscale.cm.body.CmServiceStateBody;
import com.bsg.dbscale.cm.body.CmServicesLinkBody;
import com.bsg.dbscale.cm.body.CmSiteBody;
import com.bsg.dbscale.cm.body.CmStorageclassBody;
import com.bsg.dbscale.cm.body.CmUnitRebuildBody;
import com.bsg.dbscale.cm.body.CmUnitRestoreBody;
import com.bsg.dbscale.cm.body.CmUnitStateBody;
import com.bsg.dbscale.cm.constant.CmConsts;
import com.bsg.dbscale.cm.exception.CallingInterfaceException;
import com.bsg.dbscale.cm.exception.ValueNotBlankException;
import com.bsg.dbscale.cm.model.CmAction;
import com.bsg.dbscale.cm.model.CmBackupFile;
import com.bsg.dbscale.cm.model.CmCluster;
import com.bsg.dbscale.cm.model.CmDBSchema;
import com.bsg.dbscale.cm.model.CmDBUser;
import com.bsg.dbscale.cm.model.CmHost;
import com.bsg.dbscale.cm.model.CmHostEvent;
import com.bsg.dbscale.cm.model.CmHostUnit;
import com.bsg.dbscale.cm.model.CmImage;
import com.bsg.dbscale.cm.model.CmImageTemplate;
import com.bsg.dbscale.cm.model.CmLoadbalancer;
import com.bsg.dbscale.cm.model.CmNetwork;
import com.bsg.dbscale.cm.model.CmNfs;
import com.bsg.dbscale.cm.model.CmNode;
import com.bsg.dbscale.cm.model.CmProxyUser;
import com.bsg.dbscale.cm.model.CmRemoteStorage;
import com.bsg.dbscale.cm.model.CmRemoteStoragePool;
import com.bsg.dbscale.cm.model.CmService;
import com.bsg.dbscale.cm.model.CmServiceArch;
import com.bsg.dbscale.cm.model.CmServiceArchBase;
import com.bsg.dbscale.cm.model.CmServiceCfg;
import com.bsg.dbscale.cm.model.CmSite;
import com.bsg.dbscale.cm.model.CmStorageclass;
import com.bsg.dbscale.cm.model.CmTerminal;
import com.bsg.dbscale.cm.model.CmTopology;
import com.bsg.dbscale.cm.model.CmUnitEvent;
import com.bsg.dbscale.cm.model.CmVersion;
import com.bsg.dbscale.cm.query.CmBackupFileQuery;
import com.bsg.dbscale.cm.query.CmClusterQuery;
import com.bsg.dbscale.cm.query.CmHostQuery;
import com.bsg.dbscale.cm.query.CmImageQuery;
import com.bsg.dbscale.cm.query.CmLoadbalancerQuery;
import com.bsg.dbscale.cm.query.CmNetworkQuery;
import com.bsg.dbscale.cm.query.CmNfsQuery;
import com.bsg.dbscale.cm.query.CmNodeQuery;
import com.bsg.dbscale.cm.query.CmRemoteStoragePoolQuery;
import com.bsg.dbscale.cm.query.CmRemoteStorageQuery;
import com.bsg.dbscale.cm.query.CmServiceArchQuery;
import com.bsg.dbscale.cm.query.CmServiceQuery;
import com.bsg.dbscale.cm.query.CmSiteQuery;
import com.bsg.dbscale.cm.query.CmStorageclassQuery;
import com.bsg.dbscale.cm.response.CmBackupExternalResp;
import com.bsg.dbscale.cm.response.CmBackupResp;
import com.bsg.dbscale.cm.response.CmSaveHostResp;
import com.bsg.dbscale.cm.response.CmSaveServiceResp;
import com.bsg.dbscale.cm.response.CmUnitRestoreResp;
import com.bsg.dbscale.util.http.HttpRequestUtil;
import com.bsg.dbscale.util.http.HttpResp;

public class CmApi {

    protected static Logger logger = LoggerFactory.getLogger(CmApi.class);

    public static String FILEPATH = File.separator + "etc" + File.separator + "dbscale" + File.separator
            + "sys.properties";

    public static String exceptionPrefix = "调用cm接口异常：";

    public static CmVersion getVersion() throws Exception {
        HttpResp httpResp = getRequest("/version", null);
        return JSONObject.parseObject(httpResp.getResponseContent(), CmVersion.class);
    }

    public static List<CmSite> listSite(CmSiteQuery siteQuery) throws Exception {
        HttpResp httpResp = getRequest("/sites", siteQuery);
        return JSONArray.parseArray(httpResp.getResponseContent(), CmSite.class);
    }

    public static CmSite getSite(String cmSiteId) throws Exception {
        CmSiteQuery siteQuery = new CmSiteQuery();
        siteQuery.setId(cmSiteId);
        List<CmSite> cmSites = listSite(siteQuery);
        if (cmSites.size() == 1) {
            return cmSites.get(0);
        }
        return null;
    }

    public static void saveSite(CmSiteBody siteBody) throws Exception {
        postRequest("/sites", siteBody);
    }

    public static void updateSite(String cmSiteId, CmSiteBody siteBody) throws Exception {
        String endpoint = "/sites/{0}";
        endpoint = MessageFormat.format(endpoint, cmSiteId);
        putRequest(endpoint, siteBody);
    }

    public static void removeSite(String siteId) throws Exception {
        String endpoint = "/sites/{0}";
        endpoint = MessageFormat.format(endpoint, siteId);
        deleteRequest(endpoint);
    }

    public static List<CmNfs> listNfs(CmNfsQuery nfsQuery) throws Exception {
        HttpResp httpResp = getRequest("/backups/nfs", nfsQuery);
        return JSONArray.parseArray(httpResp.getResponseContent(), CmNfs.class);
    }

    public static CmNfs getNfs(String nfsId) throws Exception {
        CmNfsQuery nfsQuery = new CmNfsQuery();
        nfsQuery.setId(nfsId);
        List<CmNfs> cmNfs = listNfs(nfsQuery);
        if (cmNfs.size() == 1) {
            return cmNfs.get(0);
        }
        return null;
    }

    public static void saveNFS(CmNfsBody nfsBody) throws Exception {
        postRequest("/backups/nfs", nfsBody);
    }

    public static void updateNFS(String cmNfsId, CmNfsBody cmNfsBody) throws Exception {
        String endpoint = "/backups/nfs/{0}";
        endpoint = MessageFormat.format(endpoint, cmNfsId);
        putRequest(endpoint, cmNfsBody);
    }

    public static void enabledNFS(String cmNfsId) throws Exception {
        CmNfsBody cmNfsBody = new CmNfsBody();
        cmNfsBody.setUnschedulable(false);
        updateNFS(cmNfsId, cmNfsBody);
    }

    public static void disabledNFS(String cmNfsId) throws Exception {
        CmNfsBody cmNfsBody = new CmNfsBody();
        cmNfsBody.setUnschedulable(true);
        updateNFS(cmNfsId, cmNfsBody);
    }

    public static void removeNfs(String nfsId) throws Exception {
        String endpoint = "/backups/nfs/{0}";
        endpoint = MessageFormat.format(endpoint, nfsId);
        deleteRequest(endpoint);
    }

    public static List<CmCluster> listCluster(CmClusterQuery clusterQuery) throws Exception {
        HttpResp httpResp = getRequest("/clusters", clusterQuery);
        return JSONArray.parseArray(httpResp.getResponseContent(), CmCluster.class);
    }

    public static CmCluster getCluster(String cmClusterId) throws Exception {
        CmClusterQuery clusterQuery = new CmClusterQuery();
        clusterQuery.setId(cmClusterId);
        List<CmCluster> cmClusters = listCluster(clusterQuery);
        if (cmClusters.size() == 1) {
            return cmClusters.get(0);
        }
        return null;
    }

    public static void saveCluster(CmClusterBody clusterBody) throws Exception {
        postRequest("/clusters", clusterBody);
    }

    public static void updateCluster(String cmClusterId, CmClusterBody clusterBody) throws Exception {
        String endpoint = "/clusters/{0}";
        endpoint = MessageFormat.format(endpoint, cmClusterId);
        putRequest(endpoint, clusterBody);
    }

    public static void enabledCluster(String cmClusterId) throws Exception {
        CmClusterBody clusterBody = new CmClusterBody();
        clusterBody.setUnschedulable(false);
        updateCluster(cmClusterId, clusterBody);
    }

    public static void disabledCluster(String cmClusterId) throws Exception {
        CmClusterBody clusterBody = new CmClusterBody();
        clusterBody.setUnschedulable(true);
        updateCluster(cmClusterId, clusterBody);
    }

    public static void removeCluster(String cmClusterId) throws Exception {
        String endpoint = "/clusters/{0}";
        endpoint = MessageFormat.format(endpoint, cmClusterId);
        deleteRequest(endpoint);
    }

    public static List<CmNetwork> listNetwork(CmNetworkQuery networkQuery) throws Exception {
        HttpResp httpResp = getRequest("/networks", networkQuery);
        return JSONArray.parseArray(httpResp.getResponseContent(), CmNetwork.class);
    }

    public static CmNetwork getNetwork(String cmNetworkId) throws Exception {
        CmNetworkQuery networkQuery = new CmNetworkQuery();
        networkQuery.setId(cmNetworkId);
        List<CmNetwork> cmNetworks = listNetwork(networkQuery);
        if (cmNetworks.size() == 1) {
            return cmNetworks.get(0);
        }
        return null;
    }

    public static void saveNetwork(CmNetworkBody networkBody) throws Exception {
        postRequest("/networks", networkBody);
    }

    public static void updateNetwork(String cmNetworkId, CmNetworkBody networkBody) throws Exception {
        String endpoint = "/networks/{0}";
        endpoint = MessageFormat.format(endpoint, cmNetworkId);
        putRequest(endpoint, networkBody);
    }

    public static void enabledNetwork(String cmNetworkId) throws Exception {
        CmNetworkBody networkBody = new CmNetworkBody();
        networkBody.setUnschedulable(false);
        updateNetwork(cmNetworkId, networkBody);
    }

    public static void disabledNetwork(String cmNetworkId) throws Exception {
        CmNetworkBody networkBody = new CmNetworkBody();
        networkBody.setUnschedulable(true);
        updateNetwork(cmNetworkId, networkBody);
    }

    public static void removeNetwork(String cmNetworkId) throws Exception {
        String endpoint = "/networks/{0}";
        endpoint = MessageFormat.format(endpoint, cmNetworkId);
        deleteRequest(endpoint);
    }

    public static List<CmRemoteStorage> listRemoteStorage(CmRemoteStorageQuery remoteStorageQuery) throws Exception {
        HttpResp httpResp = getRequest("/storages/remote", remoteStorageQuery);
        return JSONArray.parseArray(httpResp.getResponseContent(), CmRemoteStorage.class);
    }

    public static CmRemoteStorage getRemoteStorage(String cmRemoteStorageId) throws Exception {
        CmRemoteStorageQuery remoteStorageQuery = new CmRemoteStorageQuery();
        remoteStorageQuery.setId(cmRemoteStorageId);
        List<CmRemoteStorage> cmRemoteStorages = listRemoteStorage(remoteStorageQuery);
        if (cmRemoteStorages.size() == 1) {
            return cmRemoteStorages.get(0);
        }
        return null;
    }

    public static void saveRemoteStorage(CmRemoteStorageBody remoteStorageBody) throws Exception {
        postRequest("/storages/remote", remoteStorageBody);
    }

    public static void updateRemoteStorage(String cmRemoteStorageId, CmRemoteStorageBody remoteStorageBody)
            throws Exception {
        String endpoint = "/storages/remote/{0}";
        endpoint = MessageFormat.format(endpoint, cmRemoteStorageId);
        putRequest(endpoint, remoteStorageBody);
    }

    public static void enabledRemoteStorage(String cmRemoteStorageId) throws Exception {
        CmRemoteStorageBody remoteStorageBody = new CmRemoteStorageBody();
        remoteStorageBody.setUnschedulable(false);
        updateRemoteStorage(cmRemoteStorageId, remoteStorageBody);
    }

    public static void disabledRemoteStorage(String cmRemoteStorageId) throws Exception {
        CmRemoteStorageBody remoteStorageBody = new CmRemoteStorageBody();
        remoteStorageBody.setUnschedulable(true);
        updateRemoteStorage(cmRemoteStorageId, remoteStorageBody);
    }

    public static void removeRemoteStorage(String cmRemoteStorageId) throws Exception {
        String endpoint = "/storages/remote/{0}";
        endpoint = MessageFormat.format(endpoint, cmRemoteStorageId);
        deleteRequest(endpoint + cmRemoteStorageId);
    }

    public static List<CmRemoteStoragePool> listRemoteStoragePool(String cmRemoteStorageId,
            CmRemoteStoragePoolQuery remoteStoragePoolQuery) throws Exception {
        String endpoint = "/storages/remote/{0}/pools";
        endpoint = MessageFormat.format(endpoint, cmRemoteStorageId);
        HttpResp httpResp = getRequest(endpoint, remoteStoragePoolQuery);
        return JSONArray.parseArray(httpResp.getResponseContent(), CmRemoteStoragePool.class);
    }

    public static CmRemoteStoragePool getRemoteStoragePool(String cmRemoteStorageId, String cmPoolId) throws Exception {
        CmRemoteStoragePoolQuery remoteStoragePoolQuery = new CmRemoteStoragePoolQuery();
        remoteStoragePoolQuery.setId(cmPoolId);
        List<CmRemoteStoragePool> cmRemoteStoragePools = listRemoteStoragePool(cmRemoteStorageId,
                remoteStoragePoolQuery);
        if (cmRemoteStoragePools.size() == 1) {
            return cmRemoteStoragePools.get(0);
        }
        return null;
    }

    public static void saveRemoteStoragePool(String cmRemoteStorageId, CmRemoteStoragePoolBody remoteStoragePoolBody)
            throws Exception {
        String endpoint = "/storages/remote/{0}/pools";
        endpoint = MessageFormat.format(endpoint, cmRemoteStorageId);
        postRequest(endpoint, remoteStoragePoolBody);
    }

    public static void updateRemoteStoragePool(String cmRemoteStorageId, String cmPoolId,
            CmRemoteStoragePoolBody remoteStoragePoolBody) throws Exception {
        String endpoint = "/storages/remote/{0}/pools/{1}";
        endpoint = MessageFormat.format(endpoint, cmRemoteStorageId, cmPoolId);
        putRequest(endpoint, remoteStoragePoolBody);
    }

    public static void enabledRemoteStoragePool(String cmRemoteStorageId, String cmPoolId) throws Exception {
        CmRemoteStoragePoolBody remoteStoragePoolBody = new CmRemoteStoragePoolBody();
        remoteStoragePoolBody.setUnschedulable(false);
        updateRemoteStoragePool(cmRemoteStorageId, cmPoolId, remoteStoragePoolBody);
    }

    public static void disabledRemoteStoragePool(String cmRemoteStorageId, String cmPoolId) throws Exception {
        CmRemoteStoragePoolBody remoteStoragePoolBody = new CmRemoteStoragePoolBody();
        remoteStoragePoolBody.setUnschedulable(true);
        updateRemoteStoragePool(cmRemoteStorageId, cmPoolId, remoteStoragePoolBody);
    }

    public static void removeRemoteStoragePool(String cmRemoteStorageId, String cmPoolId) throws Exception {
        String endpoint = "/storages/remote/{0}/pools/{1}";
        endpoint = MessageFormat.format(endpoint, cmRemoteStorageId, cmPoolId);
        deleteRequest(endpoint + cmRemoteStorageId);
    }

    public static List<CmStorageclass> listStorageclass(CmStorageclassQuery storageclassQuery) throws Exception {
        HttpResp httpResp = getRequest("/storageclasses", storageclassQuery);
        return JSONArray.parseArray(httpResp.getResponseContent(), CmStorageclass.class);
    }

    public static CmStorageclass getStorageclass(String cmStorageclassId) throws Exception {
        CmStorageclassQuery storageclassQuery = new CmStorageclassQuery();
        storageclassQuery.setId(cmStorageclassId);
        List<CmStorageclass> cmRemoteStorages = listStorageclass(storageclassQuery);
        if (cmRemoteStorages.size() == 1) {
            return cmRemoteStorages.get(0);
        }
        return null;
    }

    public static void saveStorageclass(CmStorageclassBody storageclassBody) throws Exception {
        postRequest("/storageclasses", storageclassBody);
    }

    public static void updateStorageclass(String cmStorageclassId, CmStorageclassBody storageclassBody)
            throws Exception {
        String endpoint = "/storageclasses/{0}";
        endpoint = MessageFormat.format(endpoint, cmStorageclassId);
        putRequest(endpoint, storageclassBody);
    }

    public static void enabledStorageclass(String cmStorageclassId) throws Exception {
        CmStorageclassBody storageclassBody = new CmStorageclassBody();
        storageclassBody.setUnschedulable(false);
        updateStorageclass(cmStorageclassId, storageclassBody);
    }

    public static void disabledStorageclass(String cmStorageclassId) throws Exception {
        CmStorageclassBody storageclassBody = new CmStorageclassBody();
        storageclassBody.setUnschedulable(true);
        updateStorageclass(cmStorageclassId, storageclassBody);
    }

    public static void removeStorageclass(String cmStorageclassId) throws Exception {
        String endpoint = "/storageclasses/{0}";
        endpoint = MessageFormat.format(endpoint, cmStorageclassId);
        deleteRequest(endpoint + cmStorageclassId);
    }

    public static List<CmImage> listImage(CmImageQuery imageQuery) throws Exception {
        HttpResp httpResp = getRequest("/images", imageQuery);
        return JSONArray.parseArray(httpResp.getResponseContent(), CmImage.class);
    }

    public static CmImage getImage(String cmImageId) throws Exception {
        CmImageQuery imageQuery = new CmImageQuery();
        imageQuery.setId(cmImageId);
        List<CmImage> cmImages = listImage(imageQuery);
        if (cmImages.size() == 1) {
            return cmImages.get(0);
        }
        return null;
    }

    public static void saveImage(CmImageBody cmImageBody) throws Exception {
        postRequest("/images", cmImageBody);
    }

    public static void enabledImage(String cmImageId) throws Exception {
        CmImageBody imageBody = new CmImageBody();
        imageBody.setUnschedulable(false);
        String endpoint = "/images/{0}";
        endpoint = MessageFormat.format(endpoint, cmImageId);
        putRequest(endpoint, imageBody);
    }

    public static void disabledImage(String cmImageId) throws Exception {
        CmImageBody imageBody = new CmImageBody();
        imageBody.setUnschedulable(true);
        String endpoint = "/images/{0}";
        endpoint = MessageFormat.format(endpoint, cmImageId);
        putRequest(endpoint, imageBody);
    }

    public static void removeImage(String cmImageId) throws Exception {
        String endpoint = "/images/{0}";
        endpoint = MessageFormat.format(endpoint, cmImageId);
        deleteRequest(endpoint);
    }

    public static CmImageTemplate getImageTemplate(String cmImageId) throws Exception {
        String endpoint = "/images/{0}/templates";
        endpoint = MessageFormat.format(endpoint, cmImageId);
        HttpResp httpResp = getRequest(endpoint, null);
        return JSONObject.parseObject(httpResp.getResponseContent(), CmImageTemplate.class);
    }

    public static void updateImageTemplate(String cmImageId, CmImageTemplateBody cmImageTemplateBody) throws Exception {
        String endpoint = "/images/{0}/templates";
        endpoint = MessageFormat.format(endpoint, cmImageId);
        putRequest(endpoint, cmImageTemplateBody);
    }

    public static List<CmNode> listNode(String cmSiteId, CmNodeQuery nodeQuery) throws Exception {
        String endpoint = "/sites/{0}/nodes";
        endpoint = MessageFormat.format(endpoint, cmSiteId);
        HttpResp httpResp = getRequest(endpoint, nodeQuery);
        return JSONArray.parseArray(httpResp.getResponseContent(), CmNode.class);
    }

    public static List<CmHost> listHost(CmHostQuery hostQuery) throws Exception {
        HttpResp httpResp = getRequest("/hosts", hostQuery);
        return JSONArray.parseArray(httpResp.getResponseContent(), CmHost.class);
    }

    public static CmHost getHost(String cmHostId) throws Exception {
        CmHostQuery hostQuery = new CmHostQuery();
        hostQuery.setId(cmHostId);
        List<CmHost> cmHosts = listHost(hostQuery);
        if (cmHosts.size() == 1) {
            return cmHosts.get(0);
        }
        return null;
    }

    public static List<CmHostUnit> listHostUnit(String cmHostId) throws Exception {
        String endpoint = "/hosts/{0}/units";
        endpoint = MessageFormat.format(endpoint, cmHostId);
        HttpResp httpResp = getRequest(endpoint, null);
        return JSONArray.parseArray(httpResp.getResponseContent(), CmHostUnit.class);
    }

    public static CmHostEvent getHostEvent(String cmSiteId, String cmHostId) throws Exception {
        String endpoint = "/sites/{0}/events?type=host&id={1}";
        endpoint = MessageFormat.format(endpoint, cmSiteId, cmHostId);
        HttpResp httpResp = getRequest(endpoint, null);
        return JSONObject.parseObject(httpResp.getResponseContent(), CmHostEvent.class);
    }

    public static CmSaveHostResp saveHost(CmHostBody hostBody) throws Exception {
        JSONObject jsonObject = postRequest("/hosts", hostBody);
        return JSONObject.toJavaObject(jsonObject, CmSaveHostResp.class);
    }

    public static void updateHost(String cmHostId, CmHostBody hostBody) throws Exception {
        String endpoint = "/hosts/{0}";
        endpoint = MessageFormat.format(endpoint, cmHostId);
        putRequest(endpoint, hostBody);
    }

    public static void enabledHost(String cmHostId) throws Exception {
        CmHostBody hostBody = new CmHostBody();
        hostBody.setUnschedulable(false);
        String endpoint = "/hosts/{0}";
        endpoint = MessageFormat.format(endpoint, cmHostId);
        putRequest(endpoint, hostBody);
    }

    public static void disabledHost(String cmHostId) throws Exception {
        CmHostBody hostBody = new CmHostBody();
        hostBody.setUnschedulable(true);
        String endpoint = "/hosts/{0}";
        endpoint = MessageFormat.format(endpoint, cmHostId);
        putRequest(endpoint, hostBody);
    }

    public static void removeHost(String cmHostId, Integer timeout) throws Exception {
        String endpoint = "/hosts/{0}";
        endpoint = MessageFormat.format(endpoint, cmHostId);
        deleteRequest(endpoint, timeout);
    }

    public static List<CmService> listService(CmServiceQuery serviceQuery) throws Exception {
        HttpResp httpResp = getRequest("/services", serviceQuery);
        return JSONArray.parseArray(httpResp.getResponseContent(), CmService.class);
    }

    public static CmService getService(String cmServiceId) throws Exception {
        CmServiceQuery serviceQuery = new CmServiceQuery();
        serviceQuery.setId(cmServiceId);
        List<CmService> cmServices = listService(serviceQuery);
        if (cmServices.size() == 1) {
            return cmServices.get(0);
        }
        return null;
    }

    public static CmTopology getTopology(String cmServiceId) throws Exception {
        String endpoint = "/services/{0}/topology";
        endpoint = MessageFormat.format(endpoint, cmServiceId);
        HttpResp httpResp = getRequest(endpoint, null);
        return JSONObject.parseObject(httpResp.getResponseContent(), CmTopology.class);
    }

    public static List<CmService> listServiceDetail(CmServiceQuery serviceQuery) throws Exception {
        HttpResp httpResp = getRequest("/services/detail", serviceQuery);
        return JSONArray.parseArray(httpResp.getResponseContent(), CmService.class);
    }

    public static CmService getServiceDetail(String cmServiceId) throws Exception {
        CmServiceQuery serviceQuery = new CmServiceQuery();
        serviceQuery.setId(cmServiceId);
        List<CmService> cmServices = listServiceDetail(serviceQuery);
        if (cmServices.size() == 1) {
            return cmServices.get(0);
        }
        return null;
    }

    public static CmSaveServiceResp saveService(CmServiceBody serviceBody) throws Exception {
        JSONObject jsonObject = postRequest("/services", serviceBody);
        return JSONObject.toJavaObject(jsonObject, CmSaveServiceResp.class);
    }

    public static void linkServices(String cmServiceId, CmServicesLinkBody cmServicesLinkBody, Integer timeout)
            throws Exception {
        String endpoint = "/services/{0}/link";
        endpoint = MessageFormat.format(endpoint, cmServiceId);
        postRequest(endpoint, cmServicesLinkBody, timeout);
    }

    public static void registerMonitor(String cmServiceId) throws Exception {
        String endpoint = "/services/{0}/monitor";
        endpoint = MessageFormat.format(endpoint, cmServiceId);
        postRequest(endpoint, null);
    }

    public static void unregisterMonitor(String cmServiceId) throws Exception {
        String endpoint = "/services/{0}/monitor";
        endpoint = MessageFormat.format(endpoint, cmServiceId);
        deleteRequest(endpoint);
    }

    public static List<CmLoadbalancer> listLoadbalancer(CmLoadbalancerQuery loadbalancerQuery) throws Exception {
        HttpResp httpResp = getRequest("/services/loadbalancer", loadbalancerQuery);
        return JSONArray.parseArray(httpResp.getResponseContent(), CmLoadbalancer.class);
    }

    public static CmLoadbalancer getLoadbalancer(String cmServiceId) throws Exception {
        String endpoint = "/services/{0}/loadbalancer";
        endpoint = MessageFormat.format(endpoint, cmServiceId);
        HttpResp httpResp = getRequest(endpoint, null);
        return JSONObject.parseObject(httpResp.getResponseContent(), CmLoadbalancer.class);
    }

    public static void saveLoadbalancer(String cmServiceId) throws Exception {
        String endpoint = "/services/{0}/loadbalancer";
        endpoint = MessageFormat.format(endpoint, cmServiceId);
        postRequest(endpoint, null);
    }

    public static void removeLoadbalancer(String cmServiceId) throws Exception {
        String endpoint = "/services/{0}/loadbalancer";
        endpoint = MessageFormat.format(endpoint, cmServiceId);
        deleteRequest(endpoint);
    }

    public static void startService(String cmServiceId) throws Exception {
        CmServiceStateBody body = new CmServiceStateBody();
        body.setState(CmConsts.STATE_PASSING);
        String endpoint = "/services/{0}/state";
        endpoint = MessageFormat.format(endpoint, cmServiceId);
        putRequest(endpoint, body);
    }

    public static void stopService(String cmServiceId) throws Exception {
        CmServiceStateBody body = new CmServiceStateBody();
        body.setState(CmConsts.STATE_CRITICAL);
        String endpoint = "/services/{0}/state";
        endpoint = MessageFormat.format(endpoint, cmServiceId);
        putRequest(endpoint, body);
    }

    public static void updateServiceImage(String cmServiceId, CmServiceBody serviceBody) throws Exception {
        String endpoint = "/services/{0}/image";
        endpoint = MessageFormat.format(endpoint, cmServiceId);
        putRequest(endpoint, serviceBody);
    }

    public static void scaleUpCpuMemService(String cmServiceId, CmServiceBody serviceBody) throws Exception {
        String endpoint = "/services/{0}/resource/requests";
        endpoint = MessageFormat.format(endpoint, cmServiceId);
        putRequest(endpoint, serviceBody);
    }

    public static void scaleUpStorageService(String cmServiceId, CmServiceBody serviceBody) throws Exception {
        String endpoint = "/services/{0}/resource/storages";
        endpoint = MessageFormat.format(endpoint, cmServiceId);
        putRequest(endpoint, serviceBody);
    }

    public static void archUpService(String cmServiceId, CmServiceBody serviceBody) throws Exception {
        String endpoint = "/services/{0}/arch";
        endpoint = MessageFormat.format(endpoint, cmServiceId);
        putRequest(endpoint, serviceBody);
    }

    public static void removeService(String cmServiceId, Integer timeout) throws Exception {
        String endpoint = "/services/{0}";
        endpoint = MessageFormat.format(endpoint, cmServiceId);
        deleteRequest(endpoint, timeout);
    }

    public static List<CmServiceCfg> listServiceCfg(String cmServiceId) throws Exception {
        String endpoint = "/services/{0}/config";
        endpoint = MessageFormat.format(endpoint, cmServiceId);
        HttpResp httpResp = getRequest(endpoint, null);
        return JSONArray.parseArray(httpResp.getResponseContent(), CmServiceCfg.class);
    }

    public static void updateServiceCfg(String cmServiceId, CmCfgBody cmCfgBody) throws Exception {
        String endpoint = "/services/{0}/config";
        endpoint = MessageFormat.format(endpoint, cmServiceId);
        putRequest(endpoint, cmCfgBody);
    }

    public static CmTerminal getUnitTerminal(String cmServiceId, String cmUnitId) throws Exception {
        String endpoint = "/services/{0}/units/{1}/terminal";
        endpoint = MessageFormat.format(endpoint, cmServiceId, cmUnitId);
        HttpResp httpResp = getRequest(endpoint, null);
        return JSONArray.parseObject(httpResp.getResponseContent(), CmTerminal.class);
    }

    public static void startUnit(String cmServiceId, String cmUnitId) throws Exception {
        CmUnitStateBody body = new CmUnitStateBody();
        body.setState(CmConsts.STATE_PASSING);
        String endpoint = "/services/{0}/units/{1}/state";
        endpoint = MessageFormat.format(endpoint, cmServiceId, cmUnitId);
        putRequest(endpoint, body);
    }

    public static void stopUnit(String cmServiceId, String cmUnitId) throws Exception {
        CmUnitStateBody body = new CmUnitStateBody();
        body.setState(CmConsts.STATE_CRITICAL);
        String endpoint = "/services/{0}/units/{1}/state";
        endpoint = MessageFormat.format(endpoint, cmServiceId, cmUnitId);
        putRequest(endpoint, body);
    }

    public static void rebuildUnit(String cmServiceId, String cmUnitId, Boolean force,
            CmUnitRebuildBody cmUnitRebuildBody) throws Exception {
        String endpoint = "/services/{0}/units/{1}/rebuild?force={2}";
        endpoint = MessageFormat.format(endpoint, cmServiceId, cmUnitId, BooleanUtils.isTrue(force));
        putRequest(endpoint, cmUnitRebuildBody);
    }

    public static CmUnitRestoreResp restoreUnit(String cmServiceId, String cmUnitId,
            CmUnitRestoreBody cmUnitRestoreBody) throws Exception {
        String endpoint = "/services/{0}/units/{1}/restore";
        endpoint = MessageFormat.format(endpoint, cmServiceId, cmUnitId);
        JSONObject jsonObject = putRequest(endpoint, cmUnitRestoreBody);
        return JSONObject.toJavaObject(jsonObject, CmUnitRestoreResp.class);
    }

    public static void updateRole(CmServiceRoleBody cmServiceRoleBody) throws Exception {
        String endpoint = "/services/role";
        putRequest(endpoint, cmServiceRoleBody);
    }

    public static void updateReplicationMode(String cmServiceId, CmReplicationModeBody cmReplicationModeBody)
            throws Exception {
        String endpoint = "/services/{0}/replication/semi_sync";
        endpoint = MessageFormat.format(endpoint, cmServiceId, cmServiceId);
        putRequest(endpoint, cmReplicationModeBody);
    }

    public static void setSource(String cmServiceId, CmReplicationSetSourceBody cmReplicationSetSourceBody)
            throws Exception {
        String endpoint = "/services/{0}/replication/set_source";
        endpoint = MessageFormat.format(endpoint, cmServiceId, cmServiceId);
        putRequest(endpoint, cmReplicationSetSourceBody);
    }

    public static void maintenance(String cmServiceId, CmMaintenanceBody cmMaintenanceBody) throws Exception {
        String endpoint = "/services/{0}/maintenance";
        endpoint = MessageFormat.format(endpoint, cmServiceId, cmServiceId);
        putRequest(endpoint, cmMaintenanceBody);
    }

    public static List<CmDBSchema> listDBSchema(String cmServiceId) throws Exception {
        String endpoint = "/services/mysql/{0}/schemas";
        endpoint = MessageFormat.format(endpoint, cmServiceId);
        HttpResp httpResp = getRequest(endpoint, null);
        return JSONArray.parseArray(httpResp.getResponseContent(), CmDBSchema.class);
    }

    public static CmDBSchema getDBSchema(String cmServiceId, String dbSchema) throws Exception {
        String endpoint = "/services/mysql/{0}/schemas/{1}";
        endpoint = MessageFormat.format(endpoint, cmServiceId, dbSchema);
        HttpResp httpResp = getRequest(endpoint, null);
        if (httpResp.getResponseContent().equals("{}")) {
            return null;
        }
        return JSONObject.parseObject(httpResp.getResponseContent(), CmDBSchema.class);
    }

    public static void saveDBSchema(String cmServiceId, CmDBSchemaBody schemaBody) throws Exception {
        String endpoint = "/services/mysql/{0}/schemas";
        endpoint = MessageFormat.format(endpoint, cmServiceId);
        postRequest(endpoint, schemaBody);
    }

    public static void removeDBSchema(String cmServiceId, String dbSchema) throws Exception {
        String endpoint = "/services/mysql/{0}/schemas/{1}";
        endpoint = MessageFormat.format(endpoint, cmServiceId, dbSchema);
        deleteRequest(endpoint);
    }

    public static List<CmDBUser> listDBUser(String cmServiceId) throws Exception {
        String endpoint = "/services/mysql/{0}/users";
        endpoint = MessageFormat.format(endpoint, cmServiceId);
        HttpResp httpResp = getRequest(endpoint, null);
        return JSONArray.parseArray(httpResp.getResponseContent(), CmDBUser.class);
    }

    public static CmDBUser getDBUser(String cmServiceId, String username, String ip) throws Exception {
        String endpoint = "/services/mysql/{0}/users/{1}?ip={2}";
        ip = URLEncoder.encode(ip, "utf-8");
        endpoint = MessageFormat.format(endpoint, cmServiceId, username, ip);
        HttpResp httpResp = getRequest(endpoint, null);
        if (httpResp.getResponseContent().equals("{}")) {
            return null;
        }
        return JSONObject.parseObject(httpResp.getResponseContent(), CmDBUser.class);
    }

    public static void saveDBUser(String cmServiceId, CmDBUserBody userBody) throws Exception {
        String endpoint = "/services/mysql/{0}/users";
        endpoint = MessageFormat.format(endpoint, cmServiceId);
        postRequest(endpoint, userBody);
    }

    public static void updateDBUserPrivileges(String cmServiceId, String username, String ip, CmDBUserBody userBody)
            throws Exception {
        String endpoint = "/services/mysql/{0}/users/{1}/privileges?ip={2}";
        ip = URLEncoder.encode(ip, "utf-8");
        endpoint = MessageFormat.format(endpoint, cmServiceId, username, ip);
        putRequest(endpoint, userBody);
    }

    public static void resetDBUserPwd(String cmServiceId, String username, String ip, CmDBUserBody userBody)
            throws Exception {
        String endpoint = "/services/mysql/{0}/users/{1}/pwd/reset?ip={2}";
        ip = URLEncoder.encode(ip, "utf-8");
        endpoint = MessageFormat.format(endpoint, cmServiceId, username, ip);
        putRequest(endpoint, userBody);
    }

    public static void removeDBUser(String cmServiceId, String username, String ip) throws Exception {
        String endpoint = "/services/mysql/{0}/users/{1}?ip={2}";
        ip = URLEncoder.encode(ip, "utf-8");
        endpoint = MessageFormat.format(endpoint, cmServiceId, username, ip);
        deleteRequest(endpoint);
    }

    public static List<CmProxyUser> listProxyUser(String cmServiceId) throws Exception {
        String endpoint = "/services/proxysql/{0}/users";
        endpoint = MessageFormat.format(endpoint, cmServiceId);
        HttpResp httpResp = getRequest(endpoint, null);
        return JSONArray.parseArray(httpResp.getResponseContent(), CmProxyUser.class);
    }

    public static CmProxyUser getProxyUser(String cmServiceId, String username) throws Exception {
        String endpoint = "/services/proxysql/{0}/users/{1}";
        endpoint = MessageFormat.format(endpoint, cmServiceId, username);
        HttpResp httpResp = getRequest(endpoint, null);
        if (httpResp.getResponseContent().equals("{}")) {
            return null;
        }
        return JSONObject.parseObject(httpResp.getResponseContent(), CmProxyUser.class);
    }

    public static void saveProxyUser(String cmServiceId, CmProxyUserBody userBody) throws Exception {
        String endpoint = "/services/proxysql/{0}/users";
        endpoint = MessageFormat.format(endpoint, cmServiceId);
        postRequest(endpoint, userBody);
    }

    public static void updateProxyUser(String cmServiceId, String username, CmProxyUserBody userBody) throws Exception {
        String endpoint = "/services/proxysql/{0}/users/{1}";
        endpoint = MessageFormat.format(endpoint, cmServiceId, username);
        putRequest(endpoint, userBody);
    }

    public static void resetProxyUserPwd(String cmServiceId, String username, CmProxyUserBody userBody)
            throws Exception {
        String endpoint = "/services/proxysql/{0}/users/{1}/pwd/reset";
        endpoint = MessageFormat.format(endpoint, cmServiceId, username);
        putRequest(endpoint, userBody);
    }

    public static void removeProxyUser(String cmServiceId, String username) throws Exception {
        String endpoint = "/services/proxysql/{0}/users/{1}";
        endpoint = MessageFormat.format(endpoint, cmServiceId, username);
        deleteRequest(endpoint);
    }

    public static CmBackupResp backup(String cmServiceId, CmBackupBody backupBody) throws Exception {
        String endpoint = "/services/{0}/backups";
        endpoint = MessageFormat.format(endpoint, cmServiceId);
        JSONObject jsonObject = postRequest(endpoint, backupBody);
        return JSONObject.toJavaObject(jsonObject, CmBackupResp.class);
    }

    public static List<CmBackupFile> listBackupFile(CmBackupFileQuery backupFileQuery) throws Exception {
        String endpoint = "/backup/files";
        HttpResp httpResp = getRequest(endpoint, backupFileQuery);
        return JSONArray.parseArray(httpResp.getResponseContent(), CmBackupFile.class);
    }

    public static CmBackupFile getBackupFile(String backupFileId) throws Exception {
        CmBackupFileQuery backupFileQuery = new CmBackupFileQuery();
        backupFileQuery.setId(backupFileId);
        List<CmBackupFile> backupFiles = listBackupFile(backupFileQuery);
        if (backupFiles.size() == 1) {
            return backupFiles.get(0);
        }
        return null;
    }

    public static void removeBackupFile(String backupFileId) throws Exception {
        String endpoint = "/backup/files?id={0}";
        endpoint = MessageFormat.format(endpoint, backupFileId);
        deleteRequest(endpoint);
    }

    public static void removeBackupFile(Long expiredAt) throws Exception {
        String endpoint = "/backup/files?expired_at={0}";
        endpoint = MessageFormat.format(endpoint, expiredAt);
        deleteRequest(endpoint);
    }

    public static CmBackupExternalResp backupExternal(CmBackupExternalBody backupExternalBody) throws Exception {
        String endpoint = "/backup/files/external";
        JSONObject jsonObject = postRequest(endpoint, backupExternalBody);
        return JSONObject.toJavaObject(jsonObject, CmBackupExternalResp.class);
    }

    public static CmUnitEvent getUnitEvent(String cmSiteId, String cmUnitId) throws Exception {
        String endpoint = "/sites/{0}/events?type=unit&id={1}";
        endpoint = MessageFormat.format(endpoint, cmSiteId, cmUnitId);
        HttpResp httpResp = getRequest(endpoint, null);
        return JSONObject.parseObject(httpResp.getResponseContent(), CmUnitEvent.class);
    }

    public static CmAction getAction(String actionId) throws Exception {
        String endpoint = "/action/{0}";
        endpoint = MessageFormat.format(endpoint, actionId);
        HttpResp httpResp = getRequest(endpoint, null);
        return JSONObject.parseObject(httpResp.getResponseContent(), CmAction.class);
    }

    public static List<CmServiceArch> listServiceArch(CmServiceArchQuery serviceArchQuery) throws Exception {
        String endpoint = "/services/arch";
        HttpResp httpResp = getRequest(endpoint, serviceArchQuery);
        return JSONArray.parseArray(httpResp.getResponseContent(), CmServiceArch.class);
    }

    public static void resetPwd(String cmServiceId, CmResetPwdBody cmResetPwdBody) throws Exception {
        String endpoint = "/services/{0}/auth";
        endpoint = MessageFormat.format(endpoint, cmServiceId);
        putRequest(endpoint, cmResetPwdBody);
    }

    private static HttpResp getRequest(String endPoint, Object queryObject) throws Exception {
        return getRequest(endPoint, queryObject, null);
    }

    private static HttpResp getRequest(String endPoint, Object queryObject, Integer socketTimeout) throws Exception {
        String addr = getCmAddr();
        String url = "http://{0}/{1}/manager" + endPoint;
        url = MessageFormat.format(url, addr, "v1.0");
        String query = buildQuery(queryObject);
        if (StringUtils.isNoneBlank(query)) {
            url += "?" + query;
        }
        HttpResp httpResp = HttpRequestUtil.sendGetRequest(url, socketTimeout);
        if (httpResp.getStatusCode() != HttpStatus.SC_OK) {
            throw new CallingInterfaceException(exceptionPrefix + httpResp.getResponseContent());
        }
        return httpResp;
    }

    private static JSONObject postRequest(String endPoint, Object bodyObject) throws Exception {
        return postRequest(endPoint, bodyObject, null);
    }

    private static JSONObject postRequest(String endPoint, Object bodyObject, Integer socketTimeout) throws Exception {
        String addr = getCmAddr();
        String url = "http://{0}/{1}/manager" + endPoint;
        url = MessageFormat.format(url, addr, "v1.0");
        String bodyContent = JSONObject.toJSONString(bodyObject, SerializerFeature.WriteMapNullValue);
        HttpResp httpResp = HttpRequestUtil.sendPostRequest(url, bodyContent, socketTimeout);
        if (httpResp.getStatusCode() != HttpStatus.SC_CREATED) {
            throw new CallingInterfaceException(exceptionPrefix + httpResp.getResponseContent());
        }
        return JSONObject.parseObject(httpResp.getResponseContent());
    }

    private static JSONObject putRequest(String endPoint, Object bodyObject) throws Exception {
        return putRequest(endPoint, bodyObject, null);
    }

    private static JSONObject putRequest(String endPoint, Object bodyObject, Integer socketTimeout) throws Exception {
        String addr = getCmAddr();
        String url = "http://{0}/{1}/manager" + endPoint;
        url = MessageFormat.format(url, addr, "v1.0");
        String bodyContent = JSONObject.toJSONString(bodyObject);
        HttpResp httpResp = HttpRequestUtil.sendPutRequest(url, bodyContent, socketTimeout);
        if (httpResp.getStatusCode() != HttpStatus.SC_OK) {
            throw new CallingInterfaceException(exceptionPrefix + httpResp.getResponseContent());
        }
        return JSONObject.parseObject(httpResp.getResponseContent());
    }

    private static void deleteRequest(String endPoint) throws Exception {
        deleteRequest(endPoint, null);
    }

    private static void deleteRequest(String endPoint, Integer socketTimeout) throws Exception {
        String addr = getCmAddr();
        String url = "http://{0}/{1}/manager" + endPoint;
        url = MessageFormat.format(url, addr, "v1.0");
        HttpResp httpResp = HttpRequestUtil.sendDeleteRequest(url, socketTimeout);
        if (httpResp.getStatusCode() != HttpStatus.SC_NO_CONTENT) {
            throw new CallingInterfaceException(exceptionPrefix + httpResp.getResponseContent());
        }
    }

    public static CmSite findSite(List<CmSite> cmSites, String siteId) {
        if (cmSites == null || cmSites.size() == 0 || StringUtils.isBlank(siteId)) {
            return null;
        }

        for (CmSite cmSite : cmSites) {
            if (cmSite.getId().equals(siteId)) {
                return cmSite;
            }
        }
        return null;
    }

    public static CmNfs findNfsByZone(List<CmNfs> cmNfs, String zone) {
        if (cmNfs == null || cmNfs.size() == 0 || StringUtils.isBlank(zone)) {
            return null;
        }

        for (CmNfs nfs : cmNfs) {
            if (nfs.getZone().equals(zone)) {
                return nfs;
            }
        }
        return null;
    }

    public static CmCluster findCluster(List<CmCluster> cmClusters, String cmClusterId) {
        if (cmClusters == null || cmClusters.size() == 0 || StringUtils.isBlank(cmClusterId)) {
            return null;
        }

        for (CmCluster cmCluster : cmClusters) {
            if (cmCluster.getId().equals(cmClusterId)) {
                return cmCluster;
            }
        }
        return null;
    }

    public static CmNetwork findNetwork(List<CmNetwork> cmNetworks, String cmNetworkId) {
        if (cmNetworks == null || cmNetworks.size() == 0 || StringUtils.isBlank(cmNetworkId)) {
            return null;
        }

        for (CmNetwork cmNetwork : cmNetworks) {
            if (cmNetwork.getId().equals(cmNetworkId)) {
                return cmNetwork;
            }
        }
        return null;
    }

    public static CmImage findLatestImage(List<CmImage> cmImages, String type, Integer major, Integer minor,
            Integer patch, Integer build) {
        if (cmImages == null || cmImages.size() == 0 || StringUtils.isBlank(type)) {
            return null;
        }

        List<CmImage> list1 = new ArrayList<>();
        for (CmImage cmImage : cmImages) {
            if (cmImage.getType().equals(type)) {
                list1.add(cmImage);
            }
        }

        int maxMajor = 0;
        if (major != null) {
            maxMajor = major;
        } else {
            for (CmImage cmImage : list1) {
                if (cmImage.getMajor().intValue() > maxMajor) {
                    maxMajor = cmImage.getMajor();
                }
            }
        }

        int maxMinor = 0;
        if (minor != null) {
            maxMinor = minor;
        } else {
            for (CmImage cmImage : list1) {
                if (cmImage.getMajor().intValue() == maxMajor && cmImage.getMinor().intValue() > maxMinor) {
                    maxMinor = cmImage.getMinor();
                }
            }
        }

        int maxPatch = 0;
        if (patch != null) {
            maxPatch = patch;
        } else {
            for (CmImage cmImage : list1) {
                if (cmImage.getMajor().intValue() == maxMajor && cmImage.getMinor().intValue() == maxMinor
                        && cmImage.getPatch().intValue() > maxPatch) {
                    maxPatch = cmImage.getPatch();
                }
            }
        }

        int maxBuild = 0;
        if (build != null) {
            maxBuild = build;
        } else {
            for (CmImage cmImage : list1) {
                if (cmImage.getMajor().intValue() == maxMajor && cmImage.getMinor().intValue() == maxMinor
                        && cmImage.getPatch().intValue() == maxPatch && cmImage.getBuild().intValue() > maxBuild) {
                    maxBuild = cmImage.getBuild();
                }
            }
        }

        for (CmImage cmImage : list1) {
            if (cmImage.getMajor().intValue() == maxMajor && cmImage.getMinor().intValue() == maxMinor
                    && cmImage.getPatch().intValue() == maxPatch && cmImage.getBuild().intValue() == maxBuild) {
                return cmImage;
            }
        }
        return null;
    }

    public static CmRemoteStorage findRemoteStorage(List<CmRemoteStorage> cmRemoteStorages, String cmRemoteStorageId) {
        if (cmRemoteStorages == null || cmRemoteStorages.size() == 0 || StringUtils.isBlank(cmRemoteStorageId)) {
            return null;
        }

        for (CmRemoteStorage cmRemoteStorage : cmRemoteStorages) {
            if (cmRemoteStorage.getId().equals(cmRemoteStorageId)) {
                return cmRemoteStorage;
            }
        }
        return null;
    }

    public static CmHost findHost(List<CmHost> cmHosts, String cmHostId) {
        if (cmHosts == null || cmHosts.size() == 0 || StringUtils.isBlank(cmHostId)) {
            return null;
        }

        for (CmHost cmHost : cmHosts) {
            if (cmHost.getId().equals(cmHostId)) {
                return cmHost;
            }
        }
        return null;
    }

    public static CmService findService(List<CmService> cmServices, String cmServiceId) {
        if (cmServices == null || cmServices.size() == 0 || StringUtils.isBlank(cmServiceId)) {
            return null;
        }

        for (CmService cmService : cmServices) {
            if (cmService.getId().equals(cmServiceId)) {
                return cmService;
            }
        }
        return null;
    }

    public static List<CmService> findServiceByGroupName(List<CmService> cmServices, String groupName) {
        List<CmService> results = new ArrayList<>();
        if (cmServices == null || cmServices.size() == 0 || StringUtils.isBlank(groupName)) {
            return results;
        }

        for (CmService cmService : cmServices) {
            if (cmService.getGroupName().equals(groupName)) {
                results.add(cmService);
            }
        }
        return results;
    }

    public static CmServiceArch findServiceArch(List<CmServiceArch> cmServiceArchs, String type, String version,
            String mode, Integer replicas) {
        if (cmServiceArchs == null || cmServiceArchs.size() == 0) {
            return null;
        }

        for (CmServiceArch cmServiceArch : cmServiceArchs) {
            if (cmServiceArch.getServiceType().equals(type)
                    && (StringUtils.isBlank(version) || cmServiceArch.getServiceVersion().equals(version))
                    && cmServiceArch.getMode().equals(mode) && cmServiceArch.getReplicas().equals(replicas)) {
                return cmServiceArch;
            }
        }
        return null;
    }

    public static CmService.Status.Unit findMasterUnit(CmService cmService) {
        if (cmService != null) {
            CmService.Status cmStatus = cmService.getStatus();
            if (cmStatus != null) {
                CmServiceArchBase cmArch = cmStatus.getArch();
                if (cmArch != null) {
                    List<CmService.Status.Unit> cmUnits = cmStatus.getUnits();
                    if (cmUnits != null && cmUnits.size() > 0) {
                        for (CmService.Status.Unit cmUnit : cmUnits) {
                            Map<String, CmService.Status.Unit.Replication> cmReplicationMap = cmUnit.getReplication();
                            if (cmReplicationMap != null) {
                                String mode = cmArch.getMode();
                                CmService.Status.Unit.Replication cmReplication = cmReplicationMap.get(mode);
                                if (cmReplication != null) {
                                    if (CmConsts.ROLE_MASTER.equals(cmReplication.getRole())) {
                                        return cmUnit;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return null;
    }

    public static CmTopology.Node findMasterUnit(CmTopology cmTopology) {
        if (cmTopology != null) {
            List<CmTopology.Node> cmNodes = cmTopology.getNodes();
            if (cmNodes != null) {
                for (CmTopology.Node cmNode : cmNodes) {
                    if (StringUtils.equals(cmNode.getRole(), CmConsts.ROLE_MASTER)) {
                        return cmNode;
                    }
                }
            }
        }
        return null;
    }

    public static CmLoadbalancer findLoadbalancer(List<CmLoadbalancer> cmLoadbalancers, String cmServiceId) {
        if (cmLoadbalancers == null || cmLoadbalancers.size() == 0 || StringUtils.isBlank(cmServiceId)) {
            return null;
        }

        for (CmLoadbalancer cmLoadbalancer : cmLoadbalancers) {
            if (cmServiceId.equals(cmLoadbalancer.getServiceId())) {
                return cmLoadbalancer;
            }
        }
        return null;
    }

    public static String getCmAddr() throws IOException {
        FileSystemResource file = new FileSystemResource(FILEPATH);
        if (!file.exists()) {
            throw new FileNotFoundException(FILEPATH + " not found。");
        }
        String addr = null;
        Properties prop = PropertiesLoaderUtils.loadProperties(file);
        addr = prop.getProperty("connect.cm.addr");
        if (StringUtils.isBlank(addr)) {
            logger.error("connect.cm.addr is blank。");
            throw new ValueNotBlankException("connect.cm.addr is blank。");
        }
        return addr;
    }

    private static String buildQuery(Object object) throws IllegalArgumentException, IllegalAccessException {
        String path = "";
        if (object == null) {
            return path;
        }
        for (Field field : object.getClass().getDeclaredFields()) {
            JSONField jsonField = field.getAnnotation(JSONField.class);
            if (jsonField != null) {
                field.setAccessible(true);
                Object fieldValue = field.get(object);
                if (fieldValue != null) {
                    path += jsonField.name() + "=" + fieldValue + "&";
                }
            }
        }
        if (!path.equals("")) {
            path = path.substring(0, path.length() - 1);
        }
        return path;
    }
}
