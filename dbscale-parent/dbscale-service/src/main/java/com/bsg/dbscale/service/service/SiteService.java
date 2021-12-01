package com.bsg.dbscale.service.service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bsg.dbscale.cm.api.CmApi;
import com.bsg.dbscale.cm.body.CmSiteBody;
import com.bsg.dbscale.cm.constant.CmConsts;
import com.bsg.dbscale.cm.model.CmCluster;
import com.bsg.dbscale.cm.model.CmHost;
import com.bsg.dbscale.cm.model.CmNetwork;
import com.bsg.dbscale.cm.model.CmService;
import com.bsg.dbscale.cm.model.CmSite;
import com.bsg.dbscale.cm.query.CmClusterQuery;
import com.bsg.dbscale.cm.query.CmHostQuery;
import com.bsg.dbscale.cm.query.CmNetworkQuery;
import com.bsg.dbscale.cm.query.CmServiceQuery;
import com.bsg.dbscale.dao.domain.DictDO;
import com.bsg.dbscale.dao.domain.DictTypeDO;
import com.bsg.dbscale.dao.domain.ServDO;
import com.bsg.dbscale.dao.domain.ServGroupDO;
import com.bsg.dbscale.dao.query.ServGroupQuery;
import com.bsg.dbscale.service.check.CheckResult;
import com.bsg.dbscale.service.check.SiteCheck;
import com.bsg.dbscale.service.constant.Consts;
import com.bsg.dbscale.service.constant.DictConsts;
import com.bsg.dbscale.service.constant.DictTypeConsts;
import com.bsg.dbscale.service.dto.DisplayDTO;
import com.bsg.dbscale.service.dto.InfoDTO;
import com.bsg.dbscale.service.dto.SiteDTO;
import com.bsg.dbscale.service.dto.SiteResourceDTO;
import com.bsg.dbscale.service.dto.StatisticsDTO;
import com.bsg.dbscale.service.form.SiteForm;
import com.bsg.dbscale.util.NumberUnits;

@Service
public class SiteService extends BaseService {

    @Autowired
    private SiteCheck siteCheck;

    @Autowired
    private HostService hostService;

    @Autowired
    private MysqlServGroupService mysqlServGroupService;

    @Autowired
    private CmhaServGroupService cmhaServGroupService;

    @Autowired
    private RedisServGroupService redisServGroupService;

    public Result list() throws Exception {
        List<SiteDTO> siteDTOs = new ArrayList<>();

        List<CmSite> cmSites = CmApi.listSite(null);
        if (cmSites.size() > 0) {
            List<DictTypeDO> dictTypeDOs = dictTypeDAO.list();
            for (CmSite cmSite : cmSites) {
                SiteDTO siteDTO = getShowDTO(cmSite, dictTypeDOs);
                siteDTOs.add(siteDTO);
            }
        }

        return Result.success(siteDTOs);
    }

    public Result get(String siteId) throws Exception {
        SiteDTO siteDTO = null;

        CmSite cmSite = CmApi.getSite(siteId);
        if (cmSite != null) {
            List<DictTypeDO> dictTypeDOs = dictTypeDAO.list();
            siteDTO = getShowDTO(cmSite, dictTypeDOs);
        }

        return Result.success(siteDTO);
    }

    public Result getResourceStatistics(String siteId) throws Exception {
        final SiteResourceDTO siteResourceDTO = new SiteResourceDTO();

        CmSite cmSite = CmApi.getSite(siteId);
        if (cmSite == null) {
            return Result.success(null);
        }

        int countDownLatchCnt = 2;
        CountDownLatch countDownLatch = new CountDownLatch(countDownLatchCnt);
        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    SiteResourceDTO.Resource resourceDTO = siteResourceDTO.new Resource();

                    CmHostQuery cmHostQuery = new CmHostQuery();
                    cmHostQuery.setSiteId(siteId);
                    List<CmHost> cmHosts = CmApi.listHost(cmHostQuery);

                    CmClusterQuery cmClusterQuery = new CmClusterQuery();
                    cmClusterQuery.setSiteId(siteId);
                    cmClusterQuery.setUnschedulable(false);
                    List<CmCluster> cmClusters = CmApi.listCluster(cmClusterQuery);

                    Long cpuCapacity = 0L;
                    Long cpuAllocatable = 0L;
                    Long memCapacity = 0L;
                    Long memAllocatable = 0L;
                    Long ssdCapacity = 0L;
                    Long ssdAllocatable = 0L;
                    Long hddCapacity = 0L;
                    Long hddAllocatable = 0L;
                    Integer unitCapacity = 0;
                    Integer unitAllocatable = 0;
                    int passingCnt = 0;
                    int warningCnt = 0;
                    int criticalCnt = 0;
                    int distributableCnt = 0;
                    if (cmHosts != null) {
                        for (CmHost cmHost : cmHosts) {
                            CmHost.Status cmStatus = cmHost.getStatus();
                            if (cmStatus != null) {
                                if (cmStatus.getPhase().equals(CmConsts.HOST_PHASE_READY)) {
                                    String state = hostService.getState(cmSite, cmHost);
                                    switch (state) {
                                    case DictConsts.STATE_PASSING:
                                        passingCnt++;
                                        break;
                                    case DictConsts.STATE_WARNNING:
                                        warningCnt++;
                                        break;
                                    case DictConsts.STATE_CRITICAL:
                                        criticalCnt++;
                                        break;

                                    default:
                                        break;
                                    }

                                    if (cmStatus.getPhase().equals(CmConsts.HOST_PHASE_READY)
                                            && BooleanUtils.negate(cmStatus.getUnschedulable())
                                            && state.equals(DictConsts.STATE_PASSING)) {
                                        List<String> resourceSoftLimit = cmStatus.getResourceSoftLimit();
                                        if (resourceSoftLimit == null || resourceSoftLimit.size() == 0) {
                                            CmHost.Label cmLable = cmHost.getLabel();
                                            if (cmLable != null) {
                                                CmCluster cmCluster = CmApi.findCluster(cmClusters,
                                                        cmLable.getClusterId());
                                                if (cmCluster != null) {
                                                    distributableCnt++;
                                                }
                                            }
                                        }
                                    }
                                    CmHost.Status.Usage cmCapacity = cmStatus.getCapacity();
                                    if (cmCapacity != null) {
                                        if (cmCapacity.getMilicpu() != null) {
                                            cpuCapacity += cmCapacity.getMilicpu();
                                        }

                                        if (cmCapacity.getMemory() != null) {
                                            memCapacity += cmCapacity.getMemory();
                                        }

                                        List<CmHost.Status.Usage.Storage> capacityStorages = cmCapacity.getStorages();
                                        if (capacityStorages != null && capacityStorages.size() > 0) {
                                            for (CmHost.Status.Usage.Storage cmStorage : capacityStorages) {
                                                if (cmStorage.getSize() != null) {
                                                    if (cmStorage.getPerformance().equals(CmConsts.PERFORMANCE_HIGH)) {
                                                        ssdCapacity += cmStorage.getSize();
                                                    } else if (cmStorage.getPerformance()
                                                            .equals(CmConsts.PERFORMANCE_MEDIUM)) {
                                                        hddCapacity += cmStorage.getSize();
                                                    }
                                                }
                                            }
                                        }

                                        if (cmCapacity.getUnit() != null) {
                                            unitCapacity += cmCapacity.getUnit();
                                        }
                                    }

                                    CmHost.Status.Usage cmAllocatable = cmStatus.getAllocatable();
                                    if (cmAllocatable != null) {
                                        if (cmAllocatable.getMilicpu() != null) {
                                            cpuAllocatable += cmAllocatable.getMilicpu();
                                        }

                                        if (cmAllocatable.getMemory() != null) {
                                            memAllocatable += cmAllocatable.getMemory();
                                        }

                                        List<CmHost.Status.Usage.Storage> allocatableStorages = cmAllocatable
                                                .getStorages();
                                        if (allocatableStorages != null && allocatableStorages.size() > 0) {
                                            for (CmHost.Status.Usage.Storage cmStorage : allocatableStorages) {
                                                if (cmStorage.getSize() != null) {
                                                    if (cmStorage.getPerformance().equals(CmConsts.PERFORMANCE_HIGH)) {
                                                        ssdAllocatable += cmStorage.getSize();
                                                    } else if (cmStorage.getPerformance()
                                                            .equals(CmConsts.PERFORMANCE_MEDIUM)) {
                                                        hddAllocatable += cmStorage.getSize();
                                                    }
                                                }
                                            }
                                        }

                                        if (cmAllocatable.getUnit() != null) {
                                            unitAllocatable += cmAllocatable.getUnit();
                                        }
                                    }
                                }
                            }
                        }
                    }

                    siteResourceDTO.setResource(resourceDTO);

                    SiteResourceDTO.Resource.HostState host = resourceDTO.new HostState();
                    resourceDTO.setHost(host);
                    host.setPassingCnt(passingCnt);
                    host.setWarningCnt(warningCnt);
                    host.setCriticalCnt(criticalCnt);
                    host.setDistributableCnt(distributableCnt);
                    host.setUndistributableCnt(cmHosts.size() - distributableCnt);

                    StatisticsDTO<Double> cpu = new StatisticsDTO<Double>();
                    resourceDTO.setCpu(cpu);
                    cpu.setCapacity(NumberUnits.retainDigits(cpuCapacity / 1000.0));
                    cpu.setUsed(NumberUnits.retainDigits((cpuCapacity - cpuAllocatable) / 1000.0));

                    StatisticsDTO<Double> mem = new StatisticsDTO<Double>();
                    resourceDTO.setMem(mem);
                    mem.setCapacity(NumberUnits.retainDigits(memCapacity / 1024.0));
                    mem.setUsed(NumberUnits.retainDigits((memCapacity - memAllocatable) / 1024.0));

                    StatisticsDTO<Double> hdd = new StatisticsDTO<Double>();
                    resourceDTO.setHdd(hdd);
                    hdd.setCapacity(NumberUnits.retainDigits(hddCapacity / 1024.0));
                    hdd.setUsed(NumberUnits.retainDigits((hddCapacity - hddAllocatable) / 1024.0));

                    StatisticsDTO<Double> ssd = new StatisticsDTO<Double>();
                    resourceDTO.setSsd(ssd);
                    ssd.setCapacity(NumberUnits.retainDigits(ssdCapacity / 1024.0));
                    ssd.setUsed(NumberUnits.retainDigits((ssdCapacity - ssdAllocatable) / 1024.0));

                    StatisticsDTO<Integer> unit = new StatisticsDTO<Integer>();
                    unit.setCapacity(unitCapacity);
                    unit.setUsed(unitCapacity - unitAllocatable);
                    resourceDTO.setUnit(unit);

                    CmNetworkQuery cmNetworkQuery = new CmNetworkQuery();
                    cmNetworkQuery.setSiteId(siteId);

                    List<CmNetwork> cmNetworks = CmApi.listNetwork(cmNetworkQuery);
                    int used = 0;
                    int total = 0;
                    if (cmNetworks != null) {
                        for (CmNetwork cmNetwork : cmNetworks) {
                            CmNetwork.IpSummary cmIpSummary = cmNetwork.getIpSummary();
                            if (cmIpSummary != null) {
                                used += cmIpSummary.getUsed();
                                total += cmIpSummary.getTotal();
                            }
                        }
                    }
                    StatisticsDTO<Integer> network = new StatisticsDTO<Integer>();
                    network.setUsed(used);
                    network.setCapacity(total);
                    resourceDTO.setNetwork(network);

                } catch (Exception e) {
                    logger.error("查询主机或网段信息异常：", e);
                } finally {
                    countDownLatch.countDown();
                }
            }
        });

        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    int mysqlServGroupPassingCnt = 0;
                    int mysqlServGroupWarningCnt = 0;
                    int mysqlServGroupCriticalCnt = 0;

                    int cmhaServGroupPassingCnt = 0;
                    int cmhaServGroupWarningCnt = 0;
                    int cmhaServGroupCriticalCnt = 0;

                    int redisServGroupPassingCnt = 0;
                    int redisServGroupWarningCnt = 0;
                    int redisServGroupCriticalCnt = 0;

                    int unitPassingCnt = 0;
                    int unitCriticalCnt = 0;
                    int unitUnknownCnt = 0;

                    ServGroupQuery daoQuery = new ServGroupQuery();
                    daoQuery.setSiteId(siteId);
                    List<ServGroupDO> servGroupDOs = servGroupDAO.list(daoQuery);
                    if (servGroupDOs.size() > 0) {
                        CmServiceQuery cmServiceQuery = new CmServiceQuery();
                        cmServiceQuery.setSiteId(siteId);
                        List<CmService> cmServices = CmApi.listService(cmServiceQuery);
                        for (ServGroupDO servGroupDO : servGroupDOs) {
                            if (servGroupDO.getCategory().equals(Consts.SERV_GROUP_TYPE_MYSQL)) {
                                String state = mysqlServGroupService.getState(servGroupDO, cmServices);
                                switch (state) {
                                case DictConsts.STATE_PASSING:
                                    mysqlServGroupPassingCnt++;
                                    break;
                                case DictConsts.STATE_WARNNING:
                                    mysqlServGroupWarningCnt++;
                                    break;
                                case DictConsts.STATE_CRITICAL:
                                    mysqlServGroupCriticalCnt++;
                                    break;

                                default:
                                    break;
                                }
                            } else if (servGroupDO.getCategory().equals(Consts.SERV_GROUP_TYPE_CMHA)) {
                                String state = cmhaServGroupService.getState(servGroupDO, cmServices);
                                switch (state) {
                                case DictConsts.STATE_PASSING:
                                    cmhaServGroupPassingCnt++;
                                    break;
                                case DictConsts.STATE_WARNNING:
                                    cmhaServGroupWarningCnt++;
                                    break;
                                case DictConsts.STATE_CRITICAL:
                                    cmhaServGroupCriticalCnt++;
                                    break;

                                default:
                                    break;
                                }
                            } else if (servGroupDO.getCategory().equals(Consts.SERV_GROUP_TYPE_REDIS)) {
                                String state = redisServGroupService.getState(servGroupDO, cmServices);
                                switch (state) {
                                case DictConsts.STATE_PASSING:
                                    redisServGroupPassingCnt++;
                                    break;
                                case DictConsts.STATE_WARNNING:
                                    redisServGroupWarningCnt++;
                                    break;
                                case DictConsts.STATE_CRITICAL:
                                    redisServGroupCriticalCnt++;
                                    break;

                                default:
                                    break;
                                }
                            }
                        }

                        for (ServGroupDO servGroupDO : servGroupDOs) {
                            List<ServDO> servDOs = servGroupDO.getServs();
                            for (ServDO servDO : servDOs) {
                                CmService cmService = CmApi.findService(cmServices, servDO.getRelateId());
                                if (cmService != null) {
                                    CmService.Status cmStatus = cmService.getStatus();
                                    if (cmStatus != null) {
                                        List<CmService.Status.Unit> cmUnits = cmStatus.getUnits();
                                        if (cmUnits != null && cmUnits.size() > 0) {
                                            for (CmService.Status.Unit cmUnit : cmUnits) {
                                                String state = cmUnit.getState();
                                                if (StringUtils.isBlank(state)) {
                                                    unitUnknownCnt++;
                                                } else {
                                                    switch (state) {
                                                    case DictConsts.STATE_PASSING:
                                                        unitPassingCnt++;
                                                        break;
                                                    case DictConsts.STATE_CRITICAL:
                                                        unitCriticalCnt++;
                                                        break;
                                                    default:
                                                        break;
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                    SiteResourceDTO.ServGroup servGroup = siteResourceDTO.new ServGroup();
                    siteResourceDTO.setServGroup(servGroup);

                    SiteResourceDTO.State mysql = siteResourceDTO.new State();
                    servGroup.setMysql(mysql);
                    mysql.setPassingCnt(mysqlServGroupPassingCnt);
                    mysql.setWarningCnt(mysqlServGroupWarningCnt);
                    mysql.setCriticalCnt(mysqlServGroupCriticalCnt);

                    SiteResourceDTO.State cmha = siteResourceDTO.new State();
                    servGroup.setCmha(cmha);
                    cmha.setPassingCnt(cmhaServGroupPassingCnt);
                    cmha.setWarningCnt(cmhaServGroupWarningCnt);
                    cmha.setCriticalCnt(cmhaServGroupCriticalCnt);

                    SiteResourceDTO.State redis = siteResourceDTO.new State();
                    servGroup.setRedis(redis);
                    redis.setPassingCnt(redisServGroupPassingCnt);
                    redis.setWarningCnt(redisServGroupWarningCnt);
                    redis.setCriticalCnt(redisServGroupCriticalCnt);

                    SiteResourceDTO.Unit unit = siteResourceDTO.new Unit();
                    siteResourceDTO.setUnit(unit);
                    unit.setPassingCnt(unitPassingCnt);
                    unit.setCriticalCnt(unitCriticalCnt);
                    unit.setUnknownCnt(unitUnknownCnt);
                } catch (Exception e) {
                    logger.error("查询服务信息异常：", e);
                } finally {
                    countDownLatch.countDown();
                }
            }
        });

        countDownLatch.await();

        return Result.success(siteResourceDTO);
    }

    public Result save(SiteForm siteForm) throws Exception {
        CheckResult checkResult = siteCheck.checkSave(siteForm);
        if (checkResult.getCode() != CheckResult.SUCCESS) {
            logger.error(checkResult.getMsg());
            return Result.failure(checkResult);
        }

        CmSiteBody cmSiteBody = new CmSiteBody();
        cmSiteBody.setName(siteForm.getName());
        cmSiteBody.setRegion(siteForm.getRegion());
        cmSiteBody.setDescription(siteForm.getDescription());
        cmSiteBody.setKubeconfig(siteForm.getKubeconfig());

        CmApi.saveSite(cmSiteBody);

        return Result.success();
    }

    public Result update(String siteId, SiteForm siteForm) throws Exception {
        CheckResult checkResult = siteCheck.checkUpdate(siteId, siteForm);
        if (checkResult.getCode() != CheckResult.SUCCESS) {
            logger.error(checkResult.getMsg());
            return Result.failure(checkResult);
        }

        CmSiteBody cmSiteBody = new CmSiteBody();
        cmSiteBody.setName(siteForm.getName());
        CmApi.updateSite(siteId, cmSiteBody);

        return Result.success();
    }

    public Result remove(String siteId) throws Exception {
        CheckResult checkResult = siteCheck.checkRemove(siteId);
        if (checkResult.getCode() != CheckResult.SUCCESS) {
            logger.error(checkResult.getMsg());
            return Result.failure(checkResult);
        }

        CmApi.removeSite(siteId);

        return Result.success();
    }

    public SiteDTO getShowDTO(CmSite cmSite, List<DictTypeDO> dictTypeDOs) {
        SiteDTO siteDTO = new SiteDTO();
        siteDTO.setId(cmSite.getId());
        siteDTO.setName(cmSite.getName());
        CmSite.Kubernetes kubernetes = cmSite.getKubernetes();
        if (kubernetes != null) {
            siteDTO.setType(kubernetes.getType());
            siteDTO.setDomain(kubernetes.getDomain());
            siteDTO.setPort(kubernetes.getPort());
            siteDTO.setVersion(kubernetes.getVersion());
            siteDTO.setNetworkMode(kubernetes.getNetworkMode());
            siteDTO.setStorageMode(kubernetes.getStorageMode());
        }
        siteDTO.setDescription(cmSite.getDesc());

        DisplayDTO stateDisplayDTO = new DisplayDTO();
        siteDTO.setState(stateDisplayDTO);
        stateDisplayDTO.setCode(cmSite.getState());
        DictDO stateDictDO = findDictDO(dictTypeDOs, DictTypeConsts.STATE, cmSite.getState());
        if (stateDictDO != null) {
            stateDisplayDTO.setDisplay(stateDictDO.getName());
        }

        // CmSite.ImageRegistryInfo cmImageRegistry = cmSite.getImageRegistry();
        // if (cmImageRegistry != null) {
        // SiteDTO.ImageRegistryDTO imageRegistryDTO = siteDTO.new ImageRegistryDTO();
        // siteDTO.setImageRegistry(imageRegistryDTO);
        // imageRegistryDTO.setType(cmImageRegistry.getType());
        // imageRegistryDTO.setScheme(cmImageRegistry.getScheme());
        // imageRegistryDTO.setDomain(cmImageRegistry.getDomain());
        // imageRegistryDTO.setPort(cmImageRegistry.getPort());
        // }
        //
        // CmSite.MonitorServerInfo cmMonitorServer = cmSite.getMonitorServer();
        // if (cmMonitorServer != null) {
        // SiteDTO.MonitorServerDTO monitorServerDTO = siteDTO.new MonitorServerDTO();
        // siteDTO.setMonitorServer(monitorServerDTO);
        // monitorServerDTO.setType(cmMonitorServer.getType());
        // monitorServerDTO.setScheme(cmMonitorServer.getScheme());
        // monitorServerDTO.setDomain(cmMonitorServer.getDomain());
        // monitorServerDTO.setPort(cmMonitorServer.getPort());
        // }
        //
        // CmSite.MonitorUiInfo cmMonitorUi = cmSite.getMonitorUi();
        // if (cmMonitorUi != null) {
        // SiteDTO.MonitorUiDTO monitorUiDTO = siteDTO.new MonitorUiDTO();
        // siteDTO.setMonitorUi(monitorUiDTO);
        // monitorUiDTO.setType(cmMonitorUi.getType());
        // monitorUiDTO.setScheme(cmMonitorUi.getScheme());
        // monitorUiDTO.setDomain(cmMonitorUi.getDomain());
        // monitorUiDTO.setPort(cmMonitorUi.getPort());
        // }
        //
        // CmSite.DashboardInfo cmDashboard = cmSite.getDashboard();
        // if (cmDashboard != null) {
        // SiteDTO.DashboardDTO dashboardDTO = siteDTO.new DashboardDTO();
        // siteDTO.setDashboard(dashboardDTO);
        // dashboardDTO.setType(cmDashboard.getType());
        // dashboardDTO.setScheme(cmDashboard.getScheme());
        // dashboardDTO.setDomain(cmDashboard.getDomain());
        // dashboardDTO.setPort(cmDashboard.getPort());
        // }

        if (cmSite.getRegion() != null) {
            DisplayDTO region = new DisplayDTO();
            siteDTO.setRegion(region);

            region.setCode(cmSite.getRegion());
            DictDO regionDictDO = findDictDO(dictTypeDOs, DictTypeConsts.REGION, cmSite.getRegion());
            if (regionDictDO != null) {
                region.setDisplay(regionDictDO.getName());
            }
        }

        InfoDTO createdDTO = new InfoDTO();
        siteDTO.setCreated(createdDTO);
        createdDTO.setTimestamp(cmSite.getCreatedAt());

        return siteDTO;
    }

    public ObjModel getObjModel(String siteId) throws Exception {
        CmSite cmSite = CmApi.getSite(siteId);
        if (cmSite != null) {
            return new ObjModel(cmSite.getName(), siteId);
        }
        return null;
    }

}
