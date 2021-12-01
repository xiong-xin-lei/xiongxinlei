package com.bsg.dbscale.service.check;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.bsg.dbscale.dao.domain.ServGroupDO;
import com.bsg.dbscale.service.constant.Consts;
import com.bsg.dbscale.service.form.ResetPwdForm;
import com.bsg.dbscale.service.form.ServArchUpForm;
import com.bsg.dbscale.service.form.ServImageForm;
import com.bsg.dbscale.service.form.ServScaleCpuMemForm;
import com.bsg.dbscale.service.form.ServScaleStorageForm;

@Service
public class RedisServGroupCheck extends ServGroupCheck {

    public static final String PW_PATTERN = "^(?=.*[0-9].*)(?=.*[A-Za-z].*).{8,}$";

    public static final String SPECIAL_PATTERN = "[^&$:()'\"\\s]{1,}";

    public CheckResult checkResetPwd(ServGroupDO servGroupDO, ResetPwdForm resetPwdForm) {
        if (StringUtils.isBlank(resetPwdForm.getPwd())) {
            String msg = "密码不能为空。";
            return CheckResult.failure(msg);
        }

        if (!resetPwdForm.getPwd().matches(PW_PATTERN)) {
            String msg = "密码必须为8位以上且包含数字和字母。";
            return CheckResult.failure(msg);
        }

        if (!resetPwdForm.getPwd().matches(SPECIAL_PATTERN)) {
            String msg = "密码不能含有以下特殊字符(&、$、冒号、小括号、单引号、双引号、空格)。";
            return CheckResult.failure(msg);
        }
        return checkServGroupCommon(servGroupDO, Consts.SERV_GROUP_TYPE_REDIS);
    }

    public CheckResult checkStart(ServGroupDO servGroupDO) {
        return checkStart(servGroupDO, Consts.SERV_GROUP_TYPE_REDIS);
    }

    public CheckResult checkStop(ServGroupDO servGroupDO) {
        return checkStop(servGroupDO, Consts.SERV_GROUP_TYPE_REDIS);
    }

    public CheckResult checkImageUpdate(ServGroupDO servGroupDO, ServImageForm imageForm) {
        return checkImageUpdate(servGroupDO, Consts.SERV_GROUP_TYPE_REDIS, imageForm);
    }

    public CheckResult checkScaleUpCpuMem(ServGroupDO servGroupDO, ServScaleCpuMemForm scaleCpuMemForm) {
        return checkScaleUpCpuMem(servGroupDO, Consts.SERV_GROUP_TYPE_REDIS, scaleCpuMemForm);
    }

    public CheckResult checkScaleUpStorage(ServGroupDO servGroupDO, ServScaleStorageForm scaleStorageForm) {
        return checkScaleUpStorage(servGroupDO, Consts.SERV_GROUP_TYPE_REDIS, scaleStorageForm);
    }

    public CheckResult checkArchUp(ServGroupDO servGroupDO, ServArchUpForm archUpForm) {
        return checkArchUp(servGroupDO, Consts.SERV_GROUP_TYPE_REDIS, archUpForm);
    }

    public CheckResult checkRemove(ServGroupDO servGroupDO) {
        return checkRemove(servGroupDO, Consts.SERV_GROUP_TYPE_REDIS);
    }

}
