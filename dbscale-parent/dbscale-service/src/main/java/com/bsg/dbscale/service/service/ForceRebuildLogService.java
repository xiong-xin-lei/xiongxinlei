package com.bsg.dbscale.service.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import com.bsg.dbscale.dao.domain.DictDO;
import com.bsg.dbscale.dao.domain.DictTypeDO;
import com.bsg.dbscale.dao.domain.ForceRebuildLogDO;
import com.bsg.dbscale.dao.domain.HostDO;
import com.bsg.dbscale.dao.domain.TaskDO;
import com.bsg.dbscale.dao.query.TaskQuery;
import com.bsg.dbscale.service.constant.DictConsts;
import com.bsg.dbscale.service.constant.DictTypeConsts;
import com.bsg.dbscale.service.dto.DisplayDTO;
import com.bsg.dbscale.service.dto.ForceRebuildLogDTO;
import com.bsg.dbscale.service.dto.TaskBaseDTO;
import com.bsg.dbscale.service.query.ForceRebuildLogQuery;

@Service
public class ForceRebuildLogService extends BaseService {

    public Result list(ForceRebuildLogQuery forceRebuildLogQuery) throws Exception {
        List<ForceRebuildLogDTO> forceRebuildLogDTOs = new ArrayList<>();
        List<ForceRebuildLogDO> forceRebuildLogDOs = forceRebuildLogDAO.list(null);
        if (forceRebuildLogDOs.size() > 0) {
            TaskQuery taskQuery = new TaskQuery();
            if (forceRebuildLogQuery.getStart() != null) {
                taskQuery.setStartDate(new Date(forceRebuildLogQuery.getStart() * 1000));
            }
            if (forceRebuildLogQuery.getEnd() != null) {
                taskQuery.setEndDate(new Date(forceRebuildLogQuery.getEnd() * 1000));
            }
            taskQuery.setSiteId(forceRebuildLogQuery.getSiteId());
            taskQuery.setObjType(DictConsts.OBJ_TYPE_UNIT);
            taskQuery.setActionType(DictConsts.ACTION_TYPE_FORCE_REBUILD);
            List<TaskDO> taskDOs = taskDAO.list(taskQuery);

            if (taskDOs.size() > 0) {
                List<HostDO> hostDOs = hostDAO.list(null);
                List<DictTypeDO> dictTypeDOs = dictTypeDAO.list();
                for (ForceRebuildLogDO forceRebuildLogDO : forceRebuildLogDOs) {
                    TaskDO taskDO = findTaskDO(taskDOs, forceRebuildLogDO.getTaskId());
                    if (taskDO != null) {
                        HostDO sourceHostDO = findHostDOByRelateId(hostDOs, forceRebuildLogDO.getSourceHostRelateId());
                        if (sourceHostDO != null) {
                            ForceRebuildLogDTO forceRebuildLogDTO = new ForceRebuildLogDTO();
                            forceRebuildLogDTOs.add(forceRebuildLogDTO);
                            forceRebuildLogDTO.setId(forceRebuildLogDO.getId());
                            forceRebuildLogDTO.setUnitRelateName(forceRebuildLogDO.getUnitRelateId());
                            forceRebuildLogDTO.setSourceHostIp(sourceHostDO.getIp());

                            HostDO targetHostDO = findHostDOByRelateId(hostDOs,
                                    forceRebuildLogDO.getTargetHostRelateId());
                            if (targetHostDO != null) {
                                forceRebuildLogDTO.setTargetHostIp(targetHostDO.getIp());
                            }

                            TaskBaseDTO taskDTO = new TaskBaseDTO();
                            forceRebuildLogDTO.setTask(taskDTO);

                            taskDTO.setId(taskDO.getId());
                            DisplayDTO actionDisplayDTO = new DisplayDTO();
                            taskDTO.setAction(actionDisplayDTO);
                            actionDisplayDTO.setCode(taskDO.getActionType());
                            DictDO actionDictDO = findDictDO(dictTypeDOs, DictTypeConsts.ACTION_TYPE,
                                    taskDO.getActionType());
                            if (actionDictDO != null) {
                                actionDisplayDTO.setDisplay(actionDictDO.getName());
                            }

                            DisplayDTO taskStateDisplayDTO = new DisplayDTO();
                            taskDTO.setState(taskStateDisplayDTO);
                            taskStateDisplayDTO.setCode(taskDO.getState());
                            DictDO taskStateDictDO = findDictDO(dictTypeDOs, DictTypeConsts.TASK_STATE,
                                    taskDO.getState());
                            if (taskStateDictDO != null) {
                                taskStateDisplayDTO.setDisplay(taskStateDictDO.getName());
                            }

                        }
                    }
                }
            }
        }
        return Result.success(forceRebuildLogDTOs);
    }

}
