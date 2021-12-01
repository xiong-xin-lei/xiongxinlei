package com.bsg.dbscale.service.check;

import java.util.List;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.bsg.dbscale.cm.api.CmApi;
import com.bsg.dbscale.cm.model.CmNfs;
import com.bsg.dbscale.cm.query.CmNfsQuery;
import com.bsg.dbscale.dao.domain.BusinessAreaDO;
import com.bsg.dbscale.service.form.ClusterForm;
import com.bsg.dbscale.service.form.NfsForm;

@Service
public class NfsCheck extends BaseCheck {

    public CheckResult checkSave(NfsForm nfsForm) throws Exception {
        // Non-logical check for save
        CheckResult checkResult = checkSaveNonLogic(nfsForm);
        if (checkResult.getCode() != CheckResult.SUCCESS) {
            return checkResult;
        }

        // logical check for save
        checkResult = checkSaveLogic(nfsForm);

        return checkResult;
    }

    public CheckResult checkUpdate(String clusterId, ClusterForm clusterForm) throws Exception {
        // Non-logical check for update
        CheckResult checkResult = checkUpdateNonLogic(clusterForm);
        if (checkResult.getCode() != CheckResult.SUCCESS) {
            return checkResult;
        }

        // logical check for update
        checkResult = checkUpdateLogic(clusterId, clusterForm);

        return checkResult;
    }

    public CheckResult checkEnabled(String nfsId, boolean enabled) throws Exception {
        CmNfs cmNfs = CmApi.getNfs(nfsId);
        if (cmNfs == null) {
            String msg = "该NFS不存在。";
            return CheckResult.failure(msg);
        }
        return CheckResult.success();
    }

    public CheckResult checkRemove(String nfsId) throws Exception {
        CmNfs cmNfs = CmApi.getNfs(nfsId);
        if (cmNfs == null) {
            String msg = "该NFS不存在。";
            return CheckResult.failure(msg);
        }
        return CheckResult.success();
    }

    private CheckResult checkSaveNonLogic(NfsForm nfsForm) {
        if (StringUtils.isBlank(nfsForm.getBusinessAreaId())) {
            String msg = "业务区不能为空。";
            return CheckResult.failure(msg);
        }

        if (StringUtils.isBlank(nfsForm.getName())) {
            String msg = "NFS名不能为空。";
            return CheckResult.failure(msg);
        }

        if (StringUtils.isBlank(nfsForm.getNfsIp())) {
            String msg = "NFS地址不能为空。";
            return CheckResult.failure(msg);
        }

        if (StringUtils.isBlank(nfsForm.getNfsSource())) {
            String msg = "NFS源目录不能为空。";
            return CheckResult.failure(msg);
        }

        return CheckResult.success();
    }

    private CheckResult checkSaveLogic(NfsForm nfsForm) throws Exception {
        BusinessAreaDO businessAreaDO = businessAreaDAO.get(nfsForm.getBusinessAreaId());
        if (businessAreaDO == null) {
            String msg = "业务区不存在。";
            return CheckResult.failure(msg);
        }

        if (BooleanUtils.isFalse(businessAreaDO.getEnabled())) {
            String msg = "该业务区已停用。";
            return CheckResult.failure(msg);
        }

        CmNfsQuery cmNfsQuery = new CmNfsQuery();
        cmNfsQuery.setZone(nfsForm.getBusinessAreaId());

        List<CmNfs> cmNfsBackups = CmApi.listNfs(cmNfsQuery);
        if (cmNfsBackups != null && cmNfsBackups.size() > 0) {
            String msg = "该业务区已关联备份存储。";
            return CheckResult.failure(msg);
        }

        return CheckResult.success();
    }

    private CheckResult checkUpdateNonLogic(ClusterForm clusterForm) {
        return CheckResult.success();
    }

    private CheckResult checkUpdateLogic(String clusterId, ClusterForm clusterForm) throws Exception {
        return CheckResult.success();
    }
}
