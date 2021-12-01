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
import com.bsg.dbscale.cm.model.CmService;
import com.bsg.dbscale.cm.model.CmServiceArch;
import com.bsg.dbscale.cm.model.CmSite;
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
import com.bsg.dbscale.service.check.MysqlServGroupCheck;
import com.bsg.dbscale.service.constant.Consts;
import com.bsg.dbscale.service.constant.DictConsts;
import com.bsg.dbscale.service.constant.DictTypeConsts;
import com.bsg.dbscale.service.dto.ArchBaseDTO;
import com.bsg.dbscale.service.dto.DBUserDTO;
import com.bsg.dbscale.service.dto.DisplayDTO;
import com.bsg.dbscale.service.dto.MysqlServDTO;
import com.bsg.dbscale.service.dto.MysqlServGroupDTO;
import com.bsg.dbscale.service.dto.MysqlServGroupDetailDTO;
import com.bsg.dbscale.service.dto.MysqlServGroupUserDTO;
import com.bsg.dbscale.service.dto.ScaleBaseDTO;
import com.bsg.dbscale.service.dto.ServDTO;
import com.bsg.dbscale.service.dto.ServGroupDTO;
import com.bsg.dbscale.service.dto.VersionDTO;
import com.bsg.dbscale.service.form.BackupForm;
import com.bsg.dbscale.service.form.BackupStrategyForm;
import com.bsg.dbscale.service.form.DBSchemaForm;
import com.bsg.dbscale.service.form.MysqlServGroupUserForm;
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
public class MysqlServGroupService extends ServGroupService {

    @Autowired
    private MysqlServGroupCheck mysqlServGroupCheck;

    @Autowired
    private BackupStrategyService backupStrategyService;

    @Autowired
    private MysqlServService mysqlServService;

    public Result list(ServGroupQuery servGroupQuery, String activeUsername) throws Exception {
        List<MysqlServGroupDTO> mysqlServGroupDTOs = new ArrayList<>();
        servGroupQuery.setCategory(Consts.SERV_GROUP_TYPE_MYSQL);
        List<ServGroupDO> servGroupDOs = listQualifiedData(servGroupQuery, activeUsername);
        if (servGroupDOs.size() > 0) {
            List<CmSite> cmSites = CmApi.listSite(null);
            List<CmServiceArch> cmServiceArchs = CmApi.listServiceArch(null);
            List<DictTypeDO> dictTypeDOs = dictTypeDAO.list();
            List<UserDO> userDOs = userDAO.list(null);

            List<TaskDO> latestTaskDOs = taskDAO.listLatest(servGroupQuery.getSiteId(), DictConsts.OBJ_TYPE_SERV_GROUP);

            CmServiceQuery cmServiceQuery = new CmServiceQuery();
            cmServiceQuery.setSiteId(servGroupQuery.getSiteId());
            cmServiceQuery.setGroupType(Consts.SERV_GROUP_TYPE_MYSQL);
            List<CmService> cmServices = CmApi.listService(cmServiceQuery);
            for (ServGroupDO servGroupDO : servGroupDOs) {
                List<CmService> eligibleCmServices = CmApi.findServiceByGroupName(cmServices, servGroupDO.getName());
                String state = getState(servGroupDO, eligibleCmServices);
                if (servGroupQuery.getState() == null || StringUtils.equals(servGroupQuery.getState(), state)) {
                    MysqlServGroupDTO servGroupDTO = new MysqlServGroupDTO();
                    mysqlServGroupDTOs.add(servGroupDTO);

                    CmSite cmSite = null;
                    BusinessAreaDO businessAreaDO = servGroupDO.getBusinessArea();
                    if (businessAreaDO != null) {
                        cmSite = CmApi.findSite(cmSites, businessAreaDO.getSiteId());
                    }

                    UserDO owner = findUserDO(userDOs, servGroupDO.getOwner());
                    TaskDO latestTaskDO = findLasterTaskDO(latestTaskDOs, DictConsts.OBJ_TYPE_SERV_GROUP,
                            servGroupDO.getId());
                    setServGroupDTO(servGroupDTO, servGroupDO, cmSite, eligibleCmServices, cmServiceArchs, dictTypeDOs,
                            owner, latestTaskDO);
                }
            }
        }
        return Result.success(mysqlServGroupDTOs);
    }

    public Result get(String servGroupId, boolean replication) throws Exception {
        MysqlServGroupDetailDTO servGroupDetailDTO = null;
        ServGroupDO servGroupDO = servGroupDAO.get(servGroupId);
        if (servGroupDO == null || !servGroupDO.getCategory().equals(Consts.SERV_GROUP_TYPE_MYSQL)) {
            return Result.success(servGroupDetailDTO);
        }
        servGroupDetailDTO = new MysqlServGroupDetailDTO();
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
            default:
                break;
            }
        }

        return Result.success(servGroupDetailDTO);
    }

    private void setServGroupDTO(MysqlServGroupDTO servGroupDTO, ServGroupDO servGroupDO, CmSite cmSite,
            List<CmService> cmServices, List<CmServiceArch> cmServiceArchs, List<DictTypeDO> dictTypeDOs, UserDO owner,
            TaskDO latestTaskDO) {
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

        String addressesServType = Consts.SERV_TYPE_MYSQL;
        for (ServDO servDO : servDOs) {
            if (servDO.getType().equals(addressesServType)) {
                CmService cmService = CmApi.findService(cmServices, servDO.getRelateId());
                if (cmService != null) {
                    List<String> addresses = new ArrayList<>();
                    servGroupDTO.setAddresses(addresses);
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

    @Transactional(rollbackFor = Exception.class)
    public Result start(String servGroupId, String activeUsername) throws Exception {
        ServGroupDO servGroupDO = servGroupDAO.get(servGroupId);

        CheckResult checkResult = mysqlServGroupCheck.checkStart(servGroupDO);
        if (checkResult.getCode() != CheckResult.SUCCESS) {
            logger.error(checkResult.getMsg());
            return Result.failure(checkResult);
        }

        return super.start(servGroupDO, activeUsername);
    }

    @Transactional(rollbackFor = Exception.class)
    public Result stop(String servGroupId, String activeUsername) throws Exception {
        ServGroupDO servGroupDO = servGroupDAO.get(servGroupId);

        CheckResult checkResult = mysqlServGroupCheck.checkStop(servGroupDO);
        if (checkResult.getCode() != CheckResult.SUCCESS) {
            logger.error(checkResult.getMsg());
            return Result.failure(checkResult);
        }

        return super.stop(servGroupDO, activeUsername);
    }

    @Transactional(rollbackFor = Exception.class)
    public Result imageUpdate(String servGroupId, ServImageForm imageForm, String activeUsername) throws Exception {
        ServGroupDO servGroupDO = servGroupDAO.get(servGroupId);

        CheckResult checkResult = mysqlServGroupCheck.checkImageUpdate(servGroupDO, imageForm);
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

        CheckResult checkResult = mysqlServGroupCheck.checkScaleUpCpuMem(servGroupDO, scaleCpuMemForm);
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

        CheckResult checkResult = mysqlServGroupCheck.checkScaleUpStorage(servGroupDO, scaleStorageForm);
        if (checkResult.getCode() != CheckResult.SUCCESS) {
            logger.error(checkResult.getMsg());
            return Result.failure(checkResult);
        }

        return super.scaleUpStorage(servGroupDO, scaleStorageForm, activeUsername);
    }

    @Transactional(rollbackFor = Exception.class)
    public Result archUp(String servGroupId, ServArchUpForm archUpForm, String activeUsername) throws Exception {
        ServGroupDO servGroupDO = servGroupDAO.get(servGroupId);

        CheckResult checkResult = mysqlServGroupCheck.checkArchUp(servGroupDO, archUpForm);
        if (checkResult.getCode() != CheckResult.SUCCESS) {
            logger.error(checkResult.getMsg());
            return Result.failure(checkResult);
        }

        return super.archUp(servGroupDO, archUpForm, activeUsername);
    }

    @Transactional(rollbackFor = Exception.class)
    public Result backup(String servGroupId, BackupForm backupForm, String activeUsername) throws Exception {
        ServGroupDO servGroupDO = servGroupDAO.get(servGroupId);

        CheckResult checkResult = mysqlServGroupCheck.checkBackup(servGroupDO);
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

        CheckResult checkResult = mysqlServGroupCheck.checkRemove(servGroupDO);
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

        OrderGroupDO orderGroupDO = orderGroupDAO.get(servGroupDO.getOrderGroupId());
        List<OrderDO> orderDOs = orderGroupDO.getOrders();
        for (OrderDO orderDO : orderDOs) {
            if (orderDO.getMajorVersion() != null) {
                List<SubtaskDO> subtaskDOs = new ArrayList<>();
                taskDO.setSubtasks(subtaskDOs);

                int priority = 1;
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

        OrderGroupDO orderGroupDO = orderGroupDAO.get(servGroupDO.getOrderGroupId());
        List<OrderDO> orderDOs = orderGroupDO.getOrders();
        for (OrderDO orderDO : orderDOs) {
            if (orderDO.getCpuCnt() != null) {
                List<SubtaskDO> subtaskDOs = new ArrayList<>();
                taskDO.setSubtasks(subtaskDOs);

                int priority = 1;

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

                break;
            }
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
                    }
                }
                CmServicesLinkBody cmServicesLinkBody = buildCmServicesLinkRequestBody(relateId, servGroupDO);
                CmApi.linkServices(relateId, cmServicesLinkBody, subtaskDO.getTimeout().intValue());

                taskResult.setState(DictConsts.TASK_STATE_SUCCESS);
            } catch (Exception e) {
                taskResult.setState(DictConsts.TASK_STATE_FAILED);
                taskResult.setMsg(e.toString());
                logger.error("LINK异常：", e);
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

    public CmServicesLinkBody buildCmServicesLinkRequestBody(String relateId, ServGroupDO servGroupDO)
            throws Exception {
        CmServicesLinkBody cmServicesLinkBody = new CmServicesLinkBody();
        cmServicesLinkBody.setServiceGroupType(servGroupDO.getCategory());

        List<String> relateIds = new ArrayList<>();
        relateIds.add(relateId);
        cmServicesLinkBody.setSerevies(relateIds);
        return cmServicesLinkBody;
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

        CheckResult checkResult = mysqlServGroupCheck.checkSaveDBSchema(servGroupDO, dbSchemaForm);
        if (checkResult.getCode() != CheckResult.SUCCESS) {
            logger.error(checkResult.getMsg());
            return Result.failure(checkResult);
        }

        ServDO mysqlServDO = findAnyServDOType(servGroupDO, Consts.SERV_TYPE_MYSQL);
        return mysqlServService.saveDBSchema(mysqlServDO, dbSchemaForm);
    }

    public Result removeDBSchema(String servGroupId, String dbSchemaName) throws Exception {
        ServGroupDO servGroupDO = servGroupDAO.get(servGroupId);

        CheckResult checkResult = mysqlServGroupCheck.checkRemoveDBSchema(servGroupDO, dbSchemaName);
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
        return Result.success(mysqlServGroupUserDTOs);
    }

    public Result getUser(String servGroupId, String username, String ip) throws Exception {
        MysqlServGroupUserDTO mysqlServGroupUserDTO = null;
        ServGroupDO servGroupDO = servGroupDAO.get(servGroupId);
        if (servGroupDO != null) {
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

    public Result saveUser(String servGroupId, MysqlServGroupUserForm mysqlServGroupUserForm) throws Exception {
        ServGroupDO servGroupDO = servGroupDAO.get(servGroupId);

        CheckResult checkResult = mysqlServGroupCheck.checkSaveUser(servGroupDO, mysqlServGroupUserForm);
        if (checkResult.getCode() != CheckResult.SUCCESS) {
            logger.error(checkResult.getMsg());
            return Result.failure(checkResult);
        }

        ServDO mysqlServDO = findAnyServDOType(servGroupDO, Consts.SERV_TYPE_MYSQL);
        mysqlServService.saveUser(mysqlServDO, mysqlServGroupUserForm);

        return Result.success();
    }

    public Result updateUser(String servGroupId, String username, String whiteIp,
            MysqlServGroupUserForm mysqlServGroupUserForm) throws Exception {
        ServGroupDO servGroupDO = servGroupDAO.get(servGroupId);

        CheckResult checkResult = mysqlServGroupCheck.checkUpdateUser(servGroupDO, username, whiteIp,
                mysqlServGroupUserForm);
        if (checkResult.getCode() != CheckResult.SUCCESS) {
            logger.error(checkResult.getMsg());
            return Result.failure(checkResult);
        }

        List<String> whiteIps = new ArrayList<>();
        whiteIps.add(whiteIp);

        ServDO mysqlServDO = findAnyServDOType(servGroupDO, Consts.SERV_TYPE_MYSQL);
        mysqlServService.updateUser(mysqlServDO, username, whiteIps, mysqlServGroupUserForm);

        return Result.success();
    }

    public Result resetUserPwd(String servGroupId, String username, String whiteIp, PwdForm pwdForm) throws Exception {
        ServGroupDO servGroupDO = servGroupDAO.get(servGroupId);

        CheckResult checkResult = mysqlServGroupCheck.checkResetUserPwd(servGroupDO, username, whiteIp);
        if (checkResult.getCode() != CheckResult.SUCCESS) {
            logger.error(checkResult.getMsg());
            return Result.failure(checkResult);
        }

        String newPwd = pwdForm.getNewPwd();
        List<String> whiteIps = new ArrayList<>();
        whiteIps.add(whiteIp);

        ServDO mysqlServDO = findAnyServDOType(servGroupDO, Consts.SERV_TYPE_MYSQL);
        mysqlServService.resetUserPwd(mysqlServDO, username, whiteIps, newPwd);

        return Result.success();
    }

    public Result removeUser(String servGroupId, String username, String whiteIp) throws Exception {
        ServGroupDO servGroupDO = servGroupDAO.get(servGroupId);

        CheckResult checkResult = mysqlServGroupCheck.checkRemoveUser(servGroupDO, username, whiteIp);
        if (checkResult.getCode() != CheckResult.SUCCESS) {
            logger.error(checkResult.getMsg());
            return Result.failure(checkResult);
        }

        List<String> whiteIps = new ArrayList<>();
        whiteIps.add(whiteIp);

        ServDO mysqlServDO = findAnyServDOType(servGroupDO, Consts.SERV_TYPE_MYSQL);
        mysqlServService.removeUser(mysqlServDO, username, whiteIps);

        return Result.success();
    }

}