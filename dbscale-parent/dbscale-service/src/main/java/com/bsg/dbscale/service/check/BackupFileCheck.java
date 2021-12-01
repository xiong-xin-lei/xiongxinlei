package com.bsg.dbscale.service.check;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.bsg.dbscale.cm.api.CmApi;
import com.bsg.dbscale.cm.model.CmNfs;
import com.bsg.dbscale.cm.query.CmNfsQuery;
import com.bsg.dbscale.dao.domain.ServGroupDO;
import com.bsg.dbscale.service.constant.Consts;
import com.bsg.dbscale.service.form.BackupExternalForm;

@Service
public class BackupFileCheck extends BaseCheck {

    public CheckResult saveExternalFile(BackupExternalForm backupExternalForm) throws Exception {
        if (StringUtils.isBlank(backupExternalForm.getServGroupId())) {
            String msg = "服务组编码不能为空。";
            return CheckResult.failure(msg);
        }

        ServGroupDO servGroupDO = servGroupDAO.get(backupExternalForm.getServGroupId());
        if (servGroupDO == null) {
            String msg = "该服务组不存在。";
            return CheckResult.failure(msg);
        }

        if (!servGroupDO.getCategory().equals(Consts.SERV_GROUP_TYPE_MYSQL)
                && !servGroupDO.getCategory().equals(Consts.SERV_GROUP_TYPE_CMHA)) {
            String msg = "该服务组不支持此项操作。";
            return CheckResult.failure(msg);
        }

        CmNfsQuery nfsQuery = new CmNfsQuery();
        nfsQuery.setZone(servGroupDO.getBusinessAreaId());
        nfsQuery.setUnschedulable(false);
        List<CmNfs> cmNfs = CmApi.listNfs(nfsQuery);
        if (cmNfs == null || cmNfs.size() < 1) {
            String msg = "无符合条件的NFS。";
            return CheckResult.failure(msg);
        }

        return CheckResult.success();
    }

}
