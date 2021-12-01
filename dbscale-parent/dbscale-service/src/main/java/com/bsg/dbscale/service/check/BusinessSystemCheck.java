package com.bsg.dbscale.service.check;

import java.util.List;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.bsg.dbscale.dao.domain.BusinessSubsystemDO;
import com.bsg.dbscale.dao.domain.BusinessSystemDO;
import com.bsg.dbscale.dao.query.BusinessSubsystemQuery;
import com.bsg.dbscale.service.form.BusinessSystemForm;

@Service
public class BusinessSystemCheck extends BaseCheck {

    public CheckResult checkSave(BusinessSystemForm businessSystemForm, String activeUsername) {
        // Non-logical check for save
        CheckResult checkResult = checkSaveNonLogic(businessSystemForm);
        if (checkResult.getCode() != CheckResult.SUCCESS) {
            return checkResult;
        }

        // logical check for save
        checkResult = checkSaveLogic(businessSystemForm, activeUsername);

        return checkResult;
    }

    public CheckResult checkUpdate(String businessSystemId, BusinessSystemForm businessSystemForm) {
        // Non-logical check for update
        CheckResult checkResult = checkUpdateNonLogic(businessSystemForm);
        if (checkResult.getCode() != CheckResult.SUCCESS) {
            return checkResult;
        }

        // logical check for update
        checkResult = checkUpdateLogic(businessSystemId, businessSystemForm);

        return checkResult;
    }

    public CheckResult checkRemove(String businessSystemId) {
        BusinessSystemDO businessSystemDO = businessSystemDAO.get(businessSystemId);
        if (businessSystemDO == null) {
            String msg = "业务系统不存在。";
            return CheckResult.failure(msg);
        }

        if (BooleanUtils.isTrue(businessSystemDO.getEnabled())) {
            String msg = "该业务系统已启用，不能删除。";
            return CheckResult.failure(msg);
        }

        BusinessSubsystemQuery businessSubsystemQuery = new BusinessSubsystemQuery();
        businessSubsystemQuery.setBusinessSystemId(businessSystemId);
        List<BusinessSubsystemDO> businessSubsystemDOs = businessSubsystemDAO.list(businessSubsystemQuery);

        if (businessSubsystemDOs.size() > 0) {
            String msg = "该业务系统已关联业务子系统，不能删除。";
            return CheckResult.failure(msg);
        }
        return CheckResult.success();
    }

    public CheckResult checkEnabled(String businessSystemId, boolean enabled) {
        BusinessSystemDO businessSystemDO = businessSystemDAO.get(businessSystemId);
        if (businessSystemDO == null) {
            String msg = "业务系统不存在。";
            return CheckResult.failure(msg);
        }
        return CheckResult.success();
    }

    private CheckResult checkSaveNonLogic(BusinessSystemForm businessSystemForm) {
        if (StringUtils.isBlank(businessSystemForm.getName())) {
            String msg = "业务系统不能为空。";
            return CheckResult.failure(msg);
        }
        return CheckResult.success();
    }

    private CheckResult checkSaveLogic(BusinessSystemForm businessSystemForm, String activeUsername) {
        BusinessSystemDO businessSystemDO = businessSystemDAO.getByNameAndOwner(businessSystemForm.getName(),
                activeUsername);
        if (businessSystemDO != null) {
            String msg = "该业务系统已存在。";
            return CheckResult.failure(msg);
        }
        return CheckResult.success();
    }

    private CheckResult checkUpdateNonLogic(BusinessSystemForm businessSystemForm) {
        if (StringUtils.equals(StringUtils.EMPTY, businessSystemForm.getName())) {
            String msg = "业务系统不能为空。";
            return CheckResult.failure(msg);
        }
        return CheckResult.success();
    }

    private CheckResult checkUpdateLogic(String businessSystemId, BusinessSystemForm businessSystemForm) {
        BusinessSystemDO businessSystemDO = businessSystemDAO.get(businessSystemId);
        if (businessSystemDO == null) {
            String msg = "业务系统不存在。";
            return CheckResult.failure(msg);
        }

        if (BooleanUtils.isTrue(businessSystemDO.getEnabled())) {
            String msg = "业务系统已启用，不能编辑。";
            return CheckResult.failure(msg);
        }

        if (StringUtils.isNotBlank(businessSystemForm.getName())
                && !businessSystemForm.getName().equals(businessSystemDO.getName())) {
            businessSystemDO = businessSystemDAO.getByNameAndOwner(businessSystemForm.getName(),
                    businessSystemDO.getOwner());
            if (businessSystemDO != null) {
                String msg = "该业务系统已存在。";
                return CheckResult.failure(msg);
            }
        }
        return CheckResult.success();
    }
}
