package com.bsg.dbscale.service.service;

import java.net.InetAddress;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bsg.dbscale.cm.api.CmApi;
import com.bsg.dbscale.cm.body.CmHostBody;
import com.bsg.dbscale.cm.constant.CmConsts;
import com.bsg.dbscale.cm.exception.CallingInterfaceException;
import com.bsg.dbscale.cm.model.CmCluster;
import com.bsg.dbscale.cm.model.CmClusterBase;
import com.bsg.dbscale.cm.model.CmEvent;
import com.bsg.dbscale.cm.model.CmHost;
import com.bsg.dbscale.cm.model.CmHostEvent;
import com.bsg.dbscale.cm.model.CmHostUnit;
import com.bsg.dbscale.cm.model.CmRemoteStorage;
import com.bsg.dbscale.cm.model.CmSite;
import com.bsg.dbscale.cm.model.CmSiteBase;
import com.bsg.dbscale.cm.query.CmClusterQuery;
import com.bsg.dbscale.cm.query.CmHostQuery;
import com.bsg.dbscale.cm.query.CmRemoteStorageQuery;
import com.bsg.dbscale.cm.query.CmSiteQuery;
import com.bsg.dbscale.cm.response.CmSaveHostResp;
import com.bsg.dbscale.dao.domain.BusinessAreaDO;
import com.bsg.dbscale.dao.domain.DefServDO;
import com.bsg.dbscale.dao.domain.DictDO;
import com.bsg.dbscale.dao.domain.DictTypeDO;
import com.bsg.dbscale.dao.domain.HostDO;
import com.bsg.dbscale.dao.domain.ServDO;
import com.bsg.dbscale.dao.domain.ServGroupDO;
import com.bsg.dbscale.dao.domain.SubtaskCfgDO;
import com.bsg.dbscale.dao.domain.SubtaskDO;
import com.bsg.dbscale.dao.domain.TaskDO;
import com.bsg.dbscale.dao.domain.UnitDO;
import com.bsg.dbscale.dao.domain.UserDO;
import com.bsg.dbscale.service.check.CheckResult;
import com.bsg.dbscale.service.check.HostCheck;
import com.bsg.dbscale.service.constant.DictConsts;
import com.bsg.dbscale.service.constant.DictTypeConsts;
import com.bsg.dbscale.service.dto.DisplayDTO;
import com.bsg.dbscale.service.dto.EventDTO;
import com.bsg.dbscale.service.dto.HostDTO;
import com.bsg.dbscale.service.dto.HostEventDTO;
import com.bsg.dbscale.service.dto.HostStatisticsDTO;
import com.bsg.dbscale.service.dto.HostUnitDTO;
import com.bsg.dbscale.service.dto.IdentificationDTO;
import com.bsg.dbscale.service.dto.IdentificationStatusDTO;
import com.bsg.dbscale.service.dto.InfoDTO;
import com.bsg.dbscale.service.dto.ServGroupBaseDTO;
import com.bsg.dbscale.service.dto.TaskBaseDTO;
import com.bsg.dbscale.service.dto.VersionDTO;
import com.bsg.dbscale.service.form.HostForm;
import com.bsg.dbscale.service.form.VolumePathForm;
import com.bsg.dbscale.service.query.HostQuery;
import com.bsg.dbscale.service.util.PrimaryKeyUtil;
import com.bsg.dbscale.service.util.TaskResult;
import com.bsg.dbscale.util.DateUtils;
import com.bsg.dbscale.util.NumberUnits;
import com.bsg.dbscale.util.http.HttpRequestUtil;
import com.bsg.dbscale.util.http.HttpResp;

@Service
public class HostService extends BaseService {

    @Autowired
    private HostCheck hostCheck;

    @Autowired
    private ServGroupService servGroupService;

    public Result list(HostQuery hostQuery) throws Exception {
        List<HostDTO> hostDTOs = new ArrayList<>();
        com.bsg.dbscale.dao.query.HostQuery hostDAOQuery = new com.bsg.dbscale.dao.query.HostQuery();
        hostDAOQuery.setClusterId(hostQuery.getClusterId());
        List<HostDO> hostDOs = hostDAO.list(hostDAOQuery);

        if (hostDOs.size() > 0) {
            CmHostQuery cmHostQuery = new CmHostQuery();
            cmHostQuery.setSiteId(hostQuery.getSiteId());
            cmHostQuery.setClusterId(hostQuery.getClusterId());
            List<CmHost> cmHosts = CmApi.listHost(cmHostQuery);

            CmSiteQuery cmSiteQuery = new CmSiteQuery();
            cmSiteQuery.setId(hostQuery.getSiteId());
            List<CmSite> cmSites = CmApi.listSite(cmSiteQuery);

            CmClusterQuery cmClusterQuery = new CmClusterQuery();
            cmClusterQuery.setSiteId(hostQuery.getSiteId());
            List<CmCluster> cmClusters = CmApi.listCluster(cmClusterQuery);

            CmRemoteStorageQuery cmRemoteStorageQuery = new CmRemoteStorageQuery();
            cmRemoteStorageQuery.setId(hostQuery.getSiteId());
            List<CmRemoteStorage> cmRemoteStorages = CmApi.listRemoteStorage(cmRemoteStorageQuery);

            List<BusinessAreaDO> businessAreaDOs = businessAreaDAO.list(null);
            List<DictTypeDO> dictTypeDOs = dictTypeDAO.list();
            List<UserDO> userDOs = userDAO.list(null);

            List<TaskDO> latestTaskDOs = taskDAO.listLatest(hostQuery.getSiteId(), DictConsts.OBJ_TYPE_HOST);

            for (HostDO hostDO : hostDOs) {
                CmHost cmHost = CmApi.findHost(cmHosts, hostDO.getRelateId());
                CmRemoteStorage cmRemoteStorage = CmApi.findRemoteStorage(cmRemoteStorages,
                        hostDO.getRemoteStorageId());
                CmCluster cmCluster = CmApi.findCluster(cmClusters, hostDO.getClusterId());
                BusinessAreaDO businessAreaDO = null;
                CmSite cmSite = null;
                if (cmCluster != null) {
                    businessAreaDO = findBusinessAreaDO(businessAreaDOs, cmCluster.getZone());
                    cmSite = CmApi.findSite(cmSites, cmCluster.getSite().getId());
                }
                TaskDO latestTaskDO = findLasterTaskDO(latestTaskDOs, DictConsts.OBJ_TYPE_HOST, hostDO.getId());

                HostModel hostModel = new HostModel();
                hostModel.setHostDO(hostDO);
                hostModel.setCmHost(cmHost);
                hostModel.setCmRemoteStorage(cmRemoteStorage);
                hostModel.setCmCluster(cmCluster);
                hostModel.setCmsite(cmSite);
                hostModel.setBusinessAreaDO(businessAreaDO);
                hostModel.setDictTypeDOs(dictTypeDOs);
                hostModel.setLatestTaskDO(latestTaskDO);
                hostModel.setUserDOs(userDOs);
                HostDTO hostDTO = getShowDTO(hostModel);

                boolean match = true;
                if (StringUtils.isNotBlank(hostQuery.getSiteId())) {
                    IdentificationDTO siteDTO = hostDTO.getSite();
                    if (siteDTO == null || !hostQuery.getSiteId().equals(siteDTO.getId())) {
                        match = false;
                        continue;
                    }
                }

                if (hostQuery.getEnabled() != null && hostQuery.getEnabled() != hostDTO.getEnabled()) {
                    match = false;
                    continue;
                }

                if (DictConsts.DISK_TYPE_LOCAL_HDD.equals(hostQuery.getDiskType())
                        && StringUtils.isBlank(hostDO.getHddPath())
                        || DictConsts.DISK_TYPE_LOCAL_SSD.equals(hostQuery.getDiskType())
                                && StringUtils.isBlank(hostDO.getSsdPath())) {
                    match = false;
                    continue;
                }

                if (StringUtils.isNotBlank(hostQuery.getState())) {
                    DisplayDTO stateDisplayDTO = hostDTO.getState();
                    if (stateDisplayDTO == null || !hostQuery.getState().equals(stateDisplayDTO.getCode())) {
                        match = false;
                        continue;
                    }
                }

                if (match) {
                    hostDTOs.add(hostDTO);
                }
            }
        }

        return Result.success(hostDTOs);
    }

    public Result get(String hostId) throws Exception {
        HostDTO hostDTO = null;
        HostDO hostDO = hostDAO.getByIdOrIp(hostId);

        if (hostDO != null) {
            CmHost cmHost = null;
            if (StringUtils.isNotBlank(hostDO.getRelateId())) {
                CmHostQuery cmHostQuery = new CmHostQuery();
                cmHostQuery.setId(hostDO.getRelateId());
                List<CmHost> cmHosts = CmApi.listHost(cmHostQuery);
                if (cmHosts.size() == 1) {
                    cmHost = cmHosts.get(0);
                }
            }

            CmCluster cmCluster = CmApi.getCluster(hostDO.getClusterId());
            BusinessAreaDO businessAreaDO = null;
            if (cmCluster != null) {
                businessAreaDO = businessAreaDAO.get(cmCluster.getZone());
            }

            List<DictTypeDO> dictTypeDOs = dictTypeDAO.list();
            List<UserDO> userDOs = userDAO.list(null);

            TaskDO latestTaskDO = taskDAO.getLatest(DictConsts.OBJ_TYPE_HOST, hostDO.getId(), null);

            HostModel hostModel = new HostModel();
            hostModel.setHostDO(hostDO);
            hostModel.setCmHost(cmHost);
            hostModel.setCmCluster(cmCluster);
            if (StringUtils.isNoneBlank(hostDO.getRemoteStorageId())) {
                CmRemoteStorage cmRemoteStorage = CmApi.getRemoteStorage(hostDO.getRemoteStorageId());
                hostModel.setCmRemoteStorage(cmRemoteStorage);
            }
            hostModel.setBusinessAreaDO(businessAreaDO);
            hostModel.setDictTypeDOs(dictTypeDOs);
            hostModel.setLatestTaskDO(latestTaskDO);
            hostModel.setUserDOs(userDOs);

            hostDTO = getShowDTO(hostModel);
        }

        return Result.success(hostDTO);
    }

    public Result getEvent(String hostId) throws Exception {
        HostEventDTO hostEventDTO = null;
        HostDO hostDO = hostDAO.get(hostId);
        if (hostDO != null && StringUtils.isNotBlank(hostDO.getRelateId())) {
            CmCluster cmCluster = CmApi.getCluster(hostDO.getClusterId());
            if (cmCluster != null) {
                CmSiteBase cmSite = cmCluster.getSite();
                if (cmSite != null) {
                    CmHostEvent cmHostEvent = CmApi.getHostEvent(cmSite.getId(), hostDO.getRelateId());
                    if (cmHostEvent != null) {
                        hostEventDTO = new HostEventDTO();

                        List<CmEvent> eventHosts = cmHostEvent.getHosts();
                        if (eventHosts != null) {
                            List<EventDTO> eventHostDTOs = new ArrayList<>();
                            hostEventDTO.setHosts(eventHostDTOs);
                            for (CmEvent cmEvent : eventHosts) {
                                EventDTO eventDTO = new EventDTO();
                                eventHostDTOs.add(eventDTO);
                                setEventDTO(eventDTO, cmEvent);
                            }
                        }

                        List<CmEvent> eventNodes = cmHostEvent.getHosts();
                        if (eventNodes != null) {
                            List<EventDTO> eventNodeDTOs = new ArrayList<>();
                            hostEventDTO.setNodes(eventNodeDTOs);
                            for (CmEvent cmEvent : eventHosts) {
                                EventDTO eventDTO = new EventDTO();
                                eventNodeDTOs.add(eventDTO);
                                setEventDTO(eventDTO, cmEvent);
                            }
                        }
                    }
                }
            }
        }
        return Result.success(hostEventDTO);
    }

    public Result monitor(String hostId) throws Exception {
        HostDO hostDO = hostDAO.get(hostId);
        if (hostDO != null && StringUtils.isNotBlank(hostDO.getRelateId())) {
            CmCluster cmCluster = CmApi.getCluster(hostDO.getClusterId());
            if (cmCluster != null) {
                CmSiteBase cmSiteBase = cmCluster.getSite();
                if (cmSiteBase != null) {
                    CmSite cmSite = CmApi.getSite(cmSiteBase.getId());
                    CmSite.MonitorUiInfo cmMonitorUi = cmSite.getMonitorUi();
                    if (cmMonitorUi != null) {
                        String url = "{0}://{1}:{2}";
                        url = MessageFormat.format(url, cmMonitorUi.getScheme(),
                                InetAddress.getByName(cmMonitorUi.getServiceName()).getHostAddress(),
                                cmMonitorUi.getPort());
                        HttpResp httpResp = HttpRequestUtil.sendGetRequest(url + "/api/search");
                        if (httpResp.getStatusCode() != HttpStatus.SC_OK) {
                            throw new CallingInterfaceException("监控异常：" + httpResp.getResponseContent());
                        }
                        JSONArray jsonArray = JSONArray.parseArray(httpResp.getResponseContent());
                        for (int i = 0; i < jsonArray.size(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            if ("db/nodes".equals(jsonObject.getString("uri"))) {
                                String resultUrl = url + jsonObject.getString("url") + "?var-instance=" + hostDO.getIp()
                                        + ":9100&kiosk";
                                return Result.success(resultUrl);
                            }
                        }
                    }
                }
            }
        }
        return Result.success();
    }

    public Result listUnit(String hostId) throws Exception {
        List<HostUnitDTO> hostUnitDTOs = new ArrayList<>();
        HostDO hostDO = hostDAO.get(hostId);
        if (hostDO != null && StringUtils.isNotBlank(hostDO.getRelateId())) {
            List<CmHostUnit> cmHostUnits = CmApi.listHostUnit(hostDO.getRelateId());
            if (cmHostUnits != null && cmHostUnits.size() > 0) {
                List<ServGroupDO> servGroupDOs = servGroupDAO.list(null);
                List<DefServDO> defServDOs = defServDAO.list(null);
                List<DictTypeDO> dictTypeDOs = dictTypeDAO.list();
                List<UserDO> userDOs = userDAO.list(null);
                List<TaskDO> latestTaskDOs = taskDAO.listLatest(null, DictConsts.OBJ_TYPE_UNIT);
                CmCluster cmCluster = CmApi.getCluster(hostDO.getClusterId());
                for (CmHostUnit cmHostUnit : cmHostUnits) {
                    HostUnitDTO hostUnitDTO = new HostUnitDTO();
                    hostUnitDTOs.add(hostUnitDTO);

                    boolean isBreak = false;
                    for (ServGroupDO servGroupDO : servGroupDOs) {
                        List<ServDO> servDOs = servGroupDO.getServs();
                        for (ServDO servDO : servDOs) {
                            List<UnitDO> unitDOs = servDO.getUnits();
                            for (UnitDO unitDO : unitDOs) {
                                if (cmHostUnit.getId().equals(unitDO.getRelateId())) {
                                    hostUnitDTO.setId(unitDO.getId());

                                    DisplayDTO typeDisplayDTO = new DisplayDTO();
                                    hostUnitDTO.setType(typeDisplayDTO);
                                    typeDisplayDTO.setCode(unitDO.getType());
                                    DefServDO defServDO = findDefServDO(defServDOs, unitDO.getType());
                                    if (defServDO != null) {
                                        typeDisplayDTO.setDisplay(defServDO.getName());
                                    }

                                    hostUnitDTO.setRelateId(unitDO.getRelateId());

                                    TaskDO unitLatestTaskDO = findLasterTaskDO(latestTaskDOs, DictConsts.OBJ_TYPE_UNIT,
                                            unitDO.getId());
                                    if (unitLatestTaskDO != null) {
                                        TaskBaseDTO taskDTO = new TaskBaseDTO();
                                        hostUnitDTO.setTask(taskDTO);

                                        taskDTO.setId(unitLatestTaskDO.getId());
                                        DisplayDTO actionDisplayDTO = new DisplayDTO();
                                        taskDTO.setAction(actionDisplayDTO);
                                        actionDisplayDTO.setCode(unitLatestTaskDO.getActionType());
                                        DictDO actionDictDO = findDictDO(dictTypeDOs, DictTypeConsts.ACTION_TYPE,
                                                unitLatestTaskDO.getActionType());
                                        if (actionDictDO != null) {
                                            actionDisplayDTO.setDisplay(actionDictDO.getName());
                                        }

                                        DisplayDTO taskStateDisplayDTO = new DisplayDTO();
                                        taskDTO.setState(taskStateDisplayDTO);
                                        taskStateDisplayDTO.setCode(unitLatestTaskDO.getState());
                                        DictDO taskStateDictDO = findDictDO(dictTypeDOs, DictTypeConsts.TASK_STATE,
                                                unitLatestTaskDO.getState());
                                        if (taskStateDictDO != null) {
                                            taskStateDisplayDTO.setDisplay(taskStateDictDO.getName());

                                        }
                                    }

                                    hostUnitDTO.setIp(cmHostUnit.getIp());
                                    hostUnitDTO.setPort(servDO.getPort());

                                    DisplayDTO stateDisplayDTO = new DisplayDTO();
                                    hostUnitDTO.setState(stateDisplayDTO);
                                    stateDisplayDTO.setCode(cmHostUnit.getState());
                                    DictDO stateDictDO = findDictDO(dictTypeDOs, DictTypeConsts.STATE,
                                            cmHostUnit.getState());
                                    if (stateDictDO != null) {
                                        stateDisplayDTO.setDisplay(stateDictDO.getName());
                                    }

                                    DisplayDTO podStateDisplayDTO = new DisplayDTO();
                                    hostUnitDTO.setPodState(podStateDisplayDTO);
                                    podStateDisplayDTO.setCode(cmHostUnit.getPodState());
                                    DictDO podStateDictDO = findDictDO(dictTypeDOs, DictTypeConsts.POD_STATE,
                                            cmHostUnit.getPodState());
                                    if (podStateDictDO != null) {
                                        podStateDisplayDTO.setDisplay(podStateDictDO.getName());
                                    }

                                    CmHostUnit.Node cmNode = cmHostUnit.getNode();
                                    if (cmNode != null) {
                                        HostUnitDTO.Host hostDTO = hostUnitDTO.new Host();
                                        hostDTO.setIp(cmNode.getHostIp());
                                        hostDTO.setName(cmNode.getName());
                                        hostUnitDTO.setHost(hostDTO);

                                        CmClusterBase cmClusterBase = cmNode.getCluster();
                                        if (cmCluster != null) {
                                            IdentificationDTO cluterDTO = new IdentificationDTO();
                                            hostDTO.setCluster(cluterDTO);
                                            cluterDTO.setId(cmClusterBase.getId());
                                            cluterDTO.setName(cmClusterBase.getName());
                                        }
                                    }

                                    CmHostUnit.Image cmImage = cmHostUnit.getImage();
                                    if (cmImage != null) {
                                        VersionDTO versionDTO = new VersionDTO();
                                        versionDTO.setMajor(cmImage.getMajor());
                                        versionDTO.setMinor(cmImage.getMinor());
                                        versionDTO.setPatch(cmImage.getPatch());
                                        versionDTO.setBuild(cmImage.getBuild());
                                        hostUnitDTO.setVersion(versionDTO);
                                    }

                                    CmHostUnit.Resources cmResources = cmHostUnit.getResources();
                                    if (cmResources != null) {
                                        double cpuCnt = 0;
                                        if (cmResources.getMilicpu() != null) {
                                            cpuCnt = NumberUnits.retainDigits(cmResources.getMilicpu() / 1000.0);
                                        }
                                        hostUnitDTO.setCpuCnt(cpuCnt);

                                        double memSize = 0;
                                        if (cmResources.getMemory() != null) {
                                            memSize = NumberUnits.retainDigits(cmResources.getMemory() / 1024.0);
                                        }
                                        hostUnitDTO.setMemSize(memSize);

                                        CmHostUnit.Resources.Storage cmStorage = cmResources.getStorage();
                                        if (cmStorage != null) {
                                            String diskType = null;
                                            if (CmConsts.STORAGE_TYPE_VOLUMEPATH.equals(cmStorage.getMode())) {
                                                CmHostUnit.Resources.Storage.VolumePath volumePath = cmStorage
                                                        .getVolumePath();
                                                if (volumePath.getType().equals(CmConsts.STORAGE_LOCAL) && volumePath
                                                        .getPerformance().equals(CmConsts.PERFORMANCE_MEDIUM)) {
                                                    diskType = DictConsts.DISK_TYPE_LOCAL_HDD;
                                                } else if (volumePath.getType().equals(CmConsts.STORAGE_LOCAL)
                                                        && volumePath.getPerformance()
                                                                .equals(CmConsts.PERFORMANCE_HIGH)) {
                                                    diskType = DictConsts.DISK_TYPE_LOCAL_SSD;
                                                }
                                            }

                                            if (diskType != null) {
                                                DisplayDTO diskTypeDisplayDTO = new DisplayDTO();
                                                hostUnitDTO.setDiskType(diskTypeDisplayDTO);
                                                diskTypeDisplayDTO.setCode(diskType);
                                                DictDO diskTypeDictDO = findDictDO(dictTypeDOs,
                                                        DictTypeConsts.DISK_TYPE, diskType);
                                                if (diskTypeDictDO != null) {
                                                    diskTypeDisplayDTO.setDisplay(diskTypeDictDO.getName());
                                                }
                                            }

                                            List<CmHostUnit.Resources.Storage.Volume> cmVolumes = cmStorage
                                                    .getVolumes();

                                            Long dataSize = 0L;
                                            Long logSize = 0L;
                                            for (CmHostUnit.Resources.Storage.Volume cmVolume : cmVolumes) {
                                                if (cmVolume.getCapacity() != null) {
                                                    if (cmVolume.getType().equals(CmConsts.VOLUME_DATA)) {
                                                        dataSize += cmVolume.getCapacity();
                                                    } else if (cmVolume.getType().equals(CmConsts.VOLUME_LOG)) {
                                                        logSize += cmVolume.getCapacity();
                                                    }
                                                }
                                            }

                                            hostUnitDTO.setDataSize(NumberUnits.ceil(dataSize / 1024.0));
                                            hostUnitDTO.setLogSize(NumberUnits.ceil(logSize / 1024.0));
                                        }
                                    }

                                    ServGroupBaseDTO servGroupBaseDTO = new ServGroupBaseDTO();
                                    hostUnitDTO.setServGroup(servGroupBaseDTO);
                                    UserDO owner = findUserDO(userDOs, servGroupDO.getOwner());
                                    CmSiteBase cmSiteBase = cmCluster.getSite();
                                    servGroupService.setServGroupBaseDTO(servGroupBaseDTO, servGroupDO, cmSiteBase,
                                            dictTypeDOs, owner);
                                    isBreak = true;
                                    break;
                                }
                            }
                            if (isBreak) {
                                break;
                            }
                        }
                        if (isBreak) {
                            break;
                        }
                    }
                }
            }
        }
        return Result.success(hostUnitDTOs);
    }

    private void setEventDTO(EventDTO eventDTO, CmEvent cmEvent) {
        eventDTO.setType(cmEvent.getType());
        eventDTO.setInterval(cmEvent.getInterval());
        eventDTO.setMessage(cmEvent.getMessage());
        eventDTO.setReason(cmEvent.getReason());
        eventDTO.setSource(cmEvent.getSource());
    }

    @Transactional(rollbackFor = Exception.class)
    public Result save(HostForm hostForm, String activeUsername) throws Exception {
        CheckResult checkResult = hostCheck.checkSave(hostForm);
        if (checkResult.getCode() != CheckResult.SUCCESS) {
            logger.error(checkResult.getMsg());
            return Result.failure(checkResult);
        }

        Date date = systemDAO.getCurrentSqlDateTime();
        HostDO hostDO = buildHostDOForSave(hostForm, activeUsername, date);

        hostDAO.save(hostDO);

        return Result.success();
    }

    @Transactional(rollbackFor = Exception.class)
    public Result update(String hostId, HostForm hostForm) throws Exception {
        CheckResult checkResult = hostCheck.checkUpdate(hostId, hostForm);
        if (checkResult.getCode() != CheckResult.SUCCESS) {
            logger.error(checkResult.getMsg());
            return Result.failure(checkResult);
        }
        HostDO hostDO = hostDAO.get(hostId);

        if (StringUtils.isNotBlank(hostDO.getRelateId())) {
            CmHostBody hostBody = buildCmHosUpdateBody(hostForm);
            CmApi.updateHost(hostDO.getRelateId(), hostBody);
        }
        hostDO = buildHostDOForUpdate(hostDO, hostForm);
        hostDAO.update(hostDO);

        return Result.success();
    }

    @Transactional(rollbackFor = Exception.class)
    public Result in(String hostId, String activeUsername) throws Exception {
        CheckResult checkResult = hostCheck.checkIn(hostId);
        if (checkResult.getCode() != CheckResult.SUCCESS) {
            logger.error(checkResult.getMsg());
            return Result.failure(checkResult);
        }
        HostDO hostDO = hostDAO.get(hostId);
        Date nowDate = systemDAO.getCurrentSqlDateTime();

        TaskDO taskDO = buildInTask(hostDO, activeUsername, nowDate);
        List<SubtaskDO> subtaskDOs = taskDO.getSubtasks();
        for (SubtaskDO subtaskDO : subtaskDOs) {
            subtaskDAO.save(subtaskDO);
        }
        taskDAO.save(taskDO);

        executor.execute(new Task(taskDO));
        return Result.success();
    }

    @Transactional(rollbackFor = Exception.class)
    public Result out(String hostId, String activeUsername) throws Exception {
        CheckResult checkResult = hostCheck.checkOut(hostId);
        if (checkResult.getCode() != CheckResult.SUCCESS) {
            logger.error(checkResult.getMsg());
            return Result.failure(checkResult);
        }
        HostDO hostDO = hostDAO.get(hostId);
        Date nowDate = systemDAO.getCurrentSqlDateTime();
        TaskDO taskDO = buildOutTask(hostDO, activeUsername, nowDate);
        List<SubtaskDO> subtaskDOs = taskDO.getSubtasks();
        for (SubtaskDO subtaskDO : subtaskDOs) {
            subtaskDAO.save(subtaskDO);
        }
        taskDAO.save(taskDO);

        executor.execute(new Task(taskDO));
        return Result.success();
    }

    public Result enabled(String hostId, boolean enabled) throws Exception {
        CheckResult checkResult = hostCheck.checkEnabled(hostId, enabled);
        if (checkResult.getCode() != CheckResult.SUCCESS) {
            logger.error(checkResult.getMsg());
            return Result.failure(checkResult);
        }
        HostDO hostDO = hostDAO.get(hostId);
        if (enabled) {
            CmApi.enabledHost(hostDO.getRelateId());
        } else {
            CmApi.disabledHost(hostDO.getRelateId());
        }

        return Result.success();
    }

    @Transactional(rollbackFor = Exception.class)
    public Result remove(String hostId) throws Exception {
        CheckResult checkResult = hostCheck.checkRemove(hostId);
        if (checkResult.getCode() != CheckResult.SUCCESS) {
            logger.error(checkResult.getMsg());
            return Result.failure(checkResult);
        }
        hostDAO.remove(hostId);
        taskDAO.removeCascadeByObjTypeAndObjId(DictConsts.OBJ_TYPE_HOST, hostId);

        return Result.success();
    }

    private HostDTO getShowDTO(HostModel hostModel) {
        HostDTO hostDTO = new HostDTO();
        HostDO hostDO = hostModel.getHostDO();
        hostDTO.setId(hostDO.getId());
        hostDTO.setIp(hostDO.getIp());
        hostDTO.setRoom(hostDO.getRoom());
        hostDTO.setSeat(hostDO.getSeat());

        List<DictTypeDO> dictTypeDOs = hostModel.getDictTypeDOs();
        if (StringUtils.isNoneBlank(hostDO.getRole())) {
            DisplayDTO roleDisplayDTO = new DisplayDTO();
            hostDTO.setRole(roleDisplayDTO);
            roleDisplayDTO.setCode(hostDO.getRole());
            DictDO roleDictDO = findDictDO(dictTypeDOs, DictTypeConsts.HOST_ROLE, hostDO.getRole());
            if (roleDictDO != null) {
                roleDisplayDTO.setDisplay(roleDictDO.getName());
            }
        }

        hostDTO.setDescription(hostDO.getDescription());
        hostDTO.setRelateId(hostDO.getRelateId());
        if (StringUtils.isBlank(hostDO.getRelateId())) {
            hostDTO.setEnabled(false);
        }

        HostDTO.Statistics unit = hostDTO.new Statistics();
        hostDTO.setUnit(unit);
        hostDTO.setMaxUsage(hostDO.getMaxUsage());

        HostStatisticsDTO<Double> cpu = new HostStatisticsDTO<Double>();
        hostDTO.setCpu(cpu);

        HostStatisticsDTO<Double> mem = new HostStatisticsDTO<Double>();
        hostDTO.setMem(mem);

        HostDTO.Storage hdd = hostDTO.new Storage();
        if (StringUtils.isNotBlank(hostDO.getHddPath())) {
            hostDTO.setHdd(hdd);
            hdd.setPaths((Arrays.asList(StringUtils.split(hostDO.getHddPath(), ","))));
        }

        HostDTO.Storage ssd = hostDTO.new Storage();
        if (StringUtils.isNotBlank(hostDO.getSsdPath())) {
            hostDTO.setSsd(ssd);
            ssd.setPaths((Arrays.asList(StringUtils.split(hostDO.getSsdPath(), ","))));
        }

        CmRemoteStorage cmRemoteStorage = hostModel.getCmRemoteStorage();
        if (cmRemoteStorage != null) {
            IdentificationDTO remoteStorageDTO = new IdentificationDTO();
            hostDTO.setRemoteStorage(remoteStorageDTO);
            remoteStorageDTO.setId(cmRemoteStorage.getId());
            remoteStorageDTO.setName(cmRemoteStorage.getName());
        }

        CmCluster cmCluster = hostModel.getCmCluster();
        if (cmCluster != null) {
            IdentificationStatusDTO clusterDTO = new IdentificationStatusDTO();
            hostDTO.setCluster(clusterDTO);

            clusterDTO.setId(cmCluster.getId());
            clusterDTO.setName(cmCluster.getName());
            clusterDTO.setEnabled(BooleanUtils.negate(cmCluster.getUnschedulable()));

            IdentificationDTO siteDTO = new IdentificationDTO();
            hostDTO.setSite(siteDTO);

            siteDTO.setId(cmCluster.getSite().getId());
            siteDTO.setName(cmCluster.getSite().getName());
        }

        BusinessAreaDO businessAreaDO = hostModel.getBusinessAreaDO();
        if (businessAreaDO != null) {
            IdentificationStatusDTO businessAreaDTO = new IdentificationStatusDTO();
            hostDTO.setBusinessArea(businessAreaDTO);

            businessAreaDTO.setId(businessAreaDO.getId());
            businessAreaDTO.setName(businessAreaDO.getName());
            businessAreaDTO.setEnabled(businessAreaDO.getEnabled());
        }

        List<UserDO> userDOs = hostModel.getUserDOs();
        InfoDTO createdDTO = new InfoDTO();
        hostDTO.setCreated(createdDTO);
        createdDTO.setUsername(hostDO.getCreator());
        UserDO createdUserDO = findUserDO(userDOs, hostDO.getCreator());
        if (createdUserDO != null) {
            createdDTO.setName(createdUserDO.getName());
        }
        createdDTO.setTimestamp(DateUtils.dateTimeToString(hostDO.getGmtCreate()));

        TaskDO latestTaskDO = hostModel.getLatestTaskDO();
        if (latestTaskDO != null) {
            TaskBaseDTO taskDTO = new TaskBaseDTO();
            hostDTO.setTask(taskDTO);

            taskDTO.setId(latestTaskDO.getId());
            DisplayDTO actionDisplayDTO = new DisplayDTO();
            taskDTO.setAction(actionDisplayDTO);
            actionDisplayDTO.setCode(latestTaskDO.getActionType());
            DictDO actionDictDO = findDictDO(dictTypeDOs, DictTypeConsts.ACTION_TYPE, latestTaskDO.getActionType());
            if (actionDictDO != null) {
                actionDisplayDTO.setDisplay(actionDictDO.getName());
            }

            DisplayDTO taskStateDisplayDTO = new DisplayDTO();
            taskDTO.setState(taskStateDisplayDTO);
            taskStateDisplayDTO.setCode(latestTaskDO.getState());
            DictDO taskStateDictDO = findDictDO(dictTypeDOs, DictTypeConsts.TASK_STATE, latestTaskDO.getState());
            if (taskStateDictDO != null) {
                taskStateDisplayDTO.setDisplay(taskStateDictDO.getName());
            }
        }

        CmHost cmHost = hostModel.getCmHost();
        if (cmHost != null) {
            hostDTO.setName(cmHost.getName());
            String nodeState = getNodeState(cmHost);
            DisplayDTO nodeStateDisplayDTO = new DisplayDTO();
            hostDTO.setNodeState(nodeStateDisplayDTO);
            nodeStateDisplayDTO.setCode(nodeState);
            DictDO nodeStateDictDO = findDictDO(dictTypeDOs, DictTypeConsts.STATE, nodeState);
            if (nodeStateDictDO != null) {
                nodeStateDisplayDTO.setDisplay(nodeStateDictDO.getName());
            }

            CmSite cmSite = hostModel.getCmsite();
            String agentState = getAgentState(cmSite, cmHost);
            DisplayDTO agentStateDisplayDTO = new DisplayDTO();
            hostDTO.setAgentState(agentStateDisplayDTO);
            agentStateDisplayDTO.setCode(agentState);
            DictDO agentStateDictDO = findDictDO(dictTypeDOs, DictTypeConsts.STATE, agentState);
            if (agentStateDictDO != null) {
                agentStateDisplayDTO.setDisplay(agentStateDictDO.getName());
            }

            DisplayDTO stateDisplayDTO = new DisplayDTO();
            hostDTO.setState(stateDisplayDTO);
            String state = getState(cmSite, cmHost);
            stateDisplayDTO.setCode(state);
            DictDO stateDictDO = findDictDO(dictTypeDOs, DictTypeConsts.STATE, state);
            if (stateDictDO != null) {
                stateDisplayDTO.setDisplay(stateDictDO.getName());
            }

            hostDTO.setDescription(cmHost.getDesc());
            InfoDTO inputDTO = new InfoDTO();
            hostDTO.setInput(inputDTO);
            inputDTO.setTimestamp(cmHost.getCreatedAt());

            CmHost.Spec cmSpec = cmHost.getSpec();
            if (cmSpec != null) {
                CmHost.Spec.UsageLimit limit = cmSpec.getUsageLimit();
                if (limit != null) {
                    unit.setMaxCnt(limit.getUnit());
                }
            }

            CmHost.Status cmStatus = cmHost.getStatus();
            if (cmStatus != null) {
                if (cmStatus.getPhase().equals(CmConsts.HOST_PHASE_READY)) {
                    hostDTO.setInSuccess(true);
                } else if (cmStatus.getPhase().equals(CmConsts.HOST_PHASE_FAILED)) {
                    hostDTO.setInSuccess(false);
                }

                if (StringUtils.isNoneBlank(cmStatus.getRole())) {
                    DisplayDTO roleDisplayDTO = new DisplayDTO();
                    hostDTO.setRole(roleDisplayDTO);
                    roleDisplayDTO.setCode(cmStatus.getRole());
                    DictDO roleDictDO = findDictDO(dictTypeDOs, DictTypeConsts.HOST_ROLE, cmStatus.getRole());
                    if (roleDictDO != null) {
                        roleDisplayDTO.setDisplay(roleDictDO.getName());
                    }
                }

                if (cmStatus.getPhase().equals(CmConsts.HOST_PHASE_READY)
                        && BooleanUtils.negate(cmStatus.getUnschedulable())) {
                    hostDTO.setEnabled(true);
                } else {
                    hostDTO.setEnabled(false);
                }

                HostDTO.Statistics pod = hostDTO.new Statistics();
                hostDTO.setPod(pod);

                List<String> resourceSoftLimit = cmStatus.getResourceSoftLimit();
                if (resourceSoftLimit != null) {
                    if (resourceSoftLimit.contains("memory")) {
                        mem.setUpLimit(true);
                    }
                    if (resourceSoftLimit.contains("cpu")) {
                        cpu.setUpLimit(true);
                    }
                    if (resourceSoftLimit.contains("units")) {
                        unit.setUpLimit(true);
                    }
                    if (resourceSoftLimit.contains("storage")) {
                        if (StringUtils.isNotBlank(hostDO.getHddPath())) {
                            hdd.setUpLimit(true);
                        }

                        if (StringUtils.isNotBlank(hostDO.getSsdPath())) {
                            ssd.setUpLimit(true);
                        }
                    }
                }

                if (cmStatus.getPhase().equals(CmConsts.HOST_PHASE_READY)
                        && BooleanUtils.negate(cmStatus.getUnschedulable()) && state.equals(DictConsts.STATE_PASSING)) {
                    if (resourceSoftLimit == null || resourceSoftLimit.size() == 0) {
                        CmHost.Label cmLable = cmHost.getLabel();
                        if (cmLable != null) {
                            if (cmCluster != null) {
                                hostDTO.setDistributable(true);
                            }
                        }
                    }
                }

                CmHost.Status.Usage cmCapacity = cmStatus.getCapacity();
                if (cmCapacity != null) {
                    pod.setMaxCnt(cmCapacity.getPod());
                    unit.setMaxCnt(cmCapacity.getUnit());
                    Double cpuCapacity = 0.0;
                    if (cmCapacity.getMilicpu() != null) {
                        cpuCapacity = NumberUnits.retainDigits(cmCapacity.getMilicpu() / 1000.0);
                    }
                    cpu.setCapacity(cpuCapacity);

                    Double memCapacity = 0.0;
                    if (cmCapacity.getMemory() != null) {
                        memCapacity = NumberUnits.retainDigits(cmCapacity.getMemory() / 1024.0);
                    }
                    mem.setCapacity(memCapacity);
                    List<CmHost.Status.Usage.Storage> capacityStorages = cmCapacity.getStorages();
                    Double ssdCapacity = 0.0;
                    Double hddCapacity = 0.0;
                    if (capacityStorages != null && capacityStorages.size() > 0) {
                        for (CmHost.Status.Usage.Storage cmStorage : capacityStorages) {
                            if (cmStorage.getSize() != null) {
                                if (cmStorage.getPerformance().equals(CmConsts.PERFORMANCE_HIGH)) {
                                    ssdCapacity += cmStorage.getSize();
                                } else if (cmStorage.getPerformance().equals(CmConsts.PERFORMANCE_MEDIUM)) {
                                    hddCapacity += cmStorage.getSize();
                                }
                            }
                        }
                        ssd.setCapacity(NumberUnits.retainDigits(ssdCapacity / 1024.0));
                        hdd.setCapacity(NumberUnits.retainDigits(hddCapacity / 1024.0));
                    }

                    CmHost.Status.Usage cmAllocatable = cmStatus.getAllocatable();
                    if (cmAllocatable != null) {
                        Double cpuAllocatable = 0.0;
                        if (cmAllocatable.getMilicpu() != null) {
                            cpuAllocatable = NumberUnits.retainDigits(cmAllocatable.getMilicpu() / 1000.0);
                        }

                        Double memAllocatable = 0.0;
                        if (cmAllocatable.getMemory() != null) {
                            memAllocatable = NumberUnits.retainDigits(cmAllocatable.getMemory() / 1024.0);
                        }

                        unit.setCnt(cmCapacity.getUnit() - cmAllocatable.getUnit());
                        pod.setCnt(cmCapacity.getPod() - cmAllocatable.getPod());
                        cpu.setUsed(NumberUnits.retainDigits(cpuCapacity - cpuAllocatable));
                        mem.setUsed(NumberUnits.retainDigits(memCapacity - memAllocatable));

                        Double ssdAllocatable = 0.0;
                        Double hddAllocatable = 0.0;

                        List<CmHost.Status.Usage.Storage> allocatableStorages = cmAllocatable.getStorages();
                        if (allocatableStorages != null && allocatableStorages.size() > 0) {
                            for (CmHost.Status.Usage.Storage cmStorage : allocatableStorages) {
                                if (cmStorage.getSize() != null) {
                                    if (cmStorage.getPerformance().equals(CmConsts.PERFORMANCE_HIGH)) {
                                        ssdAllocatable += cmStorage.getSize();
                                    } else if (cmStorage.getPerformance().equals(CmConsts.PERFORMANCE_MEDIUM)) {
                                        hddAllocatable += cmStorage.getSize();
                                    }
                                }
                            }
                        }
                        ssd.setUsed(NumberUnits.retainDigits((ssdCapacity - ssdAllocatable) / 1024.0));
                        hdd.setUsed(NumberUnits.retainDigits((hddCapacity - hddAllocatable) / 1024.0));
                    }

                }

                CmHost.Status.NodeInfo cmNodeInfo = cmStatus.getNodeInfo();
                if (cmNodeInfo != null) {
                    hostDTO.setKernelVersion(cmNodeInfo.getKernelVersion());
                    hostDTO.setOsImage(cmNodeInfo.getOsImage());
                    hostDTO.setOperatingSystem(cmNodeInfo.getOperatingSystem());

                    DisplayDTO archDisplayDTO = new DisplayDTO();
                    archDisplayDTO.setCode(cmNodeInfo.getArchitecture());
                    DictDO archDictDO = findDictDO(dictTypeDOs, DictTypeConsts.SYS_ARCHITECTURE,
                            cmNodeInfo.getArchitecture());
                    if (archDictDO != null) {
                        archDisplayDTO.setDisplay(archDictDO.getName());
                    }
                    hostDTO.setArchitecture(archDisplayDTO);
                    hostDTO.setContainerRuntimeVersion(cmNodeInfo.getContainerRuntimeVersion());
                    hostDTO.setKubeletVersion(cmNodeInfo.getKubeletVersion());
                    hostDTO.setKubeProxyVersion(cmNodeInfo.getKubeProxyVersion());
                }
            }
        }
        return hostDTO;
    }

    public String getState(CmSite cmSite, CmHost cmHost) {
        String nodeState = getNodeState(cmHost);
        String state = nodeState;

        String agentState = getAgentState(cmSite, cmHost);
        if (agentState != null) {
            if (DictConsts.STATE_PASSING.equals(nodeState) && DictConsts.STATE_PASSING.equals(agentState)) {
                state = DictConsts.STATE_PASSING;
            } else if (DictConsts.STATE_CRITICAL.equals(nodeState) && DictConsts.STATE_CRITICAL.equals(agentState)) {
                state = DictConsts.STATE_CRITICAL;
            } else {
                state = DictConsts.STATE_WARNNING;
            }
        }
        return state;
    }

    private String getNodeState(CmHost cmHost) {
        String status = DictConsts.STATE_WARNNING;
        if (cmHost != null) {
            CmHost.Status cmStatus = cmHost.getStatus();
            if (cmStatus != null) {
                String nodeCondition = cmStatus.getNodeCondition();
                if (nodeCondition != null) {
                    if (nodeCondition.toLowerCase().equals("true")) {
                        status = DictConsts.STATE_PASSING;
                    } else if (nodeCondition.toLowerCase().equals("false")) {
                        status = DictConsts.STATE_CRITICAL;
                    }
                }
            }
        }
        return status;
    }

    private String getAgentState(CmSite cmSite, CmHost cmHost) {
        String status = null;
        if (cmSite != null) {
            CmSite.Kubernetes kubernetes = cmSite.getKubernetes();
            if (kubernetes != null) {
                if (CmConsts.STORAGE_TYPE_VOLUMEPATH.equals(kubernetes.getStorageMode())) {
                    status = DictConsts.STATE_WARNNING;
                    if (cmHost != null) {
                        CmHost.Status cmStatus = cmHost.getStatus();
                        if (cmStatus != null) {
                            String agentCondition = cmStatus.getAgentCondition();
                            if (agentCondition != null) {
                                if (agentCondition.toLowerCase().equals("true")) {
                                    status = DictConsts.STATE_PASSING;
                                } else if (agentCondition.toLowerCase().equals("false")) {
                                    status = DictConsts.STATE_CRITICAL;
                                }
                            }
                        }
                    }
                }
            }
        }
        return status;
    }

    private HostDO buildHostDOForSave(HostForm hostForm, String activeUsername, Date date) throws Exception {
        HostDO hostDO = new HostDO();
        hostDO.setId(PrimaryKeyUtil.get());
        hostDO.setClusterId(hostForm.getClusterId());
        hostDO.setIp(hostForm.getIp());
        hostDO.setRoom(hostForm.getRoom());
        hostDO.setSeat(hostForm.getSeat());

        CmCluster cmCluster = CmApi.getCluster(hostForm.getClusterId());
        if (cmCluster != null) {
            CmSiteBase cmSiteBase = cmCluster.getSite();
            if (cmSiteBase != null) {
                CmSite cmSite = CmApi.getSite(cmSiteBase.getId());
                CmSite.Kubernetes kubernetes = cmSite.getKubernetes();
                if (kubernetes.getStorageMode().equals(CmConsts.STORAGE_TYPE_VOLUMEPATH)) {
                    VolumePathForm volumePathForm = hostForm.getVolumePath();
                    if (volumePathForm != null) {
                        hostDO.setHddPath(StringUtils.join(volumePathForm.getHddPaths(), ","));
                        hostDO.setSsdPath(StringUtils.join(volumePathForm.getSsdPaths(), ","));
                        hostDO.setRemoteStorageId(volumePathForm.getRemoteStorageId());
                    }
                }
            }
        }

        hostDO.setMaxUsage(hostForm.getMaxUsage());
        hostDO.setRole(hostForm.getRole());
        hostDO.setDescription(StringUtils.trimToEmpty(hostForm.getDescription()));
        hostDO.setCreator(activeUsername);
        hostDO.setGmtCreate(date);
        return hostDO;
    }

    private TaskDO buildInTask(HostDO hostDO, String activeUsername, Date nowDate) throws Exception {
        TaskDO taskDO = new TaskDO();
        taskDO.setId(PrimaryKeyUtil.get());
        CmCluster cmCluster = CmApi.getCluster(hostDO.getClusterId());
        String siteId = cmCluster.getSite().getId();
        taskDO.setSiteId(siteId);
        taskDO.setObjType(DictConsts.OBJ_TYPE_HOST);
        taskDO.setObjId(hostDO.getId());
        taskDO.setObjName(hostDO.getIp());
        taskDO.setActionType(DictConsts.ACTION_TYPE_IN);
        taskDO.setBlock(true);
        taskDO.setState(DictConsts.TASK_STATE_READY);
        taskDO.setOwner(activeUsername);
        taskDO.setCreator(activeUsername);
        taskDO.setGmtCreate(nowDate);

        List<SubtaskDO> subtaskDOs = new ArrayList<>();
        taskDO.setSubtasks(subtaskDOs);

        SubtaskDO subtask_in = new SubtaskDO();
        subtaskDOs.add(subtask_in);

        subtask_in.setId(PrimaryKeyUtil.get());
        subtask_in.setTaskId(taskDO.getId());
        subtask_in.setObjType(DictConsts.OBJ_TYPE_HOST);
        subtask_in.setObjId(hostDO.getId());
        subtask_in.setObjName(hostDO.getIp());
        subtask_in.setActionType(DictConsts.ACTION_TYPE_IN);
        subtask_in.setPriority(1);
        subtask_in.setState(DictConsts.TASK_STATE_READY);
        SubtaskCfgDO subtaskCfgDO = subtaskCfgDAO.get(DictConsts.OBJ_TYPE_HOST, DictConsts.ACTION_TYPE_IN);
        if (subtaskCfgDO != null) {
            subtask_in.setTimeout(subtaskCfgDO.getTimeout());
        }

        subtask_in.setDataSource(hostDO);
        return taskDO;
    }

    private TaskDO buildOutTask(HostDO hostDO, String activeUsername, Date nowDate) throws Exception {
        TaskDO taskDO = new TaskDO();
        taskDO.setId(PrimaryKeyUtil.get());
        CmCluster cmCluster = CmApi.getCluster(hostDO.getClusterId());
        String siteId = cmCluster.getSite().getId();
        taskDO.setSiteId(siteId);
        taskDO.setObjType(DictConsts.OBJ_TYPE_HOST);
        taskDO.setObjId(hostDO.getId());
        taskDO.setObjName(hostDO.getIp());
        taskDO.setActionType(DictConsts.ACTION_TYPE_OUT);
        taskDO.setBlock(true);
        taskDO.setState(DictConsts.TASK_STATE_READY);
        taskDO.setOwner(activeUsername);
        taskDO.setCreator(activeUsername);
        taskDO.setGmtCreate(nowDate);

        List<SubtaskDO> subtaskDOs = new ArrayList<>();
        taskDO.setSubtasks(subtaskDOs);

        SubtaskDO subtask_out = new SubtaskDO();
        subtaskDOs.add(subtask_out);

        subtask_out.setId(PrimaryKeyUtil.get());
        subtask_out.setTaskId(taskDO.getId());
        subtask_out.setObjType(DictConsts.OBJ_TYPE_HOST);
        subtask_out.setObjId(hostDO.getId());
        subtask_out.setObjName(hostDO.getIp());
        subtask_out.setActionType(DictConsts.ACTION_TYPE_OUT);
        subtask_out.setPriority(1);
        subtask_out.setState(DictConsts.TASK_STATE_READY);
        SubtaskCfgDO subtaskCfgDO = subtaskCfgDAO.get(DictConsts.OBJ_TYPE_HOST, DictConsts.ACTION_TYPE_OUT);
        if (subtaskCfgDO != null) {
            subtask_out.setTimeout(subtaskCfgDO.getTimeout());
        }

        subtask_out.setDataSource(hostDO);

        return taskDO;
    }

    private CmHostBody buildCmHosUpdateBody(HostForm hostForm) {
        CmHostBody hostBody = new CmHostBody();
        if (hostForm.getDescription() != null) {
            hostBody.setDesc(hostForm.getDescription());
        }
        if (hostForm.getRoom() != null) {
            hostBody.setRoom(hostForm.getRoom());
        }
        if (hostForm.getSeat() != null) {
            hostBody.setSeat(hostForm.getSeat());
        }

        if (hostForm.getRole() != null) {
            hostBody.setRole(hostForm.getRole());
        }

        CmHostBody.UsageLimit usageLimit = null;
        if (hostForm.getMaxUnitCnt() != null) {
            if (usageLimit == null) {
                usageLimit = hostBody.new UsageLimit();
                usageLimit.setUnit(hostForm.getMaxUnitCnt());
            }
        }

        if (hostForm.getMaxUsage() != null) {
            if (usageLimit == null) {
                usageLimit = hostBody.new UsageLimit();
                usageLimit.setCpu(hostForm.getMaxUsage());
                usageLimit.setMemory(hostForm.getMaxUsage());
                usageLimit.setStorage(hostForm.getMaxUsage());
            }
        }
        hostBody.setUsageLimit(usageLimit);
        return hostBody;
    }

    private HostDO buildHostDOForUpdate(HostDO hostDO, HostForm hostForm) throws Exception {
        if (hostForm.getClusterId() != null) {
            hostDO.setClusterId(hostForm.getClusterId());
        }
        if (hostForm.getIp() != null) {
            hostDO.setIp(hostForm.getIp());
        }
        if (hostForm.getRoom() != null) {
            hostDO.setRoom(hostForm.getRoom());
        }
        if (hostForm.getSeat() != null) {
            hostDO.setSeat(hostForm.getSeat());
        }

        CmCluster cmCluster = CmApi.getCluster(hostForm.getClusterId());
        if (cmCluster != null) {
            CmSiteBase cmSiteBase = cmCluster.getSite();
            if (cmSiteBase != null) {
                CmSite cmSite = CmApi.getSite(cmSiteBase.getId());
                CmSite.Kubernetes kubernetes = cmSite.getKubernetes();
                if (kubernetes.getStorageMode().equals(CmConsts.STORAGE_TYPE_VOLUMEPATH)) {
                    VolumePathForm volumePathForm = hostForm.getVolumePath();
                    if (volumePathForm != null) {

                        if (volumePathForm.getHddPaths() != null && volumePathForm.getHddPaths().size() != 0) {
                            hostDO.setHddPath(StringUtils.join(volumePathForm.getHddPaths(), ","));
                        }
                        if (volumePathForm.getSsdPaths() != null && volumePathForm.getSsdPaths().size() != 0) {
                            hostDO.setSsdPath(StringUtils.join(volumePathForm.getSsdPaths(), ","));
                        }
                        if (volumePathForm.getRemoteStorageId() != null) {
                            hostDO.setRemoteStorageId(volumePathForm.getRemoteStorageId());
                        }
                    }
                }
            }
        }

        if (hostForm.getMaxUsage() != null) {
            hostDO.setMaxUsage(hostForm.getMaxUsage());
        }
        if (hostForm.getRole() != null) {
            hostDO.setRole(hostForm.getRole());
        }
        if (hostForm.getDescription() != null) {
            hostDO.setDescription(hostForm.getDescription());
        }
        return hostDO;
    }

    private CmHostBody buildCmHostRequestBody(HostDO hostDO) {
        CmHostBody hostBody = new CmHostBody();
        hostBody.setClusterId(hostDO.getClusterId());
        hostBody.setIp(hostDO.getIp());
        hostBody.setDesc(hostDO.getDescription());
        CmHostBody.UsageLimit cmUsageLimit = hostBody.new UsageLimit();
        hostBody.setUsageLimit(cmUsageLimit);
        cmUsageLimit.setCpu(hostDO.getMaxUsage());
        cmUsageLimit.setMemory(hostDO.getMaxUsage());
        cmUsageLimit.setStorage(hostDO.getMaxUsage());

        hostBody.setRole(hostDO.getRole());
        hostBody.setRoom(hostDO.getRoom());
        hostBody.setSeat(hostDO.getSeat());

        if (!StringUtils.isNotBlank(hostDO.getHddPath()) || !StringUtils.isNotBlank(hostDO.getSsdPath())
                || !StringUtils.isNotBlank(hostDO.getRemoteStorageId())) {
            CmHostBody.VolumePath volumePath = hostBody.new VolumePath();
            hostBody.setVolumePath(volumePath);

            if (StringUtils.isNotBlank(hostDO.getRemoteStorageId())) {
                volumePath.setStorageRemoteId(hostDO.getRemoteStorageId());
            } else {
                List<CmHostBody.VolumePath.Storage> storages = new ArrayList<>();
                volumePath.setStorages(storages);
                if (StringUtils.isNotBlank(hostDO.getSsdPath())) {
                    CmHostBody.VolumePath.Storage storage = volumePath.new Storage();
                    storages.add(storage);
                    storage.setPerformance(CmConsts.PERFORMANCE_HIGH);
                    storage.setPaths((Arrays.asList(StringUtils.split(hostDO.getSsdPath(), ","))));
                }

                if (StringUtils.isNotBlank(hostDO.getHddPath())) {
                    CmHostBody.VolumePath.Storage storage = volumePath.new Storage();
                    storages.add(storage);
                    storage.setPerformance(CmConsts.PERFORMANCE_MEDIUM);
                    storage.setPaths((Arrays.asList(StringUtils.split(hostDO.getHddPath(), ","))));
                }
            }
        }

        return hostBody;
    }

    @Override
    public TaskResult executeSubtask(TaskDO taskDO, SubtaskDO subtaskDO) {
        TaskResult taskResult = new TaskResult();
        taskResult.setState(subtaskDO.getState());
        switch (subtaskDO.getActionType()) {
        case DictConsts.ACTION_TYPE_IN:
            try {
                HostDO hostDO = (HostDO) subtaskDO.getDataSource();
                CmHostBody hostBody = buildCmHostRequestBody(hostDO);
                CmSaveHostResp cmSaveHostResp = CmApi.saveHost(hostBody);
                if (cmSaveHostResp == null) {
                    taskResult.setState(DictConsts.TASK_STATE_FAILED);
                    taskResult.setMsg("接口返回错误。");
                    return taskResult;
                }

                String relateId = cmSaveHostResp.getHostId();
                hostDO.setRelateId(relateId);
                hostDAO.updateRelateId(hostDO);

                logger.info("polling cm host state.");
                while (true) {
                    if (subtaskDO.getState().equals(DictConsts.TASK_STATE_RUNNING)) {
                        CmHost cmHost = CmApi.getHost(relateId);
                        if (cmHost == null) {
                            taskResult.setState(DictConsts.TASK_STATE_FAILED);
                            taskResult.setMsg("none object");
                            return taskResult;
                        }

                        CmHost.Status cmStatus = cmHost.getStatus();
                        if (cmStatus != null && cmStatus.getPhase().equals(CmConsts.HOST_PHASE_READY)) {
                            taskResult.setState(DictConsts.TASK_STATE_SUCCESS);
                            return taskResult;
                        } else if (cmStatus != null && cmStatus.getPhase().equals(CmConsts.HOST_PHASE_FAILED)) {
                            taskResult.setState(DictConsts.TASK_STATE_FAILED);
                            return taskResult;
                        } else {
                            Date nowDate = systemDAO.getCurrentSqlDateTime();
                            if ((nowDate.getTime() - subtaskDO.getStartDateTime().getTime()) / 1000 > subtaskDO
                                    .getTimeout()) {
                                taskResult.setState(DictConsts.TASK_STATE_TIMEOUT);
                                taskResult.setMsg("timeout");
                                return taskResult;
                            } else {
                                Thread.sleep(10000);
                                subtaskDO = subtaskDAO.get(subtaskDO.getId());
                            }
                        }
                    } else {
                        taskResult.setState(subtaskDO.getState());
                        taskResult.setMsg(subtaskDO.getMsg());
                        return taskResult;
                    }
                }

            } catch (Exception e) {
                taskResult.setState(DictConsts.TASK_STATE_FAILED);
                taskResult.setMsg(e.toString());
                logger.error("入库异常：", e);
            }
            break;
        case DictConsts.ACTION_TYPE_OUT:
            try {
                HostDO hostDO = (HostDO) subtaskDO.getDataSource();
                CmApi.removeHost(hostDO.getRelateId(), subtaskDO.getTimeout().intValue());

                hostDO.setRelateId(null);
                hostDAO.updateRelateId(hostDO);

                forceRebuildLogDAO.removeByHostRelateId(hostDO.getRelateId());

                taskResult.setState(DictConsts.TASK_STATE_SUCCESS);
            } catch (Exception e) {
                taskResult.setState(DictConsts.TASK_STATE_FAILED);
                taskResult.setMsg(e.toString());
                logger.error("出库异常：", e);
            }
            break;
        }
        return taskResult;
    }

    @Override
    public void executeTaskDone(TaskDO taskDO) {
    }

    public ObjModel getObjModel(String hostId) throws Exception {
        HostDO hostDO = hostDAO.get(hostId);
        CmCluster cmCluster = CmApi.getCluster(hostDO.getClusterId());
        if (cmCluster != null) {
            return new ObjModel(hostDO.getIp(), cmCluster.getSite().getId());
        }
        return null;
    }

    public class HostModel {
        private HostDO hostDO;
        private CmHost cmHost;
        private CmCluster cmCluster;
        private CmSite cmsite;
        private CmRemoteStorage cmRemoteStorage;
        private BusinessAreaDO businessAreaDO;
        private List<DictTypeDO> dictTypeDOs;
        private List<UserDO> userDOs;
        private TaskDO latestTaskDO;

        public HostDO getHostDO() {
            return hostDO;
        }

        public void setHostDO(HostDO hostDO) {
            this.hostDO = hostDO;
        }

        public CmHost getCmHost() {
            return cmHost;
        }

        public void setCmHost(CmHost cmHost) {
            this.cmHost = cmHost;
        }

        public CmCluster getCmCluster() {
            return cmCluster;
        }

        public void setCmCluster(CmCluster cmCluster) {
            this.cmCluster = cmCluster;
        }

        public CmSite getCmsite() {
            return cmsite;
        }

        public void setCmsite(CmSite cmsite) {
            this.cmsite = cmsite;
        }

        public CmRemoteStorage getCmRemoteStorage() {
            return cmRemoteStorage;
        }

        public void setCmRemoteStorage(CmRemoteStorage cmRemoteStorage) {
            this.cmRemoteStorage = cmRemoteStorage;
        }

        public BusinessAreaDO getBusinessAreaDO() {
            return businessAreaDO;
        }

        public void setBusinessAreaDO(BusinessAreaDO businessAreaDO) {
            this.businessAreaDO = businessAreaDO;
        }

        public List<DictTypeDO> getDictTypeDOs() {
            return dictTypeDOs;
        }

        public void setDictTypeDOs(List<DictTypeDO> dictTypeDOs) {
            this.dictTypeDOs = dictTypeDOs;
        }

        public List<UserDO> getUserDOs() {
            return userDOs;
        }

        public void setUserDOs(List<UserDO> userDOs) {
            this.userDOs = userDOs;
        }

        public TaskDO getLatestTaskDO() {
            return latestTaskDO;
        }

        public void setLatestTaskDO(TaskDO latestTaskDO) {
            this.latestTaskDO = latestTaskDO;
        }

        @Override
        public String toString() {
            return "HostModel [hostDO=" + hostDO + ", cmHost=" + cmHost + ", cmCluster=" + cmCluster + ", cmsite="
                    + cmsite + ", cmRemoteStorage=" + cmRemoteStorage + ", businessAreaDO=" + businessAreaDO
                    + ", dictTypeDOs=" + dictTypeDOs + ", userDOs=" + userDOs + ", latestTaskDO=" + latestTaskDO + "]";
        }

    }

}
