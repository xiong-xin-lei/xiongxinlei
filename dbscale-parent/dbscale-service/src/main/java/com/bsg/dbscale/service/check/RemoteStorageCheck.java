package com.bsg.dbscale.service.check;

import java.util.List;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.bsg.dbscale.cm.api.CmApi;
import com.bsg.dbscale.cm.model.CmRemoteStorage;
import com.bsg.dbscale.cm.query.CmRemoteStorageQuery;
import com.bsg.dbscale.dao.domain.DictDO;
import com.bsg.dbscale.service.constant.DictTypeConsts;
import com.bsg.dbscale.service.form.RemoteStorageForm;

@Service
public class RemoteStorageCheck extends BaseCheck {

    public CheckResult checkSave(RemoteStorageForm storageForm) throws Exception {
        // Non-logical check for save
        CheckResult checkResult = checkSaveNonLogic(storageForm);
        if (checkResult.getCode() != CheckResult.SUCCESS) {
            return checkResult;
        }

        // logical check for save
        checkResult = checkSaveLogic(storageForm);

        return checkResult;
    }

    public CheckResult checkUpdate(String storageId, RemoteStorageForm storageForm) throws Exception {
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
        CmRemoteStorage cmRemoteStorage = CmApi.getRemoteStorage(storageId);
        if (cmRemoteStorage == null) {
            String msg = "该存储不存在。";
            return CheckResult.failure(msg);
        }

        return CheckResult.success();
    }

    public CheckResult checkEnabled(String storageId, boolean enabled) throws Exception {
        CmRemoteStorage cmRemoteStorage = CmApi.getRemoteStorage(storageId);
        if (cmRemoteStorage == null) {
            String msg = "该存储不存在。";
            return CheckResult.failure(msg);
        }

        return CheckResult.success();
    }

    private CheckResult checkSaveNonLogic(RemoteStorageForm storageForm) {
        if (StringUtils.isBlank(storageForm.getSiteId())) {
            String msg = "站点不能为空。";
            return CheckResult.failure(msg);
        }

        if (StringUtils.isBlank(storageForm.getName())) {
            String msg = "存储名不能为空。";
            return CheckResult.failure(msg);
        }

        if (StringUtils.isBlank(storageForm.getVendor())) {
            String msg = "品牌不能为空。";
            return CheckResult.failure(msg);
        }

        if (StringUtils.isBlank(storageForm.getVersion())) {
            String msg = "版本不能为空。";
            return CheckResult.failure(msg);
        }

        if (StringUtils.isBlank(storageForm.getType())) {
            String msg = "类型不能为空。";
            return CheckResult.failure(msg);
        }

        if (StringUtils.isBlank(storageForm.getIp())) {
            String msg = "IP地址不能为空。";
            return CheckResult.failure(msg);
        }

        if (storageForm.getPort() == null) {
            String msg = "端口不能为空。";
            return CheckResult.failure(msg);
        }

        if (StringUtils.isBlank(storageForm.getUsername())) {
            String msg = "用户名不能为空。";
            return CheckResult.failure(msg);
        }

        if (StringUtils.isBlank(storageForm.getPassword())) {
            String msg = "密码不能为空。";
            return CheckResult.failure(msg);
        }

        return CheckResult.success();
    }

    private CheckResult checkSaveLogic(RemoteStorageForm storageForm) throws Exception {
        CmRemoteStorageQuery cmStorageQuery = new CmRemoteStorageQuery();
        cmStorageQuery.setSiteId(storageForm.getSiteId());
        cmStorageQuery.setName(storageForm.getName());
        List<CmRemoteStorage> cmStorages = CmApi.listRemoteStorage(cmStorageQuery);
        if (cmStorages.size() != 0) {
            String msg = "存储名已存在。";
            return CheckResult.failure(msg);
        }

        DictDO dictDO = dictDAO.get(DictTypeConsts.REMOTE_STORAGE_TYPE, storageForm.getType());
        if (dictDO == null) {
            String msg = "存储类型不存在。";
            return CheckResult.failure(msg);
        }

        return CheckResult.success();
    }

    private CheckResult checkUpdateNonLogic(RemoteStorageForm storageForm) {
        if (storageForm.getName() != null && StringUtils.isEmpty(storageForm.getName())) {
            String msg = "存储名不能为空。";
            return CheckResult.failure(msg);
        }
        return CheckResult.success();
    }

    private CheckResult checkUpdateLogic(String storageId, RemoteStorageForm storageForm) throws Exception {
        CmRemoteStorage cmRemoteStorage = CmApi.getRemoteStorage(storageId);
        if (cmRemoteStorage == null) {
            String msg = "该存储不存在。";
            return CheckResult.failure(msg);
        }

        if (BooleanUtils.isFalse(cmRemoteStorage.getUnschedulable())) {
            String msg = "该存储已启用，不能编辑。";
            return CheckResult.failure(msg);
        }

        if (StringUtils.isNotBlank(storageForm.getName()) && !cmRemoteStorage.getName().equals(storageForm.getName())) {
            CmRemoteStorageQuery cmRemoteStorageQuery = new CmRemoteStorageQuery();
            cmRemoteStorageQuery.setSiteId(cmRemoteStorage.getSite().getId());
            cmRemoteStorageQuery.setName(storageForm.getName());
            List<CmRemoteStorage> cmRemoteStorages = CmApi.listRemoteStorage(cmRemoteStorageQuery);
            if (cmRemoteStorages.size() > 0) {
                String msg = "该站点下存储名已存在。";
                return CheckResult.failure(msg);
            }
        }

        return CheckResult.success();
    }
}
