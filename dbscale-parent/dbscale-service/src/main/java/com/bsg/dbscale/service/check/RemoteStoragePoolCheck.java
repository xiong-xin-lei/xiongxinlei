package com.bsg.dbscale.service.check;

import java.util.List;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.bsg.dbscale.cm.api.CmApi;
import com.bsg.dbscale.cm.model.CmRemoteStorage;
import com.bsg.dbscale.cm.model.CmRemoteStoragePool;
import com.bsg.dbscale.cm.query.CmRemoteStoragePoolQuery;
import com.bsg.dbscale.dao.domain.DictDO;
import com.bsg.dbscale.service.constant.DictTypeConsts;
import com.bsg.dbscale.service.form.RemoteStoragePoolForm;

@Service
public class RemoteStoragePoolCheck extends BaseCheck {

    public CheckResult checkSave(String remoteStorageId, RemoteStoragePoolForm storagePoolForm) throws Exception {
        // Non-logical check for save
        CheckResult checkResult = checkSaveNonLogic(storagePoolForm);
        if (checkResult.getCode() != CheckResult.SUCCESS) {
            return checkResult;
        }

        // logical check for save
        checkResult = checkSaveLogic(remoteStorageId, storagePoolForm);

        return checkResult;
    }

    public CheckResult checkUpdate(String remoteStorageId, String poolId, RemoteStoragePoolForm storagePoolForm)
            throws Exception {
        // Non-logical check for update
        CheckResult checkResult = checkUpdateNonLogic(storagePoolForm);
        if (checkResult.getCode() != CheckResult.SUCCESS) {
            return checkResult;
        }

        // logical check for update
        checkResult = checkUpdateLogic(remoteStorageId, poolId, storagePoolForm);

        return checkResult;
    }

    public CheckResult checkRemove(String remoteStorageId, String poolId) throws Exception {
        CmRemoteStoragePool cmRemoteStoragePool = CmApi.getRemoteStoragePool(remoteStorageId, poolId);
        if (cmRemoteStoragePool == null) {
            String msg = "该存储池不存在。";
            return CheckResult.failure(msg);
        }

        return CheckResult.success();
    }

    public CheckResult checkEnabled(String remoteStorageId, String poolId, boolean enabled) throws Exception {
        CmRemoteStoragePool cmRemoteStoragePool = CmApi.getRemoteStoragePool(remoteStorageId, poolId);
        if (cmRemoteStoragePool == null) {
            String msg = "该存储池不存在。";
            return CheckResult.failure(msg);
        }

        return CheckResult.success();
    }

    private CheckResult checkSaveNonLogic(RemoteStoragePoolForm storagePoolForm) {
        if (StringUtils.isBlank(storagePoolForm.getName())) {
            String msg = "存储池名不能为空。";
            return CheckResult.failure(msg);
        }

        if (StringUtils.isBlank(storagePoolForm.getPerformance())) {
            String msg = "性能等级不能为空。";
            return CheckResult.failure(msg);
        }
        return CheckResult.success();
    }

    private CheckResult checkSaveLogic(String remoteStorageId, RemoteStoragePoolForm storagePoolForm) throws Exception {
        CmRemoteStorage cmStorage = CmApi.getRemoteStorage(remoteStorageId);
        if (cmStorage == null) {
            String msg = "改存储不存在。";
            return CheckResult.failure(msg);
        }

        CmRemoteStoragePoolQuery cmRemoteStoragePoolQuery = new CmRemoteStoragePoolQuery();
        cmRemoteStoragePoolQuery.setName(storagePoolForm.getName());
        List<CmRemoteStoragePool> cmRemoteStoragePools = CmApi.listRemoteStoragePool(remoteStorageId,
                cmRemoteStoragePoolQuery);
        if (cmRemoteStoragePools.size() > 0) {
            String msg = "该存储下存储池名已存在。";
            return CheckResult.failure(msg);
        }

        DictDO dictDO = dictDAO.get(DictTypeConsts.STORAGE_PERFORMANCE, storagePoolForm.getPerformance());
        if (dictDO == null) {
            String msg = "性能等级不存在。";
            return CheckResult.failure(msg);
        }

        return CheckResult.success();
    }

    private CheckResult checkUpdateNonLogic(RemoteStoragePoolForm storagePoolForm) {
        if (storagePoolForm.getName() != null && StringUtils.isEmpty(storagePoolForm.getName())) {
            String msg = "存储名不能为空。";
            return CheckResult.failure(msg);
        }

        return CheckResult.success();
    }

    private CheckResult checkUpdateLogic(String remoteStorageId, String poolId, RemoteStoragePoolForm storagePoolForm)
            throws Exception {
        CmRemoteStoragePool cmRemoteStoragePool = CmApi.getRemoteStoragePool(remoteStorageId, poolId);
        if (cmRemoteStoragePool == null) {
            String msg = "该存储池不存在。";
            return CheckResult.failure(msg);
        }

        if (BooleanUtils.isFalse(cmRemoteStoragePool.getUnschedulable())) {
            String msg = "该存储池已启用，不能编辑。";
            return CheckResult.failure(msg);
        }

        if (StringUtils.isNotBlank(storagePoolForm.getName())
                && !cmRemoteStoragePool.getName().equals(storagePoolForm.getName())) {
            CmRemoteStoragePoolQuery cmRemoteStoragePoolQuery = new CmRemoteStoragePoolQuery();
            cmRemoteStoragePoolQuery.setName(storagePoolForm.getName());
            List<CmRemoteStoragePool> cmRemoteStoragePools = CmApi.listRemoteStoragePool(remoteStorageId,
                    cmRemoteStoragePoolQuery);
            if (cmRemoteStoragePools.size() > 0) {
                String msg = "该存储下存储池名已存在。";
                return CheckResult.failure(msg);
            }
        }

        return CheckResult.success();
    }
}
