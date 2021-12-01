package com.bsg.dbscale.service.check;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.bsg.dbscale.dao.domain.DefServDO;
import com.bsg.dbscale.dao.domain.ScaleDO;
import com.bsg.dbscale.service.constant.Consts;
import com.bsg.dbscale.service.form.ScaleForm;

@Service
public class ScaleCheck extends BaseCheck {

    public CheckResult checkSave(ScaleForm scaleForm) {
        // Non-logical check for save
        CheckResult checkResult = checkSaveNonLogic(scaleForm);
        if (checkResult.getCode() != CheckResult.SUCCESS) {
            return checkResult;
        }

        // logical check for save
        checkResult = checkSaveLogic(scaleForm);

        return checkResult;
    }

    public CheckResult checkEnabled(String type, Double cpuCnt, Double memSize, Boolean enabled) {
        ScaleDO scaleDO = scaleDAO.get(type, cpuCnt, memSize);
        if (scaleDO == null) {
            String msg = "规模不存在。";
            return CheckResult.failure(msg);
        }

        int orderCnt = orderCfgDAO.countByScale(type, cpuCnt, memSize);
        if (orderCnt > 0) {
            String msg = "该规模已工单默认配置，禁止操作。";
            return CheckResult.failure(msg);
        }

        return CheckResult.success();
    }

    public CheckResult checkRemove(String type, Double cpuCnt, Double memSize) {
        ScaleDO scaleDO = scaleDAO.get(type, cpuCnt, memSize);
        if (scaleDO == null) {
            String msg = "规模不存在。";
            return CheckResult.failure(msg);
        }

        if (BooleanUtils.isTrue(scaleDO.getEnabled())) {
            String msg = "该规模已启用，不能删除。";
            return CheckResult.failure(msg);
        }

        int orderCnt = orderCfgDAO.countByScale(type, cpuCnt, memSize);
        if (orderCnt > 0) {
            String msg = "该规模已工单默认配置，禁止操作。";
            return CheckResult.failure(msg);
        }

        return CheckResult.success();
    }

    private CheckResult checkSaveNonLogic(ScaleForm scaleForm) {
        if (StringUtils.isBlank(scaleForm.getType())) {
            String msg = "类型不能为空。";
            return CheckResult.failure(msg);
        }

        if (scaleForm.getCpuCnt() == null) {
            String msg = "CPU数量不能为空。";
            return CheckResult.failure(msg);
        }
        if (scaleForm.getCpuCnt() < 0 || scaleForm.getCpuCnt() > 999.9) {
            String msg = "CPU数量必须介于0~999.9。";
            return CheckResult.failure(msg);
        }

        if (scaleForm.getMemSize() == null) {
            String msg = "内存容量不能为空。";
            return CheckResult.failure(msg);
        }
        
        if (scaleForm.getType().equals(Consts.SERV_TYPE_MYSQL)) {
            if (scaleForm.getMemSize() < 2 || scaleForm.getMemSize() > 999.9) {
                String msg = "数据库内存容量必须介于2~999.9。";
                return CheckResult.failure(msg);
            }
        } else {
            if (scaleForm.getMemSize() < 1 || scaleForm.getMemSize() > 999.9) {
                String msg = "内存容量必须介于1~999.9。";
                return CheckResult.failure(msg);
            }
        }
        return CheckResult.success();
    }

    private CheckResult checkSaveLogic(ScaleForm scaleForm) {
        DefServDO defServDO = defServDAO.get(scaleForm.getType());
        if (defServDO == null) {
            String msg = "该类型不存在。";
            return CheckResult.failure(msg);
        }

        ScaleDO scaleDO = scaleDAO.get(scaleForm.getType(), scaleForm.getCpuCnt(), scaleForm.getMemSize());
        if (scaleDO != null) {
            String msg = "该类型下规模已存在。";
            return CheckResult.failure(msg);
        }

        return CheckResult.success();
    }
}
