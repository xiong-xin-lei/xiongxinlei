package com.bsg.dbscale.service.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.bsg.dbscale.cm.api.CmApi;
import com.bsg.dbscale.cm.body.CmBackupBody;
import com.bsg.dbscale.cm.body.CmMaintenanceBody;
import com.bsg.dbscale.cm.body.CmServicesLinkBody;
import com.bsg.dbscale.cm.constant.CmConsts;
import com.bsg.dbscale.cm.model.CmLoadbalancer;
import com.bsg.dbscale.cm.model.CmService;
import com.bsg.dbscale.cm.model.CmServiceArch;
import com.bsg.dbscale.cm.model.CmSite;
import com.bsg.dbscale.cm.model.CmTopology;
import com.bsg.dbscale.cm.query.CmLoadbalancerQuery;
import com.bsg.dbscale.cm.query.CmServiceQuery;
import com.bsg.dbscale.cm.response.CmBackupResp;
import com.bsg.dbscale.dao.domain.BackupStrategyDO;
import com.bsg.dbscale.dao.domain.BusinessAreaDO;
import com.bsg.dbscale.dao.domain.DefServDO;
import com.bsg.dbscale.dao.domain.DictDO;
import com.bsg.dbscale.dao.domain.DictTypeDO;
import com.bsg.dbscale.dao.domain.OrderDO;
import com.bsg.dbscale.dao.domain.OrderGroupDO;
import com.bsg.dbscale.dao.domain.ServDO;
import com.bsg.dbscale.dao.domain.ServGroupDO;
import com.bsg.dbscale.dao.domain.SubtaskCfgDO;
import com.bsg.dbscale.dao.domain.SubtaskDO;
import com.bsg.dbscale.dao.domain.TaskDO;
import com.bsg.dbscale.dao.domain.UnitDO;
import com.bsg.dbscale.dao.domain.UserDO;
import com.bsg.dbscale.service.check.CheckResult;
import com.bsg.dbscale.service.check.CmhaServGroupCheck;
import com.bsg.dbscale.service.constant.Consts;
import com.bsg.dbscale.service.constant.DictConsts;
import com.bsg.dbscale.service.constant.DictTypeConsts;
import com.bsg.dbscale.service.dto.ArchBaseDTO;
import com.bsg.dbscale.service.dto.CmhaServDTO;
import com.bsg.dbscale.service.dto.CmhaServGroupDTO;
import com.bsg.dbscale.service.dto.CmhaServGroupDetailDTO;
import com.bsg.dbscale.service.dto.DBUserDTO;
import com.bsg.dbscale.service.dto.DisplayDTO;
import com.bsg.dbscale.service.dto.MysqlServDTO;
import com.bsg.dbscale.service.dto.MysqlServGroupUserDTO;
import com.bsg.dbscale.service.dto.ProxyUserDTO;
import com.bsg.dbscale.service.dto.ProxysqlServDTO;
import com.bsg.dbscale.service.dto.ScaleBaseDTO;
import com.bsg.dbscale.service.dto.ServDTO;
import com.bsg.dbscale.service.dto.ServGroupDTO;
import com.bsg.dbscale.service.dto.VersionDTO;
import com.bsg.dbscale.service.form.BackupForm;
import com.bsg.dbscale.service.form.BackupStrategyForm;
import com.bsg.dbscale.service.form.CmhaServGroupUserForm;
import com.bsg.dbscale.service.form.DBSchemaForm;
import com.bsg.dbscale.service.form.PwdForm;
import com.bsg.dbscale.service.form.ServArchUpForm;
import com.bsg.dbscale.service.form.ServImageForm;
import com.bsg.dbscale.service.form.ServScaleCpuMemForm;
import com.bsg.dbscale.service.form.ServScaleStorageForm;
import com.bsg.dbscale.service.query.ServGroupQuery;
import com.bsg.dbscale.service.task.datasource.BackupDataSource;
import com.bsg.dbscale.service.util.PrimaryKeyUtil;
import com.bsg.dbscale.service.util.TaskResult;

@Service
public class CmhaServGroupService extends ServGroupService {

    @Autowired
    private CmhaServGroupCheck servGroupCheck;

    @Autowired
    private BackupStrategyService backupStrategyService;

    @Autowired
    private MysqlServService mysqlServService;

    @Autowired
    private ProxySqlServService proxySqlServService;

    @Autowired
    private CmhaServService cmhaServService;

    public Result list(ServGroupQuery servGroupQuery, String activeUsername) throws Exception {
        List<CmhaServGroupDTO> cmhaServGroupDTOs = new ArrayList<>();
        servGroupQuery.setCategory(Consts.SERV_GROUP_TYPE_CMHA);
        List<ServGroupDO> servGroupDOs = listQualifiedData(servGroupQuery, activeUsername);
        if (servGroupDOs.size() > 0) {
            List<CmSite> cmSites = CmApi.listSite(null);
            List<CmServiceArch> cmServiceArchs = CmApi.listServiceArch(null);
            List<DictTypeDO> dictTypeDOs = dictTypeDAO.list();
            List<UserDO> userDOs = userDAO.list(null);

            List<TaskDO> latestTaskDOs = taskDAO.listLatest(servGroupQuery.getSiteId(), DictConsts.OBJ_TYPE_SERV_GROUP);

            CmServiceQuery cmServiceQuery = new CmServiceQuery();
            cmServiceQuery.setSiteId(servGroupQuery.getSiteId());
            cmServiceQuery.setGroupType(Consts.SERV_GROUP_TYPE_CMHA);
            List<CmService> cmServices = CmApi.listService(cmServiceQuery);

            CmLoadbalancerQuery cmLoadbalancerQuery = new CmLoadbalancerQuery();
            cmLoadbalancerQuery.setSiteId(servGroupQuery.getSiteId());
            cmLoadbalancerQuery.setGroupType(Consts.SERV_GROUP_TYPE_CMHA);
            List<CmLoadbalancer> cmLoadbalancers = CmApi.listLoadbalancer(cmLoadbalancerQuery);

            for (ServGroupDO servGroupDO : servGroupDOs) {
                List<CmService> eligibleCmServices = CmApi.findServiceByGroupName(cmServices, servGroupDO.getName());
                String state = getState(servGroupDO, eligibleCmServices);
                if (servGroupQuery.getState() == null || StringUtils.equals(servGroupQuery.getState(), state)) {
                    CmhaServGroupDTO servGroupDTO = new CmhaServGroupDTO();
                    cmhaServGroupDTOs.add(servGroupDTO);

                    CmSite cmSite = null;
                    BusinessAreaDO businessAreaDO = servGroupDO.getBusinessArea();
                    if (businessAreaDO != null) {
                        cmSite = CmApi.findSite(cmSites, businessAreaDO.getSiteId());
                    }

                    UserDO owner = findUserDO(userDOs, servGroupDO.getOwner());
                    TaskDO latestTaskDO = findLasterTaskDO(latestTaskDOs, DictConsts.OBJ_TYPE_SERV_GROUP,
                            servGroupDO.getId());

                    ServDO proxysqlServDO = findAnyServDOType(servGroupDO, Consts.SERV_TYPE_PROXYSQL);
                    CmLoadbalancer cmLoadbalancer = CmApi.findLoadbalancer(cmLoadbalancers,
                            proxysqlServDO.getRelateId());
                    setServGroupDTO(servGroupDTO, servGroupDO, cmSite, eligibleCmServices, cmServiceArchs, dictTypeDOs,
                            owner, latestTaskDO, cmLoadbalancer);
                }
            }
        }
        return Result.success(cmhaServGroupDTOs);
    }

    public Result get(String servGroupId, boolean replication, boolean topology) throws Exception {
        CmhaServGroupDetailDTO servGroupDetailDTO = null;
        ServGroupDO servGroupDO = servGroupDAO.get(servGroupId);
        if (servGroupDO == null || !servGroupDO.getCategory().equals(Consts.SERV_GROUP_TYPE_CMHA)) {
            return Result.success(servGroupDetailDTO);
        }
        servGroupDetailDTO = new CmhaServGroupDetailDTO();
        CmSite cmSite = null;
        BusinessAreaDO businessAreaDO = servGroupDO.getBusinessArea();
        if (businessAreaDO != null) {
            cmSite = CmApi.getSite(businessAreaDO.getSiteId());
        }
        List<DictTypeDO> dictTypeDOs = dictTypeDAO.list();
        UserDO owner = userDAO.get(servGroupDO.getOwner());
        List<DefServDO> defServDOs = defServDAO.list(null);

        TaskDO servGroupLatestTaskDO = taskDAO.getLatest(DictConsts.OBJ_TYPE_SERV_GROUP, servGroupId, null);

        CmServiceQuery cmServiceQuery = new CmServiceQuery();
        cmServiceQuery.setGroup(servGroupDO.getName());
        List<CmService> cmServices = null;
        if (replication) {
            cmServices = CmApi.listServiceDetail(cmServiceQuery);
        } else {
            cmServices = CmApi.listService(cmServiceQuery);
        }
        setServGroupDetailDTO(servGroupDetailDTO, servGroupDO, cmSite, cmServices, dictTypeDOs, owner,
                servGroupLatestTaskDO);

        List<ServDO> servDOs = servGroupDO.getServs();
        CmTopology cmTopology = null;
        if (topology) {
            ServDO cmhaServDO = findAnyServDOType(servDOs, Consts.SERV_TYPE_CMHA);
            if (cmhaServDO != null && StringUtils.isNotBlank(cmhaServDO.getRelateId())) {
                try {
                    cmTopology = CmApi.getTopology(cmhaServDO.getRelateId());
                } catch (Exception e) {
                    logger.error("查询拓扑异常：", e);
                }
            }
        }

        List<ServDTO> servDTOs = new ArrayList<>(servDOs.size());
        servGroupDetailDTO.setServs(servDTOs);

        List<CmServiceArch> cmServiceArchs = CmApi.listServiceArch(null);
        for (ServDO servDO : servDOs) {
            CmService cmService = CmApi.findService(cmServices, servDO.getRelateId());
            CmServiceArch cmServiceArch = CmApi.findServiceArch(cmServiceArchs, servDO.getType(),
                    servDO.getMajorVersion() + "." + servDO.getMinorVersion(), servDO.getArchMode(),
                    servDO.getUnitCnt());
            DefServDO defServDO = findDefServDO(defServDOs, servDO.getType());
            switch (servDO.getType()) {
            case Consts.SERV_TYPE_MYSQL:
                MysqlServDTO mysqlServDTO = mysqlServService.getServDTO(servDO, cmService, cmServiceArch, defServDO,
                        dictTypeDOs);
                servDTOs.add(mysqlServDTO);
                break;
            case Consts.SERV_TYPE_PROXYSQL:
                CmLoadbalancer cmLoadbalancer = null;
                if (cmSite != null && StringUtils.isNotBlank(cmSite.getLoadbalancer())
                        && !StringUtils.equalsIgnoreCase(CmConsts.LOADBALANCER_NONE, cmSite.getLoadbalancer())) {
                    if (StringUtils.isNotBlank(servDO.getRelateId())) {
                        cmLoadbalancer = CmApi.getLoadbalancer(servDO.getRelateId());
                    }
                }
                ProxysqlServDTO proxysqlServDTO = proxySqlServService.getServDTO(servDO, cmService, cmServiceArch,
                        cmLoadbalancer, defServDO, dictTypeDOs);
                servDTOs.add(proxysqlServDTO);
                break;
            case Consts.SERV_TYPE_CMHA:
                CmhaServDTO cmhaServDTO = cmhaServService.getServDTO(servDO, cmService, cmTopology, cmServiceArch,
                        defServDO, dictTypeDOs);
                servDTOs.add(cmhaServDTO);
                break;
            default:
                break;
            }
        }

        return Result.success(servGroupDetailDTO);
    }

    private void setServGroupDTO(CmhaServGroupDTO servGroupDTO, ServGroupDO servGroupDO, CmSite cmSite,
            List<CmService> cmServices, List<CmServiceArch> cmServiceArchs, List<DictTypeDO> dictTypeDOs, UserDO owner,
            TaskDO latestTaskDO, CmLoadbalancer cmLoadbalancer) {
        setServGroupStateBaseDTO(servGroupDTO, servGroupDO, cmSite, cmServices, dictTypeDOs, owner, latestTaskDO);

        List<ServDO> servDOs = servGroupDO.getServs();
        ServDO mysqlServDO = findAnyServDOType(servDOs, Consts.SERV_TYPE_MYSQL);

        ScaleBaseDTO scaleDTO = new ScaleBaseDTO();
        String scaleName = getScaleName(mysqlServDO.getCpuCnt(), mysqlServDO.getMemSize());
        scaleDTO.setName(scaleName);
        scaleDTO.setCpuCnt(mysqlServDO.getCpuCnt());
        scaleDTO.setMemSize(mysqlServDO.getMemSize());
        servGroupDTO.setScale(scaleDTO);

        ServGroupDTO.ImageDTO imageDTO = servGroupDTO.new ImageDTO();
        servGroupDTO.setImage(imageDTO);

        imageDTO.setType(mysqlServDO.getType());
        VersionDTO versionDTO = new VersionDTO();
        imageDTO.setVersion(versionDTO);

        versionDTO.setMajor(mysqlServDO.getMajorVersion());
        versionDTO.setMinor(mysqlServDO.getMinorVersion());
        versionDTO.setPatch(mysqlServDO.getPatchVersion());
        versionDTO.setBuild(mysqlServDO.getBuildVersion());

        DisplayDTO diskTypeDisplayDTO = new DisplayDTO();
        servGroupDTO.setDiskType(diskTypeDisplayDTO);
        diskTypeDisplayDTO.setCode(mysqlServDO.getDiskType());
        DictDO diskTypeDictDO = findDictDO(dictTypeDOs, DictTypeConsts.DISK_TYPE, mysqlServDO.getDiskType());
        if (diskTypeDictDO != null) {
            diskTypeDisplayDTO.setDisplay(diskTypeDictDO.getName());
        }

        servGroupDTO.setDataSize(mysqlServDO.getDataSize());
        servGroupDTO.setLogSize(mysqlServDO.getLogSize());

        ArchBaseDTO archDTO = new ArchBaseDTO();
        DisplayDTO modeDisplayDTO = new DisplayDTO();
        archDTO.setMode(modeDisplayDTO);

        modeDisplayDTO.setCode(mysqlServDO.getArchMode());
        DictDO modeDictDO = findDictDO(dictTypeDOs, DictTypeConsts.ARCH_MODE, mysqlServDO.getArchMode());
        if (modeDictDO != null) {
            modeDisplayDTO.setDisplay(modeDictDO.getName());
        } else {
            modeDisplayDTO.setDisplay(mysqlServDO.getArchMode());
        }
        archDTO.setUnitCnt(mysqlServDO.getUnitCnt());

        CmServiceArch cmServiceArch = CmApi.findServiceArch(cmServiceArchs, mysqlServDO.getType(),
                mysqlServDO.getMajorVersion() + "." + mysqlServDO.getMinorVersion(), mysqlServDO.getArchMode(),
                mysqlServDO.getUnitCnt());
        if (cmServiceArch != null) {
            archDTO.setName(cmServiceArch.getDesc());
        }
        servGroupDTO.setArch(archDTO);

        List<String> addresses = new ArrayList<>();
        servGroupDTO.setAddresses(addresses);
        if (cmSite != null && StringUtils.isNotBlank(cmSite.getLoadbalancer())
                && !StringUtils.equalsIgnoreCase(CmConsts.LOADBALANCER_NONE, cmSite.getLoadbalancer())) {
            if (cmLoadbalancer != null) {
                ServDO proxysqlServDO = findAnyServDOType(servGroupDO, Consts.SERV_TYPE_PROXYSQL);
                List<String> ips = cmLoadbalancer.getIps();
                if (ips != null) {
                    for (String ip : ips) {
                        addresses.add(ip + ":" + proxysqlServDO.getPort());
                    }
                }
            }
        } else {
            for (ServDO servDO : servDOs) {
                if (servDO.getType().equals(Consts.SERV_TYPE_PROXYSQL)) {
                    CmService cmService = CmApi.findService(cmServices, servDO.getRelateId());
                    if (cmService != null) {
                        CmService.Status cmStatus = cmService.getStatus();
                        if (cmStatus != null) {
                            List<CmService.Status.Unit> cmUnits = cmStatus.getUnits();
                            if (cmUnits != null && cmUnits.size() > 0) {
                                for (CmService.Status.Unit cmUnit : cmUnits) {
                                    if (StringUtils.isNotBlank(cmUnit.getIp())) {
                                        Integer portValue = null;
                                        List<CmService.Port> ports = cmStatus.getPorts();
                                        for (CmService.Port port : ports) {
                                            if (port.getName().equals(CmConsts.PORT_NAME)) {
                                                portValue = port.getPort();
                                                break;
                                            }
                                        }
                                        String address = cmUnit.getIp() + ":" + portValue;
                                        addresses.add(address);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public TaskDO buildCreateTask(ServGroupDO servGroupDO, String activeUsername, Date nowDate) throws Exception {
        TaskDO taskDO = super.buildCreateTask(servGroupDO, activeUsername, nowDate);
        List<SubtaskDO> subtaskDOs = taskDO.getSubtasks();
        int priority = subtaskDOs.get(subtaskDOs.size() - 1).getPriority();
        priority++;

        SubtaskDO subtask_mysqlLink = buildMysqlLinkSubtaskDO(taskDO, servGroupDO, priority);
        if (subtask_mysqlLink != null) {
            subtaskDOs.add(subtask_mysqlLink);
            priority++;
        }

        SubtaskDO subtask_cmhaLink = buildCmhaLinkSubtaskDO(taskDO, servGroupDO, priority);
        if (subtask_cmhaLink != null) {
            subtaskDOs.add(subtask_cmhaLink);
            priority++;
        }

        BusinessAreaDO businessAreaDO = businessAreaDAO.get(servGroupDO.getBusinessAreaId());
        if (businessAreaDO != null) {
            CmSite cmSite = CmApi.getSite(businessAreaDO.getSiteId());
            if (cmSite != null && StringUtils.isNotBlank(cmSite.getLoadbalancer())
                    && !StringUtils.equalsIgnoreCase(CmConsts.LOADBALANCER_NONE, cmSite.getLoadbalancer())) {
                SubtaskDO subtask_saveLoadbalance = buildSaveLoadbalancerSubtaskDO(taskDO, servGroupDO, priority);
                if (subtask_saveLoadbalance != null) {
                    subtaskDOs.add(subtask_saveLoadbalance);
                    priority++;
                }
            }
        }

        OrderGroupDO orderGroupDO = servGroupDO.getOrderGroup();
        List<OrderDO> orderDOs = orderGroupDO.getOrders();
        for (OrderDO orderDO : orderDOs) {
            if (orderDO.getType().equals(Consts.SERV_TYPE_MYSQL)) {
                JSONObject cfgJson = JSONObject.parseObject(orderDO.getCfg());
                if (cfgJson.containsKey("backupStrategy")) {
                    BackupStrategyForm backupStrategy = cfgJson.getObject("backupStrategy", BackupStrategyForm.class);
                    if (backupStrategy != null) {
                        SubtaskDO subtask_backupStorage = new SubtaskDO();
                        subtaskDOs.add(subtask_backupStorage);

                        subtask_backupStorage.setId(PrimaryKeyUtil.get());
                        subtask_backupStorage.setTaskId(taskDO.getId());
                        subtask_backupStorage.setObjType(DictConsts.OBJ_TYPE_SERV_GROUP);
                        subtask_backupStorage.setObjId(servGroupDO.getId());
                        subtask_backupStorage.setObjName(servGroupDO.getName());
                        subtask_backupStorage.setActionType(DictConsts.ACTION_TYPE_ADD_BACKUP_STRATEGY);
                        subtask_backupStorage.setPriority(priority);
                        subtask_backupStorage.setState(DictConsts.TASK_STATE_READY);
                        subtask_backupStorage.setTimeout(60L);
                        subtask_backupStorage.setDataSource(servGroupDO);
                    }
                }
                break;
            }
        }
        return taskDO;
    }

    public SubtaskDO buildMysqlLinkSubtaskDO(TaskDO taskDO, ServGroupDO servGroupDO, int priority) {
        SubtaskDO subtask_link = null;
        ServDO mysqlServDO = findAnyServDOType(servGroupDO, Consts.SERV_TYPE_MYSQL);
        if (mysqlServDO != null) {
            subtask_link = new SubtaskDO();

            subtask_link.setId(PrimaryKeyUtil.get());
            subtask_link.setTaskId(taskDO.getId());
            subtask_link.setObjType(DictConsts.OBJ_TYPE_SERV);
            subtask_link.setObjId(mysqlServDO.getId());
            subtask_link.setObjName(mysqlServDO.getType());
            subtask_link.setActionType(DictConsts.ACTION_TYPE_LINK);
            subtask_link.setPriority(priority);
            subtask_link.setState(DictConsts.TASK_STATE_READY);
            SubtaskCfgDO subtaskCfgDO = subtaskCfgDAO.get(DictConsts.OBJ_TYPE_SERV, DictConsts.ACTION_TYPE_LINK);
            if (subtaskCfgDO != null) {
                subtask_link.setTimeout(subtaskCfgDO.getTimeout());
            }
            subtask_link.setDataSource(servGroupDO);
        }
        return subtask_link;
    }

    public SubtaskDO buildCmhaLinkSubtaskDO(TaskDO taskDO, ServGroupDO servGroupDO, int priority) {
        SubtaskDO subtask_link = null;
        ServDO cmhaServDO = findAnyServDOType(servGroupDO, Consts.SERV_TYPE_CMHA);
        if (cmhaServDO != null) {
            subtask_link = new SubtaskDO();
            subtask_link.setId(PrimaryKeyUtil.get());
            subtask_link.setTaskId(taskDO.getId());
            subtask_link.setObjType(DictConsts.OBJ_TYPE_SERV);
            subtask_link.setObjId(cmhaServDO.getId());
            subtask_link.setObjName(cmhaServDO.getType());
            subtask_link.setActionType(DictConsts.ACTION_TYPE_LINK);
            subtask_link.setPriority(priority);
            subtask_link.setState(DictConsts.TASK_STATE_READY);
            SubtaskCfgDO subtaskCfgDO = subtaskCfgDAO.get(DictConsts.OBJ_TYPE_SERV, DictConsts.ACTION_TYPE_LINK);
            if (subtaskCfgDO != null) {
                subtask_link.setTimeout(subtaskCfgDO.getTimeout());
            }
            subtask_link.setDataSource(servGroupDO);
        }
        return subtask_link;
    }

    private SubtaskDO buildSaveLoadbalancerSubtaskDO(TaskDO taskDO, ServGroupDO servGroupDO, int priority) {
        SubtaskDO subtask_saveLoadbalance = null;
        ServDO proxysqlServDO = findAnyServDOType(servGroupDO, Consts.SERV_TYPE_PROXYSQL);
        if (proxysqlServDO != null) {
            subtask_saveLoadbalance = new SubtaskDO();
            subtask_saveLoadbalance.setId(PrimaryKeyUtil.get());
            subtask_saveLoadbalance.setTaskId(taskDO.getId());
            subtask_saveLoadbalance.setObjType(DictConsts.OBJ_TYPE_SERV);
            subtask_saveLoadbalance.setObjId(proxysqlServDO.getId());
            subtask_saveLoadbalance.setObjName(proxysqlServDO.getType());
            subtask_saveLoadbalance.setActionType(DictConsts.ACTION_TYPE_SAVE_LOADBALANCER);
            subtask_saveLoadbalance.setPriority(priority);
            subtask_saveLoadbalance.setState(DictConsts.TASK_STATE_READY);
            SubtaskCfgDO subtaskCfgDO = subtaskCfgDAO.get(DictConsts.OBJ_TYPE_SERV,
                    DictConsts.ACTION_TYPE_SAVE_LOADBALANCER);
            if (subtaskCfgDO != null) {
                subtask_saveLoadbalance.setTimeout(subtaskCfgDO.getTimeout());
            }
            subtask_saveLoadbalance.setDataSource(proxysqlServDO);
        }
        return subtask_saveLoadbalance;
    }

    @Transactional(rollbackFor = Exception.class)
    public Result start(String servGroupId, String activeUsername) throws Exception {
        ServGroupDO servGroupDO = servGroupDAO.get(servGroupId);

        CheckResult checkResult = servGroupCheck.checkStart(servGroupDO);
        if (checkResult.getCode() != CheckResult.SUCCESS) {
            logger.error(checkResult.getMsg());
            return Result.failure(checkResult);
        }

        return super.start(servGroupDO, activeUsername);
    }

    @Transactional(rollbackFor = Exception.class)
    public Result stop(String servGroupId, String activeUsername) throws Exception {
        ServGroupDO servGroupDO = servGroupDAO.get(servGroupId);

        CheckResult checkResult = servGroupCheck.checkStop(servGroupDO);
        if (checkResult.getCode() != CheckResult.SUCCESS) {
            logger.error(checkResult.getMsg());
            return Result.failure(checkResult);
        }

        return super.stop(servGroupDO, activeUsername);
    }

    @Transactional(rollbackFor = Exception.class)
    public Result imageUpdate(String servGroupId, ServImageForm imageForm, String activeUsername) throws Exception {
        ServGroupDO servGroupDO = servGroupDAO.get(servGroupId);

        CheckResult checkResult = servGroupCheck.checkImageUpdate(servGroupDO, imageForm);
        if (checkResult.getCode() != CheckResult.SUCCESS) {
            logger.error(checkResult.getMsg());
            return Result.failure(checkResult);
        }

        return super.imageUpdate(servGroupDO, imageForm, activeUsername);
    }

    @Transactional(rollbackFor = Exception.class)
    public Result scaleUpCpuMem(String servGroupId, ServScaleCpuMemForm scaleCpuMemForm, String activeUsername)
            throws Exception {
        ServGroupDO servGroupDO = servGroupDAO.get(servGroupId);

        CheckResult checkResult = servGroupCheck.checkScaleUpCpuMem(servGroupDO, scaleCpuMemForm);
        if (checkResult.getCode() != CheckResult.SUCCESS) {
            logger.error(checkResult.getMsg());
            return Result.failure(checkResult);
        }

        return super.scaleUpCpuMem(servGroupDO, scaleCpuMemForm, activeUsername);
    }

    @Transactional(rollbackFor = Exception.class)
    public Result scaleUpStorage(String servGroupId, ServScaleStorageForm scaleStorageForm, String activeUsername)
            throws Exception {
        ServGroupDO servGroupDO = servGroupDAO.get(servGroupId);

        CheckResult checkResult = servGroupCheck.checkScaleUpStorage(servGroupDO, scaleStorageForm);
        if (checkResult.getCode() != CheckResult.SUCCESS) {
            logger.error(checkResult.getMsg());
            return Result.failure(checkResult);
        }

        return super.scaleUpStorage(servGroupDO, scaleStorageForm, activeUsername);
    }

    @Transactional(rollbackFor = Exception.class)
    public Result archUp(String servGroupId, ServArchUpForm archUpForm, String activeUsername) throws Exception {
        ServGroupDO servGroupDO = servGroupDAO.get(servGroupId);

        CheckResult checkResult = servGroupCheck.checkArchUp(servGroupDO, archUpForm);
        if (checkResult.getCode() != CheckResult.SUCCESS) {
            logger.error(checkResult.getMsg());
            return Result.failure(checkResult);
        }

        return super.archUp(servGroupDO, archUpForm, activeUsername);
    }

    @Transactional(rollbackFor = Exception.class)
    public Result backup(String servGroupId, BackupForm backupForm, String activeUsername) throws Exception {
        ServGroupDO servGroupDO = servGroupDAO.get(servGroupId);

        CheckResult checkResult = servGroupCheck.checkBackup(servGroupDO);
        if (checkResult.getCode() != CheckResult.SUCCESS) {
            logger.error(checkResult.getMsg());
            return Result.failure(checkResult);
        }

        Date nowDate = systemDAO.getCurrentSqlDateTime();
        TaskDO taskDO = buildBackupTask(servGroupDO, backupForm, activeUsername, nowDate);
        List<SubtaskDO> subtaskDOs = taskDO.getSubtasks();
        for (SubtaskDO subtaskDO : subtaskDOs) {
            subtaskDAO.save(subtaskDO);
        }
        taskDAO.save(taskDO);

        executor.execute(new Task(taskDO));

        return Result.success();
    }

    @Transactional(rollbackFor = Exception.class)
    public Result remove(String servGroupId, String activeUsername) throws Exception {
        ServGroupDO servGroupDO = servGroupDAO.get(servGroupId);

        CheckResult checkResult = servGroupCheck.checkRemove(servGroupDO);
        if (checkResult.getCode() != CheckResult.SUCCESS) {
            logger.error(checkResult.getMsg());
            return Result.failure(checkResult);
        }

        return super.remove(servGroupDO, activeUsername);
    }

    @Override
    public TaskDO buildStartTask(ServGroupDO servGroupDO, String activeUsername, Date nowDate) {
        TaskDO taskDO = super.buildStartTask(servGroupDO, activeUsername, nowDate);
        List<ServDO> servDOs = servGroupDO.getServs();
        if (servDOs.size() > 1) {
            List<SubtaskDO> subtaskDOs = taskDO.getSubtasks();
            for (SubtaskDO subtaskDO : subtaskDOs) {
                ServDO servDO = (ServDO) subtaskDO.getDataSource();
                switch (servDO.getType()) {
                case Consts.SERV_TYPE_MYSQL:
                    subtaskDO.setPriority(1);
                    break;
                case Consts.SERV_TYPE_PROXYSQL:
                    subtaskDO.setPriority(2);
                    break;
                case Consts.SERV_TYPE_CMHA:
                    subtaskDO.setPriority(3);
                    break;
                default:
                    break;
                }
            }
        }
        return taskDO;
    }

    @Override
    public TaskDO buildStopTask(ServGroupDO servGroupDO, String activeUsername, Date nowDate) {
        TaskDO taskDO = super.buildStopTask(servGroupDO, activeUsername, nowDate);
        List<ServDO> servDOs = servGroupDO.getServs();
        if (servDOs.size() > 1) {
            List<SubtaskDO> subtaskDOs = taskDO.getSubtasks();
            for (SubtaskDO subtaskDO : subtaskDOs) {
                ServDO servDO = (ServDO) subtaskDO.getDataSource();
                switch (servDO.getType()) {
                case Consts.SERV_TYPE_MYSQL:
                    subtaskDO.setPriority(3);
                    break;
                case Consts.SERV_TYPE_PROXYSQL:
                    subtaskDO.setPriority(2);
                    break;
                case Consts.SERV_TYPE_CMHA:
                    subtaskDO.setPriority(1);
                    break;
                default:
                    break;
                }
            }
        }
        return taskDO;
    }

    private TaskDO buildBackupTask(ServGroupDO servGroupDO, BackupForm backupForm, String activeUsername,
            Date nowDate) {
        TaskDO taskDO = new TaskDO();
        taskDO.setId(PrimaryKeyUtil.get());
        BusinessAreaDO businessAreaDO = businessAreaDAO.get(servGroupDO.getBusinessAreaId());
        taskDO.setSiteId(businessAreaDO.getSiteId());
        taskDO.setObjType(DictConsts.OBJ_TYPE_SERV_GROUP);
        taskDO.setObjId(servGroupDO.getId());
        taskDO.setObjName(servGroupDO.getName());
        taskDO.setActionType(DictConsts.ACTION_TYPE_BACKUP);
        taskDO.setBlock(true);
        taskDO.setState(DictConsts.TASK_STATE_READY);
        taskDO.setOwner(servGroupDO.getOwner());
        taskDO.setCreator(activeUsername);
        taskDO.setGmtCreate(nowDate);

        List<SubtaskDO> subtaskDOs = new ArrayList<>();
        taskDO.setSubtasks(subtaskDOs);

        int priority = 1;
        List<ServDO> servDOs = servGroupDO.getServs();
        for (ServDO servDO : servDOs) {
            if (servDO.getType().equals(Consts.SERV_TYPE_MYSQL)) {
                SubtaskDO subtask = new SubtaskDO();
                subtaskDOs.add(subtask);

                subtask.setId(PrimaryKeyUtil.get());
                subtask.setTaskId(taskDO.getId());
                subtask.setObjType(DictConsts.OBJ_TYPE_SERV);
                subtask.setObjId(servDO.getId());
                subtask.setObjName(servDO.getType());
                subtask.setActionType(DictConsts.ACTION_TYPE_BACKUP);
                subtask.setPriority(priority);
                subtask.setState(DictConsts.TASK_STATE_READY);
                SubtaskCfgDO subtaskCfgDO = subtaskCfgDAO.get(DictConsts.OBJ_TYPE_SERV, DictConsts.ACTION_TYPE_BACKUP);
                if (subtaskCfgDO != null) {
                    subtask.setTimeout(subtaskCfgDO.getTimeout());
                }

                BackupDataSource backupDataSource = new BackupDataSource();
                backupDataSource.setBackupStorageType(backupForm.getBackupStorageType());
                backupDataSource.setType(backupForm.getType());
                backupDataSource.setTables(backupForm.getTables());
                backupDataSource.setExpired(backupForm.getExpired());
                subtask.setDataSource(backupDataSource);
            }
        }
        return taskDO;
    }

    @Override
    public TaskDO buildImageUpdateTask(ServGroupDO servGroupDO, String activeUsername, Date nowDate) {
        TaskDO taskDO = new TaskDO();
        taskDO.setId(PrimaryKeyUtil.get());
        BusinessAreaDO businessAreaDO = businessAreaDAO.get(servGroupDO.getBusinessAreaId());
        taskDO.setSiteId(businessAreaDO.getSiteId());
        taskDO.setObjType(DictConsts.OBJ_TYPE_SERV_GROUP);
        taskDO.setObjId(servGroupDO.getId());
        taskDO.setObjName(servGroupDO.getName());
        taskDO.setActionType(DictConsts.ACTION_TYPE_IMAGE_UPDATE);
        taskDO.setBlock(true);
        taskDO.setState(DictConsts.TASK_STATE_READY);
        taskDO.setOwner(servGroupDO.getOwner());
        taskDO.setCreator(activeUsername);
        taskDO.setGmtCreate(nowDate);

        List<ServDO> servDOs = servGroupDO.getServs();
        ServDO cmhaServDO = findAnyServDOType(servDOs, Consts.SERV_TYPE_CMHA);

        OrderGroupDO orderGroupDO = orderGroupDAO.get(servGroupDO.getOrderGroupId());
        List<OrderDO> orderDOs = orderGroupDO.getOrders();
        for (OrderDO orderDO : orderDOs) {
            if (orderDO.getMajorVersion() != null) {
                List<SubtaskDO> subtaskDOs = new ArrayList<>();
                taskDO.setSubtasks(subtaskDOs);

                int priority = 1;
                if (cmhaServDO != null && orderDO.getType().equals(Consts.SERV_TYPE_MYSQL)) {
                    ServDO mysqlServDO = findAnyServDOType(servDOs, Consts.SERV_TYPE_MYSQL);
                    SubtaskDO subtask = new SubtaskDO();
                    subtaskDOs.add(subtask);

                    subtask.setId(PrimaryKeyUtil.get());
                    subtask.setTaskId(taskDO.getId());
                    subtask.setObjType(DictConsts.OBJ_TYPE_SERV);
                    subtask.setObjId(mysqlServDO.getId());
                    subtask.setObjName(mysqlServDO.getType());
                    subtask.setActionType(DictConsts.ACTION_TYPE_SET_MAINTENANCE);
                    subtask.setPriority(priority);
                    subtask.setState(DictConsts.TASK_STATE_READY);
                    SubtaskCfgDO subtaskCfgDO = subtaskCfgDAO.get(DictConsts.OBJ_TYPE_SERV,
                            DictConsts.ACTION_TYPE_SET_MAINTENANCE);
                    if (subtaskCfgDO != null) {
                        subtask.setTimeout(subtaskCfgDO.getTimeout());
                    }
                    subtask.setDataSource(cmhaServDO);
                    priority++;
                }

                for (ServDO servDO : servDOs) {
                    if (orderDO.getType().equals(servDO.getType())) {
                        SubtaskDO subtask = new SubtaskDO();
                        subtaskDOs.add(subtask);

                        subtask.setId(PrimaryKeyUtil.get());
                        subtask.setTaskId(taskDO.getId());
                        subtask.setObjType(DictConsts.OBJ_TYPE_SERV);
                        subtask.setObjId(servDO.getId());
                        subtask.setObjName(servDO.getType());
                        subtask.setActionType(DictConsts.ACTION_TYPE_IMAGE_UPDATE);
                        subtask.setPriority(priority);
                        subtask.setState(DictConsts.TASK_STATE_READY);
                        SubtaskCfgDO subtaskCfgDO = subtaskCfgDAO.get(DictConsts.OBJ_TYPE_SERV,
                                DictConsts.ACTION_TYPE_IMAGE_UPDATE);
                        if (subtaskCfgDO != null) {
                            subtask.setTimeout(subtaskCfgDO.getTimeout());
                        }
                        subtask.setDataSource(servDO);
                    }
                }

                priority++;
                if (cmhaServDO != null && orderDO.getType().equals(Consts.SERV_TYPE_MYSQL)) {
                    ServDO mysqlServDO = findAnyServDOType(servDOs, Consts.SERV_TYPE_MYSQL);
                    SubtaskDO subtask = new SubtaskDO();
                    subtaskDOs.add(subtask);

                    subtask.setId(PrimaryKeyUtil.get());
                    subtask.setTaskId(taskDO.getId());
                    subtask.setObjType(DictConsts.OBJ_TYPE_SERV);
                    subtask.setObjId(mysqlServDO.getId());
                    subtask.setObjName(mysqlServDO.getType());
                    subtask.setActionType(DictConsts.ACTION_TYPE_CANCEL_MAINTENANCE);
                    subtask.setPriority(priority);
                    subtask.setState(DictConsts.TASK_STATE_READY);
                    SubtaskCfgDO subtaskCfgDO = subtaskCfgDAO.get(DictConsts.OBJ_TYPE_SERV,
                            DictConsts.ACTION_TYPE_CANCEL_MAINTENANCE);
                    if (subtaskCfgDO != null) {
                        subtask.setTimeout(subtaskCfgDO.getTimeout());
                    }
                    subtask.setDataSource(cmhaServDO);
                    priority++;
                    break;
                }
                break;
            }
        }

        return taskDO;
    }

    @Override
    public TaskDO buildScaleUpCpuMemTask(ServGroupDO servGroupDO, String activeUsername, Date nowDate) {
        TaskDO taskDO = new TaskDO();
        taskDO.setId(PrimaryKeyUtil.get());
        BusinessAreaDO businessAreaDO = businessAreaDAO.get(servGroupDO.getBusinessAreaId());
        taskDO.setSiteId(businessAreaDO.getSiteId());
        taskDO.setObjType(DictConsts.OBJ_TYPE_SERV_GROUP);
        taskDO.setObjId(servGroupDO.getId());
        taskDO.setObjName(servGroupDO.getName());
        taskDO.setActionType(DictConsts.ACTION_TYPE_SCALE_UP_CPUMEM);
        taskDO.setBlock(true);
        taskDO.setState(DictConsts.TASK_STATE_READY);
        taskDO.setOwner(servGroupDO.getOwner());
        taskDO.setCreator(activeUsername);
        taskDO.setGmtCreate(nowDate);

        List<ServDO> servDOs = servGroupDO.getServs();
        ServDO cmhaServDO = findAnyServDOType(servDOs, Consts.SERV_TYPE_CMHA);

        OrderGroupDO orderGroupDO = orderGroupDAO.get(servGroupDO.getOrderGroupId());
        List<OrderDO> orderDOs = orderGroupDO.getOrders();
        for (OrderDO orderDO : orderDOs) {
            if (orderDO.getCpuCnt() != null) {
                List<SubtaskDO> subtaskDOs = new ArrayList<>();
                taskDO.setSubtasks(subtaskDOs);

                int priority = 1;
                if (cmhaServDO != null && orderDO.getType().equals(Consts.SERV_TYPE_MYSQL)) {
                    ServDO mysqlServDO = findAnyServDOType(servDOs, Consts.SERV_TYPE_MYSQL);
                    SubtaskDO subtask = new SubtaskDO();
                    subtaskDOs.add(subtask);

                    subtask.setId(PrimaryKeyUtil.get());
                    subtask.setTaskId(taskDO.getId());
                    subtask.setObjType(DictConsts.OBJ_TYPE_SERV);
                    subtask.setObjId(mysqlServDO.getId());
                    subtask.setObjName(mysqlServDO.getType());
                    subtask.setActionType(DictConsts.ACTION_TYPE_SET_MAINTENANCE);
                    subtask.setPriority(priority);
                    subtask.setState(DictConsts.TASK_STATE_READY);
                    SubtaskCfgDO subtaskCfgDO = subtaskCfgDAO.get(DictConsts.OBJ_TYPE_SERV,
                            DictConsts.ACTION_TYPE_SET_MAINTENANCE);
                    if (subtaskCfgDO != null) {
                        subtask.setTimeout(subtaskCfgDO.getTimeout());
                    }
                    subtask.setDataSource(cmhaServDO);
                    priority++;
                }

                for (ServDO servDO : servDOs) {
                    if (orderDO.getType().equals(servDO.getType())) {
                        SubtaskDO subtask = new SubtaskDO();
                        subtaskDOs.add(subtask);

                        subtask.setId(PrimaryKeyUtil.get());
                        subtask.setTaskId(taskDO.getId());
                        subtask.setObjType(DictConsts.OBJ_TYPE_SERV);
                        subtask.setObjId(servDO.getId());
                        subtask.setObjName(servDO.getType());
                        subtask.setActionType(DictConsts.ACTION_TYPE_SCALE_UP_CPUMEM);
                        subtask.setPriority(priority);
                        subtask.setState(DictConsts.TASK_STATE_READY);
                        SubtaskCfgDO subtaskCfgDO = subtaskCfgDAO.get(DictConsts.OBJ_TYPE_SERV,
                                DictConsts.ACTION_TYPE_SCALE_UP_CPUMEM);
                        if (subtaskCfgDO != null) {
                            subtask.setTimeout(subtaskCfgDO.getTimeout());
                        }
                        subtask.setDataSource(servDO);
                    }
                }

                priority++;
                if (cmhaServDO != null && orderDO.getType().equals(Consts.SERV_TYPE_MYSQL)) {
                    ServDO mysqlServDO = findAnyServDOType(servDOs, Consts.SERV_TYPE_MYSQL);
                    SubtaskDO subtask = new SubtaskDO();
                    subtaskDOs.add(subtask);

                    subtask.setId(PrimaryKeyUtil.get());
                    subtask.setTaskId(taskDO.getId());
                    subtask.setObjType(DictConsts.OBJ_TYPE_SERV);
                    subtask.setObjId(mysqlServDO.getId());
                    subtask.setObjName(mysqlServDO.getType());
                    subtask.setActionType(DictConsts.ACTION_TYPE_CANCEL_MAINTENANCE);
                    subtask.setPriority(priority);
                    subtask.setState(DictConsts.TASK_STATE_READY);
                    SubtaskCfgDO subtaskCfgDO = subtaskCfgDAO.get(DictConsts.OBJ_TYPE_SERV,
                            DictConsts.ACTION_TYPE_CANCEL_MAINTENANCE);
                    if (subtaskCfgDO != null) {
                        subtask.setTimeout(subtaskCfgDO.getTimeout());
                    }
                    subtask.setDataSource(cmhaServDO);
                    priority++;
                    break;
                }
                break;
            }
        }

        return taskDO;
    }

    @Override
    public TaskDO buildRemoveTask(ServGroupDO servGroupDO, String activeUsername, Date nowDate) throws Exception {
        TaskDO taskDO = new TaskDO();
        taskDO.setId(PrimaryKeyUtil.get());
        BusinessAreaDO businessAreaDO = businessAreaDAO.get(servGroupDO.getBusinessAreaId());
        taskDO.setSiteId(businessAreaDO.getSiteId());
        taskDO.setObjType(DictConsts.OBJ_TYPE_SERV_GROUP);
        taskDO.setObjId(servGroupDO.getId());
        taskDO.setObjName(servGroupDO.getName());
        taskDO.setActionType(DictConsts.ACTION_TYPE_REMOVE);
        taskDO.setBlock(true);
        taskDO.setState(DictConsts.TASK_STATE_READY);
        taskDO.setOwner(servGroupDO.getOwner());
        taskDO.setCreator(activeUsername);
        taskDO.setGmtCreate(nowDate);

        List<SubtaskDO> subtaskDOs = new ArrayList<>();
        taskDO.setSubtasks(subtaskDOs);

        int priority = 1;
        if (businessAreaDO != null) {
            CmSite cmSite = CmApi.getSite(businessAreaDO.getSiteId());
            if (cmSite != null && StringUtils.isNotBlank(cmSite.getLoadbalancer())
                    && !StringUtils.equalsIgnoreCase(CmConsts.LOADBALANCER_NONE, cmSite.getLoadbalancer())) {
                SubtaskDO subtask_removeLoadbalance = null;
                ServDO proxysqlServDO = findAnyServDOType(servGroupDO, Consts.SERV_TYPE_PROXYSQL);
                if (proxysqlServDO != null) {
                    subtask_removeLoadbalance = new SubtaskDO();
                    subtaskDOs.add(subtask_removeLoadbalance);

                    subtask_removeLoadbalance.setId(PrimaryKeyUtil.get());
                    subtask_removeLoadbalance.setTaskId(taskDO.getId());
                    subtask_removeLoadbalance.setObjType(DictConsts.OBJ_TYPE_SERV);
                    subtask_removeLoadbalance.setObjId(proxysqlServDO.getId());
                    subtask_removeLoadbalance.setObjName(proxysqlServDO.getType());
                    subtask_removeLoadbalance.setActionType(DictConsts.ACTION_TYPE_REMOVE_LOADBALANCER);
                    subtask_removeLoadbalance.setPriority(priority);
                    subtask_removeLoadbalance.setState(DictConsts.TASK_STATE_READY);
                    SubtaskCfgDO subtaskCfgDO = subtaskCfgDAO.get(DictConsts.OBJ_TYPE_SERV,
                            DictConsts.ACTION_TYPE_REMOVE_LOADBALANCER);
                    if (subtaskCfgDO != null) {
                        subtask_removeLoadbalance.setTimeout(subtaskCfgDO.getTimeout());
                    }
                    subtask_removeLoadbalance.setDataSource(proxysqlServDO);
                    priority++;
                }
            }
        }

        List<ServDO> servDOs = servGroupDO.getServs();
        for (ServDO servDO : servDOs) {
            SubtaskDO subtask_remove = new SubtaskDO();
            subtaskDOs.add(subtask_remove);

            subtask_remove.setId(PrimaryKeyUtil.get());
            subtask_remove.setTaskId(taskDO.getId());
            subtask_remove.setObjType(DictConsts.OBJ_TYPE_SERV);
            subtask_remove.setObjId(servDO.getId());
            subtask_remove.setObjName(servDO.getType());
            subtask_remove.setActionType(DictConsts.ACTION_TYPE_REMOVE);
            subtask_remove.setPriority(priority);
            subtask_remove.setState(DictConsts.TASK_STATE_READY);
            SubtaskCfgDO subtaskCfgDO = subtaskCfgDAO.get(DictConsts.OBJ_TYPE_SERV, DictConsts.ACTION_TYPE_REMOVE);
            if (subtaskCfgDO != null) {
                subtask_remove.setTimeout(subtaskCfgDO.getTimeout());
            }
            subtask_remove.setDataSource(servDO);
        }
        return taskDO;
    }

    @Override
    public TaskResult executeSubtask(TaskDO taskDO, SubtaskDO subtaskDO) {
        TaskResult taskResult = super.executeSubtask(taskDO, subtaskDO);
        if (!taskResult.getState().equals(DictConsts.TASK_STATE_RUNNING)) {
            return taskResult;
        }
        switch (subtaskDO.getActionType()) {
        case DictConsts.ACTION_TYPE_LINK:
            try {
                ServGroupDO servGroupDO = (ServGroupDO) subtaskDO.getDataSource();
                servGroupDO = servGroupDAO.get(servGroupDO.getId());

                String relateId = "";
                List<ServDO> servDOs = servGroupDO.getServs();
                for (ServDO servDO : servDOs) {
                    if (servDO.getId().equals(subtaskDO.getObjId())) {
                        relateId = servDO.getRelateId();
                        break;
                    }
                }
                CmServicesLinkBody cmServicesLinkBody = buildCmServicesLinkRequestBody(subtaskDO.getObjId(),
                        servGroupDO);
                CmApi.linkServices(relateId, cmServicesLinkBody, subtaskDO.getTimeout().intValue());

                taskResult.setState(DictConsts.TASK_STATE_SUCCESS);
            } catch (Exception e) {
                taskResult.setState(DictConsts.TASK_STATE_FAILED);
                taskResult.setMsg(e.toString());
                logger.error("LINK异常：", e);
            }
            break;
        case DictConsts.ACTION_TYPE_SAVE_LOADBALANCER:
            try {
                ServDO proxysqlServDO = (ServDO) subtaskDO.getDataSource();
                CmApi.saveLoadbalancer(proxysqlServDO.getRelateId());

                taskResult = pollingSaveLoadbalancerTask(subtaskDO);
            } catch (Exception e) {
                taskResult.setState(DictConsts.TASK_STATE_FAILED);
                taskResult.setMsg(e.toString());
                logger.error("注册loadbalance异常：", e);
            }
            break;
        case DictConsts.ACTION_TYPE_REMOVE_LOADBALANCER:
            try {
                ServDO proxysqlServDO = (ServDO) subtaskDO.getDataSource();
                if (StringUtils.isNotBlank(proxysqlServDO.getRelateId())) {
                    CmApi.removeLoadbalancer(proxysqlServDO.getRelateId());
                }

                taskResult.setState(DictConsts.TASK_STATE_SUCCESS);
            } catch (Exception e) {
                taskResult.setState(DictConsts.TASK_STATE_FAILED);
                taskResult.setMsg(e.toString());
                logger.error("删除loadbalance异常：", e);
            }
            break;
        case DictConsts.ACTION_TYPE_ADD_BACKUP_STRATEGY:
            try {
                ServGroupDO servGroupDO = (ServGroupDO) subtaskDO.getDataSource();
                OrderGroupDO orderGroupDO = servGroupDO.getOrderGroup();
                List<OrderDO> orderDOs = orderGroupDO.getOrders();
                for (OrderDO orderDO : orderDOs) {
                    if (orderDO.getType().equals(Consts.SERV_TYPE_MYSQL)) {

                        JSONObject cfgJson = JSONObject.parseObject(orderDO.getCfg());
                        BackupStrategyForm backupStrategyForm = cfgJson.getObject("backupStrategy",
                                BackupStrategyForm.class);
                        backupStrategyForm.setServGroupId(servGroupDO.getId());
                        backupStrategyForm.setEnabled(true);
                        Date date = systemDAO.getCurrentSqlDateTime();
                        BackupStrategyDO backupStrategyDO = backupStrategyService
                                .buildBackupStrategyDOForSave(backupStrategyForm, servGroupDO.getOwner(), date);
                        backupStrategyDAO.save(backupStrategyDO);
                        break;
                    }
                }

                taskResult.setState(DictConsts.TASK_STATE_SUCCESS);
            } catch (Exception e) {
                taskResult.setState(DictConsts.TASK_STATE_FAILED);
                taskResult.setMsg(e.toString());
                logger.error("添加备份策略异常：", e);
            }
            break;
        case DictConsts.ACTION_TYPE_BACKUP:
            try {
                BackupDataSource backupDataSource = (BackupDataSource) subtaskDO.getDataSource();
                ServDO servDO = servDAO.get(subtaskDO.getObjId());

                CmBackupBody cmBackupBody = mysqlServService.buildCmServiceBackupRequestBody(backupDataSource, servDO);
                CmBackupResp cmBackupResp = CmApi.backup(servDO.getRelateId(), cmBackupBody);
                if (cmBackupResp == null) {
                    taskResult.setState(DictConsts.TASK_STATE_FAILED);
                    taskResult.setMsg("接口返回错误。");
                    return taskResult;
                }

                String backupFileId = cmBackupResp.getId();
                taskResult = mysqlServService.pollingBackup(subtaskDO, backupFileId);
            } catch (Exception e) {
                taskResult.setState(DictConsts.TASK_STATE_FAILED);
                taskResult.setMsg(e.toString());
                logger.error("备份异常：", e);
            }
            break;
        case DictConsts.ACTION_TYPE_SET_MAINTENANCE:
            try {
                ServDO cmhaServDO = (ServDO) subtaskDO.getDataSource();
                ServDO servDO = servDAO.get(subtaskDO.getObjId());
                List<UnitDO> unitDOs = servDO.getUnits();
                for (UnitDO unitDO : unitDOs) {
                    CmMaintenanceBody cmMaintenanceBody = new CmMaintenanceBody();
                    cmMaintenanceBody.setUnitId(unitDO.getRelateId());
                    cmMaintenanceBody.setMaintenance(true);
                    CmApi.maintenance(cmhaServDO.getRelateId(), cmMaintenanceBody);
                }

                taskResult.setState(DictConsts.TASK_STATE_SUCCESS);
            } catch (Exception e) {
                taskResult.setState(DictConsts.TASK_STATE_FAILED);
                taskResult.setMsg(e.toString());
                logger.error("设置维护模式异常：", e);
            }
            break;
        case DictConsts.ACTION_TYPE_CANCEL_MAINTENANCE:
            try {
                ServDO cmhaServDO = (ServDO) subtaskDO.getDataSource();
                ServDO servDO = servDAO.get(subtaskDO.getObjId());
                List<UnitDO> unitDOs = servDO.getUnits();
                for (UnitDO unitDO : unitDOs) {
                    CmMaintenanceBody cmMaintenanceBody = new CmMaintenanceBody();
                    cmMaintenanceBody.setUnitId(unitDO.getRelateId());
                    cmMaintenanceBody.setMaintenance(false);
                    CmApi.maintenance(cmhaServDO.getRelateId(), cmMaintenanceBody);
                }

                taskResult.setState(DictConsts.TASK_STATE_SUCCESS);
            } catch (Exception e) {
                taskResult.setState(DictConsts.TASK_STATE_FAILED);
                taskResult.setMsg(e.toString());
                logger.error("取消维护模式异常：", e);
            }
            break;
        }
        return taskResult;
    }

    @Override
    public void executeTaskDone(TaskDO taskDO) {
        ServGroupDO servGroupDO = servGroupDAO.get(taskDO.getObjId());
        switch (taskDO.getActionType()) {
        case DictConsts.ACTION_TYPE_REMOVE:
            if (taskDO.getState().equals(DictConsts.TASK_STATE_SUCCESS)) {
                backupStrategyDAO.removeByServGroupId(servGroupDO.getId());
            }
            break;
        default:
            break;
        }
        super.executeTaskDone(taskDO);
    }

    public CmServicesLinkBody buildCmServicesLinkRequestBody(String servId, ServGroupDO servGroupDO) throws Exception {
        CmServicesLinkBody cmServicesLinkBody = new CmServicesLinkBody();
        cmServicesLinkBody.setServiceGroupType(servGroupDO.getCategory());
        servGroupDO = servGroupDAO.get(servGroupDO.getId());

        List<String> relateIds = new ArrayList<>();
        cmServicesLinkBody.setSerevies(relateIds);

        String type = "";
        List<ServDO> servDOs = servGroupDO.getServs();
        for (ServDO servDO : servDOs) {
            if (servDO.getId().equals(servId)) {
                type = servDO.getType();
                break;
            }
        }

        if (type.equals(Consts.SERV_TYPE_MYSQL)) {
            for (ServDO servDO : servDOs) {
                if (servDO.getId().equals(servId)) {
                    relateIds.add(servDO.getRelateId());
                }

                if (servDO.getType().equals(Consts.SERV_TYPE_PROXYSQL)
                        || servDO.getType().equals(Consts.SERV_TYPE_CMHA)) {
                    relateIds.add(servDO.getRelateId());
                }
            }
        } else if (type.equals(Consts.SERV_TYPE_CMHA)) {
            for (ServDO servDO : servDOs) {
                relateIds.add(servDO.getRelateId());
            }
        }
        return cmServicesLinkBody;
    }

    private TaskResult pollingSaveLoadbalancerTask(SubtaskDO subtaskDO) throws Exception {
        TaskResult taskResult = new TaskResult();
        taskResult.setState(DictConsts.TASK_STATE_RUNNING);
        ServDO proxysqlServDO = (ServDO) subtaskDO.getDataSource();
        while (true) {
            if (subtaskDO.getState().equals(DictConsts.TASK_STATE_RUNNING)) {
                CmLoadbalancer cmLoadbalancer = CmApi.getLoadbalancer(proxysqlServDO.getRelateId());
                if (cmLoadbalancer != null) {
                    List<String> ips = cmLoadbalancer.getIps();
                    if (ips != null && ips.size() > 0) {
                        taskResult.setState(DictConsts.TASK_STATE_SUCCESS);
                        return taskResult;
                    }
                }

                Date nowDate = systemDAO.getCurrentSqlDateTime();
                if ((nowDate.getTime() - subtaskDO.getStartDateTime().getTime()) / 1000 > subtaskDO.getTimeout()) {
                    taskResult.setState(DictConsts.TASK_STATE_TIMEOUT);
                    taskResult.setMsg("timeout");
                    return taskResult;
                } else {
                    Thread.sleep(10000);
                    subtaskDO = subtaskDAO.get(subtaskDO.getId());
                    if (subtaskDO == null) {
                        taskResult.setState(DictConsts.TASK_STATE_FAILED);
                        taskResult.setMsg("none object");
                        return taskResult;
                    }
                }
            } else {
                taskResult.setState(subtaskDO.getState());
                taskResult.setMsg(subtaskDO.getMsg());
                return taskResult;
            }
        }
    }

    public Result listDBSchema(String servGroupId) throws Exception {
        ServGroupDO servGroupDO = servGroupDAO.get(servGroupId);
        if (servGroupDO != null) {
            List<ServDO> servDOs = servGroupDO.getServs();
            ServDO mysqlServDO = findAnyServDOType(servDOs, Consts.SERV_TYPE_MYSQL);
            if (mysqlServDO != null) {
                return mysqlServService.listDBSchema(mysqlServDO);
            }
        }
        return Result.success();
    }

    public Result getDBSchema(String servGroupId, String dbSchemaName) throws Exception {
        ServGroupDO servGroupDO = servGroupDAO.get(servGroupId);
        if (servGroupDO != null) {
            List<ServDO> servDOs = servGroupDO.getServs();
            ServDO mysqlServDO = findAnyServDOType(servDOs, Consts.SERV_TYPE_MYSQL);
            if (mysqlServDO != null) {
                return mysqlServService.getDBSchema(mysqlServDO, dbSchemaName);
            }
        }
        return Result.success();
    }

    public Result saveDBSchema(String servGroupId, DBSchemaForm dbSchemaForm) throws Exception {
        ServGroupDO servGroupDO = servGroupDAO.get(servGroupId);

        CheckResult checkResult = servGroupCheck.checkSaveDBSchema(servGroupDO, dbSchemaForm);
        if (checkResult.getCode() != CheckResult.SUCCESS) {
            logger.error(checkResult.getMsg());
            return Result.failure(checkResult);
        }

        ServDO mysqlServDO = findAnyServDOType(servGroupDO, Consts.SERV_TYPE_MYSQL);
        return mysqlServService.saveDBSchema(mysqlServDO, dbSchemaForm);
    }

    public Result removeDBSchema(String servGroupId, String dbSchemaName) throws Exception {
        ServGroupDO servGroupDO = servGroupDAO.get(servGroupId);

        CheckResult checkResult = servGroupCheck.checkRemoveDBSchema(servGroupDO, dbSchemaName);
        if (checkResult.getCode() != CheckResult.SUCCESS) {
            logger.error(checkResult.getMsg());
            return Result.failure(checkResult);
        }

        ServDO mysqlServDO = findAnyServDOType(servGroupDO, Consts.SERV_TYPE_MYSQL);
        return mysqlServService.removeDBSchema(mysqlServDO, dbSchemaName);
    }

    public Result listUser(String servGroupId) throws Exception {
        List<MysqlServGroupUserDTO> mysqlServGroupUserDTOs = new ArrayList<>();
        ServGroupDO servGroupDO = servGroupDAO.get(servGroupId);
        if (servGroupDO != null) {
            ServDO proxySqlServDO = findAnyServDOType(servGroupDO, Consts.SERV_TYPE_PROXYSQL);
            if (proxySqlServDO != null) {
                List<ProxyUserDTO> proxyUserDTOs = proxySqlServService.listProxyUser(proxySqlServDO);
                if (proxyUserDTOs != null) {
                    ServDO mysqlServDO = findAnyServDOType(servGroupDO, Consts.SERV_TYPE_MYSQL);
                    List<DBUserDTO> dbUserDTOs = mysqlServService.listDBUser(mysqlServDO);

                    for (ProxyUserDTO proxyUserDTO : proxyUserDTOs) {
                        MysqlServGroupUserDTO mysqlServGroupUserDTO = new MysqlServGroupUserDTO();
                        mysqlServGroupUserDTOs.add(mysqlServGroupUserDTO);
                        mysqlServGroupUserDTO.setUsername(proxyUserDTO.getUsername());
                        mysqlServGroupUserDTO.setMaxConnection(proxyUserDTO.getMaxConnection());
                        mysqlServGroupUserDTO.setProperties(proxyUserDTO.getProperties());

                        if (dbUserDTOs != null) {
                            for (DBUserDTO dbUserDTO : dbUserDTOs) {
                                if (dbUserDTO.getUsername().equals(proxyUserDTO.getUsername())) {
                                    mysqlServGroupUserDTO.setDbPrivileges(dbUserDTO.getDbPrivileges());
                                    break;
                                }
                            }
                        }
                    }
                }
            } else {
                ServDO mysqlServDO = findAnyServDOType(servGroupDO, Consts.SERV_TYPE_MYSQL);
                List<DBUserDTO> dbUserDTOs = mysqlServService.listDBUser(mysqlServDO);
                if (dbUserDTOs != null) {
                    for (DBUserDTO dbUserDTO : dbUserDTOs) {
                        MysqlServGroupUserDTO mysqlServGroupUserDTO = new MysqlServGroupUserDTO();
                        mysqlServGroupUserDTOs.add(mysqlServGroupUserDTO);
                        mysqlServGroupUserDTO.setUsername(dbUserDTO.getUsername());
                        mysqlServGroupUserDTO.setWhiteIp(dbUserDTO.getWhiteIp());
                        mysqlServGroupUserDTO.setDbPrivileges(dbUserDTO.getDbPrivileges());
                    }
                }
            }
        }
        return Result.success(mysqlServGroupUserDTOs);
    }

    public Result getUser(String servGroupId, String username, String ip) throws Exception {
        MysqlServGroupUserDTO mysqlServGroupUserDTO = null;
        ServGroupDO servGroupDO = servGroupDAO.get(servGroupId);
        if (servGroupDO != null) {
            ServDO proxySqlServDO = findAnyServDOType(servGroupDO, Consts.SERV_TYPE_PROXYSQL);
            if (proxySqlServDO != null) {
                ProxyUserDTO proxyUserDTO = proxySqlServService.getProxyUserDTO(proxySqlServDO, username);
                if (proxyUserDTO != null) {
                    mysqlServGroupUserDTO = new MysqlServGroupUserDTO();
                    mysqlServGroupUserDTO.setUsername(username);
                    mysqlServGroupUserDTO.setMaxConnection(proxyUserDTO.getMaxConnection());
                    mysqlServGroupUserDTO.setProperties(proxyUserDTO.getProperties());

                    CmService cmService = CmApi.getService(proxySqlServDO.getRelateId());
                    CmService.Status cmStatus = cmService.getStatus();
                    if (cmStatus != null) {
                        List<CmService.Status.Unit> cmUnits = cmStatus.getUnits();
                        if (cmUnits != null && cmUnits.size() > 0) {
                            for (CmService.Status.Unit cmUnit : cmUnits) {
                                ip = cmUnit.getIp();
                                break;
                            }
                        }
                    }
                }
            }

            ServDO mysqlServDO = findAnyServDOType(servGroupDO, Consts.SERV_TYPE_MYSQL);
            DBUserDTO dbUserDTO = mysqlServService.getDBUser(mysqlServDO, username, ip);
            if (mysqlServGroupUserDTO == null) {
                mysqlServGroupUserDTO = new MysqlServGroupUserDTO();
            }
            if (dbUserDTO != null) {
                mysqlServGroupUserDTO.setUsername(username);
                mysqlServGroupUserDTO.setWhiteIp(ip);
                mysqlServGroupUserDTO.setDbPrivileges(dbUserDTO.getDbPrivileges());
            }

        }
        return Result.success(mysqlServGroupUserDTO);
    }

    public Result saveUser(String servGroupId, CmhaServGroupUserForm userForm) throws Exception {
        ServGroupDO servGroupDO = servGroupDAO.get(servGroupId);

        CheckResult checkResult = servGroupCheck.checkSaveUser(servGroupDO, userForm);
        if (checkResult.getCode() != CheckResult.SUCCESS) {
            logger.error(checkResult.getMsg());
            return Result.failure(checkResult);
        }

        ServDO proxySqlServDO = findAnyServDOType(servGroupDO, Consts.SERV_TYPE_PROXYSQL);
        if (proxySqlServDO != null) {
            proxySqlServService.saveUser(proxySqlServDO, userForm);

            List<String> whiteIps = new ArrayList<>();
            CmService cmService = CmApi.getService(proxySqlServDO.getRelateId());
            CmService.Status cmStatus = cmService.getStatus();
            if (cmStatus != null) {
                List<CmService.Status.Unit> cmUnits = cmStatus.getUnits();
                if (cmUnits != null && cmUnits.size() > 0) {
                    for (CmService.Status.Unit cmUnit : cmUnits) {
                        whiteIps.add(cmUnit.getIp());
                    }
                }
            }
            userForm.setWhiteIps(whiteIps);
        }

        ServDO mysqlServDO = findAnyServDOType(servGroupDO, Consts.SERV_TYPE_MYSQL);
        mysqlServService.saveUser(mysqlServDO, userForm);

        return Result.success();
    }

    public Result updateUser(String servGroupId, String username, String whiteIp, CmhaServGroupUserForm userForm)
            throws Exception {
        ServGroupDO servGroupDO = servGroupDAO.get(servGroupId);

        CheckResult checkResult = servGroupCheck.checkUpdateUser(servGroupDO, username, whiteIp, userForm);
        if (checkResult.getCode() != CheckResult.SUCCESS) {
            logger.error(checkResult.getMsg());
            return Result.failure(checkResult);
        }

        List<String> whiteIps = new ArrayList<>();
        ServDO proxySqlServDO = findAnyServDOType(servGroupDO, Consts.SERV_TYPE_PROXYSQL);
        if (proxySqlServDO != null) {
            proxySqlServService.updateUser(proxySqlServDO, username, userForm);

            CmService cmService = CmApi.getService(proxySqlServDO.getRelateId());
            CmService.Status cmStatus = cmService.getStatus();
            if (cmStatus != null) {
                List<CmService.Status.Unit> cmUnits = cmStatus.getUnits();
                if (cmUnits != null && cmUnits.size() > 0) {
                    for (CmService.Status.Unit cmUnit : cmUnits) {
                        whiteIps.add(cmUnit.getIp());
                    }
                }
            }
        } else {
            whiteIps.add(whiteIp);
        }

        ServDO mysqlServDO = findAnyServDOType(servGroupDO, Consts.SERV_TYPE_MYSQL);
        mysqlServService.updateUser(mysqlServDO, username, whiteIps, userForm);

        return Result.success();
    }

    public Result resetUserPwd(String servGroupId, String username, String whiteIp, PwdForm pwdForm) throws Exception {
        ServGroupDO servGroupDO = servGroupDAO.get(servGroupId);

        CheckResult checkResult = servGroupCheck.checkResetUserPwd(servGroupDO, username, whiteIp);
        if (checkResult.getCode() != CheckResult.SUCCESS) {
            logger.error(checkResult.getMsg());
            return Result.failure(checkResult);
        }

        String newPwd = pwdForm.getNewPwd();
        List<String> whiteIps = new ArrayList<>();
        ServDO proxySqlServDO = findAnyServDOType(servGroupDO, Consts.SERV_TYPE_PROXYSQL);
        if (proxySqlServDO != null) {
            CmService cmService = CmApi.getService(proxySqlServDO.getRelateId());
            CmService.Status cmStatus = cmService.getStatus();
            if (cmStatus != null) {
                List<CmService.Status.Unit> cmUnits = cmStatus.getUnits();
                if (cmUnits != null && cmUnits.size() > 0) {
                    for (CmService.Status.Unit cmUnit : cmUnits) {
                        whiteIps.add(cmUnit.getIp());
                    }
                }
            }
        } else {
            whiteIps.add(whiteIp);
        }

        ServDO mysqlServDO = findAnyServDOType(servGroupDO, Consts.SERV_TYPE_MYSQL);
        mysqlServService.resetUserPwd(mysqlServDO, username, whiteIps, newPwd);

        if (proxySqlServDO != null) {
            proxySqlServService.resetUserPwd(proxySqlServDO, username, newPwd);
        }

        return Result.success();
    }

    public Result removeUser(String servGroupId, String username, String whiteIp) throws Exception {
        ServGroupDO servGroupDO = servGroupDAO.get(servGroupId);

        CheckResult checkResult = servGroupCheck.checkRemoveUser(servGroupDO, username, whiteIp);
        if (checkResult.getCode() != CheckResult.SUCCESS) {
            logger.error(checkResult.getMsg());
            return Result.failure(checkResult);
        }

        List<String> whiteIps = new ArrayList<>();
        ServDO proxySqlServDO = findAnyServDOType(servGroupDO, Consts.SERV_TYPE_PROXYSQL);
        if (proxySqlServDO != null) {
            CmService cmService = CmApi.getService(proxySqlServDO.getRelateId());
            CmService.Status cmStatus = cmService.getStatus();
            if (cmStatus != null) {
                List<CmService.Status.Unit> cmUnits = cmStatus.getUnits();
                if (cmUnits != null && cmUnits.size() > 0) {
                    for (CmService.Status.Unit cmUnit : cmUnits) {
                        whiteIps.add(cmUnit.getIp());
                    }
                }
            }
        } else {
            whiteIps.add(whiteIp);
        }

        ServDO mysqlServDO = findAnyServDOType(servGroupDO, Consts.SERV_TYPE_MYSQL);
        mysqlServService.removeUser(mysqlServDO, username, whiteIps);

        if (proxySqlServDO != null) {
            proxySqlServService.removeUser(proxySqlServDO, username);
        }

        return Result.success();
    }

}