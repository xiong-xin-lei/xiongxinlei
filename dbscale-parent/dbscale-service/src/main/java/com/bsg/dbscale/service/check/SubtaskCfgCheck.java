package com.bsg.dbscale.service.check;

import org.springframework.stereotype.Service;

import com.bsg.dbscale.dao.domain.SubtaskCfgDO;
import com.bsg.dbscale.service.form.SubtaskCfgForm;

@Service
public class SubtaskCfgCheck extends BaseCheck {

    public CheckResult checkUpdate(String objType, String actionType, SubtaskCfgForm subtaskCfgForm) {
        // Non-logical check for update
        CheckResult checkResult = checkUpdateNonLogic(subtaskCfgForm);
        if (checkResult.getCode() != CheckResult.SUCCESS) {
            return checkResult;
        }

        // logical check for update
        checkResult = checkUpdateLogic(objType, actionType);

        return checkResult;
    }

    private CheckResult checkUpdateNonLogic(SubtaskCfgForm subtaskCfgForm) {
        if (subtaskCfgForm.getTimeout() != null && subtaskCfgForm.getTimeout().longValue() <= 0) {
            String msg = "超时时间必须大于0。";
            return CheckResult.failure(msg);
        }
        return CheckResult.success();
    }

    private CheckResult checkUpdateLogic(String objType, String actionType) {
        SubtaskCfgDO subtaskCfgDO = subtaskCfgDAO.get(objType, actionType);

        if (subtaskCfgDO == null) {
            String msg = "配置不存在。";
            return CheckResult.failure(msg);
        }
        return CheckResult.success();
    }
}
