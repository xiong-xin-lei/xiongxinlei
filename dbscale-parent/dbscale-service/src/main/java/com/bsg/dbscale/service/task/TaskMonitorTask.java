package com.bsg.dbscale.service.task;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import com.bsg.dbscale.dao.domain.OrderGroupDO;
import com.bsg.dbscale.dao.domain.ServGroupDO;
import com.bsg.dbscale.dao.domain.SubtaskDO;
import com.bsg.dbscale.dao.domain.TaskDO;
import com.bsg.dbscale.dao.query.OrderGroupQuery;
import com.bsg.dbscale.service.constant.DictConsts;
import com.bsg.dbscale.service.service.BaseService;

@Service
public class TaskMonitorTask extends BaseService {

    public void doTask() {
        try {
            logger.info("定时任务：更新任务状态(WEB重启)");
            List<TaskDO> taskDOs = taskDAO.listRunning();
            Date nowDate = systemDAO.getCurrentSqlDateTime();
            for (TaskDO taskDO : taskDOs) {
                List<SubtaskDO> subtaskDOs = taskDO.getSubtasks();
                boolean timeout = false;
                for (SubtaskDO subtaskDO : subtaskDOs) {
                    if (subtaskDO.getState().equals(DictConsts.TASK_STATE_RUNNING)
                            && subtaskDO.getStartDateTime() != null && subtaskDO.getTimeout() != null
                            && (nowDate.getTime() - subtaskDO.getStartDateTime().getTime())
                                    / 1000 > subtaskDO.getTimeout() + 30) {
                        subtaskDO.setState(DictConsts.TASK_STATE_UNKNOWN);
                        subtaskDO.setMsg("WEB重启");
                        subtaskDO.setEndDateTime(nowDate);
                        subtaskDAO.updateToEnd(subtaskDO);
                        timeout = true;
                    }
                }
                if (timeout) {
                    taskDO.setState(DictConsts.TASK_STATE_UNKNOWN);
                    taskDO.setEndDateTime(nowDate);
                    taskDAO.updateToEnd(taskDO);

                    if (taskDO.getObjType().equals(DictConsts.OBJ_TYPE_SERV_GROUP)) {
                        if (taskDO.getActionType().equals(DictConsts.ACTION_TYPE_CREATE)) {
                            ServGroupDO servGroupDO = servGroupDAO.getByName(taskDO.getObjName());
                            if (servGroupDO != null) {
                                servGroupDO.setFlag(false);
                                servGroupDAO.update(servGroupDO);
                            }
                        }

                        if (taskDO.getActionType().equals(DictConsts.ACTION_TYPE_CREATE)
                                || taskDO.getActionType().equals(DictConsts.ACTION_TYPE_SCALE_UP_CPUMEM)
                                || taskDO.getActionType().equals(DictConsts.ACTION_TYPE_SCALE_UP_STORAGE)
                                || taskDO.getActionType().equals(DictConsts.ACTION_TYPE_IMAGE_UPDATE)
                                || taskDO.getActionType().equals(DictConsts.ACTION_TYPE_REMOVE)
                                || taskDO.getActionType().equals(DictConsts.ACTION_TYPE_ARCH_UP)) {
                            OrderGroupQuery orderGroupQuery = new OrderGroupQuery();
                            orderGroupQuery.setName(taskDO.getObjName());
                            orderGroupQuery.setState(DictConsts.ORDER_STATE_EXECUTING);
                            List<OrderGroupDO> orderGroupDOs = orderGroupDAO.list(orderGroupQuery);
                            for (OrderGroupDO orderGroupDO : orderGroupDOs) {
                                orderGroupDO.setState(DictConsts.ORDER_STATE_FAILED);
                                orderGroupDAO.updateStateAndMsg(orderGroupDO);
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            logger.error("定时任务：更新任务状态(因服务器宕机)异常：", e);
        }
    }

}
