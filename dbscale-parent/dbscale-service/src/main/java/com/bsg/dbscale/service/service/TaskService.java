package com.bsg.dbscale.service.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bsg.dbscale.cm.api.CmApi;
import com.bsg.dbscale.cm.model.CmSite;
import com.bsg.dbscale.dao.domain.DictDO;
import com.bsg.dbscale.dao.domain.DictTypeDO;
import com.bsg.dbscale.dao.domain.OrderGroupDO;
import com.bsg.dbscale.dao.domain.ServGroupDO;
import com.bsg.dbscale.dao.domain.SubtaskDO;
import com.bsg.dbscale.dao.domain.TaskDO;
import com.bsg.dbscale.dao.domain.UserDO;
import com.bsg.dbscale.service.check.CheckResult;
import com.bsg.dbscale.service.check.TaskCheck;
import com.bsg.dbscale.service.constant.DictConsts;
import com.bsg.dbscale.service.constant.DictTypeConsts;
import com.bsg.dbscale.service.dto.DisplayDTO;
import com.bsg.dbscale.service.dto.IdentificationDTO;
import com.bsg.dbscale.service.dto.InfoDTO;
import com.bsg.dbscale.service.dto.TaskDTO;
import com.bsg.dbscale.service.dto.TaskDetailDTO;
import com.bsg.dbscale.service.dto.UserBaseDTO;
import com.bsg.dbscale.service.query.TaskQuery;
import com.bsg.dbscale.util.DateUtils;

@Service
public class TaskService extends BaseService {

    @Autowired
    private TaskCheck taskCheck;

    public Result list(TaskQuery taskQuery) throws Exception {
        List<TaskDTO> taskDTOs = new ArrayList<>();
        com.bsg.dbscale.dao.query.TaskQuery daoQuery = convertToDAOQuery(taskQuery);

        List<TaskDO> taskDOs = taskDAO.list(daoQuery);
        if (taskDOs.size() > 0) {
            List<CmSite> cmSites = CmApi.listSite(null);
            List<UserDO> userDOs = userDAO.list(null);
            List<DictTypeDO> dictTypeDOs = dictTypeDAO.list();
            for (TaskDO taskDO : taskDOs) {
                CmSite cmSite = CmApi.findSite(cmSites, taskDO.getSiteId());
                TaskDTO taskDTO = new TaskDTO();
                setTaskDTO(taskDTO, taskDO, userDOs, dictTypeDOs, cmSite);
                taskDTOs.add(taskDTO);
            }
        }
        return Result.success(taskDTOs);
    }

    public Result get(String taskId) throws Exception {
        TaskDetailDTO taskDetailDTO = null;
        TaskDO taskDO = taskDAO.get(taskId);
        if (taskDO != null) {
            List<UserDO> userDOs = userDAO.list(null);
            List<DictTypeDO> dictTypeDOs = dictTypeDAO.list();
            CmSite cmSite = CmApi.getSite(taskDO.getSiteId());
            taskDetailDTO = new TaskDetailDTO();
            setTaskDetailDTO(taskDetailDTO, taskDO, userDOs, dictTypeDOs, cmSite);
        }

        return Result.success(taskDetailDTO);
    }

    public Result cancel(String taskId) throws Exception {
        TaskDTO taskDTO = null;
        TaskDO taskDO = taskDAO.get(taskId);

        CheckResult checkResult = taskCheck.checkCancel(taskDO);
        if (checkResult.getCode() != CheckResult.SUCCESS) {
            logger.error(checkResult.getMsg());
            return Result.failure(checkResult);
        }

        if (taskDO.getObjType().equals(DictConsts.OBJ_TYPE_SERV_GROUP)) {
            ServGroupDO servGroupDO = servGroupDAO.get(taskDO.getObjId());
            if (servGroupDO != null) {
                OrderGroupDO orderGroupDO = orderGroupDAO.get(servGroupDO.getOrderGroupId());
                if (orderGroupDO != null) {
                    switch (taskDO.getActionType()) {
                    case DictConsts.ACTION_TYPE_CREATE:
                        servGroupDO.setFlag(false);
                        servGroupDAO.update(servGroupDO);
                    case DictConsts.ACTION_TYPE_IMAGE_UPDATE:
                    case DictConsts.ACTION_TYPE_SCALE_UP_CPUMEM:
                    case DictConsts.ACTION_TYPE_SCALE_UP_STORAGE:
                    case DictConsts.ACTION_TYPE_ARCH_UP:
                    case DictConsts.ACTION_TYPE_REMOVE:
                        orderGroupDO.setState(DictConsts.ORDER_STATE_FAILED);
                        orderGroupDAO.updateStateAndMsg(orderGroupDO);
                        break;
                    default:
                        break;
                    }
                }
            }
        }

        Date nowDate = systemDAO.getCurrentSqlDateTime();
        taskDO.setEndDateTime(nowDate);
        taskDO.setState(DictConsts.TASK_STATE_FAILED);

        List<SubtaskDO> subtaskDOs = taskDO.getSubtasks();
        for (SubtaskDO subtaskDO : subtaskDOs) {
            if (subtaskDO.getState().equals(DictConsts.TASK_STATE_RUNNING)) {
                subtaskDO.setState(DictConsts.TASK_STATE_FAILED);
                subtaskDO.setEndDateTime(nowDate);
                subtaskDO.setMsg("任务取消。");
                subtaskDAO.updateToEnd(subtaskDO);
            }
        }

        taskDAO.updateToEnd(taskDO);

        return Result.success(taskDTO);
    }

    private com.bsg.dbscale.dao.query.TaskQuery convertToDAOQuery(TaskQuery taskQuery) {
        com.bsg.dbscale.dao.query.TaskQuery daoQuery = new com.bsg.dbscale.dao.query.TaskQuery();
        daoQuery.setSiteId(taskQuery.getSiteId());
        daoQuery.setObjType(taskQuery.getObjType());
        daoQuery.setObjId(taskQuery.getObjId());
        daoQuery.setOwner(taskQuery.getOwner());
        if (taskQuery.getStart() != null) {
            daoQuery.setStartDate(new Date(taskQuery.getStart() * 1000));
        }
        if (taskQuery.getEnd() != null) {
            daoQuery.setEndDate(new Date(taskQuery.getEnd() * 1000));
        }
        return daoQuery;
    }

    private void setTaskDTO(TaskDTO taskDTO, TaskDO taskDO, List<UserDO> userDOs, List<DictTypeDO> dictTypeDOs,
            CmSite cmSite) {
        taskDTO.setId(taskDO.getId());

        IdentificationDTO siteDTO = new IdentificationDTO();
        siteDTO.setId(taskDO.getSiteId());
        if (cmSite != null) {
            siteDTO.setName(cmSite.getName());
        }
        taskDTO.setSite(siteDTO);

        DisplayDTO objTypeDisplayDTO = new DisplayDTO();
        taskDTO.setObjType(objTypeDisplayDTO);
        objTypeDisplayDTO.setCode(taskDO.getObjType());

        DictDO objTypeDictDO = findDictDO(dictTypeDOs, DictTypeConsts.OBJ_TYPE, taskDO.getObjType());
        if (objTypeDictDO != null) {
            objTypeDisplayDTO.setDisplay(objTypeDictDO.getName());
        }

        taskDTO.setObjId(taskDO.getObjId());
        taskDTO.setObjName(taskDO.getObjName());

        DisplayDTO actionDisplayDTO = new DisplayDTO();
        taskDTO.setAction(actionDisplayDTO);
        actionDisplayDTO.setCode(taskDO.getActionType());

        DictDO actionDictDO = findDictDO(dictTypeDOs, DictTypeConsts.ACTION_TYPE, taskDO.getActionType());
        if (actionDictDO != null) {
            actionDisplayDTO.setDisplay(actionDictDO.getName());
        }

        taskDTO.setBlock(taskDO.getBlock());
        taskDTO.setStartDateTime(DateUtils.dateTimeToString(taskDO.getStartDateTime()));
        taskDTO.setEndDateTime(DateUtils.dateTimeToString(taskDO.getEndDateTime()));

        DisplayDTO stateDisplayDTO = new DisplayDTO();
        taskDTO.setState(stateDisplayDTO);
        stateDisplayDTO.setCode(taskDO.getState());

        DictDO stateDictDO = findDictDO(dictTypeDOs, DictTypeConsts.TASK_STATE, taskDO.getState());
        if (stateDictDO != null) {
            stateDisplayDTO.setDisplay(stateDictDO.getName());
        }

        UserBaseDTO owner = new UserBaseDTO();
        taskDTO.setOwner(owner);
        owner.setUsername(taskDO.getOwner());
        UserDO ownerUserDO = findUserDO(userDOs, taskDO.getOwner());
        if (ownerUserDO != null) {
            owner.setName(ownerUserDO.getName());
            owner.setCompany(ownerUserDO.getCompany());
            owner.setTelephone(ownerUserDO.getTelephone());
        }

        InfoDTO createdDTO = new InfoDTO();
        taskDTO.setCreated(createdDTO);
        createdDTO.setUsername(taskDO.getCreator());
        UserDO createdUserDO = findUserDO(userDOs, taskDO.getCreator());
        if (createdUserDO != null) {
            createdDTO.setName(createdUserDO.getName());
        }
        createdDTO.setTimestamp(DateUtils.dateTimeToString(taskDO.getGmtCreate()));
    }

    private void setTaskDetailDTO(TaskDetailDTO taskDetailDTO, TaskDO taskDO, List<UserDO> userDOs,
            List<DictTypeDO> dictTypeDOs, CmSite cmSite) {
        setTaskDTO(taskDetailDTO, taskDO, userDOs, dictTypeDOs, cmSite);

        List<SubtaskDO> subtaskDOs = taskDO.getSubtasks();
        List<TaskDetailDTO.SubtaskDTO> subtaskDTOs = new ArrayList<>(subtaskDOs.size());
        taskDetailDTO.setSubtasks(subtaskDTOs);

        for (SubtaskDO subtaskDO : subtaskDOs) {
            TaskDetailDTO.SubtaskDTO subtaskDTO = taskDetailDTO.new SubtaskDTO();
            subtaskDTOs.add(subtaskDTO);

            subtaskDTO.setId(subtaskDO.getId());
            DisplayDTO subtaskObjTypeDisplayDTO = new DisplayDTO();
            subtaskDTO.setObjType(subtaskObjTypeDisplayDTO);
            subtaskObjTypeDisplayDTO.setCode(subtaskDO.getObjType());

            DictDO subtaskObjTypeDictDO = findDictDO(dictTypeDOs, DictTypeConsts.OBJ_TYPE, subtaskDO.getObjType());
            if (subtaskObjTypeDictDO != null) {
                subtaskObjTypeDisplayDTO.setDisplay(subtaskObjTypeDictDO.getName());
            }

            subtaskDTO.setObjId(subtaskDO.getObjId());
            subtaskDTO.setObjName(subtaskDO.getObjName());

            DisplayDTO subtaskActionDisplayDTO = new DisplayDTO();
            subtaskDTO.setAction(subtaskActionDisplayDTO);
            subtaskActionDisplayDTO.setCode(subtaskDO.getActionType());

            DictDO subtaskActionDictDO = findDictDO(dictTypeDOs, DictTypeConsts.ACTION_TYPE, subtaskDO.getActionType());
            if (subtaskActionDictDO != null) {
                subtaskActionDisplayDTO.setDisplay(subtaskActionDictDO.getName());
            }

            subtaskDTO.setPriority(subtaskDO.getPriority());
            subtaskDTO.setStartDateTime(DateUtils.dateTimeToString(subtaskDO.getStartDateTime()));
            subtaskDTO.setEndDateTime(DateUtils.dateTimeToString(subtaskDO.getEndDateTime()));

            DisplayDTO subtaskstateDisplayDTO = new DisplayDTO();
            subtaskDTO.setState(subtaskstateDisplayDTO);
            subtaskstateDisplayDTO.setCode(subtaskDO.getState());

            DictDO subtaskStateDictDO = findDictDO(dictTypeDOs, DictTypeConsts.TASK_STATE, subtaskDO.getState());
            if (subtaskStateDictDO != null) {
                subtaskstateDisplayDTO.setDisplay(subtaskStateDictDO.getName());
            }

            subtaskDTO.setTimeout(subtaskDO.getTimeout());
            subtaskDTO.setMsg(subtaskDO.getMsg());
        }

    }
}
