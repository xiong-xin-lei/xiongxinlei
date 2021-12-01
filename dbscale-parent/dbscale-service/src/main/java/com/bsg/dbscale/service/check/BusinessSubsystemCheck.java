package com.bsg.dbscale.service.check;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.bsg.dbscale.dao.domain.BusinessSubsystemDO;
import com.bsg.dbscale.dao.domain.BusinessSystemDO;
import com.bsg.dbscale.service.form.BusinessSubsystemForm;

@Service
public class BusinessSubsystemCheck extends BaseCheck {

    public CheckResult checkSave(BusinessSubsystemForm businessSubsystemForm) {
        // Non-logical check for save
        CheckResult checkResult = checkSaveNonLogic(businessSubsystemForm);
        if (checkResult.getCode() != CheckResult.SUCCESS) {
            return checkResult;
        }

        // logical check for save
        checkResult = checkSaveLogic(businessSubsystemForm);

        return checkResult;
    }

    public CheckResult checkUpdate(String businessSubsystemId, BusinessSubsystemForm businessSubsystemForm) {
        // Non-logical check for update
        CheckResult checkResult = checkUpdateNonLogic(businessSubsystemForm);
        if (checkResult.getCode() != CheckResult.SUCCESS) {
            return checkResult;
        }

        // logical check for update
        checkResult = checkUpdateLogic(businessSubsystemId, businessSubsystemForm);

        return checkResult;
    }

    public CheckResult checkRemove(String businessSubsystemId) {
        BusinessSubsystemDO businessSubsystemDO = businessSubsystemDAO.get(businessSubsystemId);
        if (businessSubsystemDO == null) {
            String msg = "业务子系统不存在。";
            return CheckResult.failure(msg);
        }

        if (BooleanUtils.isTrue(businessSubsystemDO.getEnabled())) {
            String msg = "该业务子系统已启用，不能删除。";
            return CheckResult.failure(msg);
        }

        int orderGroupCnt = orderGroupDAO.countBySubsystemId(businessSubsystemId);
        if (orderGroupCnt > 0) {
            String msg = "该业务子系统已关联工单或服务，不能删除。";
            return CheckResult.failure(msg);
        }

        int servGroupCnt = servGroupDAO.countBySubsystemId(businessSubsystemId);
        if (servGroupCnt > 0) {
            String msg = "该业务子系统已关联工单或服务，不能删除。";
            return CheckResult.failure(msg);
        }
        return CheckResult.success();
    }

    public CheckResult checkEnabled(String businessSubsystemId, boolean enabled) {
        BusinessSubsystemDO businessSubsystemDO = businessSubsystemDAO.get(businessSubsystemId);
        if (businessSubsystemDO == null) {
            String msg = "业务子系统不存在。";
            return CheckResult.failure(msg);
        }
        return CheckResult.success();
    }

    private CheckResult checkSaveNonLogic(BusinessSubsystemForm businessSubsystemForm) {
        if (StringUtils.isBlank(businessSubsystemForm.getBusinessSystemId())) {
            String msg = "业务系统不能为空。";
            return CheckResult.failure(msg);
        }
        if (StringUtils.isBlank(businessSubsystemForm.getName())) {
            String msg = "业务子系统不能为空。";
            return CheckResult.failure(msg);
        }
        return CheckResult.success();
    }

    private CheckResult checkSaveLogic(BusinessSubsystemForm businessSubsystemForm) {
        BusinessSystemDO businessSystemDO = businessSystemDAO.get(businessSubsystemForm.getBusinessSystemId());
        if (businessSystemDO == null) {
            String msg = "该业务系统不存在。";
            return CheckResult.failure(msg);
        }

        if (BooleanUtils.isNotTrue(businessSystemDO.getEnabled())) {
            String msg = "该业务系统已停用。";
            return CheckResult.failure(msg);
        }

        BusinessSubsystemDO businessSubsystemDO = businessSubsystemDAO
                .getByNameAndSystemId(businessSubsystemForm.getName(), businessSubsystemForm.getBusinessSystemId());
        if (businessSubsystemDO != null) {
            String msg = "该业务系统下子系统已存在。";
            return CheckResult.failure(msg);
        }
        return CheckResult.success();
    }

    private CheckResult checkUpdateNonLogic(BusinessSubsystemForm businessSubsystemForm) {
        if (StringUtils.equals(StringUtils.EMPTY, businessSubsystemForm.getBusinessSystemId())) {
            String msg = "业务子系统不能为空。";
            return CheckResult.failure(msg);
        }

        if (StringUtils.equals(StringUtils.EMPTY, businessSubsystemForm.getName())) {
            String msg = "业务系统不能为空。";
            return CheckResult.failure(msg);
        }
        return CheckResult.success();
    }

    private CheckResult checkUpdateLogic(String businessSubsystemId, BusinessSubsystemForm businessSubsystemForm) {
        BusinessSubsystemDO businessSubsystemDO = businessSubsystemDAO.get(businessSubsystemId);
        if (businessSubsystemDO == null) {
            String msg = "业务子系统不存在。";
            return CheckResult.failure(msg);
        }

        if (BooleanUtils.isTrue(businessSubsystemDO.getEnabled())) {
            String msg = "业务子系统已启用，不能编辑。";
            return CheckResult.failure(msg);
        }

        if (StringUtils.isNotBlank(businessSubsystemForm.getBusinessSystemId())
                && !businessSubsystemForm.getBusinessSystemId().equals(businessSubsystemDO.getBusinessSystemId())) {
            businessSubsystemDO = businessSubsystemDAO.getByNameAndSystemId(businessSubsystemForm.getName(),
                    businessSubsystemForm.getBusinessSystemId());
            if (businessSubsystemDO != null) {
                String msg = "该业务系统下子系统已存在。";
                return CheckResult.failure(msg);
            }
        }

        if (StringUtils.isNotBlank(businessSubsystemForm.getName())
                && !businessSubsystemDO.getName().equals(businessSubsystemForm.getName())) {
            businessSubsystemDO = businessSubsystemDAO.getByNameAndSystemId(businessSubsystemForm.getName(),
                    businessSubsystemForm.getBusinessSystemId());
            if (businessSubsystemDO != null) {
                String msg = "该子系统已存在。";
                return CheckResult.failure(msg);
            }
        }
        return CheckResult.success();
    }
}
