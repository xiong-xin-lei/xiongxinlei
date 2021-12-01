package com.bsg.dbscale.service.check;

import java.util.List;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.bsg.dbscale.cm.api.CmApi;
import com.bsg.dbscale.cm.model.CmStorageclass;
import com.bsg.dbscale.cm.query.CmStorageclassQuery;
import com.bsg.dbscale.service.form.PVCStorageForm;

@Service
public class PVCStorageCheck extends BaseCheck {

    public CheckResult checkSave(PVCStorageForm storageForm) throws Exception {
        // Non-logical check for save
        CheckResult checkResult = checkSaveNonLogic(storageForm);
        if (checkResult.getCode() != CheckResult.SUCCESS) {
            return checkResult;
        }

        // logical check for save
        checkResult = checkSaveLogic(storageForm);

        return checkResult;
    }

    public CheckResult checkUpdate(String storageId, PVCStorageForm storageForm) throws Exception {
        // Non-logical check for update
        CheckResult checkResult = checkUpdateNonLogic(storageForm);
        if (checkResult.getCode() != CheckResult.SUCCESS) {
            return checkResult;
        }

        // logical check for update
        checkResult = checkUpdateLogic(storageId, storageForm);

        return checkResult;
    }

    public CheckResult checkRemove(String storageId) throws Exception {
        CmStorageclass cmStorageclass = CmApi.getStorageclass(storageId);
        if (cmStorageclass == null) {
            String msg = "该存储不存在。";
            return CheckResult.failure(msg);
        }

        return CheckResult.success();
    }

    public CheckResult checkEnabled(String storageId, boolean enabled) throws Exception {
        CmStorageclass cmStorageclass = CmApi.getStorageclass(storageId);
        if (cmStorageclass == null) {
            String msg = "该存储不存在。";
            return CheckResult.failure(msg);
        }

        return CheckResult.success();
    }

    private CheckResult checkSaveNonLogic(PVCStorageForm storageForm) {
        if (StringUtils.isBlank(storageForm.getSiteId())) {
            String msg = "站点不能为空。";
            return CheckResult.failure(msg);
        }

        if (StringUtils.isBlank(storageForm.getName())) {
            String msg = "存储名不能为空。";
            return CheckResult.failure(msg);
        }

        if (StringUtils.isBlank(storageForm.getProvisioner())) {
            String msg = "供应商不能为空。";
            return CheckResult.failure(msg);
        }

        return CheckResult.success();
    }

    private CheckResult checkSaveLogic(PVCStorageForm storageForm) throws Exception {
        CmStorageclassQuery cmStorageclassQuery = new CmStorageclassQuery();
        cmStorageclassQuery.setSiteId(storageForm.getSiteId());
        cmStorageclassQuery.setName(storageForm.getName());
        List<CmStorageclass> cmStorages = CmApi.listStorageclass(cmStorageclassQuery);
        if (cmStorages.size() != 0) {
            String msg = "存储名已存在。";
            return CheckResult.failure(msg);
        }
        return CheckResult.success();
    }

    private CheckResult checkUpdateNonLogic(PVCStorageForm storageForm) {
        if (StringUtils.equals(StringUtils.EMPTY, storageForm.getName())) {
            String msg = "存储名不能为空。";
            return CheckResult.failure(msg);
        }

        if (StringUtils.equals(StringUtils.EMPTY, storageForm.getProvisioner())) {
            String msg = "供应商不能为空。";
            return CheckResult.failure(msg);
        }
        return CheckResult.success();
    }

    private CheckResult checkUpdateLogic(String storageId, PVCStorageForm storageForm) throws Exception {
        CmStorageclass cmStorageclass = CmApi.getStorageclass(storageId);
        if (cmStorageclass == null) {
            String msg = "该存储不存在。";
            return CheckResult.failure(msg);
        }

        if (BooleanUtils.isFalse(cmStorageclass.getUnschedulable())) {
            String msg = "该存储已启用，不能编辑。";
            return CheckResult.failure(msg);
        }

        if (StringUtils.isNotBlank(storageForm.getName()) && !cmStorageclass.getName().equals(storageForm.getName())) {
            CmStorageclassQuery cmStorageclassQuery = new CmStorageclassQuery();
            cmStorageclassQuery.setSiteId(storageForm.getSiteId());
            cmStorageclassQuery.setName(storageForm.getName());
            List<CmStorageclass> cmStorages = CmApi.listStorageclass(cmStorageclassQuery);
            if (cmStorages.size() > 0) {
                String msg = "该站点下存储名已存在。";
                return CheckResult.failure(msg);
            }
        }

        return CheckResult.success();
    }
}
