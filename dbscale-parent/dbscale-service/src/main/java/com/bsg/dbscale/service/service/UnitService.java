package com.bsg.dbscale.service.service;

import java.net.InetAddress;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bsg.dbscale.cm.api.CmApi;
import com.bsg.dbscale.cm.body.CmBackupBody;
import com.bsg.dbscale.cm.body.CmMaintenanceBody;
import com.bsg.dbscale.cm.body.CmServiceRoleBody;
import com.bsg.dbscale.cm.body.CmServicesLinkBody;
import com.bsg.dbscale.cm.body.CmUnitRebuildBody;
import com.bsg.dbscale.cm.body.CmUnitRestoreBody;
import com.bsg.dbscale.cm.constant.CmConsts;
import com.bsg.dbscale.cm.exception.CallingInterfaceException;
import com.bsg.dbscale.cm.model.CmAction;
import com.bsg.dbscale.cm.model.CmBackupFile;
import com.bsg.dbscale.cm.model.CmEvent;
import com.bsg.dbscale.cm.model.CmImageBase;
import com.bsg.dbscale.cm.model.CmNfs;
import com.bsg.dbscale.cm.model.CmService;
import com.bsg.dbscale.cm.model.CmSite;
import com.bsg.dbscale.cm.model.CmSiteBase;
import com.bsg.dbscale.cm.model.CmTopology;
import com.bsg.dbscale.cm.model.CmUnitEvent;
import com.bsg.dbscale.cm.query.CmBackupFileQuery;
import com.bsg.dbscale.cm.query.CmNfsQuery;
import com.bsg.dbscale.cm.query.CmServiceQuery;
import com.bsg.dbscale.cm.response.CmBackupResp;
import com.bsg.dbscale.cm.response.CmUnitRestoreResp;
import com.bsg.dbscale.dao.domain.BusinessAreaDO;
import com.bsg.dbscale.dao.domain.BusinessSubsystemDO;
import com.bsg.dbscale.dao.domain.BusinessSystemDO;
import com.bsg.dbscale.dao.domain.DefServDO;
import com.bsg.dbscale.dao.domain.DictDO;
import com.bsg.dbscale.dao.domain.DictTypeDO;
import com.bsg.dbscale.dao.domain.ForceRebuildLogDO;
import com.bsg.dbscale.dao.domain.ServDO;
import com.bsg.dbscale.dao.domain.ServGroupDO;
import com.bsg.dbscale.dao.domain.SubtaskCfgDO;
import com.bsg.dbscale.dao.domain.SubtaskDO;
import com.bsg.dbscale.dao.domain.TaskDO;
import com.bsg.dbscale.dao.domain.UnitDO;
import com.bsg.dbscale.dao.domain.UserDO;
import com.bsg.dbscale.service.check.CheckResult;
import com.bsg.dbscale.service.check.UnitCheck;
import com.bsg.dbscale.service.constant.Consts;
import com.bsg.dbscale.service.constant.DictConsts;
import com.bsg.dbscale.service.constant.DictTypeConsts;
import com.bsg.dbscale.service.dto.BusinessSubsystemBaseDTO;
import com.bsg.dbscale.service.dto.DisplayDTO;
import com.bsg.dbscale.service.dto.EventDTO;
import com.bsg.dbscale.service.dto.IdentificationDTO;
import com.bsg.dbscale.service.dto.ServGroupBaseDTO;
import com.bsg.dbscale.service.dto.TaskBaseDTO;
import com.bsg.dbscale.service.dto.TerminalDTO;
import com.bsg.dbscale.service.dto.UnitBaseDTO;
import com.bsg.dbscale.service.dto.UnitDTO;
import com.bsg.dbscale.service.dto.UnitEventDTO;
import com.bsg.dbscale.service.dto.UserBaseDTO;
import com.bsg.dbscale.service.dto.VersionDTO;
import com.bsg.dbscale.service.form.BackupForm;
import com.bsg.dbscale.service.form.MaintenanceForm;
import com.bsg.dbscale.service.form.UnitRebuildForm;
import com.bsg.dbscale.service.form.UnitRebuildInitForm;
import com.bsg.dbscale.service.form.UnitRestoreForm;
import com.bsg.dbscale.service.task.datasource.BackupDataSource;
import com.bsg.dbscale.service.task.datasource.RebuildDataSource;
import com.bsg.dbscale.service.util.PrimaryKeyUtil;
import com.bsg.dbscale.service.util.TaskResult;
import com.bsg.dbscale.util.DateUtils;
import com.bsg.dbscale.util.NumberUnits;
import com.bsg.dbscale.util.http.HttpClientUtil;
import com.bsg.dbscale.util.http.HttpRequestUtil;
import com.bsg.dbscale.util.http.HttpResp;

@Service
public class UnitService extends BaseService {

    @Autowired
    private UnitCheck unitCheck;

    @Autowired
    private CmhaServGroupService cmhaServGroupService;

    @Autowired
    private RedisServGroupService redisServGroupService;

    public Result list(String siteId, String state) throws Exception {
        List<UnitDTO> unitDTOs = new ArrayList<>();
        CmServiceQuery cmServiceQuery = new CmServiceQuery();
        cmServiceQuery.setSiteId(siteId);
        List<CmService> cmServices = CmApi.listService(cmServiceQuery);
        if (cmServices != null && cmServices.size() > 0) {
            List<ServGroupDO> servGroupDOs = servGroupDAO.list(null);
            List<DefServDO> defServDOs = defServDAO.list(null);
            List<DictTypeDO> dictTypeDOs = dictTypeDAO.list();
            List<UserDO> userDOs = userDAO.list(null);
            List<TaskDO> latestTaskDOs = taskDAO.listLatest(null, DictConsts.OBJ_TYPE_UNIT);
            for (CmService cmService : cmServices) {
                ServGroupDO servGroupDO = findServGroupDOByRelateId(servGroupDOs, cmService.getId());
                List<ServDO> servDOs = servGroupDO.getServs();
                Set<String> types = new HashSet<>();
                for (ServDO servDO : servDOs) {
                    types.add(servDO.getType());
                }
                ServDO servDO = findServDOByRelateId(servGroupDO, cmService.getId());
                CmService.Status cmStatus = cmService.getStatus();
                if (cmStatus != null) {
                    List<CmService.Status.Unit> cmUnits = cmStatus.getUnits();
                    if (cmUnits != null && cmUnits.size() > 0) {
                        for (CmService.Status.Unit cmUnit : cmUnits) {
                            if (StringUtils.isBlank(state)
                                    || (StringUtils.isNotBlank(state) && state.equals(cmUnit.getState()))
                                    || (DictConsts.STATE_UNKNOWN.equals(state)
                                            && StringUtils.isBlank(cmUnit.getState()))) {
                                UnitDO unitDO = findUnitDOByRelateId(servDO, cmUnit.getId());
                                if (unitDO != null) {
                                    UnitDTO unitDTO = new UnitDTO();
                                    unitDTOs.add(unitDTO);

                                    unitDTO.setId(unitDO.getId());

                                    DisplayDTO typeDisplayDTO = new DisplayDTO();
                                    unitDTO.setType(typeDisplayDTO);
                                    typeDisplayDTO.setCode(unitDO.getType());
                                    DefServDO defServDO = findDefServDO(defServDOs, unitDO.getType());
                                    if (defServDO != null) {
                                        typeDisplayDTO.setDisplay(defServDO.getName());
                                    }

                                    unitDTO.setRelateId(unitDO.getRelateId());
                                    TaskDO unitLatestTaskDO = findLasterTaskDO(latestTaskDOs, DictConsts.OBJ_TYPE_UNIT,
                                            unitDO.getId());
                                    if (unitLatestTaskDO != null) {
                                        TaskBaseDTO taskDTO = new TaskBaseDTO();
                                        unitDTO.setTask(taskDTO);

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

                                    unitDTO.setIp(cmUnit.getIp());

                                    DisplayDTO stateDisplayDTO = new DisplayDTO();
                                    unitDTO.setState(stateDisplayDTO);
                                    stateDisplayDTO.setCode(cmUnit.getState());
                                    DictDO stateDictDO = findDictDO(dictTypeDOs, DictTypeConsts.STATE,
                                            cmUnit.getState());
                                    if (stateDictDO != null) {
                                        stateDisplayDTO.setDisplay(stateDictDO.getName());
                                    }

                                    DisplayDTO podStateDisplayDTO = new DisplayDTO();
                                    unitDTO.setPodState(podStateDisplayDTO);
                                    podStateDisplayDTO.setCode(cmUnit.getPodState());
                                    DictDO podStateDictDO = findDictDO(dictTypeDOs, DictTypeConsts.POD_STATE,
                                            cmUnit.getPodState());
                                    if (podStateDictDO != null) {
                                        podStateDisplayDTO.setDisplay(podStateDictDO.getName());
                                    }

                                    List<CmService.Port> ports = cmStatus.getPorts();
                                    for (CmService.Port port : ports) {
                                        if (port.getName().equals(CmConsts.PORT_NAME)) {
                                            unitDTO.setPort(port.getPort());
                                            break;
                                        }
                                    }

                                    CmService.Status.Unit.Node cmNode = cmUnit.getNode();
                                    if (cmNode != null) {
                                        UnitBaseDTO.HostState hostDTO = unitDTO.new HostState();
                                        hostDTO.setIp(cmNode.getHostIp());
                                        hostDTO.setName(cmNode.getName());
                                        unitDTO.setHost(hostDTO);

                                        CmService.Status.Unit.Node.Cluster cmCluster = cmNode.getCluster();
                                        if (cmCluster != null) {
                                            IdentificationDTO cluterDTO = new IdentificationDTO();
                                            hostDTO.setCluster(cluterDTO);
                                            cluterDTO.setId(cmCluster.getId());
                                            cluterDTO.setName(cmCluster.getName());
                                        }

                                        String nodeCondition = cmUnit.getNodeCondition();
                                        String hostState = DictConsts.STATE_WARNNING;
                                        if (nodeCondition != null) {
                                            if (nodeCondition.toLowerCase().equals("true")) {
                                                hostState = DictConsts.STATE_PASSING;
                                            } else if (nodeCondition.toLowerCase().equals("false")) {
                                                hostState = DictConsts.STATE_CRITICAL;
                                            }
                                        }
                                        DisplayDTO hostStateDisplayDTO = new DisplayDTO();
                                        hostDTO.setState(hostStateDisplayDTO);
                                        hostStateDisplayDTO.setCode(hostState);
                                        DictDO hostStateDictDO = findDictDO(dictTypeDOs, DictTypeConsts.STATE,
                                                hostState);
                                        if (hostStateDictDO != null) {
                                            hostStateDisplayDTO.setDisplay(hostStateDictDO.getName());
                                        }

                                    }

                                    CmImageBase cmImage = cmUnit.getImage();
                                    if (cmImage != null) {
                                        VersionDTO versionDTO = new VersionDTO();
                                        versionDTO.setMajor(cmImage.getMajor());
                                        versionDTO.setMinor(cmImage.getMinor());
                                        versionDTO.setPatch(cmImage.getPatch());
                                        versionDTO.setBuild(cmImage.getBuild());
                                        unitDTO.setVersion(versionDTO);
                                    }

                                    CmService.Resources cmResources = cmUnit.getResources();
                                    if (cmResources != null) {
                                        if (cmResources.getMilicpu() != null) {
                                            double cpuCnt = NumberUnits.retainDigits(cmResources.getMilicpu() / 1000.0);
                                            unitDTO.setCpuCnt(cpuCnt);
                                        }

                                        if (cmResources.getMemory() != null) {
                                            double memSize = NumberUnits.retainDigits(cmResources.getMemory() / 1024.0);
                                            unitDTO.setMemSize(memSize);
                                        }

                                        CmService.Resources.Storage cmStorage = cmResources.getStorage();
                                        if (cmStorage != null) {
                                            String diskType = null;
                                            if (CmConsts.STORAGE_TYPE_VOLUMEPATH.equals(cmStorage.getMode())) {
                                                CmService.Resources.Storage.VolumePath volumePath = cmStorage
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
                                                unitDTO.setDiskType(diskTypeDisplayDTO);
                                                diskTypeDisplayDTO.setCode(diskType);
                                                DictDO diskTypeDictDO = findDictDO(dictTypeDOs,
                                                        DictTypeConsts.DISK_TYPE, diskType);
                                                if (diskTypeDictDO != null) {
                                                    diskTypeDisplayDTO.setDisplay(diskTypeDictDO.getName());
                                                }
                                            }

                                            List<CmService.Resources.Storage.Volume> cmVolumes = cmStorage.getVolumes();

                                            Long dataSize = 0L;
                                            Long logSize = 0L;
                                            for (CmService.Resources.Storage.Volume cmVolume : cmVolumes) {
                                                if (cmVolume.getCapacity() != null) {
                                                    if (cmVolume.getType().equals(CmConsts.VOLUME_DATA)) {
                                                        dataSize += cmVolume.getCapacity();
                                                    } else if (cmVolume.getType().equals(CmConsts.VOLUME_LOG)) {
                                                        logSize += cmVolume.getCapacity();
                                                    }
                                                }
                                            }

                                            unitDTO.setDataSize(NumberUnits.ceil(dataSize / 1024.0));
                                            unitDTO.setLogSize(NumberUnits.ceil(logSize / 1024.0));
                                        }

                                        ServGroupBaseDTO servGroupBaseDTO = new ServGroupBaseDTO();
                                        unitDTO.setServGroup(servGroupBaseDTO);
                                        servGroupBaseDTO.setId(servGroupDO.getId());
                                        servGroupBaseDTO.setCategory(servGroupDO.getCategory());

                                        DisplayDTO archDisplayDTO = new DisplayDTO();
                                        servGroupBaseDTO.setSysArchitecture(archDisplayDTO);

                                        archDisplayDTO.setCode(servGroupDO.getSysArchitecture());
                                        DictDO archDictDO = findDictDO(dictTypeDOs, DictTypeConsts.SYS_ARCHITECTURE,
                                                servGroupDO.getSysArchitecture());
                                        if (archDictDO != null) {
                                            archDisplayDTO.setDisplay(archDictDO.getName());
                                        }

                                        BusinessSubsystemDO businessSubsystemDO = servGroupDO.getBusinessSubsystem();
                                        if (businessSubsystemDO != null) {
                                            BusinessSubsystemBaseDTO businessSubsystemDTO = new BusinessSubsystemBaseDTO();
                                            servGroupBaseDTO.setBusinessSubsystem(businessSubsystemDTO);

                                            businessSubsystemDTO.setId(businessSubsystemDO.getId());
                                            businessSubsystemDTO.setName(businessSubsystemDO.getName());

                                            BusinessSystemDO businessSystemDO = businessSubsystemDO.getBusinessSystem();
                                            if (businessSystemDO != null) {
                                                IdentificationDTO businessSystemDTO = new IdentificationDTO();
                                                businessSubsystemDTO.setBusinessSystem(businessSystemDTO);

                                                businessSystemDTO.setId(businessSystemDO.getId());
                                                businessSystemDTO.setName(businessSystemDO.getName());
                                            }
                                        }

                                        BusinessAreaDO businessAreaDO = servGroupDO.getBusinessArea();
                                        if (businessAreaDO != null) {
                                            IdentificationDTO businessAreaDTO = new IdentificationDTO();
                                            servGroupBaseDTO.setBusinessArea(businessAreaDTO);
                                            businessAreaDTO.setId(businessAreaDO.getId());
                                            businessAreaDTO.setName(businessAreaDO.getName());
                                        }

                                        servGroupBaseDTO.setName(servGroupDO.getName());
                                        servGroupBaseDTO.setFlag(servGroupDO.getFlag());

                                        if (types.size() > 1) {
                                            servGroupBaseDTO.setHighAvailable(true);
                                        } else {
                                            servGroupBaseDTO.setHighAvailable(false);
                                        }

                                        UserBaseDTO userDTO = new UserBaseDTO();
                                        servGroupBaseDTO.setOwner(userDTO);
                                        userDTO.setUsername(servGroupDO.getOwner());
                                        UserDO owner = findUserDO(userDOs, servGroupDO.getOwner());
                                        if (owner != null) {
                                            userDTO.setName(owner.getName());
                                            userDTO.setCompany(owner.getCompany());
                                            userDTO.setTelephone(owner.getTelephone());
                                        }
                                        servGroupBaseDTO
                                                .setGmtCreate(DateUtils.dateTimeToString(servGroupDO.getGmtCreate()));
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return Result.success(unitDTOs);
    }

    public Result monitor(String unitId, String type) throws Exception {
        UnitDO unitDO = unitDAO.get(unitId);
        if (unitDO != null) {
            ServDO servDO = servDAO.get(unitDO.getServId());
            if (servDO != null) {
                ServGroupDO servGroupDO = servGroupDAO.get(servDO.getServGroupId());
                if (servGroupDO != null) {
                    BusinessAreaDO businessAreaDO = servGroupDO.getBusinessArea();
                    if (businessAreaDO != null) {
                        CmSite cmSite = CmApi.getSite(businessAreaDO.getSiteId());
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
                                if ("service".equals(type)) {
                                    String uri = "db/" + unitDO.getType() + "-overview";
                                    if (uri.equals(jsonObject.getString("uri"))) {
                                        if (StringUtils.isNotBlank(servDO.getRelateId())) {
                                            CmService cmService = CmApi.getService(servDO.getRelateId());
                                            if (cmService != null) {
                                                CmService.Status cmStatus = cmService.getStatus();
                                                if (cmStatus != null) {
                                                    List<CmService.Status.Unit> cmUnits = cmStatus.getUnits();
                                                    for (CmService.Status.Unit cmUnit : cmUnits) {
                                                        if (cmUnit.getId().equals(unitDO.getRelateId())) {
                                                            String resultUrl = url + jsonObject.getString("url")
                                                                    + "?var-service=" + cmService.getName() + "-"
                                                                    + unitDO.getType() + "-exporter-svc" + "&var-pod="
                                                                    + unitDO.getRelateId() + "&kiosk";
                                                            return Result.success(resultUrl);
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                        break;
                                    }
                                } else if ("data".equals(type)) {
                                    if ("db/kubernetes-persistent-volumes".equals(jsonObject.getString("uri"))) {
                                        String resultUrl = url + jsonObject.getString("url") + "?var-volume="
                                                + unitDO.getRelateId() + "-data&kiosk";
                                        return Result.success(resultUrl);
                                    }
                                } else if ("log".equals(type)) {
                                    if ("db/kubernetes-persistent-volumes".equals(jsonObject.getString("uri"))) {
                                        String resultUrl = url + jsonObject.getString("url") + "?var-volume="
                                                + unitDO.getRelateId() + "-log&kiosk";
                                        return Result.success(resultUrl);
                                    }
                                } else {
                                    if ("db/kubernetes-compute-resources-pod".equals(jsonObject.getString("uri"))) {
                                        String resultUrl = url + jsonObject.getString("url") + "?var-pod="
                                                + unitDO.getRelateId() + "&kiosk";
                                        return Result.success(resultUrl);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return Result.success();
    }

    public Result getTerminal(String unitId) throws Exception {
        TerminalDTO terminalDTO = null;
        UnitDO unitDO = unitDAO.get(unitId);
        if (unitDO != null) {
            ServDO servDO = servDAO.get(unitDO.getServId());
            if (StringUtils.isNotBlank(servDO.getRelateId())) {
                CmService cmService = CmApi.getService(servDO.getRelateId());
                if (cmService != null) {
                    CmSiteBase cmSiteBase = cmService.getSite();
                    CmService.Status cmStatus = cmService.getStatus();
                    if (cmStatus != null) {
                        List<CmService.Status.Unit> cmUnits = cmStatus.getUnits();
                        for (CmService.Status.Unit cmUnit : cmUnits) {
                            if (cmUnit.getId().equals(unitDO.getRelateId())) {
                                CmSite cmSite = CmApi.getSite(cmSiteBase.getId());
                                if (cmSite != null) {
                                    CmSite.DashboardInfo cmDashBoard = cmSite.getDashboard();
                                    if (cmDashBoard != null) {
                                        HttpResp httpResp = null;
                                        String url = "{0}://{1}:{2}";
                                        url = MessageFormat.format(url, cmDashBoard.getScheme(),
                                                InetAddress.getByName(cmDashBoard.getServiceName()).getHostAddress(),
                                                cmDashBoard.getPort());
                                        Map<String, String> headMap = new HashMap<>();
                                        HttpClientUtil httpClientUtil = null;
                                        String ip = InetAddress.getByName(cmDashBoard.getServiceName())
                                                .getHostAddress();
                                        if (StringUtils.equalsIgnoreCase(cmDashBoard.getScheme(), "https")) {
                                            httpClientUtil = new HttpClientUtil(true, cmDashBoard.getCertificate());
                                            httpResp = HttpRequestUtil.sendGetRequest(url + "/api/v1/csrftoken/login",
                                                    httpClientUtil);
                                            if (httpResp.getStatusCode() != HttpStatus.SC_OK) {
                                                throw new CallingInterfaceException(
                                                        "bashboard异常：" + httpResp.getResponseContent());
                                            }
                                            JSONObject jsonObject = JSONObject
                                                    .parseObject(httpResp.getResponseContent());

                                            headMap.put("x-csrf-token", jsonObject.getString("token"));

                                            JSONObject loginBodyJson = new JSONObject();
                                            loginBodyJson.put("token", cmDashBoard.getToken());

                                            httpResp = HttpRequestUtil.sendPostRequest(url + "/api/v1/login", headMap,
                                                    loginBodyJson.toJSONString(), httpClientUtil);
                                            if (httpResp.getStatusCode() != HttpStatus.SC_OK) {
                                                throw new CallingInterfaceException(
                                                        "bashboard异常：" + httpResp.getResponseContent());
                                            }

                                            jsonObject = JSONObject.parseObject(httpResp.getResponseContent());
                                            headMap = new HashMap<>();
                                            headMap.put("jwetoken", jsonObject.getString("jweToken"));
                                        } else {
                                            httpClientUtil = new HttpClientUtil();
                                        }

                                        url = url + "/api/v1/pod/{0}/{1}/shell/{2}";
                                        url = MessageFormat.format(url, cmUnit.getNamespace(), cmUnit.getId(),
                                                unitDO.getType());
                                        httpResp = HttpRequestUtil.sendGetRequest(url, headMap, httpClientUtil);
                                        if (httpResp.getStatusCode() != HttpStatus.SC_OK) {
                                            throw new CallingInterfaceException(
                                                    "dashboard异常：" + httpResp.getResponseContent());
                                        }
                                        JSONObject jsonObject = JSONObject.parseObject(httpResp.getResponseContent());
                                        terminalDTO = new TerminalDTO();
                                        terminalDTO.setScheme(cmDashBoard.getScheme());
                                        terminalDTO.setSessionId(jsonObject.getString("id"));
                                        terminalDTO.setAddr(ip + ":" + cmDashBoard.getPort());
                                        return Result.success(terminalDTO);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return Result.success(terminalDTO);
    }

    @Transactional(rollbackFor = Exception.class)
    public Result start(String unitId, String activeUsername) throws Exception {
        UnitDO unitDO = unitDAO.get(unitId);

        CheckResult checkResult = unitCheck.checkStart(unitDO);
        if (checkResult.getCode() != CheckResult.SUCCESS) {
            logger.error(checkResult.getMsg());
            return Result.failure(checkResult);
        }

        Date nowDate = systemDAO.getCurrentSqlDateTime();
        TaskDO taskDO = buildStartTask(unitDO, activeUsername, nowDate);
        List<SubtaskDO> subtaskDOs = taskDO.getSubtasks();
        for (SubtaskDO subtaskDO : subtaskDOs) {
            subtaskDAO.save(subtaskDO);
        }
        taskDAO.save(taskDO);

        executor.execute(new Task(taskDO));

        return Result.success();
    }

    @Transactional(rollbackFor = Exception.class)
    public Result stop(String unitId, String activeUsername) throws Exception {
        UnitDO unitDO = unitDAO.get(unitId);

        CheckResult checkResult = unitCheck.checkStop(unitDO);
        if (checkResult.getCode() != CheckResult.SUCCESS) {
            logger.error(checkResult.getMsg());
            return Result.failure(checkResult);
        }

        Date nowDate = systemDAO.getCurrentSqlDateTime();
        TaskDO taskDO = buildStopTask(unitDO, activeUsername, nowDate);
        List<SubtaskDO> subtaskDOs = taskDO.getSubtasks();
        for (SubtaskDO subtaskDO : subtaskDOs) {
            subtaskDAO.save(subtaskDO);
        }
        taskDAO.save(taskDO);

        executor.execute(new Task(taskDO));

        return Result.success();
    }

    @Transactional(rollbackFor = Exception.class)
    public Result rebuild(String unitId, UnitRebuildForm unitRebuildForm, String activeUsername) throws Exception {
        UnitDO unitDO = unitDAO.get(unitId);

        CheckResult checkResult = unitCheck.checkRebuild(unitDO, unitRebuildForm);
        if (checkResult.getCode() != CheckResult.SUCCESS) {
            logger.error(checkResult.getMsg());
            return Result.failure(checkResult);
        }

        Date nowDate = systemDAO.getCurrentSqlDateTime();
        TaskDO taskDO = buildRebuildTask(unitDO, unitRebuildForm, activeUsername, nowDate);
        List<SubtaskDO> subtaskDOs = taskDO.getSubtasks();
        for (SubtaskDO subtaskDO : subtaskDOs) {
            subtaskDAO.save(subtaskDO);
        }
        taskDAO.save(taskDO);

        executor.execute(new Task(taskDO));

        return Result.success();
    }

    @Transactional(rollbackFor = Exception.class)
    public Result rebuildInit(String unitId, UnitRebuildInitForm unitRebuildInitForm, String activeUsername)
            throws Exception {
        UnitDO unitDO = unitDAO.get(unitId);

        CheckResult checkResult = unitCheck.checkRebuildInit(unitDO, unitRebuildInitForm);
        if (checkResult.getCode() != CheckResult.SUCCESS) {
            logger.error(checkResult.getMsg());
            return Result.failure(checkResult);
        }

        Date nowDate = systemDAO.getCurrentSqlDateTime();
        TaskDO taskDO = buildRebuildInitTask(unitDO, unitRebuildInitForm, activeUsername, nowDate);
        List<SubtaskDO> subtaskDOs = taskDO.getSubtasks();
        for (SubtaskDO subtaskDO : subtaskDOs) {
            subtaskDAO.save(subtaskDO);
        }
        taskDAO.save(taskDO);

        executor.execute(new Task(taskDO));

        return Result.success();
    }

    @Transactional(rollbackFor = Exception.class)
    public Result backup(String unitId, BackupForm backupForm, String activeUsername) throws Exception {
        UnitDO unitDO = unitDAO.get(unitId);

        CheckResult checkResult = unitCheck.checkBackup(backupForm, unitDO);
        if (checkResult.getCode() != CheckResult.SUCCESS) {
            logger.error(checkResult.getMsg());
            return Result.failure(checkResult);
        }

        Date nowDate = systemDAO.getCurrentSqlDateTime();
        TaskDO taskDO = buildBackupTask(unitDO, backupForm, activeUsername, nowDate);
        List<SubtaskDO> subtaskDOs = taskDO.getSubtasks();
        for (SubtaskDO subtaskDO : subtaskDOs) {
            subtaskDAO.save(subtaskDO);
        }
        taskDAO.save(taskDO);

        executor.execute(new Task(taskDO));

        return Result.success();
    }

    @Transactional(rollbackFor = Exception.class)
    public Result restore(String unitId, UnitRestoreForm unitRestoreForm, String activeUsername) throws Exception {
        UnitDO unitDO = unitDAO.get(unitId);

        CheckResult checkResult = unitCheck.checkRestore(unitDO);
        if (checkResult.getCode() != CheckResult.SUCCESS) {
            logger.error(checkResult.getMsg());
            return Result.failure(checkResult);
        }

        Date nowDate = systemDAO.getCurrentSqlDateTime();
        TaskDO taskDO = buildRestoreTask(unitDO, unitRestoreForm, activeUsername, nowDate);
        List<SubtaskDO> subtaskDOs = taskDO.getSubtasks();
        for (SubtaskDO subtaskDO : subtaskDOs) {
            subtaskDAO.save(subtaskDO);
        }
        taskDAO.save(taskDO);

        executor.execute(new Task(taskDO));

        return Result.success();
    }

    public Result resetMaster(String unitId) throws Exception {
        UnitDO unitDO = unitDAO.get(unitId);

        CheckResult checkResult = unitCheck.checkResetMaster(unitDO);
        if (checkResult.getCode() != CheckResult.SUCCESS) {
            logger.error(checkResult.getMsg());
            return Result.failure(checkResult);
        }

        ServDO servDO = servDAO.get(unitDO.getServId());
        ServGroupDO servGroupDO = servGroupDAO.get(servDO.getServGroupId());
        List<ServDO> servDOs = servGroupDO.getServs();
        List<String> execUnitIds = new ArrayList<>();
        String haType = "";
        if (servGroupDO.getCategory().equals(Consts.SERV_GROUP_TYPE_CMHA)) {
            haType = Consts.SERV_TYPE_CMHA;
        } else {
            haType = Consts.SERV_TYPE_SENTINEL;
        }

        List<String> haUnitRelateIds = new ArrayList<>();
        for (ServDO servDO2 : servDOs) {
            if (servDO2.getType().equals(haType)) {
                List<UnitDO> unitDOs = servDO2.getUnits();
                for (UnitDO unitDO2 : unitDOs) {
                    haUnitRelateIds.add(unitDO2.getRelateId());
                }
                break;
            }
        }
        if (haUnitRelateIds.size() == 0) {
            List<UnitDO> unitDOs = servDO.getUnits();
            for (UnitDO unitDO2 : unitDOs) {
                execUnitIds.add(unitDO2.getRelateId());
            }
        } else {
            execUnitIds = haUnitRelateIds;
        }

        CmServiceRoleBody cmServiceRoleBody = new CmServiceRoleBody();
        cmServiceRoleBody.setExecUnitIds(execUnitIds);

        CmServiceRoleBody.CmRoleInfo masterRoleInfo = cmServiceRoleBody.new CmRoleInfo();
        cmServiceRoleBody.setMaster(masterRoleInfo);
        List<String> masterUnitRelateIds = new ArrayList<>();
        masterUnitRelateIds.add(unitDO.getRelateId());
        masterRoleInfo.setUnitIds(masterUnitRelateIds);

        CmServiceRoleBody.CmRoleInfo slaveRoleInfo = cmServiceRoleBody.new CmRoleInfo();
        cmServiceRoleBody.setSlave(slaveRoleInfo);
        List<String> slaveUnitRelateIds = new ArrayList<>();
        List<UnitDO> unitDOs = servDO.getUnits();
        for (UnitDO unitDO2 : unitDOs) {
            if (!unitDO2.getId().equals(unitId)) {
                slaveUnitRelateIds.add(unitDO2.getRelateId());
            }
        }
        slaveRoleInfo.setUnitIds(slaveUnitRelateIds);
        CmApi.updateRole(cmServiceRoleBody);

        return Result.success();
    }

    public Result maintenance(String unitId, MaintenanceForm maintenanceForm) throws Exception {
        UnitDO unitDO = unitDAO.get(unitId);

        CheckResult checkResult = unitCheck.checkMaintenance(unitDO);
        if (checkResult.getCode() != CheckResult.SUCCESS) {
            logger.error(checkResult.getMsg());
            return Result.failure(checkResult);
        }

        ServDO servDO = servDAO.get(unitDO.getServId());
        ServGroupDO servGroupDO = servGroupDAO.get(servDO.getServGroupId());
        List<ServDO> servDOs = servGroupDO.getServs();
        ServDO cmhaServDO = findAnyServDOType(servDOs, Consts.SERV_TYPE_CMHA);
        CmMaintenanceBody cmMaintenanceBody = new CmMaintenanceBody();
        cmMaintenanceBody.setUnitId(unitDO.getRelateId());
        cmMaintenanceBody.setMaintenance(maintenanceForm.getMaintenance());
        CmApi.maintenance(cmhaServDO.getRelateId(), cmMaintenanceBody);

        return Result.success();
    }

    public Result getEvent(String unitId) throws Exception {
        UnitEventDTO unitEventDTO = null;
        UnitDO unitDO = unitDAO.get(unitId);
        if (unitDO != null) {
            ServDO servDO = servDAO.get(unitDO.getServId());
            ServGroupDO servGroupDO = servGroupDAO.get(servDO.getServGroupId());
            BusinessAreaDO businessAreaDO = servGroupDO.getBusinessArea();

            CmUnitEvent cmUnitEvent = CmApi.getUnitEvent(businessAreaDO.getSiteId(), unitDO.getRelateId());
            if (cmUnitEvent != null) {
                unitEventDTO = new UnitEventDTO();

                List<CmEvent> eventUnits = cmUnitEvent.getUnits();
                if (eventUnits != null) {
                    List<EventDTO> eventUnitDTOs = new ArrayList<>();
                    unitEventDTO.setUnits(eventUnitDTOs);
                    for (CmEvent cmEvent : eventUnits) {
                        EventDTO eventDTO = new EventDTO();
                        eventUnitDTOs.add(eventDTO);
                        setEventDTO(eventDTO, cmEvent);
                    }
                }

                List<CmEvent> eventPods = cmUnitEvent.getPods();
                if (eventPods != null) {
                    List<EventDTO> eventPodDTOs = new ArrayList<>();
                    unitEventDTO.setPods(eventPodDTOs);
                    for (CmEvent cmEvent : eventPods) {
                        EventDTO eventDTO = new EventDTO();
                        eventPodDTOs.add(eventDTO);
                        setEventDTO(eventDTO, cmEvent);
                    }
                }

                List<CmEvent> eventNetworkClaims = cmUnitEvent.getNetworkClaims();
                if (eventNetworkClaims != null) {
                    List<EventDTO> eventNetworkClaimDTOs = new ArrayList<>();
                    unitEventDTO.setNetworkClaims(eventNetworkClaimDTOs);
                    for (CmEvent cmEvent : eventNetworkClaims) {
                        EventDTO eventDTO = new EventDTO();
                        eventNetworkClaimDTOs.add(eventDTO);
                        setEventDTO(eventDTO, cmEvent);
                    }
                }

                List<CmEvent> eventVolumePathDatas = cmUnitEvent.getVolumePathDatas();
                if (eventVolumePathDatas != null) {
                    List<EventDTO> eventVolumePathDataDTOs = new ArrayList<>();
                    unitEventDTO.setVolumePathDatas(eventVolumePathDataDTOs);
                    for (CmEvent cmEvent : eventVolumePathDatas) {
                        EventDTO eventDTO = new EventDTO();
                        eventVolumePathDataDTOs.add(eventDTO);
                        setEventDTO(eventDTO, cmEvent);
                    }
                }

                List<CmEvent> eventVolumePathLogs = cmUnitEvent.getVolumePathLogs();
                if (eventVolumePathLogs != null) {
                    List<EventDTO> eventVolumePathLogDTOs = new ArrayList<>();
                    unitEventDTO.setVolumePathLogs(eventVolumePathLogDTOs);
                    for (CmEvent cmEvent : eventVolumePathLogs) {
                        EventDTO eventDTO = new EventDTO();
                        eventVolumePathLogDTOs.add(eventDTO);
                        setEventDTO(eventDTO, cmEvent);
                    }
                }
            }
        }
        return Result.success(unitEventDTO);
    }

    private void setEventDTO(EventDTO eventDTO, CmEvent cmEvent) {
        eventDTO.setType(cmEvent.getType());
        eventDTO.setInterval(cmEvent.getInterval());
        eventDTO.setMessage(cmEvent.getMessage());
        eventDTO.setReason(cmEvent.getReason());
        eventDTO.setSource(cmEvent.getSource());
    }

    private TaskDO buildStartTask(UnitDO unitDO, String activeUsername, Date nowDate) throws Exception {
        TaskDO taskDO = new TaskDO();
        taskDO.setId(PrimaryKeyUtil.get());
        ServDO servDO = servDAO.get(unitDO.getServId());
        ServGroupDO servGroupDO = servGroupDAO.get(servDO.getServGroupId());
        BusinessAreaDO businessAreaDO = servGroupDO.getBusinessArea();
        taskDO.setSiteId(businessAreaDO.getSiteId());
        taskDO.setObjType(DictConsts.OBJ_TYPE_UNIT);
        taskDO.setObjId(unitDO.getId());
        taskDO.setObjName(unitDO.getRelateId());
        taskDO.setActionType(DictConsts.ACTION_TYPE_START);
        taskDO.setBlock(true);
        taskDO.setState(DictConsts.TASK_STATE_READY);
        taskDO.setOwner(servGroupDO.getOwner());
        taskDO.setCreator(activeUsername);
        taskDO.setGmtCreate(nowDate);

        List<SubtaskDO> subtaskDOs = new ArrayList<>();
        taskDO.setSubtasks(subtaskDOs);

        int priority = 1;
        SubtaskDO subtask = buildSubTaskStart(taskDO, unitDO, priority);
        subtaskDOs.add(subtask);

        return taskDO;
    }

    private TaskDO buildStopTask(UnitDO unitDO, String activeUsername, Date nowDate) throws Exception {
        TaskDO taskDO = new TaskDO();
        taskDO.setId(PrimaryKeyUtil.get());
        ServDO servDO = servDAO.get(unitDO.getServId());
        ServGroupDO servGroupDO = servGroupDAO.get(servDO.getServGroupId());
        BusinessAreaDO businessAreaDO = servGroupDO.getBusinessArea();
        taskDO.setSiteId(businessAreaDO.getSiteId());
        taskDO.setObjType(DictConsts.OBJ_TYPE_UNIT);
        taskDO.setObjId(unitDO.getId());
        taskDO.setObjName(unitDO.getRelateId());
        taskDO.setActionType(DictConsts.ACTION_TYPE_STOP);
        taskDO.setBlock(true);
        taskDO.setState(DictConsts.TASK_STATE_READY);
        taskDO.setOwner(servGroupDO.getOwner());
        taskDO.setCreator(activeUsername);
        taskDO.setGmtCreate(nowDate);

        List<SubtaskDO> subtaskDOs = new ArrayList<>();
        taskDO.setSubtasks(subtaskDOs);

        int priority = 1;
        SubtaskDO subtask = buildSubTaskStop(taskDO, unitDO, priority);
        subtaskDOs.add(subtask);

        return taskDO;
    }

    private TaskDO buildRebuildTask(UnitDO unitDO, UnitRebuildForm unitRebuildForm, String activeUsername, Date nowDate)
            throws Exception {
        TaskDO taskDO = new TaskDO();
        taskDO.setId(PrimaryKeyUtil.get());
        ServDO servDO = servDAO.get(unitDO.getServId());
        ServGroupDO servGroupDO = servGroupDAO.get(servDO.getServGroupId());
        BusinessAreaDO businessAreaDO = servGroupDO.getBusinessArea();
        taskDO.setSiteId(businessAreaDO.getSiteId());
        taskDO.setObjType(DictConsts.OBJ_TYPE_UNIT);
        taskDO.setObjId(unitDO.getId());
        taskDO.setObjName(unitDO.getRelateId());
        if (BooleanUtils.isNotTrue(unitRebuildForm.getForce())) {
            taskDO.setActionType(DictConsts.ACTION_TYPE_REBUILD);
        } else {
            taskDO.setActionType(DictConsts.ACTION_TYPE_FORCE_REBUILD);
        }
        taskDO.setBlock(true);
        taskDO.setState(DictConsts.TASK_STATE_READY);
        taskDO.setOwner(servGroupDO.getOwner());
        taskDO.setCreator(activeUsername);
        taskDO.setGmtCreate(nowDate);

        List<SubtaskDO> subtaskDOs = new ArrayList<>();
        taskDO.setSubtasks(subtaskDOs);

        int priority = 1;
        RebuildDataSource rebuildDataSource = new RebuildDataSource();
        rebuildDataSource.setHostRelateId(unitRebuildForm.getHostRelateId());
        rebuildDataSource.setForce(unitRebuildForm.getForce());
        SubtaskDO subtaskRebuild = buildSubTaskRebuild(taskDO, unitDO, priority, rebuildDataSource);
        subtaskDOs.add(subtaskRebuild);
        priority++;

        if (BooleanUtils.isNotTrue(unitRebuildForm.getForce())) {
            UnitRebuildInitForm unitRebuildInitForm = unitRebuildForm;

            List<SubtaskDO> rebuildInitSubTasks = buildRebuildInitSubTasks(taskDO, unitDO, unitRebuildInitForm, nowDate,
                    priority);
            subtaskDOs.addAll(rebuildInitSubTasks);
        }

        return taskDO;
    }

    private TaskDO buildRebuildInitTask(UnitDO unitDO, UnitRebuildInitForm unitRebuildInitForm, String activeUsername,
            Date nowDate) throws Exception {
        TaskDO taskDO = new TaskDO();
        taskDO.setId(PrimaryKeyUtil.get());
        ServDO servDO = servDAO.get(unitDO.getServId());
        ServGroupDO servGroupDO = servGroupDAO.get(servDO.getServGroupId());
        BusinessAreaDO businessAreaDO = servGroupDO.getBusinessArea();
        taskDO.setSiteId(businessAreaDO.getSiteId());
        taskDO.setObjType(DictConsts.OBJ_TYPE_UNIT);
        taskDO.setObjId(unitDO.getId());
        taskDO.setObjName(unitDO.getRelateId());
        taskDO.setActionType(DictConsts.ACTION_TYPE_REBUILD_INIT);
        taskDO.setBlock(true);
        taskDO.setState(DictConsts.TASK_STATE_READY);
        taskDO.setOwner(servGroupDO.getOwner());
        taskDO.setCreator(activeUsername);
        taskDO.setGmtCreate(nowDate);

        List<SubtaskDO> subtaskDOs = new ArrayList<>();
        taskDO.setSubtasks(subtaskDOs);

        int priority = 1;
        List<SubtaskDO> rebuildInitSubTasks = buildRebuildInitSubTasks(taskDO, unitDO, unitRebuildInitForm, nowDate,
                priority);
        subtaskDOs.addAll(rebuildInitSubTasks);

        return taskDO;
    }

    private List<SubtaskDO> buildRebuildInitSubTasks(TaskDO taskDO, UnitDO unitDO,
            UnitRebuildInitForm unitRebuildInitForm, Date nowDate, int priority) throws Exception {
        List<SubtaskDO> subtaskDOs = new ArrayList<>();

        ServDO servDO = servDAO.get(unitDO.getServId());
        ServGroupDO servGroupDO = servGroupDAO.get(servDO.getServGroupId());

        if (unitDO.getType().equals(Consts.SERV_TYPE_MYSQL)) {
            if (StringUtils.isBlank(unitRebuildInitForm.getBackupFileId())) {
                ServDO cmhaServDO = findAnyServDOType(servGroupDO, Consts.SERV_TYPE_CMHA);
                String masterUnitRelateId = null;
                if (cmhaServDO == null) {
                    CmService cmService = CmApi.getServiceDetail(servDO.getRelateId());
                    CmService.Status.Unit cmMasterUnit = CmApi.findMasterUnit(cmService);
                    masterUnitRelateId = cmMasterUnit.getId();
                } else {
                    CmTopology cmTopology = CmApi.getTopology(cmhaServDO.getRelateId());
                    CmTopology.Node cmMasterNode = CmApi.findMasterUnit(cmTopology);
                    masterUnitRelateId = cmMasterNode.getNode();
                }
                UnitDO masterUnitDO = findUnitDOByRelateId(servDO, masterUnitRelateId);
                BackupDataSource backupDataSource = new BackupDataSource();
                backupDataSource.setType(DictConsts.BACKUP_TYPE_FULL);
                backupDataSource.setBackupStorageType(unitRebuildInitForm.getBackupStorageType());
                backupDataSource.setExpired((DateUtils.addDays(nowDate, 3).getTime()) / 1000);
                SubtaskDO subTaskBackup = buildSubTaskBackupMaster(taskDO, masterUnitDO, priority, backupDataSource);
                subtaskDOs.add(subTaskBackup);
                priority++;
            }

            String backupFileId = unitRebuildInitForm.getBackupFileId();
            SubtaskDO subTaskRestore = buildSubTaskRestore(taskDO, unitDO, priority, backupFileId);
            subtaskDOs.add(subTaskRestore);
            priority++;
        }

        SubtaskDO subtaskStart = buildSubTaskStart(taskDO, unitDO, priority);
        subtaskDOs.add(subtaskStart);
        priority++;

        List<ServDO> servDOs = servGroupDO.getServs();
        Set<String> types = new HashSet<>();
        for (ServDO servDO2 : servDOs) {
            types.add(servDO2.getType());
        }

        if (servGroupDO.getCategory().equals(Consts.SERV_GROUP_TYPE_MYSQL)) {
            SubtaskDO subtaskUpdateRole = buildSubTaskUpdateRole(taskDO, unitDO, priority);
            subtaskDOs.add(subtaskUpdateRole);
        } else if (servGroupDO.getCategory().equals(Consts.SERV_GROUP_TYPE_CMHA)) {
            if (unitDO.getType().equals(Consts.SERV_TYPE_MYSQL)) {
                SubtaskDO subtask_mysqlLink = cmhaServGroupService.buildMysqlLinkSubtaskDO(taskDO, servGroupDO,
                        priority);
                if (subtask_mysqlLink != null) {
                    subtaskDOs.add(subtask_mysqlLink);
                }
            } else if (unitDO.getType().equals(Consts.SERV_TYPE_CMHA)) {
                SubtaskDO subtask_cmhaLink = cmhaServGroupService.buildCmhaLinkSubtaskDO(taskDO, servGroupDO, priority);
                if (subtask_cmhaLink != null) {
                    subtaskDOs.add(subtask_cmhaLink);
                }
            }
        } else if (servGroupDO.getCategory().equals(Consts.SERV_GROUP_TYPE_REDIS)) {
            if (types.size() > 1) {
                if (unitDO.getType().equals(Consts.SERV_TYPE_REDIS)) {
                    List<SubtaskDO> subtask_redisLinks = redisServGroupService.buildRedisLinkSubtaskDOs(taskDO,
                            servGroupDO, priority);
                    if (subtask_redisLinks.size() > 0) {
                        subtaskDOs.addAll(subtask_redisLinks);
                    }
                } else if (unitDO.getType().equals(Consts.SERV_TYPE_SENTINEL)) {
                    SubtaskDO subtask_sentinelLink = redisServGroupService.buildSentinelLinkSubtaskDO(taskDO,
                            servGroupDO, priority);
                    if (subtask_sentinelLink != null) {
                        subtaskDOs.add(subtask_sentinelLink);
                    }
                }
            } else {
                SubtaskDO subtaskUpdateRole = buildSubTaskUpdateRole(taskDO, unitDO, priority);
                subtaskDOs.add(subtaskUpdateRole);
            }
        }
        return subtaskDOs;
    }

    private TaskDO buildBackupTask(UnitDO unitDO, BackupForm backupForm, String activeUsername, Date nowDate)
            throws Exception {
        TaskDO taskDO = new TaskDO();
        taskDO.setId(PrimaryKeyUtil.get());
        ServDO servDO = servDAO.get(unitDO.getServId());
        ServGroupDO servGroupDO = servGroupDAO.get(servDO.getServGroupId());
        BusinessAreaDO businessAreaDO = servGroupDO.getBusinessArea();
        taskDO.setSiteId(businessAreaDO.getSiteId());
        taskDO.setObjType(DictConsts.OBJ_TYPE_UNIT);
        taskDO.setObjId(unitDO.getId());
        taskDO.setObjName(unitDO.getRelateId());
        taskDO.setActionType(DictConsts.ACTION_TYPE_BACKUP);
        taskDO.setBlock(true);
        taskDO.setState(DictConsts.TASK_STATE_READY);
        taskDO.setOwner(servGroupDO.getOwner());
        taskDO.setCreator(activeUsername);
        taskDO.setGmtCreate(nowDate);

        List<SubtaskDO> subtaskDOs = new ArrayList<>();
        taskDO.setSubtasks(subtaskDOs);

        int priority = 1;
        SubtaskDO subtaskStart = buildSubTaskStart(taskDO, unitDO, priority);
        subtaskDOs.add(subtaskStart);
        priority++;

        BackupDataSource backupDataSource = new BackupDataSource();
        backupDataSource.setBackupStorageType(backupForm.getBackupStorageType());
        backupDataSource.setType(backupForm.getType());
        backupDataSource.setTables(backupForm.getTables());
        backupDataSource.setExpired(backupForm.getExpired());
        SubtaskDO subtaskBackup = buildSubTaskBackup(taskDO, unitDO, priority, backupDataSource);
        subtaskDOs.add(subtaskBackup);

        return taskDO;
    }

    private TaskDO buildRestoreTask(UnitDO unitDO, UnitRestoreForm unitRestoreForm, String activeUsername, Date nowDate)
            throws Exception {
        TaskDO taskDO = new TaskDO();
        taskDO.setId(PrimaryKeyUtil.get());
        ServDO servDO = servDAO.get(unitDO.getServId());
        ServGroupDO servGroupDO = servGroupDAO.get(servDO.getServGroupId());
        BusinessAreaDO businessAreaDO = servGroupDO.getBusinessArea();
        taskDO.setSiteId(businessAreaDO.getSiteId());
        taskDO.setObjType(DictConsts.OBJ_TYPE_UNIT);
        taskDO.setObjId(unitDO.getId());
        taskDO.setObjName(unitDO.getRelateId());
        taskDO.setActionType(DictConsts.ACTION_TYPE_RESTORE);
        taskDO.setBlock(true);
        taskDO.setState(DictConsts.TASK_STATE_READY);
        taskDO.setOwner(servGroupDO.getOwner());
        taskDO.setCreator(activeUsername);
        taskDO.setGmtCreate(nowDate);

        List<SubtaskDO> subtaskDOs = new ArrayList<>();
        taskDO.setSubtasks(subtaskDOs);

        int priority = 1;
        SubtaskDO subtaskStop = buildSubTaskStop(taskDO, unitDO, priority);
        subtaskDOs.add(subtaskStop);
        priority++;

        SubtaskDO subtaskRestore = buildSubTaskRestore(taskDO, unitDO, priority, unitRestoreForm.getBackupFileId());
        subtaskDOs.add(subtaskRestore);
        priority++;

        SubtaskDO subtaskStart = buildSubTaskStart(taskDO, unitDO, priority);
        subtaskDOs.add(subtaskStart);
        priority++;

        List<ServDO> servDOs = servGroupDO.getServs();
        if (servDOs.size() == 1) {
            SubtaskDO subtaskUpdateRole = buildSubTaskUpdateRole(taskDO, unitDO, priority);
            subtaskDOs.add(subtaskUpdateRole);
        }

        return taskDO;
    }

    private SubtaskDO buildSubTaskStart(TaskDO taskDO, UnitDO unitDO, int priority) {
        SubtaskDO subtask = new SubtaskDO();
        subtask.setId(PrimaryKeyUtil.get());
        subtask.setTaskId(taskDO.getId());
        subtask.setObjType(DictConsts.OBJ_TYPE_UNIT);
        subtask.setObjId(unitDO.getId());
        subtask.setObjName(unitDO.getType());
        subtask.setActionType(DictConsts.ACTION_TYPE_START);
        subtask.setPriority(priority);
        subtask.setState(DictConsts.TASK_STATE_READY);
        SubtaskCfgDO subtaskCfgDO = subtaskCfgDAO.get(DictConsts.OBJ_TYPE_UNIT, DictConsts.ACTION_TYPE_START);
        if (subtaskCfgDO != null) {
            subtask.setTimeout(subtaskCfgDO.getTimeout());
        }
        return subtask;
    }

    private SubtaskDO buildSubTaskStop(TaskDO taskDO, UnitDO unitDO, int priority) {
        SubtaskDO subtask = new SubtaskDO();
        subtask.setId(PrimaryKeyUtil.get());
        subtask.setTaskId(taskDO.getId());
        subtask.setObjType(DictConsts.OBJ_TYPE_UNIT);
        subtask.setObjId(unitDO.getId());
        subtask.setObjName(unitDO.getType());
        subtask.setActionType(DictConsts.ACTION_TYPE_STOP);
        subtask.setPriority(priority);
        subtask.setState(DictConsts.TASK_STATE_READY);
        SubtaskCfgDO subtaskCfgDO = subtaskCfgDAO.get(DictConsts.OBJ_TYPE_UNIT, DictConsts.ACTION_TYPE_STOP);
        if (subtaskCfgDO != null) {
            subtask.setTimeout(subtaskCfgDO.getTimeout());
        }
        return subtask;
    }

    private SubtaskDO buildSubTaskRebuild(TaskDO taskDO, UnitDO unitDO, int priority,
            RebuildDataSource rebuildDataSource) {
        SubtaskDO subtask = new SubtaskDO();
        subtask.setId(PrimaryKeyUtil.get());
        subtask.setTaskId(taskDO.getId());
        subtask.setObjType(DictConsts.OBJ_TYPE_UNIT);
        subtask.setObjId(unitDO.getId());
        subtask.setObjName(unitDO.getType());
        subtask.setActionType(DictConsts.ACTION_TYPE_REBUILD);
        subtask.setPriority(priority);
        subtask.setState(DictConsts.TASK_STATE_READY);
        SubtaskCfgDO subtaskCfgDO = subtaskCfgDAO.get(DictConsts.OBJ_TYPE_UNIT, DictConsts.ACTION_TYPE_REBUILD);
        if (subtaskCfgDO != null) {
            subtask.setTimeout(subtaskCfgDO.getTimeout());
        }
        subtask.setDataSource(rebuildDataSource);
        return subtask;
    }

    private SubtaskDO buildSubTaskUpdateRole(TaskDO taskDO, UnitDO unitDO, int priority) {
        SubtaskDO subtask = new SubtaskDO();
        subtask.setId(PrimaryKeyUtil.get());
        subtask.setTaskId(taskDO.getId());
        subtask.setObjType(DictConsts.OBJ_TYPE_UNIT);
        subtask.setObjId(unitDO.getId());
        subtask.setObjName(unitDO.getType());
        subtask.setActionType(DictConsts.ACTION_TYPE_UPDATE_ROLE);
        subtask.setPriority(priority);
        subtask.setState(DictConsts.TASK_STATE_READY);
        subtask.setTimeout(60L);
        return subtask;
    }

    private SubtaskDO buildSubTaskBackup(TaskDO taskDO, UnitDO unitDO, int priority,
            BackupDataSource backupDataSource) {
        SubtaskDO subtask = new SubtaskDO();
        subtask.setId(PrimaryKeyUtil.get());
        subtask.setTaskId(taskDO.getId());
        subtask.setObjType(DictConsts.OBJ_TYPE_UNIT);
        subtask.setObjId(unitDO.getId());
        subtask.setObjName(unitDO.getType());
        subtask.setActionType(DictConsts.ACTION_TYPE_BACKUP);
        subtask.setPriority(priority);
        subtask.setState(DictConsts.TASK_STATE_READY);
        SubtaskCfgDO subtaskCfgDO = subtaskCfgDAO.get(DictConsts.OBJ_TYPE_UNIT, DictConsts.ACTION_TYPE_BACKUP);
        if (subtaskCfgDO != null) {
            subtask.setTimeout(subtaskCfgDO.getTimeout());
        }
        subtask.setDataSource(backupDataSource);
        return subtask;
    }

    private SubtaskDO buildSubTaskBackupMaster(TaskDO taskDO, UnitDO masterUnitDO, int priority,
            BackupDataSource backupDataSource) {
        SubtaskDO subtask = new SubtaskDO();
        subtask.setId(PrimaryKeyUtil.get());
        subtask.setTaskId(taskDO.getId());
        subtask.setObjType(DictConsts.OBJ_TYPE_UNIT);
        subtask.setObjId(masterUnitDO.getId());
        subtask.setObjName(masterUnitDO.getType());
        subtask.setActionType(DictConsts.ACTION_TYPE_BACKUP_MASTER);
        subtask.setPriority(priority);
        subtask.setState(DictConsts.TASK_STATE_READY);
        SubtaskCfgDO subtaskCfgDO = subtaskCfgDAO.get(DictConsts.OBJ_TYPE_UNIT, DictConsts.ACTION_TYPE_BACKUP);
        if (subtaskCfgDO != null) {
            subtask.setTimeout(subtaskCfgDO.getTimeout());
        }
        subtask.setDataSource(backupDataSource);
        return subtask;
    }

    private SubtaskDO buildSubTaskRestore(TaskDO taskDO, UnitDO unitDO, int priority, String backupFileId) {
        SubtaskDO subtask = new SubtaskDO();
        subtask.setId(PrimaryKeyUtil.get());
        subtask.setTaskId(taskDO.getId());
        subtask.setObjType(DictConsts.OBJ_TYPE_UNIT);
        subtask.setObjId(unitDO.getId());
        subtask.setObjName(unitDO.getType());
        subtask.setActionType(DictConsts.ACTION_TYPE_RESTORE);
        subtask.setPriority(priority);
        subtask.setState(DictConsts.TASK_STATE_READY);
        SubtaskCfgDO subtaskCfgDO = subtaskCfgDAO.get(DictConsts.OBJ_TYPE_UNIT, DictConsts.ACTION_TYPE_RESTORE);
        if (subtaskCfgDO != null) {
            subtask.setTimeout(subtaskCfgDO.getTimeout());
        }
        subtask.setDataSource(backupFileId);
        return subtask;
    }

    @Override
    public TaskResult executeSubtask(TaskDO taskDO, SubtaskDO subtaskDO) {
        TaskResult taskResult = new TaskResult();
        taskResult.setState(DictConsts.TASK_STATE_RUNNING);
        UnitDO unitDO = unitDAO.get(subtaskDO.getObjId());
        switch (subtaskDO.getActionType()) {
        case DictConsts.ACTION_TYPE_START:
            try {
                ServDO servDO = servDAO.get(unitDO.getServId());
                CmApi.startUnit(servDO.getRelateId(), unitDO.getRelateId());

                taskResult = pollingState(subtaskDO, CmConsts.STATE_PASSING);
            } catch (Exception e) {
                taskResult.setState(DictConsts.TASK_STATE_FAILED);
                taskResult.setMsg(e.toString());
                logger.error("启动异常：", e);
            }
            break;
        case DictConsts.ACTION_TYPE_STOP:
            try {
                ServDO servDO = servDAO.get(unitDO.getServId());
                CmApi.stopUnit(servDO.getRelateId(), unitDO.getRelateId());

                taskResult = pollingState(subtaskDO, CmConsts.STATE_CRITICAL);
            } catch (Exception e) {
                taskResult.setState(DictConsts.TASK_STATE_FAILED);
                taskResult.setMsg(e.toString());
                logger.error("停止异常：", e);
            }
            break;
        case DictConsts.ACTION_TYPE_REBUILD:
            try {
                RebuildDataSource rebuildDataSource = (RebuildDataSource) subtaskDO.getDataSource();
                ForceRebuildLogDO forceRebuildLogDO = null;

                ServDO servDO = servDAO.get(unitDO.getServId());
                if (BooleanUtils.isTrue(rebuildDataSource.getForce())) {
                    try {
                        CmService cmService = CmApi.getService(servDO.getRelateId());
                        if (cmService != null) {
                            CmService.Status cmStatus = cmService.getStatus();
                            if (cmStatus != null) {
                                List<CmService.Status.Unit> cmUnits = cmStatus.getUnits();
                                if (cmUnits != null && cmUnits.size() > 0) {
                                    for (CmService.Status.Unit cmUnit : cmUnits) {
                                        if (cmUnit.getId().equals(unitDO.getRelateId())) {
                                            CmService.Status.Unit.Node cmNode = cmUnit.getNode();
                                            if (cmNode != null) {
                                                forceRebuildLogDO = new ForceRebuildLogDO();
                                                forceRebuildLogDO.setUnitRelateId(unitDO.getRelateId());
                                                forceRebuildLogDO.setSourceHostRelateId(cmNode.getId());
                                                if (StringUtils.isNotBlank(rebuildDataSource.getHostRelateId())) {
                                                    forceRebuildLogDO
                                                            .setTargetHostRelateId(rebuildDataSource.getHostRelateId());
                                                }
                                                forceRebuildLogDO.setTaskId(taskDO.getId());
                                                forceRebuildLogDAO.save(forceRebuildLogDO);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                CmUnitRebuildBody cmUnitRebuildBody = buildCmUnitRebuildRequestBody(rebuildDataSource);
                CmApi.rebuildUnit(servDO.getRelateId(), unitDO.getRelateId(), rebuildDataSource.getForce(),
                        cmUnitRebuildBody);

                taskResult = pollingRebuildTask(subtaskDO, forceRebuildLogDO);
            } catch (Exception e) {
                taskResult.setState(DictConsts.TASK_STATE_FAILED);
                taskResult.setMsg(e.toString());
                logger.error("重建异常：", e);
            }
            break;
        case DictConsts.ACTION_TYPE_UPDATE_ROLE:
            try {
                CmServiceRoleBody cmServiceRoleBody = buildCmUnitUpdateRoleRequestBody(unitDO);
                CmApi.updateRole(cmServiceRoleBody);
                taskResult.setState(DictConsts.TASK_STATE_SUCCESS);
            } catch (Exception e) {
                taskResult.setState(DictConsts.TASK_STATE_FAILED);
                taskResult.setMsg(e.toString());
                logger.error("设置角色异常：", e);
            }
            break;
        case DictConsts.ACTION_TYPE_BACKUP:
        case DictConsts.ACTION_TYPE_BACKUP_MASTER:
            try {
                BackupDataSource backupDataSource = (BackupDataSource) subtaskDO.getDataSource();

                ServDO servDO = servDAO.get(unitDO.getServId());
                ServGroupDO servGroupDO = servGroupDAO.get(servDO.getServGroupId());
                String businessAreaId = servGroupDO.getBusinessAreaId();

                CmBackupBody cmBackupBody = null;
                if (CmConsts.BACKUP_STORAGE_TYPE_NFS.equals(backupDataSource.getBackupStorageType())) {
                    CmNfsQuery nfsQuery = new CmNfsQuery();
                    nfsQuery.setZone(businessAreaId);
                    nfsQuery.setUnschedulable(false);
                    List<CmNfs> cmNfs = CmApi.listNfs(nfsQuery);
                    if (cmNfs == null || cmNfs.size() < 1) {
                        taskResult.setState(DictConsts.TASK_STATE_FAILED);
                        taskResult.setMsg("无符合条件的NFS。");
                        return taskResult;
                    }
                    cmBackupBody = buildCmBackupToNfsRequestBody(backupDataSource, unitDO, cmNfs.get(0));
                }
                CmBackupResp cmBackupResp = CmApi.backup(servDO.getRelateId(), cmBackupBody);
                if (cmBackupResp == null) {
                    taskResult.setState(DictConsts.TASK_STATE_FAILED);
                    taskResult.setMsg("接口返回错误。");
                    return taskResult;
                }

                String backupFileId = cmBackupResp.getId();
                taskResult = pollingBackup(subtaskDO, backupFileId);
            } catch (Exception e) {
                taskResult.setState(DictConsts.TASK_STATE_FAILED);
                taskResult.setMsg(e.toString());
                logger.error("备份异常：", e);
            }
            break;
        case DictConsts.ACTION_TYPE_RESTORE:
            try {
                String backupFileId = (String) subtaskDO.getDataSource();
                ServDO servDO = servDAO.get(unitDO.getServId());
                if (StringUtils.isBlank(backupFileId)) {
                    CmBackupFile latestBackupFile = null;
                    CmBackupFileQuery cmBackupFileQuery = new CmBackupFileQuery();
                    cmBackupFileQuery.setServiceId(servDO.getRelateId());
                    cmBackupFileQuery.setStatus(CmConsts.BACKUP_FILE_COMPLETE);
                    List<CmBackupFile> cmBackupFiles = CmApi.listBackupFile(cmBackupFileQuery);
                    if (cmBackupFiles != null && cmBackupFiles.size() > 0) {
                        latestBackupFile = cmBackupFiles.get(0);
                        String maxFinishAt = latestBackupFile.getFinishAt();
                        for (CmBackupFile cmBackupFile : cmBackupFiles) {
                            if (cmBackupFile.getFinishAt().compareTo(maxFinishAt) >= 0) {
                                latestBackupFile = cmBackupFile;
                                maxFinishAt = cmBackupFile.getFinishAt();
                            }
                        }
                    }
                    if (latestBackupFile == null) {
                        taskResult.setState(DictConsts.TASK_STATE_FAILED);
                        taskResult.setMsg("无备份文件错误。");
                        return taskResult;
                    } else {
                        backupFileId = latestBackupFile.getId();
                    }
                }

                CmUnitRestoreBody cmUnitRestoreBody = buildCmUnitRestoreRequestBody(backupFileId);
                CmUnitRestoreResp cmUnitRestoreResp = CmApi.restoreUnit(servDO.getRelateId(), unitDO.getRelateId(),
                        cmUnitRestoreBody);
                if (cmUnitRestoreResp == null) {
                    taskResult.setState(DictConsts.TASK_STATE_FAILED);
                    taskResult.setMsg("接口返回错误。");
                    return taskResult;
                }

                taskResult = pollingRestoreTask(subtaskDO, cmUnitRestoreResp.getId());
            } catch (Exception e) {
                taskResult.setState(DictConsts.TASK_STATE_FAILED);
                taskResult.setMsg(e.toString());
                logger.error("还原异常：", e);
            }
            break;
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

                CmServicesLinkBody cmServicesLinkBody = null;
                if (servGroupDO.getCategory().equals(Consts.SERV_GROUP_TYPE_CMHA)) {
                    cmServicesLinkBody = cmhaServGroupService.buildCmServicesLinkRequestBody(subtaskDO.getObjId(),
                            servGroupDO);
                } else if (servGroupDO.getCategory().equals(Consts.SERV_GROUP_TYPE_REDIS)) {
                    cmServicesLinkBody = redisServGroupService.buildCmServicesLinkRequestBody(subtaskDO.getObjId(),
                            servGroupDO);
                }
                CmApi.linkServices(relateId, cmServicesLinkBody, subtaskDO.getTimeout().intValue());

                taskResult.setState(DictConsts.TASK_STATE_SUCCESS);
            } catch (Exception e) {
                taskResult.setState(DictConsts.TASK_STATE_FAILED);
                taskResult.setMsg(e.toString());
                logger.error("LINK异常：", e);
            }
            break;
        }

        return taskResult;
    }

    private CmUnitRebuildBody buildCmUnitRebuildRequestBody(RebuildDataSource rebuildDataSource) {
        CmUnitRebuildBody cmUnitRebuildBody = new CmUnitRebuildBody();
        cmUnitRebuildBody.setNode(rebuildDataSource.getHostRelateId());
        return cmUnitRebuildBody;
    }

    private CmServiceRoleBody buildCmUnitUpdateRoleRequestBody(UnitDO unitDO) throws Exception {
        ServDO servDO = servDAO.get(unitDO.getServId());
        ServGroupDO servGroupDO = servGroupDAO.get(servDO.getServGroupId());
        List<ServDO> servDOs = servGroupDO.getServs();
        List<String> execUnitIds = new ArrayList<>();
        String haType = "";
        if (servGroupDO.getCategory().equals(Consts.SERV_GROUP_TYPE_CMHA)) {
            haType = Consts.SERV_TYPE_CMHA;
        } else {
            haType = Consts.SERV_TYPE_SENTINEL;
        }

        List<String> haUnitRelateIds = new ArrayList<>();
        for (ServDO servDO2 : servDOs) {
            if (servDO2.getType().equals(haType)) {
                List<UnitDO> unitDOs = servDO2.getUnits();
                for (UnitDO unitDO2 : unitDOs) {
                    haUnitRelateIds.add(unitDO2.getRelateId());
                }
                break;
            }
        }
        if (haUnitRelateIds.size() == 0) {
            List<UnitDO> unitDOs = servDO.getUnits();
            for (UnitDO unitDO2 : unitDOs) {
                execUnitIds.add(unitDO2.getRelateId());
            }
        } else {
            execUnitIds = haUnitRelateIds;
        }

        CmServiceRoleBody cmServiceRoleBody = new CmServiceRoleBody();
        cmServiceRoleBody.setExecUnitIds(execUnitIds);

        CmService cmService = CmApi.getServiceDetail(servDO.getRelateId());
        CmService.Status.Unit cmMasterUnit = CmApi.findMasterUnit(cmService);
        String masterUnitRelateId = unitDO.getRelateId();
        if (cmMasterUnit != null) {
            masterUnitRelateId = cmMasterUnit.getId();
        }

        CmServiceRoleBody.CmRoleInfo masterRoleInfo = cmServiceRoleBody.new CmRoleInfo();
        cmServiceRoleBody.setMaster(masterRoleInfo);
        List<String> masterUnitRelateIds = new ArrayList<>();
        masterUnitRelateIds.add(masterUnitRelateId);
        masterRoleInfo.setUnitIds(masterUnitRelateIds);

        CmServiceRoleBody.CmRoleInfo slaveRoleInfo = cmServiceRoleBody.new CmRoleInfo();
        cmServiceRoleBody.setSlave(slaveRoleInfo);
        List<String> slaveUnitRelateIds = new ArrayList<>();
        List<UnitDO> unitDOs = servDO.getUnits();
        for (UnitDO unitDO2 : unitDOs) {
            if (!unitDO2.getRelateId().equals(masterUnitRelateId)) {
                slaveUnitRelateIds.add(unitDO2.getRelateId());
            }
        }
        slaveRoleInfo.setUnitIds(slaveUnitRelateIds);

        return cmServiceRoleBody;
    }

    private CmBackupBody buildCmBackupToNfsRequestBody(BackupDataSource backupDataSource, UnitDO unitDO, CmNfs cmNfs) {
        CmBackupBody cmBackupBody = new CmBackupBody();
        cmBackupBody.setBackupStorageType(CmConsts.BACKUP_STORAGE_TYPE_NFS);
        cmBackupBody.setBackupStorageId(cmNfs.getId());
        cmBackupBody.setType(backupDataSource.getType());
        cmBackupBody.setExpiredAt(backupDataSource.getExpired());
        if (unitDO != null) {
            cmBackupBody.setUntiId(unitDO.getRelateId());
        }
        return cmBackupBody;
    }

    private CmUnitRestoreBody buildCmUnitRestoreRequestBody(String backupFileId) {
        CmUnitRestoreBody cmUnitRestoreBody = new CmUnitRestoreBody();
        cmUnitRestoreBody.setBackupFileId(backupFileId);
        return cmUnitRestoreBody;
    }

    private TaskResult pollingState(SubtaskDO subtaskDO, String specialState) throws Exception {
        TaskResult taskResult = new TaskResult();
        taskResult.setState(DictConsts.TASK_STATE_RUNNING);
        UnitDO unitDO = unitDAO.get(subtaskDO.getObjId());
        ServDO servDO = servDAO.get(unitDO.getServId());
        while (true) {
            if (subtaskDO.getState().equals(DictConsts.TASK_STATE_RUNNING)) {
                CmService cmService = CmApi.getService(servDO.getRelateId());
                if (cmService == null) {
                    taskResult.setState(DictConsts.TASK_STATE_FAILED);
                    taskResult.setMsg("none object");
                    return taskResult;
                }

                String state = "";
                CmService.Status cmStatus = cmService.getStatus();
                if (cmStatus != null) {
                    List<CmService.Status.Unit> cmUnits = cmStatus.getUnits();
                    for (CmService.Status.Unit cmUnit : cmUnits) {
                        if (cmUnit.getId().equals(unitDO.getRelateId())) {
                            state = cmUnit.getState();
                            break;
                        }
                    }
                }

                if (state.equals(specialState)) {
                    taskResult.setState(DictConsts.TASK_STATE_SUCCESS);
                    return taskResult;
                } else {
                    Date nowDate = systemDAO.getCurrentSqlDateTime();
                    if ((nowDate.getTime() - subtaskDO.getStartDateTime().getTime()) / 1000 > subtaskDO.getTimeout()) {
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
    }

    private TaskResult pollingRebuildTask(SubtaskDO subtaskDO, ForceRebuildLogDO forceRebuildLogDO) throws Exception {
        TaskResult taskResult = new TaskResult();
        taskResult.setState(DictConsts.TASK_STATE_RUNNING);
        UnitDO unitDO = unitDAO.get(subtaskDO.getObjId());
        ServDO servDO = servDAO.get(unitDO.getServId());
        while (true) {
            if (subtaskDO.getState().equals(DictConsts.TASK_STATE_RUNNING)) {
                CmService cmService = CmApi.getService(servDO.getRelateId());
                if (cmService == null) {
                    taskResult.setState(DictConsts.TASK_STATE_FAILED);
                    taskResult.setMsg("none object");
                    return taskResult;
                }

                boolean completed = false;
                CmService.Status cmStatus = cmService.getStatus();
                if (cmStatus != null) {
                    List<CmService.Status.Unit> cmUnits = cmStatus.getUnits();
                    for (CmService.Status.Unit cmUnit : cmUnits) {
                        if (cmUnit.getId().equals(unitDO.getRelateId())) {
                            if (cmUnit.getAction().equals("") && cmUnit.getPodState().equals(CmConsts.POD_STATE_RUNNING)
                                    && BooleanUtils.isTrue(cmUnit.getInitStart())) {
                                completed = true;
                            }

                            if (forceRebuildLogDO != null) {
                                CmService.Status.Unit.Node cmNode = cmUnit.getNode();
                                if (cmNode != null
                                        && StringUtils.isNotBlank(forceRebuildLogDO.getTargetHostRelateId())) {
                                    if (!forceRebuildLogDO.getSourceHostRelateId().equals(cmNode.getId())) {
                                        forceRebuildLogDO.setTargetHostRelateId(cmNode.getId());
                                        forceRebuildLogDAO.update(forceRebuildLogDO);
                                    }
                                }
                            }

                            break;
                        }
                    }
                }

                if (completed) {
                    taskResult.setState(DictConsts.TASK_STATE_SUCCESS);
                    return taskResult;
                } else {
                    Date nowDate = systemDAO.getCurrentSqlDateTime();
                    if ((nowDate.getTime() - subtaskDO.getStartDateTime().getTime()) / 1000 > subtaskDO.getTimeout()) {
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
    }

    private TaskResult pollingBackup(SubtaskDO subtaskDO, String backupFileId) throws Exception {
        TaskResult taskResult = new TaskResult();
        taskResult.setState(DictConsts.TASK_STATE_RUNNING);
        while (true) {
            if (subtaskDO.getState().equals(DictConsts.TASK_STATE_RUNNING)) {
                CmBackupFile cmBackupFile = CmApi.getBackupFile(backupFileId);
                if (cmBackupFile == null) {
                    taskResult.setState(DictConsts.TASK_STATE_FAILED);
                    taskResult.setMsg("none object");
                    return taskResult;
                }

                String state = cmBackupFile.getStatus();
                if (StringUtils.equalsIgnoreCase(state, CmConsts.BACKUP_FILE_COMPLETE)) {
                    taskResult.setState(DictConsts.TASK_STATE_SUCCESS);
                    return taskResult;
                } else if (StringUtils.equalsIgnoreCase(state, CmConsts.BACKUP_FILE_FAILED)) {
                    taskResult.setState(DictConsts.TASK_STATE_FAILED);
                    taskResult.setMsg(cmBackupFile.getMsg());
                    return taskResult;
                } else {
                    Date nowDate = systemDAO.getCurrentSqlDateTime();
                    if ((nowDate.getTime() - subtaskDO.getStartDateTime().getTime()) / 1000 > subtaskDO.getTimeout()) {
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
    }

    private TaskResult pollingRestoreTask(SubtaskDO subtaskDO, String actionId) throws Exception {
        TaskResult taskResult = new TaskResult();
        taskResult.setState(DictConsts.TASK_STATE_RUNNING);
        while (true) {
            if (subtaskDO.getState().equals(DictConsts.TASK_STATE_RUNNING)) {
                CmAction cmAction = CmApi.getAction(actionId);
                if (cmAction == null) {
                    taskResult.setState(DictConsts.TASK_STATE_FAILED);
                    taskResult.setMsg("none object");
                    return taskResult;
                } else {
                    if (StringUtils.equalsIgnoreCase(cmAction.getStatus(), CmConsts.RESTORE_COMPLETE)) {
                        taskResult.setState(DictConsts.TASK_STATE_SUCCESS);
                        return taskResult;
                    } else if (StringUtils.equalsIgnoreCase(cmAction.getStatus(), CmConsts.RESTORE_FAILED)) {
                        taskResult.setState(DictConsts.TASK_STATE_FAILED);
                        taskResult.setMsg(cmAction.getErrMsg());
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
                }
            } else {
                taskResult.setState(subtaskDO.getState());
                taskResult.setMsg(subtaskDO.getMsg());
                return taskResult;
            }
        }
    }

    public ObjModel getObjModel(String unitId) {
        UnitDO unitDO = unitDAO.get(unitId);
        if (unitDO != null) {
            ServDO servDO = servDAO.get(unitDO.getServId());
            if (servDO != null) {
                ServGroupDO servGroupDO = servGroupDAO.get(servDO.getServGroupId());
                if (servGroupDO != null) {
                    return new ObjModel(unitDO.getRelateId(), unitDO.getType(),
                            servGroupDO.getBusinessArea().getSiteId());
                }
            }
        }
        return null;
    }

}