package com.bsg.dbscale.service.check;

import java.util.List;

import org.springframework.stereotype.Service;

import com.bsg.dbscale.dao.domain.RoleDO;

@Service
public class RoleCfgAppCheck extends BaseCheck {

    public CheckResult checkSave(String roleId, List<Long> appIds) {
        RoleDO roleDO = roleDAO.get(roleId);
        if (roleDO == null) {
            String msg = "角色不存在。";
            return CheckResult.failure(msg);
        }
        return CheckResult.success();
    }
}
