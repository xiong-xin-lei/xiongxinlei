package com.bsg.dbscale.service.check;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.bsg.dbscale.dao.domain.DictDO;
import com.bsg.dbscale.service.form.DictForm;

@Service
public class DictCheck extends BaseCheck {

    public CheckResult checkUpdate(String dictTypeCode, String dictCode, DictForm dictForm) {
        // Non-logical check for update
        CheckResult checkResult = checkUpdateNonLogic(dictForm);
        if (checkResult.getCode() != CheckResult.SUCCESS) {
            return checkResult;
        }

        // logical check for update
        checkResult = checkUpdateLogic(dictTypeCode, dictCode, dictForm);

        return checkResult;
    }

    private CheckResult checkUpdateNonLogic(DictForm dictForm) {
        if (dictForm.getName() != null && StringUtils.isBlank(dictForm.getName())) {
            String msg = "名称不能为空。";
            return CheckResult.failure(msg);
        }

        return CheckResult.success();
    }

    private CheckResult checkUpdateLogic(String dictTypeCode, String dictCode, DictForm dictForm) {
        DictDO dictDO = dictDAO.get(dictTypeCode, dictCode);

        if (dictDO == null) {
            String msg = "字典不存在。";
            return CheckResult.failure(msg);
        }

        if (StringUtils.isNotBlank(dictForm.getName()) && !dictForm.getName().equals(dictDO.getName())) {
            int nameCnt = dictDAO.countByNameAndDictTypeCode(dictForm.getName(), dictDO.getDictTypeCode());
            if (nameCnt != 0) {
                String msg = "名称已存在。";
                return CheckResult.failure(msg);
            }
        }

        return CheckResult.success();
    }
}
