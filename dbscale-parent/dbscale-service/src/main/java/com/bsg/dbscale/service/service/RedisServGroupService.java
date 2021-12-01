package com.bsg.dbscale.service.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bsg.dbscale.cm.api.CmApi;
import com.bsg.dbscale.cm.body.CmResetPwdBody;
import com.bsg.dbscale.cm.body.CmServicesLinkBody;
import com.bsg.dbscale.cm.constant.CmConsts;
import com.bsg.dbscale.cm.model.CmLoadbalancer;
import com.bsg.dbscale.cm.model.CmService;
import com.bsg.dbscale.cm.model.CmServiceArch;
import com.bsg.dbscale.cm.model.CmSite;
import com.bsg.dbscale.cm.query.CmLoadbalancerQuery;
import com.bsg.dbscale.cm.query.CmServiceQuery;
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
import com.bsg.dbscale.dao.domain.UserDO;
import com.bsg.dbscale.service.check.CheckResult;
import com.bsg.dbscale.service.check.RedisServGroupCheck;
import com.bsg.dbscale.service.constant.Consts;
import com.bsg.dbscale.service.constant.DictConsts;
import com.bsg.dbscale.service.constant.DictTypeConsts;
import com.bsg.dbscale.service.dto.ArchBaseDTO;
import com.bsg.dbscale.service.dto.DisplayDTO;
import com.bsg.dbscale.service.dto.RedisServDTO;
import com.bsg.dbscale.service.dto.RedisServGroupDTO;
import com.bsg.dbscale.service.dto.RedisServGroupDetailDTO;
import com.bsg.dbscale.service.dto.ScaleBaseDTO;
import com.bsg.dbscale.service.dto.SentinelServDTO;
import com.bsg.dbscale.service.dto.ServDTO;
import com.bsg.dbscale.service.dto.ServGroupDTO;
import com.bsg.dbscale.service.dto.VersionDTO;
import com.bsg.dbscale.service.form.ResetPwdForm;
import com.bsg.dbscale.service.form.ServArchUpForm;
import com.bsg.dbscale.service.form.ServImageForm;
import com.bsg.dbscale.service.form.ServScaleCpuMemForm;
import com.bsg.dbscale.service.form.ServScaleStorageForm;
import com.bsg.dbscale.service.query.ServGroupQuery;
import com.bsg.dbscale.service.util.PrimaryKeyUtil;
import com.bsg.dbscale.service.util.TaskResult;

@Service
public class RedisServGroupService extends ServGroupService {

    @Autowired
    private RedisServGroupCheck redisServGroupCheck;

    @Autowired
    private RedisServService redisServService;

    @Autowired
    private SentinelServService sentinelServService;

    public Result list(ServGroupQuery servGroupQuery, String activeUsername) throws Exception {
        List<RedisServGroupDTO> redisServGroupDTOs = new ArrayList<>();
        servGroupQuery.setCategory(Consts.SERV_GROUP_TYPE_REDIS);
        List<ServGroupDO> servGroupDOs = listQualifiedData(servGroupQuery, activeUsername);
        if (servGroupDOs.size() > 0) {
            List<CmSite> cmSites = CmApi.listSite(null);
            List<CmServiceArch> cmServiceArchs = CmApi.listServiceArch(null);
            List<DictTypeDO> dictTypeDOs = dictTypeDAO.list();
            List<UserDO> userDOs = userDAO.list(null);

            List<TaskDO> latestTaskDOs = taskDAO.listLatest(servGroupQuery.getSiteId(), DictConsts.OBJ_TYPE_SERV_GROUP);

            CmServiceQuery cmServiceQuery = new CmServiceQuery();
            cmServiceQuery.setSiteId(servGroupQuery.getSiteId());
            cmServiceQuery.setGroupType(Consts.SERV_GROUP_TYPE_REDIS);
            List<CmService> cmServices = CmApi.listService(cmServiceQuery);

            CmLoadbalancerQuery cmLoadbalancerQuery = new CmLoadbalancerQuery();
            cmLoadbalancerQuery.setSiteId(servGroupQuery.getSiteId());
            cmLoadbalancerQuery.setGroupType(Consts.SERV_GROUP_TYPE_REDIS);
            List<CmLoadbalancer> cmLoadbalancers = CmApi.listLoadbalancer(cmLoadbalancerQuery);

            for (ServGroupDO servGroupDO : servGroupDOs) {
                List<CmService> eligibleCmServices = CmApi.findServiceByGroupName(cmServices, servGroupDO.getName());
                String state = getState(servGroupDO, eligibleCmServices);
                if (servGroupQuery.getState() == null || StringUtils.equals(servGroupQuery.getState(), state)) {
                    RedisServGroupDTO servGroupDTO = new RedisServGroupDTO();
                    redisServGroupDTOs.add(servGroupDTO);

                    CmSite cmSite = null;
                    BusinessAreaDO businessAreaDO = servGroupDO.getBusinessArea();
                    if (businessAreaDO != null) {
                        cmSite = CmApi.findSite(cmSites, businessAreaDO.getSiteId());
                    }

                    ServDO sentinelServDO = findAnyServDOType(servGroupDO, Consts.SERV_TYPE_SENTINEL);
                    CmLoadbalancer cmLoadbalancer = null;
                    if (sentinelServDO != null) {
                        cmLoadbalancer = CmApi.findLoadbalancer(cmLoadbalancers, sentinelServDO.getRelateId());
                    }

                    UserDO owner = findUserDO(userDOs, servGroupDO.getOwner());
                    TaskDO latestTaskDO = findLasterTaskDO(latestTaskDOs, DictConsts.OBJ_TYPE_SERV_GROUP,
                            servGroupDO.getId());
                    setServGroupDTO(servGroupDTO, servGroupDO, cmSite, eligibleCmServices, cmServiceArchs, dictTypeDOs,
                            owner, latestTaskDO, cmLoadbalancer);
                }
            }
        }
        return Result.success(redisServGroupDTOs);
    }

    public Result get(String servGroupId, String type) throws Exception {
        RedisServGroupDetailDTO servGroupDetailDTO = null;
        ServGroupDO servGroupDO = servGroupDAO.get(servGroupId);
        if (servGroupDO == null || !servGroupDO.getCategory().equals(Consts.SERV_GROUP_TYPE_REDIS)) {
            return Result.success(servGroupDetailDTO);
        }
        servGroupDetailDTO = new RedisServGroupDetailDTO();
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
        cmServiceQuery.setType(type);
        List<CmService> cmServices = CmApi.listServiceDetail(cmServiceQuery);

        setServGroupDetailDTO(servGroupDetailDTO, servGroupDO, cmSite, cmServices, dictTypeDOs, owner,
                servGroupLatestTaskDO);

        List<ServDTO> servDTOs = new ArrayList<>();
        servGroupDetailDTO.setServs(servDTOs);

        List<ServDO> servDOs = servGroupDO.getServs();
        List<CmServiceArch> cmServiceArchs = CmApi.listServiceArch(null);
        for (ServDO servDO : servDOs) {
            if (StringUtils.isBlank(type) || servDO.getType().equals(type)) {
                CmService cmService = CmApi.findService(cmServices, servDO.getRelateId());
                CmServiceArch cmServiceArch = CmApi.findServiceArch(cmServiceArchs, servDO.getType(),
                        servDO.getMajorVersion() + "." + servDO.getMinorVersion(), servDO.getArchMode(),
                        servDO.getUnitCnt());
                DefServDO defServDO = findDefServDO(defServDOs, servDO.getType());
                switch (servDO.getType()) {
                case Consts.SERV_TYPE_REDIS:
                    RedisServDTO redisServDTO = redisServService.getServDTO(servDO, cmService, cmServiceArch, defServDO,
                            dictTypeDOs);
                    servDTOs.add(redisServDTO);
                    break;
                case Consts.SERV_TYPE_SENTINEL:
                    CmLoadbalancer cmLoadbalancer = null;
                    if (StringUtils.isNotBlank(servDO.getRelateId())) {
                        cmLoadbalancer = CmApi.getLoadbalancer(servDO.getRelateId());
                    }
                    SentinelServDTO sentinelServDTO = sentinelServService.getServDTO(servDO, cmService, cmServiceArch,
                            cmLoadbalancer, defServDO, dictTypeDOs);
                    servDTOs.add(sentinelServDTO);
                    break;
                default:
                    break;
                }
            }
        }

        return Result.success(servGroupDetailDTO);
    }

    @Transactional(rollbackFor = Exception.class)
    public Result start(String servGroupId, String activeUsername) throws Exception {
        ServGroupDO servGroupDO = servGroupDAO.get(servGroupId);

        CheckResult checkResult = redisServGroupCheck.checkStart(servGroupDO);
        if (checkResult.getCode() != CheckResult.SUCCESS) {
            logger.error(checkResult.getMsg());
            return Result.failure(checkResult);
        }

        return super.start(servGroupDO, activeUsername);
    }

    @Transactional(rollbackFor = Exception.class)
    public Result stop(String servGroupId, String activeUsername) throws Exception {
        ServGroupDO servGroupDO = servGroupDAO.get(servGroupId);

        CheckResult checkResult = redisServGroupCheck.checkStop(servGroupDO);
        if (checkResult.getCode() != CheckResult.SUCCESS) {
            logger.error(checkResult.getMsg());
            return Result.failure(checkResult);
        }

        return super.stop(servGroupDO, activeUsername);
    }

    @Transactional(rollbackFor = Exception.class)
    public Result imageUpdate(String servGroupId, ServImageForm imageForm, String activeUsername) throws Exception {
        ServGroupDO servGroupDO = servGroupDAO.get(servGroupId);

        CheckResult checkResult = redisServGroupCheck.checkImageUpdate(servGroupDO, imageForm);
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

        CheckResult checkResult = redisServGroupCheck.checkScaleUpCpuMem(servGroupDO, scaleCpuMemForm);
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

        CheckResult checkResult = redisServGroupCheck.checkScaleUpStorage(servGroupDO, scaleStorageForm);
        if (checkResult.getCode() != CheckResult.SUCCESS) {
            logger.error(checkResult.getMsg());
            return Result.failure(checkResult);
        }

        return super.scaleUpStorage(servGroupDO, scaleStorageForm, activeUsername);
    }

    @Transactional(rollbackFor = Exception.class)
    public Result archUp(String servGroupId, ServArchUpForm archUpForm, String activeUsername) throws Exception {
        ServGroupDO servGroupDO = servGroupDAO.get(servGroupId);

        CheckResult checkResult = redisServGroupCheck.checkArchUp(servGroupDO, archUpForm);
        if (checkResult.getCode() != CheckResult.SUCCESS) {
            logger.error(checkResult.getMsg());
            return Result.failure(checkResult);
        }

        return super.archUp(servGroupDO, archUpForm, activeUsername);
    }

    @Transactional(rollbackFor = Exception.class)
    public Result resetPwd(String servGroupId, ResetPwdForm resetPwdForm, String activeUsername) throws Exception {
        ServGroupDO servGroupDO = servGroupDAO.get(servGroupId);

        CheckResult checkResult = redisServGroupCheck.checkResetPwd(servGroupDO, resetPwdForm);
        if (checkResult.getCode() != CheckResult.SUCCESS) {
            logger.error(checkResult.getMsg());
            return Result.failure(checkResult);
        }

        CmResetPwdBody cmResetPwdBody = new CmResetPwdBody();
        cmResetPwdBody.setType(CmConsts.PWD_TYPE_NATIVE);
        cmResetPwdBody.setPwd(resetPwdForm.getPwd());

        List<ServDO> servDOs = servGroupDO.getServs();
        List<Future<TaskResult>> futures = new ArrayList<Future<TaskResult>>();
        for (ServDO servDO : servDOs) {
            Future<TaskResult> future = executor.submit(new Callable<TaskResult>() {
                @Override
                public TaskResult call() throws Exception {
                    TaskResult result = new TaskResult();
                    result.setState(DictConsts.TASK_STATE_SUCCESS);
                    try {
                        CmApi.resetPwd(servDO.getRelateId(), cmResetPwdBody);
                    } catch (Exception e) {
                        result.setState(DictConsts.TASK_STATE_FAILED);
                        result.setMsg(e.getMessage());
                    }
                    return result;
                }
            });
            futures.add(future);
        }

        for (Future<TaskResult> future : futures) {
            TaskResult taskResult = future.get();
            if (!taskResult.getState().equals(DictConsts.TASK_STATE_SUCCESS)) {
                return Result.failure(taskResult.getMsg());
            }
        }

        return Result.success();
    }

    @Transactional(rollbackFor = Exception.class)
    public Result remove(String servGroupId, String activeUsername) throws Exception {
        ServGroupDO servGroupDO = servGroupDAO.get(servGroupId);

        CheckResult checkResult = redisServGroupCheck.checkRemove(servGroupDO);
        if (checkResult.getCode() != CheckResult.SUCCESS) {
            logger.error(checkResult.getMsg());
            return Result.failure(checkResult);
        }

        return super.remove(servGroupDO, activeUsername);
    }

    @Override
    public TaskDO buildCreateTask(ServGroupDO servGroupDO, String activeUsername, Date nowDate) throws Exception {
        TaskDO taskDO = super.buildCreateTask(servGroupDO, activeUsername, nowDate);
        List<SubtaskDO> subtaskDOs = taskDO.getSubtasks();
        int priority = subtaskDOs.get(subtaskDOs.size() - 1).getPriority();
        priority++;

        List<SubtaskDO> subtask_links = buildRedisLinkSubtaskDOs(taskDO, servGroupDO, priority);
        if (subtask_links.size() > 0) {
            subtaskDOs.addAll(subtask_links);
            priority++;
        }

        SubtaskDO subtask_cmhaLink = buildSentinelLinkSubtaskDO(taskDO, servGroupDO, priority);
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

        return taskDO;
    }

    public List<SubtaskDO> buildRedisLinkSubtaskDOs(TaskDO taskDO, ServGroupDO servGroupDO, int priority) {
        List<SubtaskDO> subtaskDOs = new ArrayList<>();
        List<ServDO> servDOs = servGroupDO.getServs();
        if (servDOs.size() > 1) {
            for (ServDO servDO : servDOs) {
                if (servDO.getType().equals(Consts.SERV_TYPE_REDIS)) {
                    SubtaskDO subtask_link = new SubtaskDO();
                    subtaskDOs.add(subtask_link);

                    subtask_link.setId(PrimaryKeyUtil.get());
                    subtask_link.setTaskId(taskDO.getId());
                    subtask_link.setObjType(DictConsts.OBJ_TYPE_SERV);
                    subtask_link.setObjId(servDO.getId());
                    subtask_link.setObjName(servDO.getType());
                    subtask_link.setActionType(DictConsts.ACTION_TYPE_LINK);
                    subtask_link.setPriority(priority);
                    subtask_link.setState(DictConsts.TASK_STATE_READY);
                    SubtaskCfgDO subtaskCfgDO = subtaskCfgDAO.get(DictConsts.OBJ_TYPE_SERV,
                            DictConsts.ACTION_TYPE_LINK);
                    if (subtaskCfgDO != null) {
                        subtask_link.setTimeout(subtaskCfgDO.getTimeout());
                    }
                    subtask_link.setDataSource(servGroupDO);
                }
            }
        }
        return subtaskDOs;
    }

    public SubtaskDO buildSentinelLinkSubtaskDO(TaskDO taskDO, ServGroupDO servGroupDO, int priority) {
        SubtaskDO subtask_link = null;
        ServDO sentinelServDO = findAnyServDOType(servGroupDO, Consts.SERV_TYPE_SENTINEL);
        if (sentinelServDO != null) {
            subtask_link = new SubtaskDO();

            subtask_link.setId(PrimaryKeyUtil.get());
            subtask_link.setTaskId(taskDO.getId());
            subtask_link.setObjType(DictConsts.OBJ_TYPE_SERV);
            subtask_link.setObjId(sentinelServDO.getId());
            subtask_link.setObjName(sentinelServDO.getType());
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
        ServDO sentinelServDO = findAnyServDOType(servGroupDO, Consts.SERV_TYPE_SENTINEL);
        if (sentinelServDO != null) {
            subtask_saveLoadbalance = new SubtaskDO();
            subtask_saveLoadbalance.setId(PrimaryKeyUtil.get());
            subtask_saveLoadbalance.setTaskId(taskDO.getId());
            subtask_saveLoadbalance.setObjType(DictConsts.OBJ_TYPE_SERV);
            subtask_saveLoadbalance.setObjId(sentinelServDO.getId());
            subtask_saveLoadbalance.setObjName(sentinelServDO.getType());
            subtask_saveLoadbalance.setActionType(DictConsts.ACTION_TYPE_SAVE_LOADBALANCER);
            subtask_saveLoadbalance.setPriority(priority);
            subtask_saveLoadbalance.setState(DictConsts.TASK_STATE_READY);
            SubtaskCfgDO subtaskCfgDO = subtaskCfgDAO.get(DictConsts.OBJ_TYPE_SERV,
                    DictConsts.ACTION_TYPE_SAVE_LOADBALANCER);
            if (subtaskCfgDO != null) {
                subtask_saveLoadbalance.setTimeout(subtaskCfgDO.getTimeout());
            }
            subtask_saveLoadbalance.setDataSource(sentinelServDO);
        }
        return subtask_saveLoadbalance;
    }

    @Override
    public TaskDO buildStartTask(ServGroupDO servGroupDO, String activeUsername, Date nowDate) {
        TaskDO taskDO = super.buildStartTask(servGroupDO, activeUsername, nowDate);
        List<SubtaskDO> subtaskDOs = taskDO.getSubtasks();
        for (SubtaskDO subtaskDO : subtaskDOs) {
            ServDO servDO = (ServDO) subtaskDO.getDataSource();
            switch (servDO.getType()) {
            case Consts.SERV_TYPE_REDIS:
                subtaskDO.setPriority(1);
                break;
            case Consts.SERV_TYPE_SENTINEL:
                subtaskDO.setPriority(2);
                break;
            default:
                break;
            }
        }
        return taskDO;
    }

    @Override
    public TaskDO buildStopTask(ServGroupDO servGroupDO, String activeUsername, Date nowDate) {
        TaskDO taskDO = super.buildStopTask(servGroupDO, activeUsername, nowDate);
        List<SubtaskDO> subtaskDOs = taskDO.getSubtasks();
        for (SubtaskDO subtaskDO : subtaskDOs) {
            ServDO servDO = (ServDO) subtaskDO.getDataSource();
            switch (servDO.getType()) {
            case Consts.SERV_TYPE_REDIS:
                subtaskDO.setPriority(2);
                break;
            case Consts.SERV_TYPE_SENTINEL:
                subtaskDO.setPriority(1);
                break;
            default:
                break;
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
        ServDO sentinelServDO = findAnyServDOType(servDOs, Consts.SERV_TYPE_SENTINEL);

        OrderGroupDO orderGroupDO = orderGroupDAO.get(servGroupDO.getOrderGroupId());
        List<OrderDO> orderDOs = orderGroupDO.getOrders();
        for (OrderDO orderDO : orderDOs) {
            if (orderDO.getMajorVersion() != null) {
                List<SubtaskDO> subtaskDOs = new ArrayList<>();
                taskDO.setSubtasks(subtaskDOs);

                int priority = 1;
                if (sentinelServDO != null && orderDO.getType().equals(Consts.SERV_TYPE_REDIS)) {
                    SubtaskDO subtask = new SubtaskDO();
                    subtaskDOs.add(subtask);

                    subtask.setId(PrimaryKeyUtil.get());
                    subtask.setTaskId(taskDO.getId());
                    subtask.setObjType(DictConsts.OBJ_TYPE_SERV);
                    subtask.setObjId(sentinelServDO.getId());
                    subtask.setObjName(sentinelServDO.getType());
                    subtask.setActionType(DictConsts.ACTION_TYPE_STOP);
                    subtask.setPriority(priority);
                    subtask.setState(DictConsts.TASK_STATE_READY);
                    SubtaskCfgDO subtaskCfgDO = subtaskCfgDAO.get(DictConsts.OBJ_TYPE_SERV,
                            DictConsts.ACTION_TYPE_STOP);
                    if (subtaskCfgDO != null) {
                        subtask.setTimeout(subtaskCfgDO.getTimeout());
                    }
                    subtask.setDataSource(sentinelServDO);
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
                if (sentinelServDO != null && orderDO.getType().equals(Consts.SERV_TYPE_REDIS)) {
                    SubtaskDO subtask = new SubtaskDO();
                    subtaskDOs.add(subtask);

                    subtask.setId(PrimaryKeyUtil.get());
                    subtask.setTaskId(taskDO.getId());
                    subtask.setObjType(DictConsts.OBJ_TYPE_SERV);
                    subtask.setObjId(sentinelServDO.getId());
                    subtask.setObjName(sentinelServDO.getType());
                    subtask.setActionType(DictConsts.ACTION_TYPE_START);
                    subtask.setPriority(priority);
                    subtask.setState(DictConsts.TASK_STATE_READY);
                    SubtaskCfgDO subtaskCfgDO = subtaskCfgDAO.get(DictConsts.OBJ_TYPE_SERV,
                            DictConsts.ACTION_TYPE_START);
                    if (subtaskCfgDO != null) {
                        subtask.setTimeout(subtaskCfgDO.getTimeout());
                    }
                    subtask.setDataSource(sentinelServDO);
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
        ServDO sentinelServDO = findAnyServDOType(servDOs, Consts.SERV_TYPE_SENTINEL);

        OrderGroupDO orderGroupDO = orderGroupDAO.get(servGroupDO.getOrderGroupId());
        List<OrderDO> orderDOs = orderGroupDO.getOrders();
        for (OrderDO orderDO : orderDOs) {
            if (orderDO.getCpuCnt() != null) {
                List<SubtaskDO> subtaskDOs = new ArrayList<>();
                taskDO.setSubtasks(subtaskDOs);

                int priority = 1;
                if (sentinelServDO != null && orderDO.getType().equals(Consts.SERV_TYPE_REDIS)) {
                    SubtaskDO subtask = new SubtaskDO();
                    subtaskDOs.add(subtask);

                    subtask.setId(PrimaryKeyUtil.get());
                    subtask.setTaskId(taskDO.getId());
                    subtask.setObjType(DictConsts.OBJ_TYPE_SERV);
                    subtask.setObjId(sentinelServDO.getId());
                    subtask.setObjName(sentinelServDO.getType());
                    subtask.setActionType(DictConsts.ACTION_TYPE_STOP);
                    subtask.setPriority(priority);
                    subtask.setState(DictConsts.TASK_STATE_READY);
                    SubtaskCfgDO subtaskCfgDO = subtaskCfgDAO.get(DictConsts.OBJ_TYPE_SERV,
                            DictConsts.ACTION_TYPE_STOP);
                    if (subtaskCfgDO != null) {
                        subtask.setTimeout(subtaskCfgDO.getTimeout());
                    }
                    subtask.setDataSource(sentinelServDO);
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
                if (sentinelServDO != null && orderDO.getType().equals(Consts.SERV_TYPE_REDIS)) {
                    SubtaskDO subtask = new SubtaskDO();
                    subtaskDOs.add(subtask);

                    subtask.setId(PrimaryKeyUtil.get());
                    subtask.setTaskId(taskDO.getId());
                    subtask.setObjType(DictConsts.OBJ_TYPE_SERV);
                    subtask.setObjId(sentinelServDO.getId());
                    subtask.setObjName(sentinelServDO.getType());
                    subtask.setActionType(DictConsts.ACTION_TYPE_START);
                    subtask.setPriority(priority);
                    subtask.setState(DictConsts.TASK_STATE_READY);
                    SubtaskCfgDO subtaskCfgDO = subtaskCfgDAO.get(DictConsts.OBJ_TYPE_SERV,
                            DictConsts.ACTION_TYPE_START);
                    if (subtaskCfgDO != null) {
                        subtask.setTimeout(subtaskCfgDO.getTimeout());
                    }
                    subtask.setDataSource(sentinelServDO);
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
                ServDO sentinelServDO = findAnyServDOType(servGroupDO, Consts.SERV_TYPE_SENTINEL);
                if (sentinelServDO != null) {
                    subtask_removeLoadbalance = new SubtaskDO();
                    subtaskDOs.add(subtask_removeLoadbalance);

                    subtask_removeLoadbalance.setId(PrimaryKeyUtil.get());
                    subtask_removeLoadbalance.setTaskId(taskDO.getId());
                    subtask_removeLoadbalance.setObjType(DictConsts.OBJ_TYPE_SERV);
                    subtask_removeLoadbalance.setObjId(sentinelServDO.getId());
                    subtask_removeLoadbalance.setObjName(sentinelServDO.getType());
                    subtask_removeLoadbalance.setActionType(DictConsts.ACTION_TYPE_REMOVE_LOADBALANCER);
                    subtask_removeLoadbalance.setPriority(priority);
                    subtask_removeLoadbalance.setState(DictConsts.TASK_STATE_READY);
                    SubtaskCfgDO subtaskCfgDO = subtaskCfgDAO.get(DictConsts.OBJ_TYPE_SERV,
                            DictConsts.ACTION_TYPE_REMOVE_LOADBALANCER);
                    if (subtaskCfgDO != null) {
                        subtask_removeLoadbalance.setTimeout(subtaskCfgDO.getTimeout());
                    }
                    subtask_removeLoadbalance.setDataSource(sentinelServDO);
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
                ServDO sentinelServDO = (ServDO) subtaskDO.getDataSource();
                CmApi.saveLoadbalancer(sentinelServDO.getRelateId());

                taskResult = pollingSaveLoadbalancerTask(subtaskDO);
            } catch (Exception e) {
                taskResult.setState(DictConsts.TASK_STATE_FAILED);
                taskResult.setMsg(e.toString());
                logger.error("注册loadbalance异常：", e);
            }
            break;
        case DictConsts.ACTION_TYPE_REMOVE_LOADBALANCER:
            try {
                ServDO sentinelServDO = (ServDO) subtaskDO.getDataSource();
                if (StringUtils.isNotBlank(sentinelServDO.getRelateId())) {
                    CmApi.removeLoadbalancer(sentinelServDO.getRelateId());
                }

                taskResult.setState(DictConsts.TASK_STATE_SUCCESS);
            } catch (Exception e) {
                taskResult.setState(DictConsts.TASK_STATE_FAILED);
                taskResult.setMsg(e.toString());
                logger.error("删除loadbalance异常：", e);
            }
            break;
        }
        return taskResult;
    }

    private TaskResult pollingSaveLoadbalancerTask(SubtaskDO subtaskDO) throws Exception {
        TaskResult taskResult = new TaskResult();
        taskResult.setState(DictConsts.TASK_STATE_RUNNING);
        ServDO sentinelServDO = (ServDO) subtaskDO.getDataSource();
        while (true) {
            if (subtaskDO.getState().equals(DictConsts.TASK_STATE_RUNNING)) {
                CmLoadbalancer cmLoadbalancer = CmApi.getLoadbalancer(sentinelServDO.getRelateId());
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

    private void setServGroupDTO(RedisServGroupDTO servGroupDTO, ServGroupDO servGroupDO, CmSite cmSite,
            List<CmService> cmServices, List<CmServiceArch> cmServiceArchs, List<DictTypeDO> dictTypeDOs, UserDO owner,
            TaskDO latestTaskDO, CmLoadbalancer cmLoadbalancer) {
        setServGroupStateBaseDTO(servGroupDTO, servGroupDO, cmSite, cmServices, dictTypeDOs, owner, latestTaskDO);

        List<ServDO> servDOs = servGroupDO.getServs();
        int redisServCnt = 0;
        for (ServDO servDO : servDOs) {
            if (servDO.getType().equals(Consts.SERV_TYPE_REDIS)) {
                redisServCnt++;
                ScaleBaseDTO scaleDTO = new ScaleBaseDTO();
                String scaleName = getScaleName(servDO.getCpuCnt(), servDO.getMemSize());
                scaleDTO.setName(scaleName);
                scaleDTO.setCpuCnt(servDO.getCpuCnt());
                scaleDTO.setMemSize(servDO.getMemSize());
                servGroupDTO.setScale(scaleDTO);

                ServGroupDTO.ImageDTO imageDTO = servGroupDTO.new ImageDTO();
                servGroupDTO.setImage(imageDTO);

                imageDTO.setType(servDO.getType());
                VersionDTO versionDTO = new VersionDTO();
                imageDTO.setVersion(versionDTO);

                versionDTO.setMajor(servDO.getMajorVersion());
                versionDTO.setMinor(servDO.getMinorVersion());
                versionDTO.setPatch(servDO.getPatchVersion());
                versionDTO.setBuild(servDO.getBuildVersion());

                DisplayDTO diskTypeDisplayDTO = new DisplayDTO();
                servGroupDTO.setDiskType(diskTypeDisplayDTO);
                diskTypeDisplayDTO.setCode(servDO.getDiskType());
                DictDO diskTypeDictDO = findDictDO(dictTypeDOs, DictTypeConsts.DISK_TYPE, servDO.getDiskType());
                if (diskTypeDictDO != null) {
                    diskTypeDisplayDTO.setDisplay(diskTypeDictDO.getName());
                }

                servGroupDTO.setDataSize(servDO.getDataSize());
                servGroupDTO.setLogSize(servDO.getLogSize());

                ArchBaseDTO archDTO = new ArchBaseDTO();
                DisplayDTO modeDisplayDTO = new DisplayDTO();
                archDTO.setMode(modeDisplayDTO);

                modeDisplayDTO.setCode(servDO.getArchMode());
                DictDO modeDictDO = findDictDO(dictTypeDOs, DictTypeConsts.ARCH_MODE, servDO.getArchMode());
                if (modeDictDO != null) {
                    modeDisplayDTO.setDisplay(modeDictDO.getName());
                } else {
                    modeDisplayDTO.setDisplay(servDO.getArchMode());
                }
                archDTO.setUnitCnt(servDO.getUnitCnt());

                CmServiceArch cmServiceArch = CmApi.findServiceArch(cmServiceArchs, servDO.getType(),
                        servDO.getMajorVersion() + "." + servDO.getMinorVersion(), servDO.getArchMode(),
                        servDO.getUnitCnt());
                if (cmServiceArch != null) {
                    archDTO.setName(cmServiceArch.getDesc());
                }
                servGroupDTO.setArch(archDTO);
            }
        }

        servGroupDTO.setRedisServCnt(redisServCnt);

        List<String> addresses = new ArrayList<>();
        servGroupDTO.setAddresses(addresses);
        if (cmSite != null && StringUtils.isNotBlank(cmSite.getLoadbalancer())
                && !StringUtils.equalsIgnoreCase(CmConsts.LOADBALANCER_NONE, cmSite.getLoadbalancer())) {
            if (cmLoadbalancer != null) {
                ServDO sentinelServDO = findAnyServDOType(servGroupDO, Consts.SERV_TYPE_SENTINEL);
                List<String> ips = cmLoadbalancer.getIps();
                if (ips != null) {
                    for (String ip : ips) {
                        addresses.add(ip + ":" + sentinelServDO.getPort());
                    }
                }
            }
        } else {
            String addressType = Consts.SERV_TYPE_REDIS;
            for (ServDO servDO : servDOs) {
                if (servDO.getType().equals(Consts.SERV_TYPE_SENTINEL)) {
                    addressType = Consts.SERV_TYPE_SENTINEL;
                    break;
                }
            }
            for (ServDO servDO : servDOs) {
                if (servDO.getType().equals(addressType)) {
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

    public CmServicesLinkBody buildCmServicesLinkRequestBody(String servId, ServGroupDO servGroupDO) throws Exception {
        CmServicesLinkBody cmServicesLinkBody = new CmServicesLinkBody();
        cmServicesLinkBody.setServiceGroupType(servGroupDO.getCategory());
        servGroupDO = servGroupDAO.get(servGroupDO.getId());

        List<String> relateIds = new ArrayList<>();
        cmServicesLinkBody.setSerevies(relateIds);

        List<ServDO> servDOs = servGroupDO.getServs();
        for (ServDO servDO : servDOs) {
            relateIds.add(servDO.getRelateId());
        }
        return cmServicesLinkBody;
    }

}