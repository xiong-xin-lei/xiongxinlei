package com.bsg.dbscale.service.check;

import org.springframework.stereotype.Service;

import com.bsg.dbscale.dao.domain.TaskDO;
import com.bsg.dbscale.service.constant.DictConsts;

@Service
public class TaskCheck extends BaseCheck {

    public CheckResult checkCancel(TaskDO taskDO) {
        if (taskDO == null) {
            String msg = "任务不存在。";
            return CheckResult.failure(msg);
        }

        if (!taskDO.getState().equals(DictConsts.TASK_STATE_RUNNING)) {
            String msg = "该任务已完成，不能取消。";
            return CheckResult.failure(msg);
        }
        return CheckResult.success();
    }
}
